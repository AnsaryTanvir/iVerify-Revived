<?php
    require_once '../includes/db/db.php';

    if ($_SERVER['REQUEST_METHOD'] == 'POST') {
        
        // Collect POST data
        $fullName       = isset($_POST['fullName'])     ? $_POST['fullName']        : '';
        $phoneNumber    = isset($_POST['phoneNumber'])  ? $_POST['phoneNumber']     : '';
        $password       = isset($_POST['password'])     ? $_POST['password']        : '';
        
        // Input validation
        if ( empty($fullName) || empty($phoneNumber) || empty($password) ) {
            echo 'All fields are required';
            exit;
        }

                
        // Check if phone number already exists
        $query  = "SELECT * FROM `users` WHERE `phone_number` = '$phoneNumber'";
        $result = $conn->query($query);

        if ( mysqli_num_rows($result) > 0) {
            echo 'Phone number already registered';
            exit;
        }

        // Hash the password
        $hashedPassword = password_hash($password, PASSWORD_BCRYPT);
        $query          = "INSERT INTO `users` (`phone_number`, `full_name`, `hashed_password`) VALUES ('$phoneNumber', '$fullName', '$hashedPassword')";
        $result         = $conn->query($query);    
        if ($result) {
            echo 'User registered successfully';
        }
        else {
            echo 'Failed to register user. Error: ' . mysqli_error($conn);
        }
        
    }
?>