#!/bin/bash

# Linux/Mac setup script for Bank Management RMI project

echo ""
echo "===================================================="
echo "Bank Management System - Setup Script"
echo "===================================================="
echo ""

# Create lib directory if not exists
if [ ! -d "lib" ]; then
    echo "Creating lib directory..."
    mkdir lib
fi

# Check if SQLite JDBC driver exists
if [ ! -f "lib/sqlite-jdbc-3.44.0.0.jar" ]; then
    echo ""
    echo "===================================================="
    echo "SQLite JDBC Driver Not Found"
    echo "===================================================="
    echo ""
    echo "Please download sqlite-jdbc-3.44.0.0.jar from:"
    echo "https://github.com/xerial/sqlite-jdbc/releases"
    echo ""
    echo "Place the JAR file in the 'lib' directory"
    echo ""
    read -p "Press Enter after placing the JAR file..."
else
    echo "SQLite JDBC driver found: lib/sqlite-jdbc-3.44.0.0.jar"
fi

# Compile Java files with SQLite JDBC in classpath
echo ""
echo "===================================================="
echo "Compiling Java files..."
echo "===================================================="
echo ""

if [ -f "lib/sqlite-jdbc-3.44.0.0.jar" ]; then
    javac -cp lib/sqlite-jdbc-3.44.0.0.jar *.java
    if [ $? -eq 0 ]; then
        echo ""
        echo "Compilation successful!"
        echo ""
        echo "Next steps:"
        echo "1. Start RMI Registry in a new terminal:"
        echo "   rmiregistry &"
        echo ""
        echo "2. Start the Server in another terminal:"
        echo "   java -cp .:lib/sqlite-jdbc-3.44.0.0.jar Server"
        echo ""
        echo "3. Run the Client in a third terminal:"
        echo "   java -cp .:lib/sqlite-jdbc-3.44.0.0.jar Client"
        echo ""
    else
        echo ""
        echo "Compilation failed. Please check the error messages above."
        echo ""
    fi
else
    echo "SQLite JDBC driver not found. Please download it first."
    echo "Download from: https://github.com/xerial/sqlite-jdbc/releases"
    echo "Place it in the 'lib' directory."
fi

echo ""
