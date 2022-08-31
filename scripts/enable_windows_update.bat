@Echo Starting the Windows Update Service...
@echo off
Net start wuauserv
@Echo Enabling the Windows Update Service...
@echo off
sc config "wuauserv" start=auto
pause