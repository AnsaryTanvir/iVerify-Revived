<?php
    require_once '../includes/db/db.php';                      // Includes database
    

    if ($_SERVER['REQUEST_METHOD'] == 'POST'){

        $generic_id   = isset($_POST['generic_id'])   ? $_POST['generic_id']   : '';
        $name         = isset($_POST['name'])         ? $_POST['name']         : '';
        $category     = isset($_POST['category'])     ? $_POST['category']     : '';
        $manufacturer = isset($_POST['manufacturer']) ? $_POST['manufacturer'] : '';
        $description  = isset($_POST['description'])  ? $_POST['description']  : '';

        $sql = "INSERT INTO `products_information` (`generic_id`, `name`, `category`, `manufacturer`, `description`) VALUES ('$generic_id', '$name', '$category', '$manufacturer', '$description')";
        try {
            if ($conn->query($sql) === FALSE) {
                throw new Exception("Error: " . $sql . "<br>" . $conn->error);
            }
        }
        catch (Exception $e) {
            if (strpos($conn->error, 'Duplicate entry') !== false) {
                // Duplicate entry
            }
            else {
                echo $e->getMessage(); // Other errors will be handled normally
                return;
            }
        }

        $batch_id   = isset($_POST['batch_id']) ? $_POST['batch_id']    : '';
        $expiry     = isset($_POST['expiry'])   ? $_POST['expiry']      : '';
        $uuid       = isset($_POST['uuid'])     ? $_POST['uuid']        : '';

        $sql = "INSERT INTO `products` (`uuid`, `generic_id`, `batch_id`, `expiry`) VALUES ('$uuid', '$generic_id', '$batch_id', '$expiry')";
        if ($conn->query($sql) === TRUE) {
            echo "Success";
        } else {
            echo "Error: " . $sql . "<br>" . $conn->error;
        }
        
        
    }