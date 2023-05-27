package com.project.application.javafx.shapes;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class Erase extends Shape {
    private final double size;

    public Erase(double size, Double lineWidth, String strokeColor, Double initialX, Double initialY) {
        super(lineWidth, strokeColor, initialX, initialY);
        this.size = size;
    }

    public double getSize() {
        return size;
    }
}
