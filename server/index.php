<?php 
    include 'header.php'; 
    $username  = $_SESSION['username'];
    
     if ( isset($_GET['message']) ){
        $message = $_GET['message'];
        echo "<div class=\"alert alert-primary mb-0 pb-2 pt-2\" role=\"alert\">$message</div>";
    }
?>

<div class="page-starts">
    <div class="container">
            <h1 class="welcome">Welcome to iVerify Dashboard</h1>
    </div>
</div>

<?php include "footer.php"; ?>