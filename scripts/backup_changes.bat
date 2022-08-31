@ECHO OFF
ECHO Processing backup. Copying only source files that are newer than existing destination files.
xcopy /f /s /h /d C:\Users\etern\Desktop\ D:\Backup\Desktop
xcopy /f /s /h /d C:\Users\etern\Documents\ D:\Backup\Documents
xcopy /f /s /h /d C:\Users\etern\Downloads\ D:\Backup\Downloads
echo d | xcopy /f /s /h /d C:\Users\etern\AppData\Roaming\"Opera Software"\"Opera GX Stable"\Bookmarks D:\backup\Bookmarks
echo d | xcopy /f /s /h /d C:\Users\etern\AppData\Roaming\"Opera Software"\"Opera GX Stable"\BookmarksExtras D:\backup\Bookmarks
xcopy /f /s /h /d C:\*.pdf D:\Ebooks\PDF
xcopy /f /s /h /d C:\*.epub D:\Ebooks\EPUB
xcopy /f /s /h /d C:\*.chm D:\Ebooks\CHM
xcopy /f /s /h /d C:\*.djvu D:\Ebooks\DJVU
xcopy /f /s /h /d C:\*.docx D:\Word_Documents
xcopy /f /s /h /d C:\*.doc D:\Word_Documents
xcopy /f /s /h /d C:\*.png D:\Images\PNG
xcopy /f /s /h /d C:\*.jpeg D:\Images\JPEG
xcopy /f /s /h /d C:\*.jpg D:\Images\JPG
xcopy /f /s /h /d C:\*.gif D:\Images\GIF
PAUSE