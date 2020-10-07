@echo off
pushd .
if "%1" neq "" (
    cd /d %*
)
for /f %%f in ('cd') do start "%%~nf-grails"
popd
