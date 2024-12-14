package com.iverify;

import android.app.Activity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.iverify.classes.Common;
import com.iverify.databinding.ActivityResultBinding;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

public class ResultActivity extends Activity {

    ActivityResultBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        try{
            String      response    = new String(Base64.decode(Common.response, Base64.URL_SAFE), StandardCharsets.UTF_8);
            JSONObject  jsonObject  = new JSONObject(response);

            String message                  = jsonObject.getString("message");
            String mfg                      = jsonObject.getString("mfg");
            String expiry                   = jsonObject.getString("expiry");
            String mrp                      = jsonObject.getString("mrp");
            String verificationTimestamp    = jsonObject.getString("verification_timestamp");
            String genericInformation       = jsonObject.optString("generic_information", "N/A");
            String dec_genericInformation   = new String(Base64.decode(genericInformation, Base64.DEFAULT), StandardCharsets.UTF_8);
            String verifier                 = jsonObject.getString("verifier");
            String verifierFullName         = jsonObject.optString("verifier_full_name", "N/A");



            String details;
            if ( verifier.equals("Self") || verifier.equals("N/A") ){

                 details =  message + " by You\n\n" +
                            "ᗰᗩᑎᑌᖴᗩᑕTᑌᖇIᑎG ᗪᗩTE "       + "\n" + mfg                    + "\n\n" +
                            "E᙭ᑭIᖇY ᗪᗩTE "                + "\n" + expiry                 + "\n\n" +
                            "ᗰᖇᑭ (ᑭEᖇ ᑌᑎIT)"              + "\n" + mrp                    + " TK\n\n" +
                            "TIᗰE ᗯᕼEᑎ ᖴIᖇᔕT ᐯEᖇIᖴIEᗪ "  + "\n" + verificationTimestamp  + "\n\n" +
                            "GEᑎEᖇIᑕ IᑎᖴOᖇᗰᗩTIOᑎ "        + "\n" + dec_genericInformation  + "\n\n";
            }
            else{
                 details =  message + " by " + verifierFullName + "\n\n" +
                         "ᗰᗩᑎᑌᖴᗩᑕTᑌᖇIᑎG ᗪᗩTE "          + "\n" + mfg                    + "\n\n" +
                         "E᙭ᑭIᖇY ᗪᗩTE "                   + "\n" + expiry                 + "\n\n" +
                         "ᗰᖇᑭ (ᑭEᖇ ᑌᑎIT)"                 + "\n" + mrp                    + " TK\n\n" +
                         "TIᗰE ᗯᕼEᑎ ᖴIᖇᔕT ᐯEᖇIᖴIEᗪ "     + "\n" + verificationTimestamp  + "\n\n" +
                         "GEᑎEᖇIᑕ IᑎᖴOᖇᗰᗩTIOᑎ "           + "\n" + dec_genericInformation  + "\n\n";

            }

            Log.d("Response", "Message: " + message);
            Log.d("Response", "Manufacture: " + mfg);
            Log.d("Response", "Expiry: " + expiry);
            Log.d("Response", "MRP: " + mrp);
            Log.d("Response", "Verification Timestamp: " + verificationTimestamp);
            Log.d("Response", "Generic Information: " + genericInformation);
            Log.d("Response", "Verifier: " + verifier);
            Log.d("Response", "Verifier Full Name: " + verifierFullName);

            binding.resultTextView.setText(details);

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }
}