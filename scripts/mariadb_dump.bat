echo off 
"C:\Program Files\MariaDB 10.6\bin\mysqldump.exe" --user root --password=YourPassword DatabaseName --result-file="C:\SaveFileName_%date%.sql" --databases DatabaseName
PAUSE