package com.example.messenger_vintage.controller;

import com.example.messenger_vintage.business.MessageService;
import com.example.messenger_vintage.business.MessengerBusinessFactory;
import com.example.messenger_vintage.domain.Message;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.Base64;
import java.util.ResourceBundle;

public class MessageListItemController implements Initializable, DataInitializable<Message> {

    @FXML
    private Label userLabel;

    @FXML
    private TextArea msgTextArea;

    @FXML
    private ImageView imageView;

    @FXML
    private VBox contentBox;


    public MessageListItemController(){
        MessengerBusinessFactory factory = MessengerBusinessFactory.getInstance();
        MessageService messageService = factory.getMessageService();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @Override
    public void initializeData(Message message) {
        String messageText = message.getText();

        userLabel.setText(message.getSender().getName() + " scrive:");

        contentBox.getChildren().remove(imageView);

        if(messageText.contains("[image]")) {
            String[] parts = messageText.split("\\[image\\]",2);
            if(parts.length >1){
                String base64Image = parts[1];
                byte[] imageData = Base64.getDecoder().decode(base64Image);
                Image image = new Image(new ByteArrayInputStream(imageData));
                imageView.setImage(image);
                imageView.setVisible(true);

                contentBox.getChildren().add(imageView);

                msgTextArea.setText(parts[0]);
            }
        } else {
            imageView.setVisible(false);
            msgTextArea.setText(messageText);
        }
        msgTextArea.setEditable(false);
    }
}
