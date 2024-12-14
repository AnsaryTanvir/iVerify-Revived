package com.iverify.classes;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegistrationHandle {

    private static final String API_URL     = "https://catchmeifyoucan.xyz/iverify/api/register.php";

    public interface RegistrationHandlerCallback {
        void onRegistrationSuccess(String response);
        void onRegistrationFailure(String errorMessage);
    }

    public static void register(String fullName,String phoneNumber, String password, RegistrationHandlerCallback callback ){

        //OkHttpClient client = new OkHttpClient();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10  , TimeUnit.SECONDS)     // Time to establish a connection
                .readTimeout(10     , TimeUnit.SECONDS)     // Time to read data
                .writeTimeout(10    , TimeUnit.SECONDS)     // Time to write data
                .build();

        // Prepare request data
        RequestBody body = new FormBody.Builder()
                .add("fullName"  , fullName)
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
                        callback.onRegistrationSuccess(responseBody);
                    } else {
                        callback.onRegistrationFailure("Failed to Register User: " + response.message());
                    }
                } catch (IOException e) {
                    callback.onRegistrationFailure("Error Registering User: " + e.getMessage());
                }
            }
        }).start();
    }
}
