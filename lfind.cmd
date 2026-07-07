@echo off
setlocal enableDelayedExpansion
for /f %%F in ('where find ^| find /v "Windows"') do (
  set linuxFind=%%F
)
%linuxFind% %*
endlocal