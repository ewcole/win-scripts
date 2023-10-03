@echo off
::  Kill all Gradle Processes.  That's because Gradle Daemons don't go
::      away when they are not needed.
setlocal
for /f %%J in ('jps ^| grep Gradle') do (
    echo Killing %%J
    taskkill /f /pid %%J
)
endlocal