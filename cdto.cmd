@echo off
:: cdto.cmd - cd to the directory containing the file that has been
::     passed in through the first argument.
cd /d %~dp1
title %~dp1