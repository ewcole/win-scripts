:: toupper.cmd -- cast input to upper case.
@echo off
perl -e "while(<>) { print uc($_); }" %*