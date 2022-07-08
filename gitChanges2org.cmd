:: Create a file of updated files since the last commit and open it.
@echo off
cmd /c git status -u -s | sed s/^...//; | tworkingfiles > junk.org
runemacs junk.org