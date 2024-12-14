<?php
    include 'header.php'; 
    require_once 'includes/db/db.php';                      // Includes database
    require_once 'includes/classes/UUIDGenerator.php';      // Include UUID generator
    require_once 'includes/classes/QRCodeGenerator.php';    // Include QR code generator
    require_once 'includes/classes/EncryptionHandler.php';  // Handles Cryptography
    
    
    
    // Check if the form is submitted
    if ( isset($_POST['submit']) ){
        
    
        $current_date = date('Y-m-d');
        $mfg          = $current_date;
        
        $generic_id = mysqli_real_escape_string($conn, $_POST['generic_id' ]);
        $batch_id   = mysqli_real_escape_string($conn, $_POST['batch_id'   ]);
        $expiry     = mysqli_real_escape_string($conn, $_POST['expiry'     ]);
        $mrp        = mysqli_real_escape_string($conn, $_POST['mrp'        ]);
        $quantity   = mysqli_real_escape_string($conn, $_POST['quantity'   ]);
        
        // Check if the batch_id already exists in the database
        {
            $checkBatchSql  = "SELECT COUNT(*) FROM `product_instances` WHERE `batch_id` = '$batch_id'";
            $result         = $conn->query($checkBatchSql);
            $row            = $result->fetch_row();
            if ($row[0] > 0) {
                $errorMessage = urlencode("The batch ID '$batch_id' has already been created. Please choose a different one.");
                echo "<script> window.location = 'index.php?message=$errorMessage' </script>";
                exit;
            }
        }
        
        
        try {
            
            $publicKeyPath  = 'keys/public_key.pem';
            $privateKeyPath = 'keys/private_key.pem';
            
            $encryptionHandler = new EncryptionHandler();
            $encryptionHandler->loadKeys($publicKeyPath, $privateKeyPath);
            
            $success = true;
            for ($i = 0; $i < $quantity; $i++) {
                
                $uuid           = UUIDGenerator::generateUUID();
    
                $encrypted_uuid = $encryptionHandler->encryptData($uuid);
                $encrypted_uuid = bin2hex($encrypted_uuid);
   
                $data           = $encrypted_uuid;
                $filename       = $uuid . '.png';
                QRCodeGenerator::generateQRCode($data, $filename, 15);
                
                // Define the destination directory & check if the directory exists, if not, create it
                $destinationDir = 'generated/' . $batch_id;
                if (!file_exists($destinationDir)) {
                    // Create the directory with appropriate permissions
                    if (!mkdir($destinationDir, 0777, true)) {
                        throw new RuntimeException('Error creating directory: ' . error_get_last()['message']);
                    }
                }
                
                $destinationFilePath = $destinationDir . '/' . $filename;
                if (!rename($filename, $destinationFilePath)) {
                    throw new RuntimeException('Error moving the file: ' . error_get_last()['message']);
                }
                
                // Insert info into the database
                $sql = "INSERT INTO `product_instances` (`uuid`, `generic_id`, `batch_id`, `mfg`, `expiry`, `mrp`) VALUES ('$uuid', '$generic_id', '$batch_id', '$mfg', '$expiry', '$mrp')";
                if ( $conn->query($sql) === FALSE) {
                   throw new RuntimeException("Error: " . $sql . "<br>" . $conn->error);
                }
                
                // Insert info into the database
                if ( $i == 0 ){
                    $sql = "INSERT INTO `product_batch_information` (`batch_id`, `dispatch` ) VALUES ('$batch_id', 0)";
                    if ( $conn->query($sql) === FALSE) {
                       throw new RuntimeException("Error: " . $sql . "<br>" . $conn->error);
                    }
                }
                
            }

        } catch (RuntimeException $e) {
            
            $success = false;
            $command = "rm -rf " . "/home/catchmei/public_html/iverify/generated/".$batch_id;
            shell_exec($command);
            
            $sql = "DELETE FROM `product_instances` WHERE `batch_id` = '$batch_id'";
            $conn->query($sql);
            
            $sql = "DELETE FROM `product_batch_information` WHERE `batch_id` = '$batch_id'";
            $conn->query($sql);
        }
        
        
        if ( !$success ){
            $errorMessage = urlencode($e->getMessage());
            echo "<script> window.location = 'index.php?message=$errorMessage' </script>";
            exit;
        }
        $successMessage = urlencode("Successfully generated $quantity QR codes for the Batch $batch_id");
        echo "<script> window.location = 'index.php?message=$successMessage' </script>";
        exit;
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
        
        <h1 class="mb-4">Product QR Code Generator (Bulk)</h1>
          
        
        <form method="post" action="">
            
            <div class="form-group">
                <label for="generic_id">Select Generic ID/Name</label>
                <select id="generic_id" class="form-control" name="generic_id" required>
                            <option selected>Choose...</option>
                            <?php 
                                $mysqli = "SELECT * FROM product_generic_information";
                                $query  = mysqli_query($conn, $mysqli);
                                while ($row = mysqli_fetch_assoc($query)) :
                                    $generic_id = trim($row['generic_id']);
                                    if ( !empty($generic_id) ) :
                                        ?><option value="<?php echo $generic_id ?>"><?php echo $generic_id ?></option>
                                    <?php endif;
                                endwhile;
                            ?>
                </select>
            </div>
            
             <div class="form-group">
                <label for="batch_id">Batch ID</label>
                <input class="form-control" id="batch_id" name="batch_id" type="text" value="<? echo time() ?>"  required>
            </div>
            
            <div class="form-group">
                <label for="expiry">Expiry</label>
                <input class="form-control" id="expiry" name="expiry" type="date" required>
            </div>
            
            <div class="form-group">
                <label for="mrp">MRP in TK</label>
                <input class="form-control" id="mrp" name="mrp" type="number" step="0.01" value="1" required>
            </div>

             <!-- Input for quantity of QR codes to generate -->
            <div class="form-group">
                <label for="quantity">Quantity of QR Codes</label>
                <input class="form-control" id="quantity" name="quantity" type="number" min="1" value="1" required>
            </div>
            
            <button type="submit" class="btn btn-primary" name="submit">Generate QR Code(s)</button>
        </form>
    </div>

</body>
</html>