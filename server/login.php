<?php
  require_once 'includes/db/db.php';
  session_start();

  if( isset($_SESSION['username'])  ){
    header("Location: index.php");
    die();
  }
 
  if ( isset($_POST['username']) AND isset($_POST['password']) ){
    
      $username  = mysqli_real_escape_string($conn, $_POST['username']);
      $password  = mysqli_real_escape_string($conn, $_POST['password']);

      if ( $username == "Admin" && $password == "Admin" ){
        $_SESSION['username'] = $username;
        header("Location: index.php");
        die();
      } else echo '<script type="text/JavaScript"> alert("Authentication Error"); </script>';
      
  }

?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Panel</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css" integrity="sha384-zCbKRCUGaJDkqS1kPbPd7TveP5iyJE0EjAuZQTgFLD2ylzuqKfdKlfG/eSrtxUkn" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" integrity="sha512-Fo3rlrZj/k7ujTnHg4CGR2D7kSs0v4LLanw2qksYuRlEzO+tcaEPQogQ0KaoGN26/zrn20ImR1DfuLWnOo7aBA==" crossorigin="anonymous" referrerpolicy="no-referrer" />
    <link href="css/login.css" rel="stylesheet">

</head>
<body>

<!--START LOGIN FORM -->
  <div class="container">
      <div class="wrapper">

        <div class="title"><span>Seller Login</span></div>

        <form method="POST">
          <div class="row">
            <i class="fas fa-user"></i>
            <input type="text" placeholder="Username" name="username" required>
          </div>

          <div class="row">
            <i class="fas fa-lock"></i>
            <input type="password" placeholder="Password" name="password" required>
          </div>

          <div class="row button">
            <input type="submit" value="Login">
          </div>

        </form>

      </div>
    </div>
<!-- END LOGIN FORM -->


</body>
</html>