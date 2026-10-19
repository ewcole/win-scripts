@echo off
:: purge_gitkeep.cmd - Delete unneccessary .gitkeep
::   files.  These files are used by developers to
::   make Git track an otherwise empty directory.
::   If the directory is no longer necessary, this
::   file becomes a nuisance.


set dive=dir /s /b .gitkeep
for /f %%G in ('%dive%') do (
    call :check_dir %%G
)
goto :eof

:check_dir
   for /f "usebackq" %%f in (`dir /b %~dp1 ^| grep -v \.gitkeep  ^| wc -l`) do (
      if not "%%f" == "0" (
        echo Deleting %~f1
        del %1
        rem echo ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        rem echo del %1
        rem dir %~dp1 | awk "{print \":: \" $0}"
        rem echo ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
      )
   )
   goto :eof