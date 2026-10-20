
const form = document.getElementById("registrationForm");

form.addEventListener("submit", function (event) {
    event.preventDefault();

    // Clear previous errors
    document.querySelectorAll(".error").forEach(function (error) {
        error.textContent = "";
    });

    document.getElementById("successMessage").textContent = "";

    let valid = true;

    const name = document.getElementById("name").value.trim();
    const roll = document.getElementById("roll").value.trim();
    const email = document.getElementById("email").value.trim();
    const course = document.getElementById("course").value;
    const password = document.getElementById("password").value;
    const confirmPassword =
        document.getElementById("confirmPassword").value;
    const file = document.getElementById("document").files[0];

    // Required field validation
    if (name === "") {
        document.getElementById("nameError").textContent =
            "Student name is required.";
        valid = false;
    }

    if (roll === "") {
        document.getElementById("rollError").textContent =
            "Roll number is required.";
        valid = false;
    }

    // Email format validation
    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    if (email === "") {
        document.getElementById("emailError").textContent =
            "Email is required.";
        valid = false;
    } else if (!emailPattern.test(email)) {
        document.getElementById("emailError").textContent =
            "Enter a valid email address.";
        valid = false;
    }

    if (course === "") {
        document.getElementById("courseError").textContent =
            "Please select a course.";
        valid = false;
    }

    // Password strength validation
    const strongPassword =
        /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[^A-Za-z0-9\s])\S{8,}$/;

    if (password === "") {
        document.getElementById("passwordError").textContent =
            "Password is required.";
        valid = false;
    } else if (!strongPassword.test(password)) {
        document.getElementById("passwordError").textContent =
            "Use 8+ characters with uppercase, lowercase, number and symbol.";
        valid = false;
    }

    if (confirmPassword === "") {
        document.getElementById("confirmPasswordError").textContent =
            "Please confirm your password.";
        valid = false;
    } else if (password !== confirmPassword) {
        document.getElementById("confirmPasswordError").textContent =
            "Passwords do not match.";
        valid = false;
    }

    // File type and size validation
    const fileError = document.getElementById("documentError");
    const allowedTypes = [
        "image/jpeg",
        "image/png",
        "application/pdf"
    ];

    if (!file) {
        fileError.textContent = "Please upload a file.";
        valid = false;
    } else if (!allowedTypes.includes(file.type)) {
        fileError.textContent = "Only JPG, PNG and PDF files are allowed.";
        valid = false;
    } else if (file.size > 2 * 1024 * 1024) {
        fileError.textContent = "File size must not exceed 2 MB.";
        valid = false;
    }

    // Display result
    if (valid) {
        document.getElementById("successMessage").textContent =
            "Registration form validated successfully!";

        // This is a frontend demonstration only.
        // No information or uploaded file is saved to a server.
    }
});
