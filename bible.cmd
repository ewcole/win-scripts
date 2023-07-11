@echo off
setlocal
:: Use the Douay-Rheims
set version=DRA
set searchText=%*
set searchBase=https://www.biblegateway.com/passage/
start "" "%searchBase%?search=%searchText%&version=%version%
endlocal