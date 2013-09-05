@echo off
rem roots.cmd - Combine the results of Windows net use and subst commands.  
rem             This script assumes that you have the sed & grep Unix 
rem             tools on your path.
rem Author:  Ed Cole
(
  (
    net use | sed -e "s/^.\{13\}//;" | ^
              grep "^[A-Z]:" | ^
              sed -e "s/ *Microsoft Windows Network *//;" ^
  ) ^
  & subst
)|sort
