@ECHO OFF
ECHO Processing backup. Copying only source files that are newer than existing destination files.
xcopy /f /s /h /d D:\Backup\Desktop C:\Users\etern\Desktop\
xcopy /f /s /h /d D:\Backup\Documents C:\Users\etern\Documents\
xcopy /f /s /h /d D:\Backup\Downloads C:\Users\etern\Downloads\
echo d | xcopy /f /s /h /d D:\backup\Bookmarks C:\Users\etern\AppData\Roaming\"Opera Software"\"Opera GX Stable"\Bookmarks
echo d | xcopy /f /s /h /d D:\backup\Bookmarks  C:\Users\etern\AppData\Roaming\"Opera Software"\"Opera GX Stable"\BookmarksExtras
PAUSE