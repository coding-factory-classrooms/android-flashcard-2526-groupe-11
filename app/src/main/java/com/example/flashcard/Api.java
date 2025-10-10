package com.example.flashcard;

import android.util.Log;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class Api {


    private OkHttpClient client = new OkHttpClient();

    public void getApi(String url, ApiCallback callback) {
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError(e);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) {
                        callback.onError(new IOException("Unexpected code " + response));
                        return;
                    }

                    String result = responseBody != null ? responseBody.string() : "";
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<Card>>(){}.getType();
                    ArrayList<Card> allCards = gson.fromJson(result, listType);
                    callback.onSuccess(allCards);

                } catch (IOException e) {
                    callback.onError(e);
                }
            }
        });
    }

}
