@echo off
gitWorkingFiles > working_files.org
runemacs working_files.org --eval (read-only-mode)

