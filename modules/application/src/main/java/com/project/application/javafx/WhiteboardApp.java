package com.project.application.javafx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

public class WhiteboardApp extends Application {
    private WhiteboardServerInterface server;
    private WhiteboardClientInterface client;
    public static final String validUsername = "^[a-zA-Z][a-zA-Z0-9_]{3,19}$";

    public static void main(String[] args) {
        if (args.length == 3) {
            Application.launch(args);
        } else {
            System.out.println("Insufficient arguments provided, please input server IP, server port and username");
            System.exit(0);
        }
    }

    @Override
    public void start(Stage primaryStage) throws IOException, NotBoundException {
        // FXML setup
        URL path = getClass().getResource("/fxml/Whiteboard.fxml");
        FXMLLoader loader = new FXMLLoader(path);
        Parent root = loader.load();
        WhiteboardController controller = loader.getController();
        String host = getParameters().getRaw().get(0);
        int port = Integer.parseInt(getParameters().getRaw().get(1));

        // Get server
        Registry registry = LocateRegistry.getRegistry(host, port);
        server = (WhiteboardServerInterface) registry.lookup("WhiteboardServer");
        boolean isManager = false;

        // Register client to server
        List<String> users = server.clientList();
        if (users.isEmpty()) {
            isManager = true;
        } else {
            Platform.runLater(() -> {
                try {
                    server.getWhiteboard();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } finally {
                    try {
                        client.loadWhiteboard();
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }

        // Register user
        String username = getParameters().getRaw().get(2);
        if (!username.matches(validUsername)) {
            System.out.println("Invalid username provided");
            System.exit(0);
        }
        else if (!users.contains(username)) {
            client = new WhiteboardClient(controller, username, isManager);
            String newUser = server.registerClient(client, username);
            System.out.println("Welcome user " + newUser);
        } else {
            System.out.println("Username has already been taken");
            System.exit(0);
        }
        controller.registerClientServer(client, server);

        // Set the stage
        primaryStage.setTitle("Whiteboard App");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    @Override
    public void stop() throws Exception {
        if (client.getManager()) {
            server.closeWhiteboard();
        } else {
            server.unregisterClient(client, client.getUsername(), "App closed");
        }
        Platform.exit();
        System.exit(0);
    }
}
