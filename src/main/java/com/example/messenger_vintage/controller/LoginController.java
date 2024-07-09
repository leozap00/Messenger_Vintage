package com.example.messenger_vintage.controller;

import com.example.messenger_vintage.business.BusinessException;
import com.example.messenger_vintage.business.MessengerBusinessFactory;
import com.example.messenger_vintage.business.UserNotFoundException;
import com.example.messenger_vintage.business.UserService;
import com.example.messenger_vintage.domain.Status;
import com.example.messenger_vintage.domain.User;
import com.example.messenger_vintage.view.ViewDispatcher;
import com.example.messenger_vintage.view.ViewException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable, DataInitializable<Object> {

    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Button loginButton;
    @FXML
    private Button signInButton;
    @FXML
    private Label loginErrorLabel;

    private User user;

    private ViewDispatcher dispatcher;

    private UserService userService;

    public LoginController() {
        dispatcher = ViewDispatcher.getInstance();
        MessengerBusinessFactory factory = MessengerBusinessFactory.getInstance();
        userService = factory.getUserService();
    }

    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {
        loginButton.disableProperty().bind(username.textProperty().isEmpty().or(password.textProperty().isEmpty()));

    }

    @FXML
    private void loginAction(ActionEvent event) {
        try{
            user = userService.authenticate(username.getText(), password.getText());
            user.setStatus(Status.Online.toString());
            userService.editUser(user);
            dispatcher.loggedIn(user);
        } catch(UserNotFoundException e){
            loginErrorLabel.setVisible(true);
        } catch (BusinessException e){
            e.printStackTrace();
            dispatcher.renderError(e);
        }
    }

    @FXML
    private void signInAction(ActionEvent event) {
        System.out.println("Sign-in button pressed");
        dispatcher.renderView("signin",null);
    }
}
