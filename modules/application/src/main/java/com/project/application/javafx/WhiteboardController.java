package com.project.application.javafx;

import com.project.application.javafx.shapes.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.embed.swing.SwingFXUtils;
import javafx.stage.FileChooser;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;
import java.rmi.RemoteException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;


public class WhiteboardController {

    private static final int CANVAS_WIDTH = 887;
    private static final int CANVAS_HEIGHT = 522;
    private static Canvas canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private Slider slider;
    @FXML
    private Label label;
    @FXML
    private ListView listView;
    @FXML
    private Button kickButton;
    @FXML
    private MenuBar menuBar;
    @FXML
    private Pane pane;
    @FXML
    private TextField msgField;
    @FXML
    private ScrollPane msgPane;
    @FXML
    private Button pencil;
    @FXML
    private Button eraser;
    @FXML
    private Button rectangle;
    @FXML
    private Button oval;
    @FXML
    private Button circle;
    @FXML
    private Button text;
    @FXML
    private Button line;
    @FXML
    private Button send;
    static private TextArea msgArea = new TextArea();
    private Boolean isManager = false;
    private enum ShapeType { RECTANGLE, LINE, CIRCLE, OVAL, TEXT, PENCIL, ERASER }
    private ShapeType shape = ShapeType.PENCIL;

    private String saveFilePath = "";

    private double[] shapeCoordinate = {0, 0};

    private double[] pencilCoordinate = {0, 0};

    private double[] lineCoordinate = {0, 0};

    private String selectedUser = null;

    private WhiteboardClientInterface client;

    private WhiteboardServerInterface server;


    public void registerClientServer(WhiteboardClientInterface newClient, WhiteboardServerInterface server) throws IOException {
        this.client = newClient;
        this.server = server;
        this.isManager = newClient.getManager();
        if (!isManager) {
            menuBar.setVisible(false);
            kickButton.setVisible(false);
            server.getWhiteboard();
        }
    }

    public void initialize() {
        msgArea.setPrefSize(1200, 270);
        msgArea.setWrapText(true);
        msgPane.setContent(msgArea);
        msgPane.setFitToWidth(true);
        msgArea.setEditable(false);
        pane.getChildren().add(msgArea);
        pane.getChildren().add(canvas);
        initLeftButtons();
        initDrawMethods();
    }

    private void initLeftButtons() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        pane.setStyle("-fx-background-color: white");
        label.setText("1.0");

