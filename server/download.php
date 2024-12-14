<?php

    session_start();
    include 'session.php';
    
    if ( !isset($_SESSION['username']) ){
        exit;
    }
      
    if (isset($_GET['download'])) {
        
        $batch_id = $_GET['download'];
        $file = "/home/catchmei/public_html/iverify/generated/" . $batch_id . ".zip";
    
        // Check if the file exists
        if (file_exists($file)) {
            // Set appropriate headers to force the download
            header('Content-Description: File Transfer');
            header('Content-Type: application/zip');
            header('Content-Disposition: attachment; filename="' . basename($file) . '"');
            header('Content-Transfer-Encoding: binary');
            header('Expires: 0');
            header('Cache-Control: must-revalidate');
            header('Pragma: public');
            header('Content-Length: ' . filesize($file));
    
            // Clean any previous output
            ob_clean();
            flush();
    
            // Send the file to the browser
            readfile($file);
            exit;  // Ensure no further code is executed
        } else {
            echo $batch_id . " not found.";
        }
    }
?>