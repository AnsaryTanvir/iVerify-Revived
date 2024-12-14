<?php
    include 'header.php'; 
    require_once 'includes/db/db.php';                      // Includes database
    $serial  = 1;
    
    
    if ( isset($_GET['message']) ){
        $message = $_GET['message'];
        echo "<div class=\"alert alert-primary mb-0 pb-2 pt-2\" role=\"alert\">$message</div>";
    }
    
    if ( isset($_GET['dispatch'])){

        $batch_id =  $_GET['dispatch'];
        
        $command = "zip -r " . "/home/catchmei/public_html/iverify/generated/".$batch_id.".zip " . "/home/catchmei/public_html/iverify/generated/".$batch_id;
        $retval = shell_exec($command);
        
        if ( $retval === NULL ){
            $errorMessage = urlencode("Error: " . $retval . error_get_last()['message'] );
            echo "<script> window.location = 'index.php?message=$errorMessage' </script>";
            exit;
        }
        
        $mysqli = "UPDATE `product_batch_information` SET `dispatch` = 1 WHERE `batch_id` = '$batch_id'";
        $query  = mysqli_query($conn, $mysqli);
        if (!$query) {
            $errorMessage = urlencode("Error: " . $sql . "<br>" . $conn->error);
            echo "<script> window.location = 'index.php?message=$errorMessage' </script>";
            exit;
        }
        
        $successMessage = urlencode("Successfully dispatched the Batch $batch_id");
        echo "<script> window.location = 'generated_list.php?message=$successMessage' </script>";
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
        <h1 class="mb-4">Generated Batch List</h1>
        
        <div class="table-responsive">
            <table class="table table-hover ">
    
                <thead class="thead-dark">
                    <tr>
                        <th scope="col">ID</th>
                        <th scope="col">Generic Name</th>
                        <th scope="col">Batch</th>
                        <th scope="col">MFG</th>
                        <th scope="col">Expiry</th>
                        <th scope="col">MRP</th>
                        <th scope="col">Items</th>
                        <th scope="col">Action</th>
                        <th scope="col">Action</th>
                        <!--<th scope="col">Action</th>-->
              
                    </tr>
                </thead>
    
                <tbody>

                <?php 

                    

                    $mysqli = " SELECT *, COUNT(*) as count FROM product_instances GROUP BY batch_id";
                    $query  = mysqli_query($conn, $mysqli);
                    while($row = mysqli_fetch_assoc($query)):
                        $id                     = $row['id'                 ];
                        $generic_id             = $row['generic_id'         ];
                        $batch_id               = $row['batch_id'           ];
                        $mfg                    = $row['mfg'                ];
                        $expiry                 = $row['expiry'             ];
                        $mrp                    = $row['mrp'                ];
                        $count                  = $row['count'              ];
                        
                        // Find dispatch status for each batch id
                        $dispatch_query         = "SELECT dispatch FROM product_batch_information WHERE batch_id = '$batch_id'";
                        $dispatch_result        = mysqli_query($conn, $dispatch_query);
                        $dispatch_row           = mysqli_fetch_assoc($dispatch_result);
                        $dispatch               = $dispatch_row['dispatch'];
                  
                ?>

                <tr class="user">
                    <td> <?php echo $serial++;          ?> </td>
                    <td> <?php echo $generic_id;        ?> </td>
                    <td> <?php echo $batch_id;          ?> </td>
                    <td> <?php echo $mfg;               ?> </td>
                    <td> <?php echo $expiry;            ?> </td>
                    <td> <?php echo $mrp;               ?> </td>
                    <td> <?php echo $count;             ?> </td>
                    
                    <td>
                        <a href="download.php?download=<?php echo $batch_id; ?>" class="btn btn-success">Download</a>
                    </td>
                    
                    
                    <td>
                        <?php if ( $dispatch == 0 ): ?>
                            <a href="?dispatch=<?php echo $batch_id; ?>" class="btn btn-danger" onclick="return confirm('Are you sure you want to dispatch (mark as printed) this batch?');">Dispatch</a>
                        <?php else: ?>
                            <span class="text-warning"><strong>Dispatched</strong></span>
                        <?php endif; ?>
                    </td>
        
                </tr>

                <?php endwhile;?>
            </tbody>
    
            </table>
        </div>
    </div>
</body>
</html>