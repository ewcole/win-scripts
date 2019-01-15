:: Kill any Mozilla Firefox processes that are currently running.
@echo off
tasklist | find "firefox" | gawk "{print \"taskkill /f /pid \" $2}" | cmd
