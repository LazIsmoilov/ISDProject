IoTBay Web Application – ISD Assignment 2

This project extends our initial IoTBay web application by adding new features including:

Online user access
Device management
Order management
Shipment tracking
Payment processing
User management (Admin panel)


🛠 Tech Stack & Architecture

The application follows the MVC (Model-View-Controller) design pattern:

Model: JavaBean classes and database connector classes.
View: JSP files that render the UI.
Controller: Servlets for handling HTTP requests and controlling application flow.
Each major feature has its own dedicated MVC structure with clear naming conventions.

All components are integrated with a shared SQLite database named iotBayDatabase.db.

⚙️ Project Setup

✅ Prerequisites
Ensure you have the following installed:

IntelliJ IDEA
SmartTomcat Plugin
JDK 8
📁 Opening the Project
When you open the project in IntelliJ for the first time:

Configure the Tomcat server using SmartTomcat.
Set the Project SDK to JDK 8.
Refer to the image below for guidance:
📸 Insert configuration screenshot here
🗃️ Configuring the Data Source
Add a new SQLite data source in IntelliJ.
Link it to the iotBayDatabase.db file.
Refer to the screenshot for setup instructions:
📸 Insert data source configuration screenshot here
📂 Features by Module

Feature	Description
User Access	Register, login, logout, manage profile
Device Management	Add, update, search, and remove IoT devices
Order Management	Place and view orders, track order history
Shipment	Track and update shipment status
Payment	Process and store payment details
Admin Panel	Manage users, toggle access, view logs
📦 Database

The application uses a local SQLite database named:

iotBayDatabase.db
Ensure the database file exists in the root directory or adjust the path in your DBConnector.java file accordingly.

🚀 Running the Application

Once set up:

Right-click the project and select "Run on SmartTomcat".
Visit http://localhost:8080/ISDProject to access the application.
