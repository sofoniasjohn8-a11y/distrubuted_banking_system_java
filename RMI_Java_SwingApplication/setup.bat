@echo off
REM Windows batch script to set up and compile the Bank Management RMI project

echo.
echo ====================================================
echo Bank Management System - Setup Script
echo ====================================================
echo.

REM Check if lib directory exists, create it if not
if not exist "lib" (
    echo Creating lib directory...
    mkdir lib
)

REM Check if SQLite JDBC driver exists
if not exist "lib\sqlite-jdbc-3.44.0.0.jar" (
    echo.
    echo ====================================================
    echo Downloading SQLite JDBC Driver...
    echo ====================================================
    echo.
    echo NOTE: You need to manually download sqlite-jdbc-3.44.0.0.jar from:
    echo https://github.com/xerial/sqlite-jdbc/releases
    echo.
    echo Place the JAR file in the 'lib' directory
    echo.
    pause
) else (
    echo SQLite JDBC driver found: lib\sqlite-jdbc-3.44.0.0.jar
)

REM Compile Java files with SQLite JDBC in classpath
echo.
echo ====================================================
echo Compiling Java files...
echo ====================================================
echo.

if exist "lib\sqlite-jdbc-3.44.0.0.jar" (
    javac -cp lib\sqlite-jdbc-3.44.0.0.jar *.java
    if %ERRORLEVEL% EQU 0 (
        echo.
        echo Compilation successful!
        echo.
        echo Next steps:
        echo 1. Start RMI Registry in a new terminal:
        echo    rmiregistry
        echo.
        echo 2. Start the Server in another terminal:
        echo    java -cp .;lib\sqlite-jdbc-3.44.0.0.jar Server
        echo.
        echo 3. Run the Client in a third terminal:
        echo    java -cp .;lib\sqlite-jdbc-3.44.0.0.jar Client
        echo.
    ) else (
        echo.
        echo Compilation failed. Please check the error messages above.
        echo.
    )
) else (
    echo SQLite JDBC driver not found. Please download it first.
    echo Download from: https://github.com/xerial/sqlite-jdbc/releases
    echo Place it in the 'lib' directory.
)

echo.
pause
