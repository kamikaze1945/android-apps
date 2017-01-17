package pl.servicemedia.application.scanerbarcodeqr.activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import pl.servicemedia.application.scanerbarcodeqr.R;

/**
 * Acivity for create QRcode
 *
 * Created by Dariusz Andryskowski on 17.01.2017.
 */

public class CreateQrCodeActivity extends MainActivity {

    public static final String TAG = CreateQrCodeActivity.class.getSimpleName();

    /**
     * String from input
     */
    private EditText mInputStringEditText;

    /**
     * Button create QR code
     */
    private Button mCreateButton;

    /**
     * Return bitmap QRcode
     */
    private ImageView mScannedBitmapImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_qrcode);

        mInputStringEditText = (EditText) findViewById(R.id.input_string_edit_text);
        mCreateButton = (Button) findViewById(R.id.create_button);
        mScannedBitmapImageView = (ImageView) findViewById(R.id.scanned_bitmap_image_view);

        // directory for save generate QRcode
        createDirectory();

        mCreateButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                createBarcodeBitmap();
            }
        });

    }

    /**
     * File path where generate QRcode
     *
     * @return
     */
    public String createDirectory() {
        if (isExtSDCardPresent()) {
            String filePath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/QRCODES";

            Log.d(TAG, "File path: " + filePath);
            File file = new File(filePath);
            if (!file.exists())
                file.mkdirs();

            return filePath;
        } else {
            return null;
        }
    }

    public boolean isExtSDCardPresent() {
        return Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)
                && !Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED_READ_ONLY);
    }

    /**
     * Create bitmap QRcode
     */
    public void createBarcodeBitmap(){
        String barcode_content = mInputStringEditText.getText().toString();
        String fileNames[] = barcode_content.split("\n");
        String fileName = "QR_" + fileNames[0];
        QRCodeWriter qrWriter = new QRCodeWriter();

        try {
            BitMatrix bitMatrix = qrWriter.encode(barcode_content, BarcodeFormat.QR_CODE, 250, 250);
            Bitmap bitmap = toBitmap(bitMatrix);
            mScannedBitmapImageView.setImageBitmap(bitmap);


            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

            //you can create a new file name "test.jpg" in sdcard folder.
            File f = new File(Environment.getExternalStorageDirectory()
                    + File.separator + "QRCODES" + File.separator + fileName + ".jpg");
            f.createNewFile();
            //write the bytes in file
            FileOutputStream fout = new FileOutputStream(f);
            fout.write(bytes.toByteArray());

            // remember close de FileOutput
            fout.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * This method used for converting BitMatrix to BitMap
     * @param
     * @return bitmap
     */
    public static Bitmap toBitmap(BitMatrix bitMatrix){

        int height = bitMatrix.getHeight();
        int width = bitMatrix.getWidth();

        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

        for (int x = 0; x < width; x++){
            for (int y = 0; y < height; y++){
                bmp.setPixel(x, y, bitMatrix.get(x,y) ? Color.BLACK : Color.WHITE);
            }
        }
        return bmp;
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


}
