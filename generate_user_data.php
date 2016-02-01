/*
Generate user data of [1,35000000].
*/
<?php
function microtime_float() {
	list ( $usec, $sec ) = explode ( "", microtime () );
	return (( float ) $usec + ( float ) $sec);
}

$conn = mysqli_connect ( "SQL SERVER", "USER", "PASSWORD", "TABLE" ); // Insert your user.
if (mysqli_connect_errno ( $conn ))
	die ( "Error connected sqlite." );
mysqli_query ( $conn, "TRUNCATE `user_table`" );
$st = microtime_float ();
$i = 1;
while ( $i <= 35000000 ) {
	mysqli_query ( $conn, "INSERT `user_table` (UID)
		VALUES(\"" . hash ( "crc32b", $i ) . "\")" );
	if ($i % 10000 == 0) {
		echo ("<br/>Have wrriten: " . $i);
	}
	$i ++;
}
$tt = number_format ( (microtime_float () - $st), 4 ) . " s.";
die ( "<br/>Scuess! Total time: " . $tt );
?>
