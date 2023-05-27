package com.project.application.javafx.shapes;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
public class Text extends Shape {
    private final String message;
    public Text(String message, Double lineWidth, String strokeColor, Double initialX, Double initialY) {
        super(lineWidth, strokeColor, initialX, initialY);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}