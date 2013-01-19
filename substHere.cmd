:: make the current directory a root of the system using the subst command.
@echo off
setlocal
cmd /c ptitle
if "%1" neq "" (
    set driveNum=%1
)
if "%driveNum%" equ "" (
   call :setDriveNum
)
for /d %%d in (.) do (
    echo Connecting directory to %driveNum%
    subst %driveNum% "%%~fd"
)
subst
goto :eof

:setDriveNum
    for /f %%f in ('nextFreeDrive') do (
        set driveNum=%%f
    )
    goto :eof
endlocal