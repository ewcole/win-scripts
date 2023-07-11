:: Start a new cmd window with the given title.  This is significant
::    because Windows keeps the display settings by the window title.
@echo off
for /d %%d in (.) do (
    cmd /c start "%%~nd" %*
)