<?php

require_once '../includes/db/db.php';                      // Includes database
require_once '../includes/classes/EncryptionHandler.php';  // Handles Cryptography

$response = Array();


if ($_SERVER['REQUEST_METHOD'] == 'POST') {

    $encrypted_uuid = $_POST['encrypted_uuid'   ];
    $phone_number   = $_POST['phone_number'     ];
    $self           = $phone_number;

    try {

        $publicKeyPath  = '../keys/public_key.pem';
        $privateKeyPath = '../keys/private_key.pem';
        
        $encryptionHandler  = new EncryptionHandler();
        $encryptionHandler->loadKeys($publicKeyPath, $privateKeyPath);

        $uuid               = $encryptionHandler->decryptData(hex2bin($encrypted_uuid));
        
        // Check if the product is already verified
        
        $sql    = "SELECT * FROM `product_instances` WHERE `uuid` = '$uuid'";
        $result = $conn->query($sql);
        if ( $result->num_rows > 0 ){

            $row                    = $result->fetch_assoc();
            $uuid                   = $row['uuid']; 
            $generic_id             = $row['generic_id']; 
            $batch_id               = $row['batch_id']; 
            $mfg                    = $row['mfg'];  
            $expiry                 = $row['expiry'];  
            $mrp                    = $row['mrp'];  
            $verifier               = $row['verifier'];  
            $verification_timestamp = $row['verification_timestamp']; 

            if ( is_null($verifier) || empty($verifier) ){
                
                //  Set the time zone and get the current date and time
                date_default_timezone_set('Asia/Dhaka');
                $currentDateTime            = new DateTime();
                $formattedDate              = $currentDateTime->format('l, F j, Y g:i A');
                $verification_timestamp     = $formattedDate . ' (GMT+6)';

                $sql    = "UPDATE `product_instances` SET `verifier` = '$phone_number', `verification_timestamp` = '$verification_timestamp' WHERE `uuid` = '$uuid'";
                $result = $conn->query($sql);
                if (!$result) {
                    $response["message"] = "Error: " . $conn->error . "<br>";
                }
                
                $response["message"] = "Congratulations!\nThis product has been marked as verified";
                
                $response["batch_id"]               = $batch_id;
                $response["mfg"]                    = $mfg;
                $response["expiry"]                 = $expiry;
                $response["mrp"]                    = $mrp;
                $response["verification_timestamp"] = $verification_timestamp;
                
                {
                    $sql    = "SELECT * FROM `product_generic_information` WHERE `generic_id` = '$generic_id'";
                    $result = $conn->query($sql);
                    if ( $result->num_rows > 0 ){
                        $row    = $result->fetch_assoc();
                        $generic_information = $row['generic_information'];
                        $response["generic_information"] = $generic_information;
                    }
                    
                    $response["verifier"] = "Self";
                }
                
                
            }
            
            else{
                
                $response["message"] = "This product has been marked as verified";
                
                $response["batch_id"]               = $batch_id;
                $response["mfg"]                    = $mfg;
                $response["expiry"]                 = $expiry;
                $response["mrp"]                    = $mrp;
                $response["verification_timestamp"] = $verification_timestamp;
                
                {
                    $sql    = "SELECT * FROM `product_generic_information` WHERE `generic_id` = '$generic_id'";
                    $result = $conn->query($sql);
                    if ( $result->num_rows > 0 ){
                        $row    = $result->fetch_assoc();
                        $generic_information = $row['generic_information'];
                        $response["generic_information"] = $generic_information;
                    }
                    
                    $response["verifier"] = "Self";
                    if ( $verifier != $self ){
                        $response["verifier"] = "Someone else";
                    }
                    
                    $sql    = "SELECT * FROM `users` WHERE `phone_number` = '$verifier'";
                    $result = $conn->query($sql);
                    if ( $result->num_rows > 0 ){
                        $row    = $result->fetch_assoc();
                        $response["verifier_full_name"]   = $row['full_name'];;
                    }  
                }
                
            }

        }else{
            $response["message"] = "Error: Record not found in the database";
        }

    } catch (RuntimeException $e) {
        $response["message"] = "Error: " . $e->getMessage();
    }

    $response = json_encode($response);
    echo base64_encode($response);
}

?>