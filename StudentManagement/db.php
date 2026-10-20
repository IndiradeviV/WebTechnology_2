<?php
// Connect to the local MySQL database in XAMPP
$conn = new mysqli(
    "localhost",
    "root",
    "",
    "student_management"
);

// Check database connection
if ($conn->connect_error) {
    die("Database connection failed");
}
?>