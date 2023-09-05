@echo off
rem Usage:  recurse <command>
rem 
rem Walk the submodules recursively and execute the command at
rem every level.  By default, it steps into git submodules, but
rem if there is a recurse script in a subdirectory, it will call
rem that one instead.  

setlocal
set command=%*
rem Get the subdirectories from .gitmodules.  If it doesn't
rem exit, don't process any subdirectories
if exist .gitmodules (
    for /f %%f in ('awk "/path += +/{print $3}" ^< .gitmodules') do (
        if not "%%f" == "echo" (
            call :processDir %%f
        )
    )
)
rem After the subdirectories are done, process the current directory.
for %%f in (.) do (
   echo # %%~ff
   %command%
 )
goto :eof

:processDir
    rem Replace slashes with backslashes in the directory name
    for /f %%d in ('echo %1 ^| sed s/\//\\/g;') do (
        set dirName=%%d
    )
    if exist %dirName% (
        pushd .\%dirName%
        rem Now that we are in the subdirectory, call
        rem    recurse again.  We use cmd /c because it has
        rem    to be in a separate process.  Also, if there
        rem    is a recurse script in the subdirectory, it
        rem    will use that one instead.
        cmd /c recurse %command%
        popd
    )
    goto :eof
endlocal