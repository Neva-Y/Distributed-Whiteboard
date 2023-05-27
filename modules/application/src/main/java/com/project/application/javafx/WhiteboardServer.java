package com.project.application.javafx;

import com.project.application.javafx.shapes.Shape;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WhiteboardServer extends UnicastRemoteObject implements WhiteboardServerInterface {
    private List<WhiteboardClientInterface> clients;
    private WhiteboardClientInterface managerClient;
    private List<String> clientUsernames;
    private String whiteboardState;
    private HashMap<String, WhiteboardClientInterface> userMap = new HashMap<>();

    public static void main(String[] args) {
        try {
            if (args.length == 2) {
                Registry registry = LocateRegistry.createRegistry(Integer.parseInt(args[1]));
                WhiteboardServerInterface server = new WhiteboardServer();
                registry.rebind("WhiteboardServer", server);
                System.out.println("Server started on port: " + Integer.parseInt(args[1]));

            } else {
                System.out.println("Insufficient arguments provided, please input server IP and server port");
                System.exit(0);
            }
        } catch (Exception e) {
            System.out.println("Error encountered starting server");
            e.printStackTrace();
        }
    }

    public WhiteboardServer() throws RemoteException {
        clients = new ArrayList<>();
        clientUsernames = new ArrayList<>();
    }

    @Override
    public synchronized void updateClientsWhiteboard(Shape shape) throws RemoteException {
        for (WhiteboardClientInterface client : clients) {
            client.updateWhiteboard(shape);
        }
    }

    @Override
    public synchronized void clearBoard() throws RemoteException {
        System.out.println("Clearing whiteboard");
        for (WhiteboardClientInterface client : clients) {
            client.clearBoard();
        }
    }
    @Override
    public synchronized void sendMessage(String username, String message) throws RemoteException {
        for (WhiteboardClientInterface client: clients) {
            client.writeMessage(username, message);
        }
    }
    @Override
    public synchronized void updateUserList() throws RemoteException {
        for (WhiteboardClientInterface client: clients) {
            client.updateUserList(clientUsernames);
        }
    }
    @Override
    public synchronized void broadcastMessage(String message) throws RemoteException {
        for (WhiteboardClientInterface client: clients) {
            client.showMessage(message);
        }
    }
    @Override
    public synchronized String registerClient(WhiteboardClientInterface client, String username) throws RemoteException {
        if (clients.isEmpty()) {
            managerClient = client;
        }
        clients.add(client);
        clientUsernames.add(username);
        userMap.put(username, client);
        System.out.println(username + " has joined the server");
        broadcastMessage(username + " has joined the whiteboard");
        updateUserList();
        return username;
    }
    @Override
    public synchronized void unregisterClient(WhiteboardClientInterface client, String username, String message) throws RemoteException {
        clients.remove(client);
        clientUsernames.remove(username);
        userMap.remove(username);
        broadcastMessage(username + " has left the whiteboard");
        client.disconnect(message);
        updateUserList();
    }

    @Override
    public synchronized void kickUser(String username) throws RemoteException {
        System.out.println(username + " has been kicked from the server");
        userMap.get(username).showMessage("You have been kicked out of the server");
        unregisterClient(userMap.get(username), username, "You have been kicked from the whiteboard");
    }

    @Override
    public synchronized void closeWhiteboard() throws RemoteException {
        for (WhiteboardClientInterface client : clients) {
            client.disconnect("Whiteboard has been closed");
        }
        clients = new ArrayList<>();
        clientUsernames = new ArrayList<>();
        userMap = new HashMap<>();
        managerClient = null;
        System.out.println("Whiteboard has been closed");
    }

    @Override
    public synchronized List<String> clientList() throws RemoteException {
        return clientUsernames;
    }

    @Override
    public synchronized String getWhiteboard() throws IOException {
        managerClient.updateServerWhiteboard();
        return whiteboardState;
    }

    @Override
    public synchronized void setWhiteboardState(String currentWhiteboard) throws RemoteException{
        whiteboardState = currentWhiteboard;
    }

    @Override
    public synchronized void loadWhiteboard() throws RemoteException {
        for (WhiteboardClientInterface client : clients) {
            client.loadWhiteboard();
        }
    }
}
