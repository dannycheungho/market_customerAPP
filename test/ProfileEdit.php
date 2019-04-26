<?php

     include 'config.inc.php';
	 
	 // Check whether username or password is set from android	

	 
	 
	 
	 
	 
	 
	 if(isset($_POST['name']) )
     {
		  // Innitialize Variable
		  $result='';
	   	  $name = $_POST['name'];
		  $telephone = $_POST['telephone'];
		  $email = $_POST['email'];
		  $nationality = $_POST['nationality'];
		  $sex = $_POST['sex'];
		  $age = $_POST['age'];
		  $occupation = $_POST['occupation'];
		  $balance = $_POST['balance'];
		  $username = $_POST['username'];
		  // Query database for row exist or not
		  $sql =  'UPDATE customer SET name = :name ,telephone = :telephone ,email = :email ,nationality = :nationality ,sex = :sex ,age = :age ,occupation = :occupation ,balance = :balance WHERE username = :username' ;
          $stmt = $conn->prepare($sql);
          $stmt->bindParam(':name', $name, PDO::PARAM_STR);
		  $stmt->bindParam(':telephone', $telephone, PDO::PARAM_STR);
		  $stmt->bindParam(':email', $email, PDO::PARAM_STR);
		  $stmt->bindParam(':nationality', $nationality, PDO::PARAM_STR);
		  $stmt->bindParam(':sex', $sex, PDO::PARAM_STR);
		  $stmt->bindParam(':age', $age, PDO::PARAM_INT);
		  $stmt->bindParam(':occupation', $occupation, PDO::PARAM_STR);
		  $stmt->bindParam(':balance', $balance, PDO::PARAM_INT);
		  $stmt->bindParam(':username', $username, PDO::PARAM_STR);
          $stmt->execute();
		  
		  
          if($stmt->rowCount())
          {
			 $result="true";	
          }  
          elseif(!$stmt->rowCount())
          {
			  	$result="false";
          }
		  
		  // send result back to android
   		  echo $result;
  	}
	
?>