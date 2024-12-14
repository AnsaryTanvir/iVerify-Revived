package com.iverify.classes;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginHandler {

    private static final String API_URL     = "https://catchmeifyoucan.xyz/iverify/api/login.php";

    public interface LoginHandlerCallback {
        void onLoginSuccess(String response);
        void onLoginFailure(String errorMessage);
    }

    public static void login(String phoneNumber, String password, LoginHandlerCallback callback ){

        //OkHttpClient client = new OkHttpClient();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10  , TimeUnit.SECONDS)     // Time to establish a connection
                .readTimeout(10     , TimeUnit.SECONDS)     // Time to read data
                .writeTimeout(10    , TimeUnit.SECONDS)     // Time to write data
                .build();

        // Prepare request data
        RequestBody body = new FormBody.Builder()
                .add("phoneNumber" , phoneNumber)
                .add("password"   , password)
                .build();

        // Build the request
        Request request = new Request.Builder()
                .url(API_URL)
                .post(body)
                .build();

        // Make the request in a separate thread ( network operations cannot run on the main thread)
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String responseBody = response.body().string();
                        callback.onLoginSuccess(responseBody);
                    } else {
                        callback.onLoginFailure("Failed to Login User: " + response.message());
                    }
                } catch (IOException e) {
                    callback.onLoginFailure("Error Logging in User: " + e.getMessage());
                }
            }
        }).start();
    }
}
