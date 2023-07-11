@echo off
setlocal
:: Use the Vulgate
set version=VULGATE
set searchText=%*
set searchBase=https://www.biblegateway.com/passage/
start "" "%searchBase%?search=%searchText%&version=%version%
endlocal