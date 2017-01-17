package pl.servicemedia.application.scanerbarcodeqr.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import pl.servicemedia.application.scanerbarcodeqr.R;

/**
 * Acivity for scan barcode/QRcode
 *
 * Created by Dariusz Andryskowski on 17.01.2017.
 */

public class ScanBarcodeActivity  extends MainActivity {

        private static final String TAG = ScanBarcodeActivity.class.getSimpleName();

        /**
         * Scan button
         */
        private Button mScanButton;

        /**
         * Result text scan barcode/qr code
         */
        private TextView mResutlTextView;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_scan_barcode);

            IntentIntegrator integrator = new IntentIntegrator(ScanBarcodeActivity.this);
            integrator.initiateScan();

            // text result scan code
            mResutlTextView = (TextView) findViewById(R.id.textViewResult);

            // button scan
            mScanButton = (Button) findViewById(R.id.btnScan);
            mScanButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IntentIntegrator integrator = new IntentIntegrator(ScanBarcodeActivity.this);
                    integrator.initiateScan();
                }
            });
        }

        /**
         * Function show scan barcode with information about scaned code
         * @param requestCode
         * @param resultCode
         * @param data
         */
        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {

            IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

            if (scanResult != null) {

                if (scanResult.getContents() == null) {
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Cancelled");
                } else {
                    Log.d(TAG, "Scanned");
                    mResutlTextView.setText(scanResult.toString());
                    Toast.makeText(this, "Scanned:" + scanResult.getContents(), Toast.LENGTH_SHORT).show();
                }
                mScanButton.setVisibility(View.VISIBLE);
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }

    }

