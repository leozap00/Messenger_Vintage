package com.example.messenger_vintage.controller;



import com.example.messenger_vintage.business.GiphyService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class GifPanelController implements Initializable {

    private GiphyService giphyService;
    private PrivateChatController privateChatController;


    @FXML
    private FlowPane gifFlowPane;

    @FXML
    private TextField searchField;

    public GifPanelController() {
        giphyService = new GiphyService();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setPrivateChatController(PrivateChatController privateChatController) {
        this.privateChatController = privateChatController;
    }

    @FXML
    private void searchGifs() {
        gifFlowPane.getChildren().clear();
        String query = searchField.getText();
        if(query != null && !query.isEmpty()) {
            try{
                String jsonResponse = giphyService.searchGifs(query, 10);
                List<String> gifUrls = parseGifUrlsFromJson(jsonResponse);
                for (String url : gifUrls) {
                    ImageView imageView = createGifImageView(url);
                    gifFlowPane.getChildren().add(imageView);
                }
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    private List<String> parseGifUrlsFromJson(String jsonResponse){
        List<String> gifUrls = new ArrayList<>();

        JSONObject jsonObject = new JSONObject(jsonResponse);
        JSONArray data = jsonObject.getJSONArray("data");
        for(int i = 0; i < data.length(); i++){
            JSONObject gif = data.getJSONObject(i);
            String gifUrl = gif.getJSONObject("images").getJSONObject("original").getString("url");
            gifUrls.add(gifUrl);
        }

        return gifUrls;
    }

    private ImageView createGifImageView(String url) {
        ImageView imageView = new ImageView(new Image(url));
        imageView.setFitWidth(150); // Set width as needed
        imageView.setPreserveRatio(true);
        imageView.setOnMouseClicked(event -> selectGif(url)); // Select GIF on mouse click
        return imageView;
    }

    private void selectGif(String gifUrl) {
        if (privateChatController != null) {
            privateChatController.addGifToMessage(gifUrl); // Add selected GIF to message in PrivateChatController
        }
    }

}
