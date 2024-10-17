package com.example.filescanner;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.RectF;
import android.graphics.fonts.Font;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.view.PreviewView;
import androidx.compose.ui.text.Paragraph;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.pspdfkit.annotations.AnnotationType;
import com.pspdfkit.annotations.FreeTextAnnotation;
import com.pspdfkit.document.PdfDocument;
import com.pspdfkit.document.PdfDocumentLoader;
import com.pspdfkit.document.processor.NewPage;
import com.pspdfkit.document.processor.PdfProcessor;
import com.pspdfkit.document.processor.PdfProcessorTask;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PdfEditorActivity extends AppCompatActivity {

    private PreviewView previewView;
    private Button captureButton;
    private CameraOperations cameraOperations;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pdf_editer);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        previewView = findViewById(R.id.previewView);
        captureButton = findViewById(R.id.capture);
        imageView = findViewById(R.id.showimage);

        checkAndRequestPermissions(this);

        cameraOperations = new CameraOperations();


//        if(checkAndRequestPermissions(PdfEditorActivity.this)){
//            cameraOperations.startCamera1(PdfEditorActivity.this,captureButton,previewView);
//        }else {
//            Log.d("tarun", "startCamera1: permission not granted");
//        }
//


      //  cameraOperations.startCamera1(PdfEditorActivity.this,captureButton,previewView);

//        captureButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });



    }

    private boolean checkAndRequestPermissions(AppCompatActivity context) {
        String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ) {

            // Request camera and storage permissions
            ActivityCompat.requestPermissions(context, permissions, CameraOperations.PERMISSION_REQUEST_CODE);
            return false;  // Permissions are not yet granted
        }else {
            Log.d("tarun", "checkAndRequestPermissions: not permited");
        }
        return true;  // Permissions are already granted
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("tarun", "onRequestPermissionsResult: " + grantResults.length);
        if (requestCode == CameraOperations.PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
            //        && grantResults[1] == PackageManager.PERMISSION_GRANTED
            ) {
                // Permission granted, start the camera
                Log.d("tarun", "onRequestPermissionsResult: ");
                cameraOperations.startCamera(PdfEditorActivity.this,captureButton,previewView, imageView);
            } else {
                // Permission denied, show a message to the user
                Toast.makeText(this, "Camera permission is required to use this feature", Toast.LENGTH_SHORT).show();
            }
        }
    }


//    public void createandDisplayPdf(String text) {
//
//        File file = new File(directoryName, fileName);
//
//// Creating output stream to write in the newly created file
//        FileOutputStream fOut = null;
//
//        try {
//            fOut = new FileOutputStream(file);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        DocumentsContract.Document document = new DocumentsContract.Document(PageSize.A4, 50, 50, 50, 50);
//
//        try {
//            PdfWriter.getInstance(document, fOut);
//
//            // Open the document for writing
//            document.open();
//
//            // Write in the document
//            document.add(new Paragraph("Hello world"));
//            document.close();
//
//        } catch (DocumentException de) {
//            System.err.println(de.getMessage());
//        }
//
//    }

    // Method for opening a pdf file
//    private void viewPdf(String file, String directory) {
//
//        File pdfFile = new File(Environment.getExternalStorageDirectory() + "/" + directory + "/" + file);
//        Uri path = Uri.fromFile(pdfFile);
//
//        // Setting the intent for pdf reader
//        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
//        pdfIntent.setDataAndType(path, "application/pdf");
//        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//        try {
//            startActivity(pdfIntent);
//        } catch (ActivityNotFoundException e) {
//            Toast.makeText(this, "No application has been found to open PDF files.", Toast.LENGTH_SHORT).show();
//        }
//    }





}