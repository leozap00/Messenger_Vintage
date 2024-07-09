module com.example.messenger_vintage {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires emoji.java;
    requires retrofit2;
    requires okhttp3;
    requires gson;
    requires retrofit2.converter.gson;
    requires json;
    requires java.sql;

    opens com.example.messenger_vintage to javafx.fxml;
    exports com.example.messenger_vintage;
    exports com.example.messenger_vintage.controller;
    opens com.example.messenger_vintage.controller to javafx.fxml;
}