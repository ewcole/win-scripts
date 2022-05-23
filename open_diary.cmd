:: Open and edit an ewc-diary.org file
@echo off
if not exist ewc-diary.org (
   copy %~dp0\ewc-diary.template.org ewc-diary.org
)
runemacs ewc-diary.org --eval "(goto-char(point-max))"
