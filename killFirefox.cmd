:: Kill any Mozilla Firefox processes that are currently running.
@echo off
tasklist | find "firefox" | awk "{print \"taskkill /f /pid \" $2}" | cmd
