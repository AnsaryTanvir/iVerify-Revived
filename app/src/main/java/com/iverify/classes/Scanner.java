package com.iverify.classes;

import android.os.Bundle;
import android.app.Activity;
import android.widget.Toast;
import android.content.Intent;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.iverify.ScanActivity;

public class Scanner extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startQRCodeScan();
    }

    public void startQRCodeScan() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setPrompt("Scan the QR code");
        integrator.setBeepEnabled(true);
        integrator.setOrientationLocked(false);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if ( result == null || resultCode != RESULT_OK || result.getContents() == null ){
            Toast.makeText(this, "Scan Cancelled!", Toast.LENGTH_SHORT).show();
            finish();
        }

        Intent intent = new Intent(Scanner.this, ScanActivity.class);
        intent.putExtra("result", result.getContents());
        startActivity(intent);
        finish();

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