        colorPicker.setValue(Color.BLACK);
        colorPicker.setOnAction(e -> {
            gc.setStroke(colorPicker.getValue());
            gc.setFill(colorPicker.getValue());
        });
        slider.setMin(1);
        slider.setMax(10);
        slider.valueProperty().addListener(e -> {
            double value = slider.getValue();
            String str = String.format("%.1f", value);
            label.setText(str);
            gc.setFont(Font.font("Arial", 2 * slider.getValue() + 14));
            gc.setLineWidth(value);
        });
        msgField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                sendMessage();
            }
        });
        listView.setOnMouseClicked(event -> {
            selectedUser = (String) listView.getSelectionModel().getSelectedItem();
        });
    }

    @FXML
    private void onClickPencil() {
        shape = ShapeType.PENCIL;
    }

    @FXML
    private void onClickEraser() {
        shape = ShapeType.ERASER;
    }

    @FXML
    private void onClickRectangle() {
        shape = ShapeType.RECTANGLE;
    }

    @FXML
    private void onClickLine() {
        shape = ShapeType.LINE;
    }

    @FXML
    private void onClickCircle() {
        shape = ShapeType.CIRCLE;
    }

    @FXML
    private void onClickOval() {
        shape = ShapeType.OVAL;
    }

    @FXML
    private void onClickText() {
        shape = ShapeType.TEXT;
    }

    @FXML
    private void onClickKick() throws RemoteException {
        if (selectedUser != null && !selectedUser.equals(client.getUsername()) && isManager) {
            server.kickUser(selectedUser);
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setTitle("Information Alert");
            alert.setContentText("Unable to kick yourself");
            alert.showAndWait();
        }
    }

    private void initDrawMethods() {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        canvas.setOnMousePressed(e -> {
            double x = e.getX();
            double y = e.getY();
            if (shape.equals(ShapeType.PENCIL)) {
                pencilCoordinate[0] = x;
                pencilCoordinate[1] = y;
            } else if (shape.equals(ShapeType.LINE)) {
                lineCoordinate[0] = x;
                lineCoordinate[1] = y;
            } else if (shape.equals(ShapeType.CIRCLE) || shape.equals(ShapeType.RECTANGLE) || shape.equals(ShapeType.OVAL)) {
                shapeCoordinate[0] = x;
                shapeCoordinate[1] = y;
            } else if (shape.equals(ShapeType.TEXT)) {
                TextInputDialog inputText = new TextInputDialog();
                inputText.setTitle("Text input");
                inputText.setHeaderText("Enter any text");
                Optional<String> content = inputText.showAndWait();
                content.ifPresent(s -> sendDrawingToServer(new Text(s, gc.getLineWidth(), gc.getStroke().toString(), x, y)));
            }
        });

        canvas.setOnMouseReleased(e -> {
            double x = e.getX();
            double y = e.getY();
            double originX = shapeCoordinate[0];
            double originY = shapeCoordinate[1];
            double cornerX = (originX - x > 0) ? x :originX;
            double cornerY = (originY - y > 0) ? y : originY;
            switch (shape) {
                case LINE:
                    sendDrawingToServer(new Line(x, y, gc.getLineWidth(), gc.getStroke().toString(), lineCoordinate[0], lineCoordinate[1]));
                    break;
                case RECTANGLE:
                    sendDrawingToServer(new Rectangle(Math.abs(x - originX), Math.abs(y - originY), gc.getLineWidth(),
                            gc.getStroke().toString(), cornerX, cornerY));
                    break;
                case CIRCLE:
                    double centreX = (originX + x) / 2;
                    double centreY = (originY + y) / 2;
                    double radiiDistance = Math.sqrt(Math.pow(x - originX, 2) + Math.pow(y - originY, 2));
                    sendDrawingToServer(new Circle(radiiDistance, gc.getLineWidth(), gc.getStroke().toString(),
                            (centreX - radiiDistance / 2), (centreY - radiiDistance / 2)));
                    break;
                case OVAL:
                    sendDrawingToServer(new Oval(Math.abs(x - originX), Math.abs(y - originY), gc.getLineWidth(),
                            gc.getStroke().toString(), cornerX, cornerY));
                    break;
            }
        });

        canvas.setOnMouseDragged(e -> {
            double x = e.getX();
            double y = e.getY();
            String msg = "";
            if (shape.equals(ShapeType.PENCIL)) {
                sendDrawingToServer(new Pencil(x, y, gc.getLineWidth(), gc.getStroke().toString(),
                        pencilCoordinate[0], pencilCoordinate[1]));
                pencilCoordinate[0] = x;
                pencilCoordinate[1] = y;

            } else if (shape.equals(ShapeType.ERASER)) {
                sendDrawingToServer(new Erase(slider.getValue(), gc.getLineWidth(), gc.getStroke().toString(), x, y));
            }
        });
    }

    public void save() throws IOException {
        if (!saveFilePath.equals("")) {
            SnapshotParameters snapshot = new SnapshotParameters();
            snapshot.setFill(Color.TRANSPARENT);
            WritableImage writableImage = canvas.snapshot(snapshot, null);
            File file = new File(saveFilePath);
            String[] saveToFile = saveFilePath.split("[.]");
            ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), saveToFile[1], file);
        } else {
            saveAs();
        }
    }

    public void saveAs() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save As");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(
                "Image Files", "*.png"));
        File selectedFolder = fileChooser.showSaveDialog(null);
        String filePath = selectedFolder.getAbsolutePath();
        File newFile = new File(filePath);
        SnapshotParameters snapshot = new SnapshotParameters();
        snapshot.setFill(Color.TRANSPARENT);
        WritableImage writableImage = canvas.snapshot(snapshot, null);
        ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", newFile);
        saveFilePath = newFile.getPath();
    }

    public void newCanvas() throws RemoteException {
        saveFilePath = "";
        server.clearBoard();
        server.loadWhiteboard();
    }

    public void clearCanvas() throws IOException {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
        exportWhiteboard();
    }

    public void open() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(
                "Image Files", "*.png"));
        File selectedFile  = fileChooser.showOpenDialog(null);
        File openFile = new File(selectedFile.getAbsolutePath());
        try (FileInputStream fileInputStreamReader = new FileInputStream(openFile)) {
            byte[] bytes = new byte[(int) openFile.length()];
            fileInputStreamReader.read(bytes);
            server.setWhiteboardState(Base64.getEncoder().encodeToString(bytes));
            server.loadWhiteboard();
            saveFilePath = selectedFile.getAbsolutePath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() throws RemoteException {
        server.closeWhiteboard();
    }

    public void updateUserList(List<String> users) {
        ObservableList<String> observableUsers = FXCollections.observableArrayList(users);
        listView.setItems(observableUsers);
    }

    public void sendDrawingToServer(Shape drawing) {
        try {
            server.updateClientsWhiteboard(drawing);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error sending drawing to server");
        }
    }

    public void updateWhiteBoard(Shape drawing) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Paint originalColor = gc.getStroke();
        double originalLineWidth = gc.getLineWidth();
        Font originalFont = gc.getFont();
        Color newColor = Color.web(drawing.getStrokeColor(), 1.0);
        gc.setStroke(newColor);
        gc.setFill(newColor);
        gc.setLineWidth(drawing.getLineWidth());
        gc.setFont(Font.font("Arial", 2 * drawing.getLineWidth() + 14));
        double x = drawing.getInitialX();
        double y = drawing.getInitialY();
        if (drawing instanceof Oval) {
            gc.strokeOval(x, y, ((Oval) drawing).getWidthRadius(), ((Oval) drawing).getHeightRadius());
        } else if (drawing instanceof Rectangle) {
            gc.strokeRect(x, y, ((Rectangle) drawing).getWidth(), ((Rectangle) drawing).getHeight());
        } else if (drawing instanceof Circle) {
            gc.strokeOval(x, y, ((Circle) drawing).getRadius(), ((Circle) drawing).getRadius());
        } else if (drawing instanceof Pencil) {
            gc.strokeLine(x, y, ((Pencil) drawing).getFinalX(), ((Pencil) drawing).getFinalY());
        } else if (drawing instanceof Text) {
            gc.fillText(((Text) drawing).getMessage(), x, y, 500);
        } else if (drawing instanceof Line) {
            gc.strokeLine(x, y, ((Line) drawing).getFinalX(), ((Line) drawing).getFinalY());
        } else if (drawing instanceof Erase) {
            gc.clearRect(x, y, ((Erase) drawing).getSize(), ((Erase) drawing).getSize());
        } else {
            System.out.println("Unexpected shape");
        }
        gc.setStroke(originalColor);
        gc.setFill(originalColor);
        gc.setLineWidth(originalLineWidth);
        gc.setFont(originalFont);
    }
    
    public void sendMessage() {
        try {
            String message = msgField.getText();
            if (!message.isEmpty()) {
                server.sendMessage(client.getUsername(), message);
                msgField.clear();
            }
        } catch (RemoteException e) {
            System.out.println("Error Sending message");
        }
    }

    public void writeMessage(String username, String message) {
        msgArea.appendText(username + ": " + message + "\n");
    }

    public void disconnect(String message) {
        disableGui();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Alert");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void disableGui() {
        canvas.setDisable(true);
        pencil.setDisable(true);
        eraser.setDisable(true);
        rectangle.setDisable(true);
        oval.setDisable(true);
        circle.setDisable(true);
        send.setDisable(true);
        text.setDisable(true);
        line.setDisable(true);
        menuBar.setDisable(true);
        kickButton.setDisable(true);
        msgField.setDisable(true);
        listView.setDisable(true);
        colorPicker.setDisable(true);
        slider.setDisable(true);
        msgArea.setDisable(true);
        msgPane.setDisable(true);
    }

    public void exportWhiteboard() throws IOException {
        SnapshotParameters snapshot = new SnapshotParameters();
        snapshot.setFill(Color.TRANSPARENT);
        WritableImage writableImage = canvas.snapshot(snapshot, null);
        RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
        ByteArrayOutputStream byteArrayImage = new ByteArrayOutputStream();
        ImageIO.write(renderedImage, "png", byteArrayImage);
        server.setWhiteboardState(Base64.getEncoder().encodeToString(byteArrayImage.toByteArray()));
    }

    public void loadWhiteboard() throws IOException {
        String base64Image = server.getWhiteboard();
        GraphicsContext gc = canvas.getGraphicsContext2D();
        byte[] whiteboard = Base64.getDecoder().decode(base64Image);
        ByteArrayInputStream byteArrayImage = new ByteArrayInputStream(whiteboard);
        BufferedImage bufferedImage = ImageIO.read(byteArrayImage);
        Image image = SwingFXUtils.toFXImage(bufferedImage, null);
        gc.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
        gc.drawImage(image, 0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
    }
}

