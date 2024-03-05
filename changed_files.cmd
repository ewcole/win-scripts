@echo off
rem **********************************************************************
rem ** Create an org-mode file with a listing of the uncommited changes
rem **   according to git.
rem **********************************************************************
rem **
rem ** usage: changedFiles [destination]
rem **
rem **********************************************************************
rem ** If the destination is not specified, it will be junk.org
rem **********************************************************************

rem ** Look for the output file name.
setlocal
set targetFile=junk.org
if not "%1" == "" (
  set targetFile=%1
)
git status -u -s | sed s/^...//; | tworkingfiles | t %targetFile%
endlocal