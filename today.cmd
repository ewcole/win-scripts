@echo off
rem Print today's date in XML form.
setlocal
set two_digits_group=\([0-9]\{2\}\)
echo %date% | sed -e "s/.*%two_digits_group%\/%two_digits_group%\/\([0-9]\{4\}\)/\3-\1-\2/;"
endlocal