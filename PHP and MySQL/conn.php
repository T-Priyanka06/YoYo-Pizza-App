<?php 

   $servername = "localhost";
            $username = "unistamgroups_yoyo";
            $password = "yoyopizza@06";
            $conn=null;
            $database = "unistamgroups_yoyopizza";
            try 
            {
               $conn = new PDO("mysql:host=$servername;dbname=$database", $username, $password);
               $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
             
             }
             catch(PDOException $e)
             {
              echo 'not working';
             }

?>