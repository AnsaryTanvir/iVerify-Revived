<?php
    include 'header.php'; 
    require_once 'includes/db/db.php';                      // Includes database
    $serial  = 1;
    
    if ( isset($_GET['delete'])){

        $id     =  $_GET['delete'];
        
        $mysqli = "DELETE FROM `product_generic_information` WHERE `id` = '$id'";
        $query  = mysqli_query($conn, $mysqli);
        if (!$query) {
           echo "Error: " . mysqli_error($conn);
        }
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
        <h1 class="mb-4">Registered Products List</h1>
        <p>
            <strong>Generic Information</strong> refers to the details of a product that do not vary from one version or batch to another. For instance, the name, dosage, and description of a drug like "Seclo 20 mg" remain the same every time it's produced.
        </p>
        
        <div class="table-responsive">
            <table class="table table-hover ">
    
                <thead class="thead-dark">
                    <tr>
                        <th scope="col">ID</th>
                        <th scope="col">Generic ID/Name</th>
                        <th scope="col">Action</th>
                        <th scope="col">Action</th>
                    </tr>
                </thead>
    
                <tbody>

                <?php 


                    $mysqli = "SELECT * FROM product_generic_information";
                    $query  = mysqli_query($conn, $mysqli);
                    while($row = mysqli_fetch_assoc($query)):
                        $id                     = $row['id'                 ];
                        $generic_id             = $row['generic_id'         ];
                ?>

                <tr class="user">
                    <td> <?php echo $serial++;              ?> </td>
                    <td> <?php echo $generic_id;            ?> </td>
                    <td><a href="view_edit.php?generic_id=<?php echo $generic_id;?>">View/Edit</a></td>
                    <td>
                        <a href="?delete=<?php echo $id; ?>" class="btn btn-danger" onclick="return confirm('Are you sure you want to delete this record?');">Delete</a>
                    </td>
                </tr>

                <?php endwhile;?>
            </tbody>
    
            </table>
        </div>
    </div>
</body>
</html>