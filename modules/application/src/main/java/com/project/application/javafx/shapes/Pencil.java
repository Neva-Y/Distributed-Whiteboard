package com.project.application.javafx.shapes;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class Pencil extends Shape {
    private final double finalX;
    private final double finalY;

    public Pencil(double finalX, double finalY, Double lineWidth, String strokeColor, Double initialX, Double initialY) {
        super(lineWidth, strokeColor, initialX, initialY);
        this.finalX = finalX;
        this.finalY = finalY;
    }

    public double getFinalX() {
        return finalX;
    }

    public double getFinalY() {
        return finalY;
    }
}
