@echo off
for /f %%r in ('git remote') do (
    echo === %%r
    cmd /c git push %%r
)