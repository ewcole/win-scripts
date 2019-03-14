@echo off
if "%1" neq "" (
    cd /d %*
)
for /f %%f in ('cd') do title %%~nf