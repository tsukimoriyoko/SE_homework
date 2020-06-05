package com.example.se.data;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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

public class Parking {
    public int park(int carport_id) {
        String url = Config.BaseUrl + "/client/Park";
        String json = "{\"car_port_id\":" + carport_id + "}";
        try {
            String res = post(url, json);
            JSONObject resJson = new JSONObject(res);
            return resJson.getInt("err_no");
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
    public int pickup(int carport_id) {
        String url = Config.BaseUrl + "/client/PickUp";
        String json = "{\"car_port_id\":" + carport_id + "}";
        try {
            String res = post(url, json);
            JSONObject resJson = new JSONObject(res);
            return resJson.getInt("err_no");
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int getParkedCarport() {
        try {
            return Integer.getInteger(getUserInfo("car_port_id"));
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int getUnpayedBillId() {
        try {
            return Integer.getInteger(getUserInfo("bills"));
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int pay(int bill_id) {
        String url = Config.BaseUrl + "/client/Pay";
        String json = "{\"bill_id\":" + bill_id + "}";
        try {
            String res = post(url, json);
            JSONObject resJson = new JSONObject(res);
            return resJson.getInt("err_no");
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private String post(String url, String json) throws InterruptedException {
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, json);
        String bearerToken = "Bearer " + Config.token;

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", bearerToken)
                .post(body)
                .build();

        CountDownLatch countDownLatch = new CountDownLatch(1);
        Call call = client.newCall(request);
        final String[] resBody = new String[1];
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
                    assert responseBody != null;
                    String res = responseBody.string();
                    Log.d("res", res);
                    resBody[0] = res;
                    countDownLatch.countDown();
                }
            }
        });
        countDownLatch.await();
        return resBody[0];
    }

    private String getUserInfo(String tag) throws InterruptedException, JSONException {

        String url = Config.BaseUrl + "/client/UserInfo";
        String cellphone = Config.cellphone;
        String token = Config.token;

        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder
                = HttpUrl.parse(url).newBuilder();
        urlBuilder.addQueryParameter("cellphone", cellphone);

        String newUrl = urlBuilder.build().toString();
        String bearerToken = "Bearer " + token;
        Request request = new Request.Builder()
                .url(newUrl)
                .addHeader("Authorization", bearerToken)
                .build();

        CountDownLatch countDownLatch = new CountDownLatch(1);
        Call call = client.newCall(request);
        final String[] resBody = new String[1];
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
                    assert responseBody != null;
                    String res = responseBody.string();
                    Log.d("res", res);
                    resBody[0] = res;
                    countDownLatch.countDown();
                }
            }
        });
        countDownLatch.await();
        JSONObject resJson = new JSONObject(resBody[0]);
        if (resJson.getInt("err_no") != 0) {
            return resJson.getString("err_tips");
        } else {
            switch (tag) {
                case "nickname":
                    return resJson.getString(tag);
                case "car_port_id":
                    return resJson.getString(tag);
                case "bills":
                    JSONArray bills = resJson.getJSONArray(tag);
                    JSONObject bill = bills.getJSONObject(0);
                    return bill.getString("id");
            }
        }
        return "unknown error";
    }

    private static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");
}
