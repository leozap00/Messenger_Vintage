package com.example.messenger_vintage.view;

import com.example.messenger_vintage.controller.DataInitializable;
import com.example.messenger_vintage.domain.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewDispatcher {

    private static final String RESOURCE_BASE = "/com/example/messenger_vintage/";
    private static final String FXML_SUFFIX = ".fxml";
    public static ViewDispatcher instance = new ViewDispatcher();
    private Stage stage;
    private TabPane tabPane;

    private ViewDispatcher() {
    }

    public static ViewDispatcher getInstance() {return instance;}

    public void loginView (Stage stage) throws ViewException{
        this.stage = stage;
        Parent login = loadView("login").getView();
        Scene scene = new Scene(login);
        stage.setScene(scene);
        stage.show();

    }

    public void loggedIn(User user){
        try {
            View<User> homeView = loadView("homepage");
            DataInitializable<User> homeController = homeView.getController();
            homeController.initializeData(user);

            Scene scene = new Scene(homeView.getView());
            //scene.getStylesheets().add(getClass().getResource(RESOURCE_BASE + "styles.css").toExternalForm());
            stage.setScene(scene);

        } catch(ViewException e){
            renderError(e);
        }
        }

    public void renderError (Exception e) {
        e.printStackTrace();
        System.exit(1);
    }

    public <T> View<T> loadView(String viewName) throws ViewException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(RESOURCE_BASE + viewName + FXML_SUFFIX));
            Parent parent = loader.load();
            return new View<>(parent, loader.getController());
        } catch (IOException e) {
            throw new ViewException(e);
        }
    }

    public <T> void renderView (String viewName, T data) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(RESOURCE_BASE + viewName + FXML_SUFFIX));
            Parent parent = loader.load();
            DataInitializable<T> controller = loader.getController();
            if (controller != null) {
                controller.initializeData(data);
            }

            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> void openNewWindow(String viewName, T data) {
        try {
            View<T> view = loadView(viewName);
            DataInitializable<T> controller = view.getController();
            controller.initializeData(data);

            Stage newStage = new Stage();
            Scene scene = new Scene(view.getView());
            newStage.setScene(scene);
            newStage.show();
        } catch (ViewException e) {
            renderError(e);
        }
    }

}
