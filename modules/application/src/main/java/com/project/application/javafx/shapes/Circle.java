package com.project.application.javafx.shapes;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class Circle extends Shape {
    private final double radius;
    public Circle(double radius, Double lineWidth, String strokeColor, Double initialX, Double initialY) {
        super(lineWidth, strokeColor, initialX, initialY);
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }

}
