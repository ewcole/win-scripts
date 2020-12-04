@echo off
:: This interprets each line of standard input as the name of a file and opens it in emacs.
::   Use with care.
cat | sed -e "s/^/emacs /;" | cmd
