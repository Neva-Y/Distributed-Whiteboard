package com.project.application.javafx;

import com.project.application.javafx.shapes.Shape;
import javafx.application.Platform;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class WhiteboardClient extends UnicastRemoteObject implements WhiteboardClientInterface {
    private WhiteboardController controller;
    private final String username;
    private final boolean isManager;

    public WhiteboardClient(WhiteboardController controller, String username, boolean isManager) throws RemoteException {
        this.username = username;
        this.controller = controller;
        this.isManager = isManager;
    }

    @Override
    public void updateWhiteboard(Shape shape) throws RemoteException {
        controller.updateWhiteBoard(shape);
    }

    @Override
    public void showMessage(String message) throws RemoteException {
        controller.writeMessage("", message);
    }

    @Override
    public void writeMessage(String username, String message) {
        controller.writeMessage(username, message);
    }
    @Override
    public void clearBoard() throws RemoteException {
        Platform.runLater(() -> {
            try {
                controller.clearCanvas();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public Boolean getManager() throws RemoteException{
        return isManager;
    }

    @Override
    public String getUsername() throws RemoteException{
        return username;
    }

    @Override
    public void updateUserList(List<String> users) throws RemoteException {
        Platform.runLater(() -> {
            controller.updateUserList(users);
        });
    }
    @Override
    public void disconnect(String message) throws RemoteException {
        Platform.runLater(() -> {
            controller.disconnect(message);
        });
    }

    @Override
    public void updateServerWhiteboard() throws RemoteException {
        Platform.runLater(() -> {
            try {
                controller.exportWhiteboard();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Unable to initialise whiteboard canvas");
            }
        });
    }

    @Override
    public void loadWhiteboard() throws RemoteException {
        Platform.runLater(() -> {
            try {
                controller.loadWhiteboard();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Unable to initialise whiteboard canvas");
            }
        });
    }
}
