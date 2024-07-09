package com.example.messenger_vintage.controller;

import com.example.messenger_vintage.business.BusinessException;
import com.example.messenger_vintage.business.MessengerBusinessFactory;
import com.example.messenger_vintage.business.UserNotFoundException;
import com.example.messenger_vintage.business.UserService;
import com.example.messenger_vintage.domain.Status;
import com.example.messenger_vintage.domain.User;
import com.example.messenger_vintage.view.View;
import com.example.messenger_vintage.view.ViewDispatcher;
import com.example.messenger_vintage.view.ViewException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.util.Pair;


import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

public class HomepageController implements Initializable, DataInitializable<User> {

    @FXML
    private ListView<User> userListView;

    @FXML
    private Label usernameLabel;

    @FXML
    private MenuButton statusButton;

    private ObservableList<User> userList;
    private UserService userService;
    private User user;
    private final ViewDispatcher dispatcher;
    private Status currentUserStatus;

    public HomepageController(){
        MessengerBusinessFactory factory = MessengerBusinessFactory.getInstance();
        userService = factory.getUserService();
        dispatcher = ViewDispatcher.getInstance();
    }

    @Override
    public void initializeData(User user){

        this.user = user;
        this.currentUserStatus = user.getStatus();
        usernameLabel.setText(user.getUsername());
        statusButton.setText(user.getStatus().toString());
        updateStatusMenu();

        loadUsers();
    }

    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {
        userList = FXCollections.observableArrayList();
        userListView.setItems(userList);

        userListView.setCellFactory(listView -> new ListCell<User>() {
            @Override
            protected void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || user == null) {
                    setText(null);
                    setGraphic(null);
                } else{
                    try {
                        View<User> view = dispatcher.loadView("user_list_item");
                        UserListItemController controller = (UserListItemController) view.getController();
                        controller.initializeData(item);
                        setGraphic(view.getView());
                    } catch (ViewException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        userListView.setOnMouseClicked(event -> {
            if(event.getButton() == MouseButton.PRIMARY && event.getClickCount()== 2) {
                User selectedUser = userListView.getSelectionModel().getSelectedItem();
                if(selectedUser != null) {
                    openPrivateChat(selectedUser);
                }
            }
        });

        for (Status status : Status.values()) {
            MenuItem item = new MenuItem(status.toString());
            item.setOnAction(event ->{
                user.setStatus(status.toString());
                try{
                    userService.editUser(user);
                    currentUserStatus = status;
                    statusButton.setText(currentUserStatus.toString());
                } catch(BusinessException e) {
                    e.printStackTrace();
                }
            });
            statusButton.getItems().add(item);
        }
    }

    private void openPrivateChat(User otherUser) {
        dispatcher.openNewWindow("private_chat",new Pair<>(user,otherUser));
    }



    private void loadUsers() {
        try {
            Set<User> users = userService.getUsers();
            for (User u : users) {
                if (!u.getId().equals(user.getId())) {
                    userList.add(u);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateStatusMenu() {
        statusButton.getItems().clear();
        for(Status status: Status.values()) {
            if(!status.equals(currentUserStatus)) {
                MenuItem item = new MenuItem(status.toString());
                item.setOnAction(event -> {
                    user.setStatus(status.toString());
                    try{
                        userService.editUser(user);
                        currentUserStatus = status;
                        statusButton.setText(currentUserStatus.toString());
                        updateStatusMenu();
                    } catch (BusinessException e) {
                        e.printStackTrace();
                    }
                });
                statusButton.getItems().add(item);
            }
        }
        statusButton.setText(currentUserStatus.toString());
    }
}
