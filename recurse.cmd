@echo off
setlocal
set command=%*
if exist .gitmodules (
    for /f %%f in ('awk "/path += +/{print $3}" ^< .gitmodules') do (
        if not "%%f" == "echo" (
            call :processDir %%f
        )
    )
)
for %%f in (.) do (
   echo # %%~ff
   %command%
 )
goto :eof

:processDir
    :: echo Processing %1
    for /f %%d in ('echo %1 ^| sed s/\//\\/g;') do (
        set dirName=%%d
    )
    :: echo dirName=%dirName%
    if exist %dirName% (
        pushd %dirName%
        :: echo After push to %dirName%
        for %%c in (.) do (echo # %%~fc)
        :: echo after echo cd
        recurse %command%
        popd
    )
    goto :eof
endlocal