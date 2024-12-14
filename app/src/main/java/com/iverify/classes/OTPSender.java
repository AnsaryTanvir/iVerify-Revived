package com.iverify.classes;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.FormBody;
import okhttp3.Response;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;


public class OTPSender {

    private static final String API_URL     = "http://bulksmsbd.net/api/smsapi";
    private static final String API_KEY     = "BXBz5LqiFz430WkouPTU";   // Your API Key
    private static final String SENDER_ID   = "8809617623174";          // Approved Sender ID


    public static final int SUCCESS_SMS_SUBMITTED = 202;
    public static final int ERROR_INVALID_NUMBER = 1001;
    public static final int ERROR_SENDER_ID_NOT_CORRECT = 1002;
    public static final int ERROR_REQUIRED_FIELDS_MISSING = 1003;
    public static final int ERROR_INTERNAL_ERROR = 1005;
    public static final int ERROR_BALANCE_VALIDITY_NOT_AVAILABLE = 1006;
    public static final int ERROR_BALANCE_INSUFFICIENT = 1007;
    public static final int ERROR_USER_ID_NOT_FOUND = 1011;
    public static final int ERROR_MASKING_SMS_BENGALI_REQUIRED = 1012;
    public static final int ERROR_SENDER_ID_NOT_FOUND_GATEWAY = 1013;
    public static final int ERROR_SENDER_TYPE_NAME_NOT_FOUND = 1014;
    public static final int ERROR_SENDER_ID_NOT_FOUND_VALID_GATEWAY = 1015;
    public static final int ERROR_SENDER_TYPE_NAME_ACTIVE_PRICE_NOT_FOUND = 1016;
    public static final int ERROR_SENDER_TYPE_NAME_PRICE_NOT_FOUND = 1017;
    public static final int ERROR_ACCOUNT_OWNER_DISABLED = 1018;
    public static final int ERROR_SENDER_TYPE_NAME_PRICE_DISABLED = 1019;
    public static final int ERROR_PARENT_ACCOUNT_NOT_FOUND = 1020;
    public static final int ERROR_PARENT_SENDER_TYPE_NAME_PRICE_NOT_FOUND = 1021;
    public static final int ERROR_ACCOUNT_NOT_VERIFIED = 1031;
    public static final int ERROR_IP_NOT_WHITELISTED = 1032;

    // Define an interface for callback
    public interface OTPSenderCallback {
        void onOTPSendSuccess(String response);
        void onOTPSendFailure(String errorMessage);
    }

    public static void sendOtp(String phoneNumber, String otp, OTPSenderCallback callback) {

        //OkHttpClient client = new OkHttpClient();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10  , TimeUnit.SECONDS)  // Time to establish a connection
                .readTimeout(10     , TimeUnit.SECONDS)     // Time to read data
                .writeTimeout(10    , TimeUnit.SECONDS)    // Time to write data
                .build();

        // Format the message: "Your iVerify OTP is XXXX"
        String messageContent = "Your iVerify OTP is " + otp;

        // Prepare request data
        RequestBody body = new FormBody.Builder()
                .add("api_key"  , API_KEY)
                .add("senderid" , SENDER_ID)
                .add("number"   , phoneNumber)
                .add("message"  , messageContent)
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
                        callback.onOTPSendSuccess(responseBody);
                    } else {
                        callback.onOTPSendFailure("Failed to send SMS: " + response.message());
                    }
                } catch (IOException e) {
                    callback.onOTPSendFailure("Error sending SMS: " + e.getMessage());
                }
            }
        }).start();
    }

    /**
     * Generates a random 4-digit OTP (One Time Password).
     * <p>
     * This method generates a random 4-digit integer within the range of 1000 to 9999
     * and returns it as a string
     * </p>
     *
     * @return A 4-digit OTP as a string. The value will always be between "1000" and "9999".
     */
    public static String generateOTP() {

        Random random = new Random();
        int randomNumber = 1000 + random.nextInt(9000);
        return String.valueOf(randomNumber);
    }
}
