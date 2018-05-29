<?php
define ('HOST',"localhost");
define ('DB_USER',"id5642159_c4toplist");
define ('DB_PASSWORD',"r6Wh66lD6v");
define ('DB',"id5642159_c4toplist");

$link = mysqli_connect( HOST, DB_USER, DB_PASSWORD, DB );
/* проверка соединения */
if ( mysqli_connect_errno() ) {
    printf("Не удалось подключиться: %s\n", mysqli_connect_error());
    exit();
}
else {
    printf("Удалось подключиться: %s\n<br>", mysqli_get_host_info($link));
}
if (  !$link  )   die("Error");

$query   =  "SELECT * FROM c4topList";

$result  =  mysqli_query( $link,  $query );

if ( !$result ) echo "Произошла ошибка: "  .  mysqli_error();
else echo "Данные получены <br>";

while (  $row  =  mysqli_fetch_row($result)  )
{
    echo "ID: $row[0]. DIST: $row[1]. TIME: $row[2]. NAME: $row[3]<br>";
}

mysqli_close( $link );
?>




