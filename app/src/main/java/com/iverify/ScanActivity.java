package com.iverify;

import okhttp3.Request;
import okhttp3.FormBody;
import okhttp3.Response;

import android.content.Context;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import org.json.JSONObject;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import android.app.Activity;
import okhttp3.OkHttpClient;
import android.os.AsyncTask;
import android.view.ViewGroup;
import android.widget.Toast;
import android.content.Intent;
import android.annotation.SuppressLint;

import com.iverify.classes.Common;
import com.iverify.classes.Scanner;
import com.iverify.classes.Utils;
import com.iverify.databinding.ActivityScanBinding;

@SuppressLint("StaticFieldLeak")
public class ScanActivity extends Activity {

    private static ActivityScanBinding binding;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Bind the UI Elements
        binding = ActivityScanBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScanActivity.this, Scanner.class);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        if ( intent != null && intent.hasExtra("result") ){
            String result = intent.getStringExtra("result");
            if (result != null){
                binding.resultTextView.setText(result);
                binding.verifyButton.setVisibility(View.VISIBLE);
                binding.scanButton.setVisibility(View.GONE);
            }
        }

        binding.verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Verify verify = new Verify();
                verify.execute();
            }
        });


    }


    public class Verify extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {

            String string = binding.resultTextView.getText().toString();

            if ( Utils.isInternetAvailable(getApplicationContext()) ){

                final String SERVER_URL = "https://catchmeifyoucan.xyz/iverify/api/verify.php";
                OkHttpClient client = new OkHttpClient();
                FormBody requestBody = new FormBody.Builder()
                        .add("encrypted_uuid", string)
                        .add("phone_number", Common.phoneNumber)
                        .build();

                Request request = new Request.Builder().url( SERVER_URL ).post(requestBody).build();
                try {
                    Response response = client.newCall(request).execute();
                    return response.body().string();
                } catch (IOException e) {e.printStackTrace();}
                return "Server error";
            }
            return "Internet not available";
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);

            if ( response.equals("Internet not available") || response.equals("Server error")){
                Toast.makeText(getApplicationContext(),""+response,Toast.LENGTH_SHORT).show();
                return;
            }

            Common.response = response;
            startActivity(new Intent(ScanActivity.this, ResultActivity.class));


            binding.resultTextView.setText("QR Code");
            binding.verifyButton.setVisibility(View.GONE);
            binding.scanButton.setVisibility(View.VISIBLE);

        }
    }

    public static int dpToPx(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dp * density);
    }



}