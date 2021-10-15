@echo off
rem  backupFile.cmd -- Create a copy of the current file with the 
rem                    modification date in its name.
rem  This uses an awk procedure.

dir "%1" | awk -f %~dp0backup_file.awk