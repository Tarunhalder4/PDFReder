package com.example.filescanner;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class TextActivity extends AppCompatActivity {

    Button generatePDFbtn;

    // declaring width and height
    // for our PDF file.
//    int pageHeight = 1120;
//    int pagewidth = 792;

    // creating a bitmap variable
    // for storing our images
   // Bitmap bmp, scaledbmp;

    // constant code for runtime permissions
   // private static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_text);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        String text = getIntent().getStringExtra("data");
        TextView textView = findViewById(R.id.text);
        textView.setText(text);


        //generatePDFbtn = findViewById(R.id.text);


      //  bmp = BitmapFactory.decodeResource(getResources(), com.pspdfkit.R.drawable.pspdf__ic_settings_continuous_vertical);
      //  scaledbmp = Bitmap.createScaledBitmap(bmp, 140, 140, false);

        // below code is used for
        // checking our permissions.
//        if (checkPermission()) {
//            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
//        } else {
//            requestPermission();
//        }

//        generatePDFbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // calling method to
//                // generate our PDF file.
//                generatePDF("tarun");
//            }
//        });


    }




    private void generatePDF(String string) {
        // creating an object variable for our PDF document.
        PdfDocument pdfDocument = new PdfDocument();

        // two variables for paint "paint" is used for drawing shapes and we will use "title" for adding text in our PDF file.
        Paint paint = new Paint();
        Paint title = new Paint();

        // adding page info to our PDF file in which we will be passing our pageWidth, pageHeight and number of pages and after that we are calling it to create our PDF.
        PdfDocument.PageInfo mypageInfo = new PdfDocument.PageInfo.Builder(792,1120, 1).create();

        // setting start page for our PDF file.
        PdfDocument.Page myPage = pdfDocument.startPage(mypageInfo);

        // creating a variable for canvas from our page of PDF.
        Canvas canvas = myPage.getCanvas();

//        if (scaledbmp != null) {
//            // drawing our image on our PDF file.
//            //canvas.drawBitmap(scaledbmp, 56, 40, paint);
//        } else {
//            Log.e("generatePDF", "Bitmap is null. Skipping bitmap drawing.");
//        }

        // adding typeface for our text which we will be adding in our PDF file.
        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));

        // setting text size which we will be displaying in our PDF file.
        title.setTextSize(15);

        // setting color of our text inside our PDF file.
        title.setColor(ContextCompat.getColor(this, com.pspdfkit.R.color.cardview_dark_background));

        // drawing text in our PDF file.
        canvas.drawText("A portal for IT professionals.", 209, 100, title);
        canvas.drawText("Geeks for Geeks", 209, 80, title);

        // creating another text and in this we are aligning this text to center of our PDF file.
        title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        title.setColor(ContextCompat.getColor(this, com.pspdfkit.R.color.cardview_dark_background));
        title.setTextSize(15);

        // setting our text to center of PDF.
        title.setTextAlign(Paint.Align.CENTER);
        //canvas.drawText("This is sample document which we have created.", 396, 560, title);
        canvas.drawText(string, 396, 560, title);

        // finishing our page.
        pdfDocument.finishPage(myPage);

        // setting the name of our PDF file and its path.
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "GFG.pdf");

        try {
            // writing our PDF file to that location.
            pdfDocument.writeTo(new FileOutputStream(file));

            // printing toast message on completion of PDF generation.
            Toast.makeText(TextActivity.this, "PDF file generated successfully.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            // handling error
            e.printStackTrace();
            Toast.makeText(TextActivity.this, "Failed to generate PDF file.", Toast.LENGTH_SHORT).show();
        }

        // closing our PDF file.
        pdfDocument.close();
    }

//    private boolean checkPermission() {
//        // checking of permissions.
//        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
//        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
//        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
//    }

//    private void requestPermission() {
//        // requesting permissions if not provided.
//        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == PERMISSION_REQUEST_CODE) {
//            if (grantResults.length > 0) {
//
//                // after requesting permissions we are showing
//                // users a toast message of permission granted.
//                boolean writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
//                boolean readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;
//
//                if (writeStorage && readStorage) {
//                    Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(this, "Permission Denied.", Toast.LENGTH_SHORT).show();
//                    finish();
//                }
//            }
//        }
//    }



}