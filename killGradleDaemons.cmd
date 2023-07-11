@echo off
jps | grep GradleDaemon | awk "/^[0-9]+/{print \"taskkill /p \" $1}"