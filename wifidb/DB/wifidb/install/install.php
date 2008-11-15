<title>Welcome to the Random Intervals Wireless DB --> Import Page</title>
<link rel="stylesheet" href="../css/site4.0.css">
<body topmargin="10" leftmargin="0" rightmargin="0" bottommargin="10" marginwidth="10" marginheight="10">
<div align="center">
<table border="0" width="75%" cellspacing="10" cellpadding="2">
	<tr>
		<td bgcolor="#315573">
		<p align="center"><b><font size="5" face="Arial" color="#FFFFFF">
		Randomintervals.com Wireless DataBase *Alpha* </font>
		<font color="#FFFFFF" size="2">
            <a class="links" href="/">[Root] </a>/ <a class="links" href="/wifidb/">[WifiDB] </a>/
		</font></b>
		</td>
	</tr>
</table>
</div>
<div align="center">
<table border="0" width="75%" cellspacing="10" cellpadding="2" height="90">
	<tr>
<td width="17%" bgcolor="#304D80" valign="top">

</td>
	<td width="80%" bgcolor="#A9C6FA" valign="top" align="center">
		<p align="center">
<table><tr><th>Status</th><th>Step of Install</th></tr>
<?php
$date = date("m.d.Y");
$root_sql_user	=	$_POST['root_sql_user'];
$root_sql_pwd	=	$_POST['root_sql_pwd'];

$root		=	$_POST['root'];
$hosturl	=	$_POST['hosturl'];
$sqlhost	=	$_POST['sqlhost'];
$sqlu		=	$_POST['sqlu'];
$sqlp		=	$_POST['sqlp'];
$wifi		=	$_POST['wifidb'];
$wifi_st	=	$_POST['wifistdb'];
echo '<tr><TH colspan="2">Database Install</TH><tr>';
#Connect with Root priv
$conn = mysql_connect($sqlhost, $root_sql_user, $root_sql_pwd);

#drop exisiting db if it is there and create a new one
$sqls0 =	"DROP DATABASE $wifi_st";
$sqls1 =	"CREATE DATABASE $wifi_st";
$RE_DB_ST_Re = mysql_query($sqls0, $conn);
$RE_DB_ST_Re = mysql_query($sqls1, $conn) or die(mysql_error());

if($RE_DB_ST_Re)
{echo "<tr><td>Success..........</td><td>DROP DATABASE `$wifi_st`; "
		."CREATE DATABASE `$wifi_st`</td></tr>";}
else{
echo "<tr><td>Failure..........</td><td>DROP DATABASE `$wifi_st`; "
		."CREATE DATABASE `$wifi_st`</td></tr>";
}

#same thing for the ST db
$sqls0 =	"DROP DATABASE $wifi";
$sqls1 =	"CREATE DATABASE $wifi";
$sqls2 =	"USE $wifi";
$DB_WF_Re = mysql_query($sqls0, $conn);
$DB_WF_Re = mysql_query($sqls1, $conn) or die(mysql_error());
$DB_WF_Re = mysql_query($sqls2, $conn) or die(mysql_error());

if($DB_WF_Re)
{echo "<tr><td>Success..........</td><td>DROP DATABASE `$wifi`; "
		."CREATE DATABASE `$wifi`</td></tr>";}
else{
echo "<tr><td>Failure..........</td><td>DROP DATABASE `$wifi`; "
		."CREATE DATABASE `$wifi`</td></tr>";
}

#create Settings table
$sqls =	"CREATE TABLE `settings` ("
		."`id` int(255) NOT NULL auto_increment,"
		."`table` varchar(25) NOT NULL,"
		."`size` int(254) default NULL,"
		."UNIQUE KEY `table` (`table`),"
		."KEY `id` (`id`)"
		.") ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;";
$CR_TB_SE_Re = mysql_query($sqls, $conn) or die(mysql_error());


if($CR_TB_SE_Re)
{echo "<tr><td>Success..........</td><td>CREATE TABLE `settings`</td></tr>";}
else{
echo "<tr><td>Failure..........</td><td>CREATE TABLE `settings`</td></tr>";}

#insert data into the settings table
$sqls =	"INSERT INTO `settings` (`id`, `table`, `size`) VALUES (0, 'wifi0', 0);";
$IN_TB_SE_Re = mysql_query($sqls, $conn) or die(mysql_error());

if($IN_TB_SE_Re)
{echo "<tr><td>Success..........</td><td>INSERT INTO `settings`</td></tr>";}
else{echo "<tr><td>Failure..........</td><td>INSERT INTO `settings`</td></tr>";}

#create users table (History for imports)
$sqls =	"CREATE TABLE `users` (`id` INT( 255 ) NOT NULL AUTO_INCREMENT ,`username` VARCHAR( 25 ) NOT NULL ,`points` TEXT NOT NULL ,`notes` TEXT NOT NULL ,`title` varchar(32) NOT NULL ,`date` varchar(25) NOT NULL, INDEX ( `id` )) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;";
$CR_TB_US_Re = mysql_query($sqls, $conn) or die(mysql_error());

