# Distributed Whiteboard App

This is a distributed whiteboard application that utilises Java RMI for the server and JavaFX library for the GUI interface

<p align="center"> 
    <img src="Images/DrawingTest.png" alt="Example of the App" width="700">
 </p>

## Running the server 
```
java -jar WhiteboardServer.jar "host IP" "port"
```

## Running the app
```
java -jar WhiteboardApp.jar "host IP" "port" "username"
```

## System Design
<p align="center">
    <img src="Images/UML_P1.jpeg" alt="Whiteboard App and Controller UML" width="700">
    <br>
    <strong>Figure 1:</strong> Whiteboard Application and Controller Class Diagram.
</p>

<p align="center">
    <img src="Images/UML_P2.png" alt="Whiteboard Server and Client UML" width="700">
    <br>
    <strong>Figure 2:</strong> Whiteboard Server and Client Interaction Diagram.
</p>

<p align="center">
    <img src="Images/UML_P3.png" alt="Shape Interface UML" width="700">
    <br>
    <strong>Figure 3:</strong> Shape Interface Class Diagram.
</p>
