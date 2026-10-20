package com.studentportal;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/file")
public class FileServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        // Check whether the user is logged in
        HttpSession session = request.getSession(false);

        if (session == null ||
                session.getAttribute("userId") == null) {

            response.sendError(
                HttpServletResponse.SC_UNAUTHORIZED
            );
            return;
        }

        // Validate the requested file ID
        int fileId;

        try {
            fileId = Integer.parseInt(
                request.getParameter("id")
            );

            if (fileId <= 0) {
                throw new NumberFormatException();
            }

        } catch (Exception e) {
            response.sendError(
                HttpServletResponse.SC_BAD_REQUEST
            );
            return;
        }

        // Read the actual file path and type from your database
        String sql =
            "SELECT file_path, file_type " +
            "FROM student_files WHERE id = ?";

        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, fileId);

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {
                    response.sendError(
                        HttpServletResponse.SC_NOT_FOUND
                    );
                    return;
                }

                String filePath = rs.getString("file_path");
                String fileType = rs.getString("file_type");

                // Permit only supported image and PDF formats
                if (!"image/jpeg".equals(fileType)
                        && !"image/png".equals(fileType)
                        && !"application/pdf".equals(fileType)) {

                    response.sendError(
                        HttpServletResponse.SC_FORBIDDEN
                    );
                    return;
                }

                File file = new File(filePath);

                // Check whether the file exists and can be read
                if (!file.isFile() || !file.canRead()) {
                    response.sendError(
                        HttpServletResponse.SC_NOT_FOUND
                    );
                    return;
                }

                // Send the correct content type to the browser
                response.setContentType(fileType);
                response.setHeader(
                    "X-Content-Type-Options", "nosniff"
                );
                response.setHeader(
                    "Content-Disposition", "inline"
                );
                response.setContentLengthLong(file.length());

                // Stream the file to the browser
                try (InputStream in =
                         Files.newInputStream(file.toPath());
                     OutputStream out =
                         response.getOutputStream()) {

                    byte[] buffer = new byte[8192];
                    int bytesRead;

                    while ((bytesRead = in.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }
                }
            }

        } catch (Exception e) {
            throw new ServletException(
                "Unable to display the requested file.", e
            );
        }
    }
}
