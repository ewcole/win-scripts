@echo off
:: This interprets each line of standard input as the name of a file and opens it in emacs.
::   Use with care.
setlocal enableDelayedExpansion
for /f "delims=" %%f in ('cat') do (
    set files=!files! %%f
)
runemacs %files%
endlocal