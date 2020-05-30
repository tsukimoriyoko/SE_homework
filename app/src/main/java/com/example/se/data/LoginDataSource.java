package com.example.se.data;

import com.example.se.data.model.LoggedInUser;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(String username, String password) {

        try {
            // TODO: handle loggedInUser authentication

            client = new OkHttpClient();
            String json = "{\"cellphone\":" + username + "\",\"password\":\"" + password + "\"}";
            String url = "http://121.199.32.6/account/Login";
//            String res = post(url, json);
            LoggedInUser fakeUser =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            "Jane Doe");
            return new Result.Success<>(fakeUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }

    //    private static final MediaType JSON
//            = MediaType.get("application/json; charset=utf-8");
    private OkHttpClient client;
//    private String post(String url, String json) throws IOException {
//        RequestBody body = RequestBody.create(JSON, json);
//        Request request = new Request.Builder()
//                .url(url)
//                .post(body)
//                .build();
//        try (Response response = client.newCall(request).execute()) {
//            assert response.body() != null;
//            return response.body().string();
//        }
//    }
}
