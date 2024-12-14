<?php

//   $servername = "localhost";
//   $dbusername = "root";
//   $dbpassword = "";
//   $db         = "iverify";


//   $conn = mysqli_connect($servername, $dbusername, $dbpassword, $db);
//   if (!$conn) {
//     die("Connection failed: " . mysqli_connect_error());
//   }


    $servername = "127.0.0.1";
    $dbusername = "catchmei_iverify";
    $dbpassword = "CO4%UAfzD";
    $db         = "catchmei_iverify";

    $conn = mysqli_connect($servername, $dbusername, $dbpassword, $db);
    if (!$conn)
    {
        die("Connection failed: " . mysqli_connect_error());
    }
    // else
    //     echo "Ok";