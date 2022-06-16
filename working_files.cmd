@echo off
setlocal EnableDelayedExpansion

:: Check to see if the user has specified a theme
if "%1" == "--theme" (
  call :setTheme %2
)
if not "%theme%"=="" (
  set themestr=--eval "(load-theme '%theme%)"
)
:: echo themestr=%themestr%
gitWorkingFiles > working_files.org
::echo runemacs working_files.org --eval (read-only-mode) --eval (auto-revert-mode) %themestr%
runemacs working_files.org --eval (read-only-mode) --eval (auto-revert-mode) %themestr%
goto :eof

:setTheme
  :: echo in setTheme %1
  if not "%1" == "" (
    set theme=%1
  ) else (
    set theme=leuven
  )
  goto :eof
endlocal