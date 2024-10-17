package com.example.filescanner;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.pspdfkit.configuration.activity.PdfActivityConfiguration;
import com.pspdfkit.ui.PdfActivity;
import com.pspdfkit.ui.PdfActivityIntentBuilder;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;

public class FileConverter {

    public void initPdfViewer(Context context, Uri uri) {

        final PdfActivityConfiguration config = new PdfActivityConfiguration.Builder(context)
                .enableContentEditing()
                .build();
        PdfActivity.showDocument(context, uri, config);

    }

    private void openPdf(Context context, File pdfFile) {
        Uri pdfUri = Uri.fromFile(pdfFile);
        Intent intent = PdfActivityIntentBuilder.fromUri(context, pdfUri)
                .build();
        //.startActivity(PdfConverter.this);

        context.startActivity(intent);
    }

    public void generatePdf(Context context,Uri uri, Bitmap bitmap) {
        PdfDocument document = new PdfDocument();

 //       ContentResolver contentResolver = context.getContentResolver();

//        InputStream inputStream = null;
//        try {
//            inputStream = contentResolver.openInputStream(uri);
//        } catch (Exception e) {
//            Log.d("tarun", "generatePdf: " + e.getMessage());
//        }

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder
                (bitmap.getWidth(), bitmap.getHeight(), 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#ffffff"));
        canvas.drawPaint(paint);

        bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
        canvas.drawBitmap(bitmap, 0, 0, null);
        document.finishPage(page);

        File sdcard = Environment.getExternalStorageDirectory();
        File dir = new File(sdcard.getAbsolutePath() + "/Download/Image to PDF/");

        if (dir.exists()) {
            File file = new File(dir, System.currentTimeMillis() + ".pdf");

            try {

                document.writeTo(Files.newOutputStream(file.toPath()));
                document.close();
                Toast.makeText(context, "IMAGES CONVERTED TO PDF...", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(context, "Error in creating file..." + e, Toast.LENGTH_SHORT).show();
            }
            Log.d("tarun", "generatePdf:7 ");
        } else {
            dir.mkdir();
            File file = new File(dir, System.currentTimeMillis() + ".pdf");
            try {

                document.writeTo(Files.newOutputStream(file.toPath()));
                document.close();
                Toast.makeText(context, "IMAGES CONVERTED TO PDF...", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(context, "Error in creating file..." + e, Toast.LENGTH_SHORT).show();
            }
            Log.d("tarun", "generatePdf:8 ");
        }
        Log.d("tarun", "generatePdf:9 ");
    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode
//            , @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == REQUSEST_CODE_PICK_IMAGES
//                && requestCode == RESULT_OK){
//            if (data != null){
//                ClipData clipData = data.getClipData();
//                if (clipData != null){
//                    // Multiple Images are Selected
//
//                    for (int i = 0; i<clipData.getItemCount(); i++){
//                        Uri imageUri = clipData.getItemAt(1).getUri();
//                        String imagePath = getImagepathfromUri(imageUri);
//                        selectedImagePath.add(imagePath);
//                    }
//                }else {
//                    //Single Image Selected
//                    Uri imageUri = data.getData();
//                    String imagePath = getImagepathfromUri(imageUri);
//                    selectedImagePath.add(imagePath);
//                }
//            }
//        }
//    }

    public Bitmap getImagePathFromUri(Uri imageUri, Context context) {
        Bitmap bitmap = null;

        if (imageUri != null) {
            ContentResolver contentResolver = context.getContentResolver();
            InputStream inputStream = null;
            try {
                inputStream = contentResolver.openInputStream(imageUri);
            } catch (Exception e) {
                Log.d("tarun", "generatePdf: " + e.getMessage());
            }

            bitmap = BitmapFactory.decodeStream(inputStream);


//            ContentResolver contentResolver = context.getContentResolver();
//            Cursor cursor = contentResolver.query(imageUri,projection,null,
//                    null,null);
//            Log.d("tarun", "getImagePathFromUri:2 ");
//            if (cursor != null){
//                Log.d("tarun", "getImagePathFromUri:3 ");
//                int coloumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
//                Log.d("tarun", "index " + coloumnIndex);
//                if (coloumnIndex != -1){
//                    Log.d("tarun", "getImagePathFromUri:4 ");
//                    imagePath = cursor.getString(coloumnIndex);
//                }
//                Log.d("tarun", "getImagePathFromUri:5 ");
//            }
//            cursor.close();
//            Log.d("tarun", "getImagePathFromUri:6 ");
//        }

        }
        return bitmap;
    }







}



