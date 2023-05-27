package com.project.application.javafx.shapes;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class Oval extends Shape {
    private final double widthRadius;
    private final double heightRadius;

    public Oval(double widthRadius, double heightRadius, Double lineWidth, String strokeColor, Double initialX, Double initialY) {
        super(lineWidth, strokeColor, initialX, initialY);
        this.widthRadius = widthRadius;
        this.heightRadius = heightRadius;
    }

    public double getWidthRadius() {
        return widthRadius;
    }

    public double getHeightRadius() {
        return heightRadius;
    }
}