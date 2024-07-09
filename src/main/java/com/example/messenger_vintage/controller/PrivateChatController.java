package com.example.messenger_vintage.controller;

import com.example.messenger_vintage.business.BusinessException;
import com.example.messenger_vintage.business.MessageService;
import com.example.messenger_vintage.business.MessengerBusinessFactory;
import com.example.messenger_vintage.business.UserService;
import com.example.messenger_vintage.domain.Message;
import com.example.messenger_vintage.domain.User;
import com.example.messenger_vintage.view.View;
import com.example.messenger_vintage.view.ViewDispatcher;
import com.example.messenger_vintage.view.ViewException;
import com.vdurmont.emoji.EmojiParser;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.ResourceBundle;


public class PrivateChatController implements Initializable, DataInitializable<Pair<User,User>> {

    @FXML
    private ListView<Message> messageListView;

    @FXML
    private TextArea msgTextArea;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Label nameLabel;

    @FXML
    private Button emojiButton;

    @FXML
    private Button trilloButton;

    @FXML
    private Pane emojiPane;

    @FXML
    private Pane gifPane;

    private ObservableList<Message> msgObservableList;
    private UserService userService;
    private User user;
    private User otherUser;
    private MessageService messageService;
    private final ViewDispatcher dispatcher;
    private GifPanelController gifPanelController;

    public PrivateChatController() {
        MessengerBusinessFactory factory = MessengerBusinessFactory.getInstance();
        userService = factory.getUserService();
        messageService = factory.getMessageService();
        dispatcher = ViewDispatcher.getInstance();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        msgObservableList = FXCollections.observableArrayList();
        messageListView.setItems(msgObservableList);

        messageListView.setCellFactory(listView -> new ListCell<Message>(){
            @Override
            protected void updateItem(Message item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setGraphic(null);
                } else{
                    try{
                        View<Message> view = dispatcher.loadView("message_item");
                        MessageListItemController controller = (MessageListItemController) view.getController();
                        controller.initializeData(item);
                        setGraphic(view.getView());

                    } catch(ViewException e){
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        messageListView.setOnMouseClicked(event -> {
            if (emojiPane.isVisible()) {
                emojiPane.setVisible(false);
            }
        });
        rootPane.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            if (event.getTarget() == emojiButton) {
                return;
            }
            if(!emojiPane.getChildren().isEmpty()){
                Point2D mousePosition = new Point2D(event.getSceneX(), event.getSceneY());

                Bounds emojiPaneBounds = emojiPane.localToScene(emojiPane.getBoundsInLocal());
                if(!emojiPaneBounds.contains(mousePosition)) {
                    emojiPane.getChildren().clear();
                }
            }
        });

    }

    @Override
    public void initializeData(Pair<User,User> userData) {
        user = userData.getKey();
        otherUser = userData.getValue();
        nameLabel.setText(otherUser.getName());
        loadMessages();
    }

    @FXML
    private void sendAction(ActionEvent event) {
        try{
            String messageText = msgTextArea.getText();
            if(!messageText.isEmpty()){
                Message message = new Message();
                message.setText(messageText);
                message.setSender(user);
                message.setRecipient(otherUser);
                message.setTimeStamp(LocalDateTime.now());

                messageService.sendMessage(message);
                msgObservableList.add(message);

                messageListView.scrollTo(msgObservableList.size() - 1);

                msgTextArea.clear();
            }


        } catch(BusinessException e){
            e.printStackTrace();
        }
    }


    @FXML
    private void openEmojiPane(ActionEvent event) {

        try{
            if(emojiPane.getChildren().isEmpty()) {
                View<ScrollPane> emojiPanelView = dispatcher.loadView("emojiPanel");
                EmojiController emojiController = (EmojiController) emojiPanelView.getController();
                emojiController.setPrivateChatController(this);
                ScrollPane emojiPanel = (ScrollPane) emojiPanelView.getView();
                emojiController.initializeData(null);
                emojiPane.getChildren().add(emojiPanel);

                emojiPanel.setVisible(!emojiPanel.isVisible());
            } else {
                emojiPane.getChildren().clear();
            }
        } catch (ViewException e) {
            throw new RuntimeException(e);
        }
    }

    public void addEmojiToMessage(String emojiUnicode){
        String emojiText = EmojiParser.parseToUnicode(emojiUnicode);
        msgTextArea.setFont(Font.loadFont(getClass().getResourceAsStream(
                "resources/com/example/messenger_vintage/NotoColorEmoji-SVG.otf"), 24));
        msgTextArea.appendText(emojiText);

    }

    @FXML

    private void photoButton(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleziona una Immagine");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png","*.jpg", "*.gif"));

        File selectedFile = fileChooser.showOpenDialog(rootPane.getScene().getWindow());
        if(selectedFile != null){
            try{
                byte[] imageData = Files.readAllBytes(selectedFile.toPath());
                String base64Image = Base64.getEncoder().encodeToString(imageData);

                String messageText = msgTextArea.getText();
                messageText += "[image]" + base64Image;

                Message message = new Message();
                message.setText(messageText);
                message.setSender(user);
                message.setRecipient(otherUser);
                message.setTimeStamp(LocalDateTime.now());


                messageService.sendMessage(message);
                msgObservableList.add(message);
            }catch (IOException | BusinessException e) {
                throw new RuntimeException(e);
            } {

            }
        }
    }


    @FXML

    private void gifButton(ActionEvent event){
        try {
            if (gifPane.getChildren().isEmpty()) {
                View<ScrollPane> gifPanelView = dispatcher.loadView("gifPanel");
                gifPanelController = (GifPanelController) gifPanelView.getController();
                gifPanelController.setPrivateChatController(this);
                ScrollPane gifPanel = (ScrollPane) gifPanelView.getView();
                gifPane.getChildren().add(gifPanel);

                gifPanel.setVisible(!gifPanel.isVisible());
            } else {
                gifPane.getChildren().clear();
            }
        } catch (ViewException e) {
            throw new RuntimeException(e);
        }

    }

    public void addGifToMessage(String gifUrl){
        String messageText = msgTextArea.getText();
        msgTextArea.appendText(gifUrl);

    }

    private void loadMessages() {
        try{
            List<Message> userMessages = messageService.getMessagesBetweenUsers(user,otherUser);
            msgObservableList.setAll(userMessages);

            messageListView.scrollTo(msgObservableList.size() - 1);
        } catch (BusinessException e){
            e.printStackTrace();
        }
    }

    public void shakeButton(ActionEvent actionEvent) {
        Stage stage = (Stage) rootPane.getScene().getWindow();


        TranslateTransition ttX = new TranslateTransition(Duration.millis(50), stage.getScene().getRoot());
        ttX.setByX(10f);
        ttX.setCycleCount(20);
        ttX.setAutoReverse(true);

        TranslateTransition ttY = new TranslateTransition(Duration.millis(50), stage.getScene().getRoot());
        ttY.setByY(10f);
        ttY.setCycleCount(20);
        ttY.setAutoReverse(true);

        ttX.play();
        ttY.play();

    }
}

