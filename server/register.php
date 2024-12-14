<?php
    include 'header.php'; 
    require_once 'includes/db/db.php';                      // Includes database
    
     // Check if the form is submitted
    if (isset($_POST['submit'])) {
    
        $generic_id             = mysqli_real_escape_string($conn, $_POST['generic_id']);
        $generic_information    = base64_encode($_POST['generic_information']);
    
        // Insert info into the database
        $sql = "INSERT INTO `product_generic_information` (`generic_id`, `generic_information`) VALUES ('$generic_id', '$generic_information')";
        if (!mysqli_query($conn, $sql)) {
            echo "Error: " . mysqli_error($conn);
        }
        mysqli_close($conn);
        
    }
?>


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>iVerify</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
</head>
<body>

    <div class="container mt-5">
        <h1 class="mb-4">Register Product Generic Information</h1>
        <p>
            <strong>Generic Information</strong> refers to the details of a product that do not vary from one version or batch to another. For instance, the name, dosage, and description of a drug like "Seclo 20 mg" remain the same every time it's produced.
        </p>
        <form method="post" action="">
            <div class="form-group">
                <label for="generic_id">Generic ID/Name</label>
                <input type="text" class="form-control" id="" name="generic_id" required>
            </div>
        
            <div class="form-group">
                <label for="generic_information">Generic Information</label>
                <textarea class="form-control" id="generic_information" name="generic_information" rows="10" required></textarea>
            </div>
        
            <button type="submit" class="btn btn-primary" name="submit">Register</button>
        </form>
    </div>
</body>
</html>