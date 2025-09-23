@echo off
rem kill8080.cmd - Kill the process that is using port 8080.  That is
rem     the default for many development tools, like Tomcat.
findportprocesses|grep "^8080" ^
    | awk "{print \"taskkill /f /pid \" $3}" ^
    | uniq ^
    | cmd