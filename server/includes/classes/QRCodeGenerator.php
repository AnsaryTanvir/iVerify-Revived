<?php

    require 'vendor/autoload.php';  // Include Composer autoload file
    use chillerlan\QRCode\{QRCode, QROptions};

    class QRCodeGenerator {
        
        public static function generateQRCode($data, $filename, $verison = 1) {
            
            try {
                // Configuration options for the QR code
                $options = new QROptions([
                    'version' => $verison,
                    'outputType' => QRCode::OUTPUT_IMAGE_PNG,
                    'errorCorrectionLevel' => QRCode::ECC_L,
                    'moduleValues' => [
                        1 => ['module_size' => 10, 'module_value' => 4],
                    ],
                    'margin' => 4,
                    'scale' => 8,
                    'dpi' => 300,
                    'imageBase64' => false,
                ]);
            
                // Create QR code
                $qrcode = new QRCode($options);
            
                // Set data
                $imageData = $qrcode->render($data);
            
                // Save the high-quality image to a file
                file_put_contents($filename, $imageData);

                // Optionally, you can return true or a success message here
                return true;
            } catch (\Exception $e) {
                // Handle the exception
                // Log or display an error message
                // Optionally, you can return false or an error message here
                return false;
            }
        }
    }


?>
