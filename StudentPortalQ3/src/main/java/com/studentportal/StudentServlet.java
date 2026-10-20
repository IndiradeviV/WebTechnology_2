package com.studentportal;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/students")
public class StudentServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        // Allow only logged-in users
        HttpSession session = request.getSession(false);

        if (session == null ||
                session.getAttribute("userId") == null) {

            response.sendRedirect(
                request.getContextPath() + "/login.jsp"
            );
            return;
        }

        // Store student profiles and their associated files
        Map<Integer, Map<String, Object>> studentMap =
            new LinkedHashMap<>();

        String sql =
            "SELECT s.id, s.name, s.roll_number, s.email, " +
            "s.course, f.id AS file_id, f.original_name, " +
            "f.file_type, f.uploaded_at " +
            "FROM students s " +
            "LEFT JOIN student_files f " +
            "ON s.id = f.student_id " +
            "ORDER BY s.id, f.id";

        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            // Read all student records
            while (rs.next()) {

                int studentId = rs.getInt("id");

                Map<String, Object> student =
                    studentMap.get(studentId);

                // Create each student profile only once
                if (student == null) {

                    student = new LinkedHashMap<>();

                    student.put("id", studentId);
                    student.put("name", rs.getString("name"));
                    student.put("roll",
                        rs.getString("roll_number"));
                    student.put("email", rs.getString("email"));
                    student.put("course", rs.getString("course"));

                    student.put("files",
                        new ArrayList<Map<String, Object>>());

                    studentMap.put(studentId, student);
                }

                // Add associated file information
                int fileId = rs.getInt("file_id");

                if (!rs.wasNull()) {

                    Map<String, Object> file =
                        new LinkedHashMap<>();

                    file.put("id", fileId);
                    file.put("name",
                        rs.getString("original_name"));
                    file.put("type",
                        rs.getString("file_type"));
                    file.put("date",
                        rs.getString("uploaded_at"));

                    @SuppressWarnings("unchecked")
                    List<Map<String, Object>> files =
                        (List<Map<String, Object>>)
                            student.get("files");

                    files.add(file);
                }
            }

            // Pass the retrieved data to JSP
            List<Map<String, Object>> students =
                new ArrayList<>(studentMap.values());

            request.setAttribute("students", students);

            request.getRequestDispatcher("/students.jsp")
                   .forward(request, response);

        } catch (Exception e) {

            throw new ServletException(
                "Unable to retrieve student profiles.", e
            );
        }
    }
}
