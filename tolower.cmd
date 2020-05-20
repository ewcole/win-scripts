:: tolower.cmd -- cast input to lower case.
@echo off
perl -e "while(<>) { print lc($_); }"  %*