package com.example.messenger_vintage.controller;

import com.example.messenger_vintage.business.MessengerBusinessFactory;
import com.example.messenger_vintage.business.UserService;
import com.example.messenger_vintage.domain.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class UserListItemController implements Initializable,DataInitializable<User> {

    @FXML
    private Label nameLabel;

    @FXML
    private Label surnameLabel;

    @FXML
    private Label statusLabel;

    private UserService userService;
    private User user;

    public UserListItemController(){
        MessengerBusinessFactory factory = MessengerBusinessFactory.getInstance();
        userService = factory.getUserService();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @Override
    public void initializeData(User user) {
        this.user = user;
        nameLabel.setText(user.getName());
    }
}
