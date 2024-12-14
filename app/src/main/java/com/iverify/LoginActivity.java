package com.iverify;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.iverify.classes.Common;
import com.iverify.classes.LoginHandler;
import com.iverify.classes.SharedPreferencesHelper;
import com.iverify.databinding.ActivityLoginBinding;

@SuppressLint({"StaticFieldLeak", "ObsoleteSdkInt"})
public class LoginActivity extends AppCompatActivity implements LoginHandler.LoginHandlerCallback {

    static {
        System.loadLibrary("iverify");
    }

    private static ActivityLoginBinding binding;
    private static boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide the ActionBar
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        // Bind the UI Elements
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        updateUIElements();
        bindListeners();
        handleAutoLogin();
    }


    public void updateUIElements(){

        // Set HTML text to the heading TextView
        {
            String htmlText = "<font face='monospace'><b><font color='#ff0000'>i</font><font color='#12a56b'>Verify</font></b></font>";
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                binding.headingTextView.setText(Html.fromHtml(htmlText, Html.FROM_HTML_MODE_COMPACT));
            } else {
                binding.headingTextView.setText(Html.fromHtml(htmlText));
            }
        }

        // Set initial state for visibility icon
        {
            binding.passwordEditText.setTransformationMethod(new PasswordTransformationMethod()); // Hide password by default
            binding.visibilityButton.setImageResource(R.drawable.show); // Set the initial icon to 'view'
        }

    }

    public void handleAutoLogin(){
        if (SharedPreferencesHelper.areCredentialsSaved(this)) {
            String[] credentials = SharedPreferencesHelper.getCredentials(this);
            String phoneNumber = credentials[0];
            String password   = credentials[1];
            binding.phoneNumberEditText.setText(phoneNumber);
            binding.passwordEditText.setText(password);
        }
    }

    @Override
    public void onLoginSuccess(String response) {
        runOnUiThread(() -> {

            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

            if ( !response.equals("Welcome") ){
                binding.progressBar.setVisibility(View.GONE);
                binding.loginButton.setVisibility(View.VISIBLE);
                return;
            }
            if ( Common.isStoreCredential ){
                SharedPreferencesHelper.saveCredentials(getApplicationContext(), Common.phoneNumber, Common.password);
            }
            Intent intent = new Intent(LoginActivity.this, ScanActivity.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    public void onLoginFailure(String errorMessage) {
        Log.d("iVerify" , "onUserRegistrationFailure: " + errorMessage);
        runOnUiThread(() -> {
            Toast.makeText(getApplicationContext(), "Error: " + errorMessage, Toast.LENGTH_LONG).show();
            binding.loginButton.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.GONE);
        });
    }

    public static boolean isValidBangladeshPhoneNumber(String phoneNumber) {
        if ( phoneNumber.length() != 11 )
            return false;
        return !phoneNumber.matches("^(013|017|014|019|015|016|018|011)\\d{7}$");
    }

    public void bindListeners(){

        binding.registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegiserActivity.class);
                startActivity(intent);
            }
        });

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber          = binding.phoneNumberEditText.getText().toString();
                String password             = binding.passwordEditText.getText().toString();
                Boolean isStoreCredential   = binding.rememberCheckBox.isChecked();

                if ( phoneNumber.isEmpty() || password.isEmpty() ) {
                    Toast.makeText(getApplicationContext(), "All fields are required", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isValidBangladeshPhoneNumber(phoneNumber)) {
                    Toast.makeText(getApplicationContext(), "Please enter a valid phone number (e.g. 01611929833).", Toast.LENGTH_LONG).show();
                    return;
                }

                {
                    binding.loginButton.setVisibility(View.GONE);
                    binding.progressBar.setVisibility(View.VISIBLE);
                }

                // Update globally
                {
                    Common.phoneNumber          = phoneNumber;
                    Common.password             = password;
                    Common.isStoreCredential    = isStoreCredential;
                }

                LoginHandler.login(phoneNumber, password, LoginActivity.this);
            }
        });


        binding.visibilityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPasswordVisible) {
                    // Hide password
                    isPasswordVisible = false;
                    binding.passwordEditText.setTransformationMethod(new PasswordTransformationMethod());
                    binding.visibilityButton.setImageResource(R.drawable.show); // Change icon to 'view'
                } else {
                    // Show password
                    isPasswordVisible = true;
                    binding.passwordEditText.setTransformationMethod(null); // Show plain text
                    binding.visibilityButton.setImageResource(R.drawable.hide); // Change icon to 'hide'
                }

                // Move cursor to the end of the text
                binding.passwordEditText.setSelection(binding.passwordEditText.getText().length());
            }
        });
    }

}