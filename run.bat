@echo off
echo Compiling and running the application...
if not exist "bin" mkdir bin
javac -d bin src\com\courier\ui\*.java
if %errorlevel% equ 0 (
    java -cp bin com.courier.ui.DispatchFrame
) else (
    echo Compilation failed!
    pause
) 