package com.iverify;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Html;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.iverify.classes.RegistrationHandle;
import com.iverify.databinding.ActivityRegiserBinding;
import org.json.JSONObject;
import com.iverify.classes.OTPSender;

@SuppressLint({"StaticFieldLeak", "ObsoleteSdkInt"})
public class RegiserActivity extends AppCompatActivity implements OTPSender.OTPSenderCallback , RegistrationHandle.RegistrationHandlerCallback {


    private static ActivityRegiserBinding binding;
    private static String generatedOTP;
    private static boolean isOTPVerified = false;
    private static boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Hide the ActionBar
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        binding = ActivityRegiserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        updateUIElements();
        bindListeners();

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

    @Override
    public void onOTPSendSuccess(String response) {

        runOnUiThread(() -> {

            Log.d("iVerify", "onOTPSendSuccess: " + response);

            try {
                JSONObject jsonObject = new JSONObject(response);
                int responseCode        = jsonObject.getInt("response_code");
                String successMessage   = jsonObject.getString("success_message");
                Toast.makeText(getApplicationContext(), successMessage, Toast.LENGTH_LONG).show();

                if ( responseCode != OTPSender.SUCCESS_SMS_SUBMITTED )
                    return;

                {
                    // Create an EditText to input OTP in the dialog
                    EditText inputOTPEdiText = new EditText(RegiserActivity.this);
                    inputOTPEdiText.setHint("OTP");

                    // Create and show the AlertDialog
                    new AlertDialog.Builder(RegiserActivity.this)
                            .setTitle("Account Verification")
                            .setMessage("Please enter the OTP from sms")
                            .setView(inputOTPEdiText)  // Add the EditText to the dialog
                            .setPositiveButton("OK", (dialog, which) -> {
                                String otpFromUser = inputOTPEdiText.getText().toString();
                                if ( otpFromUser.equals(generatedOTP))
                                    isOTPVerified = true;
                                else
                                    Toast.makeText(getApplicationContext(), "Invalid OTP", Toast.LENGTH_LONG).show();
                            })
                            .setNegativeButton("Cancel", null)  // Dismiss dialog on Cancel
                            .show();
                }

            }catch (Exception e){
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                return;
            }
        });
    }

    @Override
    public void onOTPSendFailure(String errorMessage) {
        Log.d("iVerify" , "onOTPSendFailure: " + errorMessage);
        runOnUiThread(() -> {
            Toast.makeText(getApplicationContext(), "Error: " + errorMessage, Toast.LENGTH_LONG).show();
            binding.registerButton.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.GONE);
        });
    }

    @Override
    public void onRegistrationSuccess(String response ) {
        Log.d("iVerify" , "onUserRegistrationSuccess: " + response);
        runOnUiThread(() -> {
            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
            if ( response.equals("User registered successfully") ){
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    @Override
    public void onRegistrationFailure(String errorMessage) {
        Log.d("iVerify" , "onUserRegistrationFailure: " + errorMessage);
        runOnUiThread(() -> {
            Toast.makeText(getApplicationContext(), "Error: " + errorMessage, Toast.LENGTH_LONG).show();
        });
    }

    public static boolean isValidBangladeshPhoneNumber(String phoneNumber) {
        if ( phoneNumber.length() != 11 )
            return false;
        return !phoneNumber.matches("^(013|017|014|019|015|016|018|011)\\d{7}$");
    }

    public static boolean isStrongPassword(String password) {

        // Check minimum length (e.g., 6 characters)
        if (password.length() < 6)
            return false;

//        // Check for at least one uppercase letter
//        if (!password.matches(".*[A-Z].*"))
//            return false;
//
//        // Check for at least one lowercase letter
//        if (!password.matches(".*[a-z].*"))
//            return false;
//
//
//        // Check for at least one digit
//        if (!password.matches(".*\\d.*"))
//            return false;
//
//
//        // Check for at least one special character
//        if (!password.matches(".*[!@#$%^&*(),.?\":{}|<>].*"))
//            return false;
//

        return true;
    }


    public void bindListeners(){

        binding.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String fullName         = binding.nameEditText.getText().toString();
                String phoneNumber      = binding.phoneNumberEditText.getText().toString();
                String password         = binding.passwordEditText.getText().toString();

                if (fullName.isEmpty() || phoneNumber.isEmpty() || password.isEmpty() ) {
                    Toast.makeText(getApplicationContext(), "All fields are required. Please complete the form.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isValidBangladeshPhoneNumber(phoneNumber)) {
                    Toast.makeText(getApplicationContext(), "Please enter a valid phone number (e.g. 01611929833).", Toast.LENGTH_LONG).show();
                    return;
                }

                if ( !isStrongPassword(password)){
                    Toast.makeText(getApplicationContext(), "Password must be at least 6 digits", Toast.LENGTH_LONG).show();
                    return;
                }

                if (!isOTPVerified) {
                    generatedOTP = OTPSender.generateOTP();
                    OTPSender.sendOtp("88" + phoneNumber, generatedOTP, RegiserActivity.this);
                    return;
                }

                {
                    binding.registerButton.setVisibility(View.GONE);
                    binding.progressBar.setVisibility(View.VISIBLE);
                }
                RegistrationHandle.register(fullName, phoneNumber, password, RegiserActivity.this);
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