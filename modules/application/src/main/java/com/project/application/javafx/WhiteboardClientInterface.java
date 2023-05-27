package com.project.application.javafx;

import com.project.application.javafx.shapes.Shape;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface WhiteboardClientInterface extends Remote {
    void updateWhiteboard(Shape shape) throws RemoteException;
    void clearBoard() throws RemoteException;
    void writeMessage(String username, String message) throws RemoteException;
    void showMessage(String message) throws RemoteException;
    Boolean getManager() throws RemoteException;
    String getUsername() throws RemoteException;
    void updateUserList(List<String> Users) throws RemoteException;
    void disconnect(String message) throws RemoteException;
    void updateServerWhiteboard() throws RemoteException;
    void loadWhiteboard() throws RemoteException;
}