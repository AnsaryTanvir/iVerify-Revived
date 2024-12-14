<?php
    require_once '../includes/db/db.php';

    if ($_SERVER['REQUEST_METHOD'] == 'POST') {
        
        // Collect POST data
        $phoneNumber = isset($_POST['phoneNumber']) ? $_POST['phoneNumber'] : '';
        $password    = isset($_POST['password']) ? $_POST['password'] : '';
        
        // Input validation
        if (empty($phoneNumber) || empty($password)) {
            echo 'Both phone number and password are required';
            exit;
        }

        // Check if phone number exists
        $query  = "SELECT * FROM `users` WHERE `phone_number` = '$phoneNumber'";
        $result = $conn->query($query);

        if (mysqli_num_rows($result) == 0) {
            echo 'User not registered';
            exit;
        }

        $user = $result->fetch_assoc();
        if ( password_verify($password, $user['hashed_password']) ) {
            echo 'Welcome';
        } else {
            echo 'Incorrect password';
        }
    }
?>