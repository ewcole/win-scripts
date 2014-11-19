:: The temporary directories in Windows can be 
:: a serious performance problem if they are 
:: allowed to grow unchecked.  This script 
:: deletes everything that is not currently in 
:: use by another application.
@echo off
:: Loop through the temporary directories and 
:: purge each one.
for /d %%d in ("%tmp%") do (
   call :purge_temp "%%~d"
)
goto :eof

:purge_temp
     pushd "%~1"
     :: We are now in the directory - will  
     ::     return to starting place when done.
     :: delete files
     for %%f in (*) do del "%%~f"
     :: delete directories
     for /d %%d in (*) do rmdir /s /q "%%~d" 
     dir "%%~d"
     popd
     goto :eof