if($CR_TB_US_Re)
{echo "<tr><td>Success..........</td><td>CREATE TABLE `users`</td></tr>";}
else{
echo "<tr><td>Failure..........</td><td>CREATE TABLE `users`</td></tr>";}

#Create Wifi0 table (Pointers to _ST tables
$sqls =	"CREATE TABLE wifi0 ("
  ."  id int(255) default NULL,"
  ."  ssid varchar(25) NOT NULL,"
  ."  mac varchar(25) NOT NULL,"
  ."  chan varchar(2) NOT NULL,"
  ."  sectype varchar(1) NOT NULL,"
  ."  radio varchar(1) NOT NULL,"
  ."  auth varchar(25) NOT NULL,"
  ."  encry varchar(25) NOT NULL,"
  ."  KEY id (id) "
  .") ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;";
$CR_TB_W0_Re = mysql_query($sqls, $conn) or die(mysql_error());

if($CR_TB_W0_Re)
{echo "<tr><td>Success..........</td><td>CREATE TABLE `wifi0`</td></tr>";}
else{
echo "<tr><td>Failure..........</td><td>CREATE TABLE `wifi0`</td></tr>";}


#Create Links table
$sqls =	"CREATE TABLE `links` ("
	."`ID` int(255) NOT NULL auto_increment,"
	."`links` varchar(255) NOT NULL,"
	."KEY `INDEX` (`ID`)"
	.") ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=6 ;";
$CR_TB_LN_Re = mysql_query($sqls, $conn) or die(mysql_error());

if($CR_TB_LN_Re)
{echo "<tr><td>Success..........</td><td>CREATE TABLE `links`</td></tr>";}
else{
echo "<tr><td>Failure..........</td><td>CREATE TABLE `links`</td></tr>";}


#Insert data into links table
$sqls =	"INSERT INTO `links` (`ID`, `links`) VALUES"
		."(1, '<a class=\"links\" href=\"$hosturl/$root/\">Main Page</a>'),"
		."(2, '<a class=\"links\" href=\"$hosturl/$root/all.php\">View All AP\'s</a>'),"
		."(3, '<a class=\"links\" href=\"$hosturl/$root/import/\">Import AP\'s</a>'),"
		."(4, '<a class=\"links\" href=\"$hosturl/$root/opt/userstats.php?func=usersall\">View All Users</a>'),"
		."(5, '<a class=\"links\" href=\"$hosturl/$root/ver.php\">WiFiDB Version</a>');";
$IN_TB_LN_Re = mysql_query($sqls, $conn) or die(mysql_error());

if($IN_TB_LN_Re)
{echo "<tr><td>Success..........</td><td>INSERT INTO `links`</td></tr>";}
else{echo "<tr><td>Failure..........</td><td>INSERT INTO `links`</td></tr>";}


if ($sqlhost !== 'localhost' or $sqlhost !== "127.0.0.1")
{$phphost = 'localhost';}
else{$phphost	=	$_POST['phphost'];}

#create WifiDB user in  WIFI
$sqls =	"GRANT ALL PRIVILEGES ON $wifi.* TO '$sqlu'@'$phphost' IDENTIFIED BY '$sqlp';";
$GR_US_WF_Re = mysql_query($sqls, $conn) or die(mysql_error());

if($GR_US_WF_Re)
{echo "<tr><td>Success..........</td><td>Created user: $sqlu @ $phphost for $wifi</td></tr>";}
else{
echo "<tr><td>Failure..........</td><td>Created user: $sqlu @ $phphost for $wifi</td></tr>";
}

#create WifiDB user in  WIFI_ST
$sqls =	"GRANT ALL PRIVILEGES ON $wifi_st.* TO '$sqlu'@'$phphost' IDENTIFIED BY '$sqlp';";
$GR_US_ST_Re = mysql_query($sqls, $conn) or die(mysql_error());

if($GR_US_WF_Re)
{echo "<tr><td>Success..........</td><td>Created user: $sqlu @ $phphost for $wifi_st</td></tr>";}
else{
echo "<tr><td>Failure..........</td><td>Created user: $sqlu @ $phphost for $wifi_st</td></tr>";}

#create config.inc.php file in /lib folder
echo '<tr><TH colspan="2"></th></tr><tr><TH colspan="2">Config.inc.php File Creation</th><tr>';
$file_ext = 'config.inc.php';
$filename = ('..\lib\\'.$file_ext);
$filewrite = fopen($filename, "w");
$fileappend = fopen($filename, "a");

if($filewrite)
{echo "<tr><td>Success..........</td><td>Created Config file</td></tr>";}
else{
echo "<tr><td>Failure..........</td><td>Creating Config file</td></tr>";}


#Add last edit date
$CR_CF_FL_Re = fwrite($fileappend, "<?php\r\n$"."lastedit	=	'$date';\r\n\r\n");

if($CR_CF_FL_Re)
{echo "<tr><td>Success..........</td><td>Add last edit date</td></tr>";}
else{
echo "<tr><td>Failure..........</td><td>Add last edit date</td></tr>";}

