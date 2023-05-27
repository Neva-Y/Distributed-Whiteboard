package com.project.application.javafx;

import com.project.application.javafx.shapes.Shape;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface WhiteboardServerInterface extends Remote {
    void updateClientsWhiteboard(Shape shape) throws RemoteException;
    void clearBoard() throws RemoteException;
    void updateUserList() throws RemoteException;
    void broadcastMessage(String message) throws RemoteException;
    String registerClient(WhiteboardClientInterface client, String username) throws RemoteException;
    void unregisterClient(WhiteboardClientInterface client, String username, String message) throws RemoteException;
    void sendMessage(String username, String message) throws RemoteException;
    void kickUser(String username) throws RemoteException;
    void closeWhiteboard() throws RemoteException;
    List<String> clientList() throws RemoteException;
    String getWhiteboard() throws IOException;
    void setWhiteboardState(String currentWhiteboard) throws RemoteException;
    void loadWhiteboard() throws RemoteException;
}

