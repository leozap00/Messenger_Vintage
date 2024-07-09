package com.example.messenger_vintage.business;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class GiphyService {

    private static final String GIPHY_API_KEY = "qN6nJZYrJLnlKkQhCMiOXDhrPtrrdF48";
    private static final String GIPHY_BASE_URL = "https://api.giphy.com/v1/gifs/search";

    private final OkHttpClient client;

    public GiphyService() {
        this.client = new OkHttpClient();
    }

    public String searchGifs(String query, int i) throws IOException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(GIPHY_BASE_URL).newBuilder();
        urlBuilder.addQueryParameter("api_key", query);
        urlBuilder.addQueryParameter("q", query);

        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .build();

        try(Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
}

