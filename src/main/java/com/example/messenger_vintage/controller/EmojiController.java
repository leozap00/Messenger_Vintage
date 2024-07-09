package com.example.messenger_vintage.controller;

import com.example.messenger_vintage.emoji.*;
import com.vdurmont.emoji.Emoji;
import com.vdurmont.emoji.EmojiManager;
import com.vdurmont.emoji.Fitzpatrick;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.File;
import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;

public class EmojiController implements Initializable, DataInitializable<ScrollPane> {

    @FXML
    private FlowPane emojiFlowPane;

    @FXML
    private ToolBar emojiToolBar;

    @FXML
    private Button activityButton;

    @FXML
    private Button diversityButton;

    @FXML
    private Button flagsButton;

    @FXML
    private Button foodButton;

    @FXML
    private Button natureButton;

    @FXML
    private Button objectsButton;

    @FXML
    private Button peopleButton;

    @FXML
    private Button regionalButton;

    @FXML
    private Button symbolsButton;

    @FXML
    private Button travelButton;

    private PrivateChatController privateChatController;

    public void setPrivateChatController(PrivateChatController privateChatController) {
        this.privateChatController = privateChatController;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeEmojiButtons();
        loadEmojis(People.class);

    }

    private void initializeEmojiButtons() {
        activityButton.setOnAction(event -> loadEmojis(Activity.class));
        flagsButton.setOnAction(event -> loadEmojis(Flags.class));
        foodButton.setOnAction(event -> loadEmojis(Food.class));
        natureButton.setOnAction(event -> loadEmojis(Nature.class));
        objectsButton.setOnAction(event -> loadEmojis(Objects.class));
        peopleButton.setOnAction(event -> loadEmojis(People.class));
        symbolsButton.setOnAction(event -> loadEmojis(Symbols.class));
        travelButton.setOnAction(event -> loadEmojis(Travel.class));
    }


    private void loadEmojis(Class<? extends Enum<?>> emojiCategory) {
        emojiFlowPane.getChildren().clear(); // Pulisce il contenuto precedente, se presente

        for (Enum<?> emoji : emojiCategory.getEnumConstants()) {
            Emoji emojiObj = EmojiManager.getForAlias(emoji.name());
            System.out.println(emoji.name());
            if (emojiObj != null) {
                    Text emojiText = new Text(emojiObj.getUnicode());
                    emojiText.setStyle("-fx-font-size: 24");
                    emojiText.setFont(Font.loadFont(getClass().getResourceAsStream(
                            "resources/com/example/messenger_vintage/NotoColorEmoji-SVG.otf"), 24));

                    emojiFlowPane.getChildren().add(emojiText);

                    emojiText.setOnMouseClicked(event -> {
                        if (privateChatController != null) {
                            privateChatController.addEmojiToMessage(emojiObj.getUnicode());
                        } else {
                            System.err.println("PrivateChatController non inizializzato");
                        }
                    });
            }
        }
    }

}
