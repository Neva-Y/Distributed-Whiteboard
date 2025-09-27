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
<figure align="center">
    <img src="Images/UML_P1.jpeg" alt="Whiteboard App and Controller UML" width="700">
    <figcaption>Figure 1: Whiteboard Application and Controller Class Diagram.</figcaption>
</figure>

---

<figure align="center">
    <img src="Images/UML_P2.png" alt="Whiteboard Server and Client UML" width="700">
    <figcaption>Figure 2: Whiteboard Server and Client Interaction Diagram.</figcaption>
</figure>

---

<figure align="center">
    <img src="Images/UML_P3.png" alt="Shape Interface UML" width="700">
    <figcaption>Figure 3: Shape Interface Class Diagram.</figcaption>
</figure>
