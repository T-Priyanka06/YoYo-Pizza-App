<?php 
require 'conn.php';
$result = array();
$postdata = file_get_contents("php://input");
$request = json_decode($postdata);
date_default_timezone_set('Asia/Kolkata');
$curr_date = date('Y-m-d H:i:s');
if(isset($_POST['cart_data'])){
//if(0){
$sql="select count(*) as count from unistamgroups_yoyopizza.orders";
$stmt = $conn->prepare($sql); 
$stmt->execute();
$row = $stmt->fetch(PDO::FETCH_BOTH);
$id=$row["count"]+1;

if($id < 10){ $id2='00000'; }
else if($id >= 10){ $id2='0000'; }
else if($id >= 100){ $id2='000'; }
else if($id >= 1000){ $id2='00'; }
else if($id >= 10000){ $id2='0'; }
else { $id2=''; }
$doc='ORD';
$id3=$doc.$id2.$id;
$cus_details=$_POST['user_data'];

$cus_items=$_POST['cart_data'];
$order_status='ordered';
$track_count=0;
$last_viewed_on='no views';

$sql="insert into orders(`order_id`,`cus_details`,`items`,`order_status`,`track_count`,`last_viewed_on`,`ordered_on`) values(:order_id,:cus_details,:items,:order_status,:track_count,:last_viewed_on,:ordered_on)";
$stmt = $conn->prepare($sql); 
$stmt->bindParam(':order_id',$id3 );

$stmt->bindParam(':ordered_on', $curr_date);
$stmt->bindParam(':cus_details',$cus_details );
$stmt->bindParam(':items',$cus_items );
$stmt->bindParam(':last_viewed_on',$last_viewed_on);
$stmt->bindParam(':track_count',$track_count );
$stmt->bindParam(':order_status',$order_status);

$stmt->execute();

$result= ["result" => 'Thank you for Your Order !! Your order ID is ' .$id3. 'Enjoy Your Order !!!!!!!' ];
echo json_encode($result);
		
}	


else if(isset($_POST['track_order'])){
//else if(1){

$order_id=$_POST['track_order'];
$sql="SELECT * FROM orders where order_id=:order_id limit 1";
$stmt = $conn->prepare($sql); 
$stmt->bindParam(':order_id', $order_id);
$stmt->execute();
if($stmt->rowCount()>0)
	{
		
	$sql2="update orders set `track_count`=`track_count`+1,last_viewed_on=:date where order_id=:order_id limit 1";
    $stmt2 = $conn->prepare($sql2); 
	$stmt2->bindParam(':order_id', $order_id);
	$stmt2->bindParam(':date', $curr_date);
	$stmt2->execute();	
		
		
   while($row= $stmt->fetch(PDO::FETCH_BOTH))
			 {

             $result='<br> Customer Details : '.$row['cus_details'].'<br> Items Ordered : '.$row['items'].'<br> Order Status :'.$row['order_status'].'<br> Total tracked Count : '.$row['track_count'].'<br> Last Tracked on : '.$row['last_viewed_on'];
          
					
			 }
	}
    else {
     $result='Sorry We can not find Your Order !!!';
	}		
			 
echo json_encode($result);
	
}

$conn=null;
?> 