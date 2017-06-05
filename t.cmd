@echo off
setlocal
    if "%1" == "" (
        set tmpfile=%tmp%\te_temp.dat
    ) else (
        set tmpfile="%~1"
    )
    tee "%tmpfile%"
    runemacs "%tmpfile%"
endlocal