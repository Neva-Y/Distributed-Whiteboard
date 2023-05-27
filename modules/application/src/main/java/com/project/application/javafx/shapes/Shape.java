package com.project.application.javafx.shapes;

import java.io.Serializable;

public abstract class Shape implements Serializable {
    private final Double lineWidth;
    private final String strokeColor;
    private final Double initialX;
    private final Double initialY;

    public Shape(Double lineWidth, String strokeColor, Double initialX, Double initialY) {
        this.lineWidth = lineWidth;
        this.strokeColor = strokeColor;
        this.initialX = initialX;
        this.initialY = initialY;
    }

    public Double getLineWidth() {
        return lineWidth;
    }

    public String getStrokeColor() {
        return strokeColor;
    }

    public Double getInitialX() {
        return initialX;
    }

    public Double getInitialY() {
        return initialY;
    }
}
