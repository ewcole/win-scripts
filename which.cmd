@echo off
rem groovy %~dp0\which.groovy %*
where %*  | head -n1
