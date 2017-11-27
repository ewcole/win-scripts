@echo off
reg query hkey_classes_root | grep \\\. | sed -e "s/^/reg query /;" | cmd | grep -v "reg query"
