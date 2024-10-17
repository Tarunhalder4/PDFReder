package com.example.krishna;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.filescanner.FileConverter;
import com.example.filescanner.PdfEditorActivity;
import com.example.krishna.databinding.ActivityPdfConverterBinding;
//import com.pspdfkit.configuration.activity.PdfActivityConfiguration;
//import com.pspdfkit.ui.PdfActivity;
//import com.pspdfkit.ui.PdfActivityIntentBuilder;

import java.io.File;
import java.util.ArrayList;

public class PdfConverter extends AppCompatActivity {

    private static final int REQUSEST_CODE_PICK_IMAGES = 1;
    private ActivityPdfConverterBinding binding;
    private  FileConverter fileConverter;
    private ArrayList<String> selectedImagePath = new ArrayList<>();
    Uri imageUri = null;
    Bitmap bitmap= null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPdfConverterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fileConverter = new FileConverter();

        binding.convertToPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               pick_images(PdfConverter.this);
            }
        });

        binding.getImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fileConverter.generatePdf(PdfConverter.this, imageUri, bitmap);
            }
        });


        binding.goToCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PdfConverter.this, PdfEditorActivity.class);
                startActivity(intent);
            }
        });


    }

    public void pick_images(AppCompatActivity context){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("application/pdf");
//        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        context.startActivityForResult(intent,REQUSEST_CODE_PICK_IMAGES);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode
            , @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUSEST_CODE_PICK_IMAGES && resultCode == RESULT_OK){

            if (data != null){

                ClipData clipData = data.getClipData();
                if (clipData != null){

                    for (int i = 0; i<clipData.getItemCount(); i++){
                        imageUri = clipData.getItemAt(1).getUri();
                        bitmap = fileConverter.getImagePathFromUri(imageUri, PdfConverter.this);
                    }
                }else {
                    imageUri = data.getData();
                    bitmap = fileConverter.getImagePathFromUri(imageUri, PdfConverter.this);
                }

                fileConverter.initPdfViewer(this,imageUri);
            }
        }
    }

//    private void initPdfViewer(Uri uri) {
//
//        final PdfActivityConfiguration config = new PdfActivityConfiguration.Builder(PdfConverter.this)
//                .enableContentEditing()
//                .build();
//        PdfActivity.showDocument(this, uri, config);
//
//    }
//
//    private void openPdf(File pdfFile) {
//        Uri pdfUri = Uri.fromFile(pdfFile);
//        Intent intent = PdfActivityIntentBuilder.fromUri(this, pdfUri)
//                .build();
//                //.startActivity(PdfConverter.this);
//
//        this.startActivity(intent);
//    }

}