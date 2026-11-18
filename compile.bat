@echo off
REM =========================================================================
REM  Mess Menu & Meal Preference Management System - Compile Script
REM  Student: JITWAR JHA | Roll No: PIET25CR022 | Section: 3CSD
REM =========================================================================

echo [INFO] Compiling Java source files...
if not exist "bin" mkdir bin

javac -cp ".;lib/mysql-connector-j-8.3.0.jar;src" -d bin src/database/*.java src/model/*.java src/dao/*.java src/utils/*.java src/ui/*.java src/Main.java

if %ERRORLEVEL% EQU 0 (
    echo [SUCCESS] Compilation finished with ZERO errors!
    echo [INFO] Run the project using run.bat
) else (
    echo [ERROR] Compilation failed! Please check JDK installation.
)
pause
