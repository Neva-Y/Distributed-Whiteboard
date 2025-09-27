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
    <img src="Images/UML_P1.png" alt="UML1" width="700">
    <figcaption>Whiteboard App and Controller.</figcaption>
 </p>

<p align="center"> 
    <img src="Images/UML_P2.png" alt="UML1" width="700">
    <figcaption>Whiteboard Server and Client</figcaption>
 </p>

<p align="center"> 
    <img src="Images/UML_P3.png" alt="UML1" width="700">
    <figcaption>Shape Interface</figcaption>
 </p>
 
