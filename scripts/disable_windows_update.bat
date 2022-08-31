@Echo Stopping the Windows Update Service...
@echo off
Net stop wuauserv
@Echo Disabling the Windows Update Service...
@echo off
sc config "wuauserv" start=disabled
pause