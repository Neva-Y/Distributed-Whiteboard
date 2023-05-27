module Test {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.swing;
    requires java.rmi;
    requires java.desktop;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires org.slf4j;
    requires spring.boot;

    opens com.project.application.javafx;
    opens com.project.application.javafx.shapes;
}