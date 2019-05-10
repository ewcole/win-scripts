:: Kill any Mozilla Firefox processes that are currently running.
@echo off
tasklist | gawk "/firefox/{print \"taskkill /f /pid \" $2}" | cmd