#add default debug values
$AD_CF_DG_Re = fwrite($fileappend, "#---------------- Debug Info ----------------#\r\n$"."rebuild	=	0;\r\n"
									."$"."debug	=	0;\r\n"
									."$"."loglev	=	0;\r\n\r\n");

if($AD_CF_DG_Re)
{echo "<tr><td>Success..........</td><td>Add default debug values</td></tr>";}
else{
echo "<tr><td>Failure..........</td><td>Add default debug values</td></tr>";}

#add url info
$AD_CF_UR_Re = fwrite($fileappend, "#---------------- URL Info ----------------#\r\n"
									."$"."root	=	'$root';\r\n"
									."$"."hosturl	=	'$hosturl';\r\n\r\n");

if($AD_CF_UR_Re)
{echo "<tr><td>Success..........</td><td>Add PHP Host URL</td></tr>";}
else{
echo "<tr><td>Failure..........</td><td>Adding PHP Host URL</td></tr>";}

#add sql host info
$AD_CF_SH_Re = fwrite($fileappend, "#---------------- SQL Host ----------------#\r\n"
									."$"."host	=	'$sqlhost';\r\n\r\n");

if($AD_CF_SH_Re)
{echo "<tr><td>Success..........</td><td>Add SQL Host info</td></tr>";}
else{
echo "<tr><td>Failure..........</td><td>Adding SQL Host info</td></tr>";}

#add Table names
$AD_CF_WT_Re = fwrite($fileappend, "#---------------- Tables ----------------#\r\n"
									."$"."settings_tb 	=	'settings';\r\n"
									."$"."users_tb 		=	'users';\r\n"
									."$"."links 			=	'links';\r\n"
									."$"."wtable 		=	'wifi0';\r\n"
									."$"."gps_ext 		=	'_GPS';\r\n"
									."$"."sep 			=	'-';\r\n\r\n");
if($AD_CF_WT_Re)
{echo "<tr><td>Success..........</td><td>Add Table names</td></tr>";}
else{
echo "<tr><td>Failure..........</td><td>Adding Table names</td></tr>";}

#add sql host info
$AD_CF_DB_Re = fwrite($fileappend, "#---------------- DataBases ----------------#\r\n"
									."$"."db			=	'$wifi';\r\n"
									."$"."db_st 		=	'$wifi_st';\r\n\r\n");
if($AD_CF_DB_Re)
{echo "<tr><td>Success..........</td><td>Add DataBase names</td></tr>";}
else{
echo "<tr><td>Failure..........</td><td>Adding DataBase names</td></tr>";}

#add sql host info
$AD_CF_SU_Re = fwrite($fileappend, "#---------------- SQL User Info ----------------#\r\n"
									."$"."db_user		=	'$sqlu';\r\n"
									."$"."db_pwd		=	'$sqlp';\r\n\r\n");
if($AD_CF_SU_Re)
{echo "<tr><td>Success..........</td><td>Add DataBase names</td></tr>";}
else{
echo "<tr><td>Failure..........</td><td>Adding DataBase names</td></tr>";}


#add sql Connection info
$AD_CF_SC_Re = fwrite($fileappend, "#---------------- SQL Connection Info ----------------#\r\n"
							."$"."conn 			=	 mysql_pconnect($"."host, $"."db_user, $"."db_pwd) or die(\"Unable to connect to SQL server: $"."host\");\r\n\r\n");
if($AD_CF_SU_Re)
{echo "<tr><td>Success..........</td><td>Add SQL Connection Info</td></tr>";}
else{
echo "<tr><td>Failure..........</td><td>Adding SQL Connection Info</td></tr>";}

#

$AD_CF_KM_Re = fwrite($fileappend, "#---------------- KML Info ----------------#\r\n"
							."$"."open_loc 		=	'http://vistumbler.sourceforge.net/images/program-images/open.png';\r\n"
							."$"."WEP_loc 		=	'http://vistumbler.sourceforge.net/images/program-images/secure-wep.png';\r\n"
							."$"."WPA_loc 		=	'http://vistumbler.sourceforge.net/images/program-images/secure.png';\r\n\r\n");
if($AD_CF_KM_Re)
{echo "<tr><td>Success..........</td><td>Add KML Info</td></tr>";}
else{
echo "<tr><td>Failure..........</td><td>Adding KML Info</td></tr>";}



fwrite($fileappend, "\r\n?>");
echo "</table>";

$filename = $_SERVER['SCRIPT_FILENAME'];
$file_ex = explode("/", $filename);
$count = count($file_ex);
$file = $file_ex[($count)-1];
if (file_exists($filename)) {
    echo "<h6><i><u>$file</u></i> was last modified: " . date ("F d Y H:i:s.", filemtime($filename)) . "</h6>";
}?>
</body>
</html>
</p>
</td>
</tr>
<tr>
<td bgcolor="#315573" height="23"><a href="/pictures/moon.png"><img border="0" src="/pictures/moon_tn.PNG"></a></td>
<td bgcolor="#315573" width="0">
</td>
</tr>
</table>
</div>
</html>