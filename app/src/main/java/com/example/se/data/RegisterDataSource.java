package com.example.se.data;

import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class RegisterDataSource {
    public Result<String> register(String cellphone, String password,
                                   String nickname, String bind_code) {
        try {
            String hashed_pw = MD5.getMd5(password);
            String json = "{\"cellphone\":\"" + cellphone + "\",\"password\":\"" + hashed_pw
                    + "\",\"nickname\":\"" + nickname + "\",\"bind_code\":\"" + bind_code + "\"}";
            String url = Config.BaseUrl + "/account/Register";

            String res = post(url, json);

            JSONObject resJson = new JSONObject(res);
            if (resJson.getInt("err_no") != 0) {
                String err = resJson.getString("err_tips");
                return new Result.Error(new Exception(err));
            } else {
                return new Result.Success<>("Register success");
            }
        } catch (Exception e) {
            return new Result.Error(new IOException("Error registering", e));
        }
    }

    public Result<String> getCaptcha(String cellphone) {
        try {
            String url = Config.BaseUrl + "/account/Verify";
            String json = "{\"cellphone\":\"" + cellphone + "\"}";

            String res = post(url, json);

            JSONObject resJson = new JSONObject(res);
            if (resJson.getInt("err_no") != 0) {
                String err = resJson.getString("err_tips");
                return new Result.Error(new Exception(err));
            } else {
                return new Result.Success<>("Get captcha success");
            }
        } catch (Exception e) {
            return new Result.Error(new IOException("Error getting captcha", e));
        }
    }

    private static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    private String post(String url, String json) throws IOException, InterruptedException {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
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
                    Log.d("resresres", res);
                    resbody[0] = res;
                    countDownLatch.countDown();
                }
            }
        });
        countDownLatch.await();
        return resbody[0];
    }
}
