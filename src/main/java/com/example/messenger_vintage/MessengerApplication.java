package com.example.messenger_vintage;

import com.example.messenger_vintage.view.ViewDispatcher;
import com.example.messenger_vintage.view.ViewException;
import javafx.application.Application;
import javafx.stage.Stage;


public class MessengerApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {

        String dbUrl = ConfigReader.getDatabaseUrl();
        String dbUsername = ConfigReader.getDatabaseUsername();
        String dbPassword = ConfigReader.getDatabasePassword();

        System.out.println("Database URL: " + dbUrl);
        System.out.println("Database Username: " + dbUsername);
        System.out.println("Database Password: " + dbPassword);

        try{
            ViewDispatcher dispatcher = ViewDispatcher.getInstance();
            dispatcher.loginView(stage);
        } catch (ViewException e){
            e.printStackTrace();
        }
    }
}