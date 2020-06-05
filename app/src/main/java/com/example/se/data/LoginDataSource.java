package com.example.se.data;

import android.util.Log;

import com.example.se.data.model.LoggedInUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {
    public interface NetworkCallback {
        void onSuccess(String response);

        void onFailure();
    }

    public Result<LoggedInUser> login(String cellphone, String password) {
        try {
            // TODO: handle loggedInUser authentication

            String hashed_pw = MD5.getMd5(password);
            String json = "{\"cellphone\":\"" + cellphone + "\",\"password\":\"" + hashed_pw + "\"}";
            String url = Config.BaseUrl + "/account/Login";
            String res = post(url, json);
            JSONObject resj = new JSONObject(res);
            if (resj.getInt("err_no") != 0) {
                String err = resj.getString("err_tips");
                return new Result.Error(new Exception(err));
            } else {
                token = resj.getString("token");
                Config.token = token;
            }
            String url2 = Config.BaseUrl + "/client/UserInfo";
            String userName = getName(url2, cellphone);
            LoggedInUser user =
                    new LoggedInUser(java.util.UUID.randomUUID().toString(), userName);
//            LoggedInUser fakeUser =
//                    new LoggedInUser(
//                            java.util.UUID.randomUUID().toString(),
//                            "Jane Doe");
            return new Result.Success<>(user);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }

    public String token;

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

    private String getName(String url, String cellphone) throws IOException, InterruptedException, JSONException {
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
        JSONObject resj = new JSONObject(resbody[0]);
        if (resj.getInt("err_no") != 0) {
            return resj.getString("err_tips");
        } else {
            return resj.getString("nickname");
        }
    }

//    private String readToken() {
//        String ret = "";
//        try {
//            InputStream inputStream = context.openFileInput("token.txt");
//
//            if ( inputStream != null ) {
//                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//                String receiveString = "";
//                StringBuilder stringBuilder = new StringBuilder();
//                while ( (receiveString = bufferedReader.readLine()) != null ) {
//                    stringBuilder.append("\n").append(receiveString);
//                }
//                inputStream.close();
//                ret = stringBuilder.toString();
//            }
//        }
//        catch (FileNotFoundException e) {
//            Log.e("login activity", "File not found: " + e.toString());
//        } catch (IOException e) {
//            Log.e("login activity", "Can not read file: " + e.toString());
//        }
//        return ret;
//    }

//    private boolean writeToken() {
//        ;
//    }
}
