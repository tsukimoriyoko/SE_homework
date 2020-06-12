package com.example.se.data;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ParkDataSource {
    public ArrayList<JSONObject> getParks() {
        try {
            String res = get(Config.BaseUrl + "/client/MetaInfo");
            JSONObject resJson = new JSONObject(res);
            JSONArray parkArray = resJson.getJSONArray("parks");
            ArrayList<JSONObject> parkList = new ArrayList<>();
            for (int i = 0; i < parkArray.length(); i++) {
                JSONObject json = parkArray.getJSONObject(i);
                parkList.add(json);
            }
            return parkList;
        } catch (Exception e) {
            return null;
        }
    }

    private static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    private String get(String url) throws IOException, InterruptedException, JSONException {
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder
                = HttpUrl.parse(url).newBuilder();

        String newUrl = urlBuilder.build().toString();
        String bearerToken = "Bearer " + Config.token;
        Request request = new Request.Builder()
                .url(newUrl)
                .addHeader("Authorization", bearerToken)
                .build();

        CountDownLatch countDownLatch = new CountDownLatch(1);
        Call call = client.newCall(request);
        final String[] resbody = new String[1];
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);
                    String res = responseBody.string();
                    Log.d("res", res);
                    resbody[0] = res;
                    countDownLatch.countDown();
                }
            }
        });
        countDownLatch.await();
        return resbody[0];
    }
}
