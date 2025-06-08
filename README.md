# Courier Management System

A Java Swing application for managing courier dispatches and deliveries.

## Prerequisites

1. Java Development Kit (JDK) 8 or higher
2. MySQL Server 8.0 or higher
3. Apache Ant (for building)

## Setup Instructions

### 1. Install MySQL Server
1. Download MySQL Server from [MySQL Downloads](https://dev.mysql.com/downloads/mysql/)
2. During installation:
   - Choose "Server only" or "Custom" installation
   - Set root password to empty (or update DB_PASSWORD in DispatchFrame.java)
   - Complete the installation

### 2. Create Database
1. Open MySQL Command Line Client or MySQL Workbench
2. Run the following commands:
   ```sql
   CREATE DATABASE IF NOT EXISTS cms;
   USE cms;
   ```

### 3. Build and Run the Application
1. Open terminal in project directory
2. Run the following commands:
   ```bash
   ant clean
   ant download-mysql-connector
   ant compile
   ant jar
   ```
3. Run the application:
   ```bash
   java -jar dist/CourierManagementSystem.jar
   ```

## Features
- Dashboard with courier statistics
- Dispatch management
- Parcel tracking
- Complaint handling
- User-friendly interface

## Database Configuration
The application uses the following database configuration:
- URL: jdbc:mysql://localhost:3306/cms
- Username: root
- Password: (empty)

To change these settings, modify the constants in `src/com/courier/ui/DispatchFrame.java` 