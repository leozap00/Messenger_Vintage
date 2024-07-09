package com.example.messenger_vintage.controller;

import com.example.messenger_vintage.business.MessengerBusinessFactory;
import com.example.messenger_vintage.business.UserService;
import com.example.messenger_vintage.domain.Status;
import com.example.messenger_vintage.domain.User;
import com.example.messenger_vintage.view.ViewDispatcher;
import com.example.messenger_vintage.view.ViewException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class SignInController implements Initializable, DataInitializable<Object> {

    @FXML
    private TextField nameField;

    @FXML
    private TextField surnameField;

    @FXML
    private TextField userField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label registerErrorLabel;

    @FXML
    private Button signInButton;

    private User user;
    private ViewDispatcher dispatcher;
    private UserService userService;

    public SignInController() {

        dispatcher = ViewDispatcher.getInstance();
        MessengerBusinessFactory factory = MessengerBusinessFactory.getInstance();
        userService = factory.getUserService();

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        signInButton.disableProperty().bind(
                nameField.textProperty().isEmpty().or(passwordField.textProperty().isEmpty()
                        .or(userField.textProperty().isEmpty().or(surnameField.textProperty().isEmpty()))));
    }

    @FXML
    private void createAction(ActionEvent event) {
        try{
            user = new User();
            user.setName(nameField.getText());
            user.setSurname(surnameField.getText());
            user.setUsername(userField.getText());
            user.setPassword(passwordField.getText());
            user.setStatus(Status.Online.toString());

            registerErrorLabel.setText(userService.findUser(user));
            if(registerErrorLabel.getText().isEmpty()){
                user.setId(userService.buildUser(user));
                dispatcher.loggedIn(user);
            }
        } catch(Exception e){
            dispatcher.renderError(e);
        }

    }

    @FXML
    private void backAction(ActionEvent event) {
        dispatcher.renderView("login",null);

    }

}
