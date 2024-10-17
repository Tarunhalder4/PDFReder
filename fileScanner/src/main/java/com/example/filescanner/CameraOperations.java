package com.example.filescanner;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.TextRecognizerOptionsInterface;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import com.pspdfkit.annotations.AnnotationType;
import com.pspdfkit.document.PdfDocumentLoader;
import com.pspdfkit.document.editor.PdfDocumentEditor;
import com.pspdfkit.document.processor.NewPage;
import com.pspdfkit.document.processor.PdfProcessor;
import com.pspdfkit.document.processor.PdfProcessorTask;
import com.pspdfkit.ui.PdfActivityIntentBuilder;

import androidx.camera.core.Camera;
import androidx.exifinterface.media.ExifInterface;

//import org.apache.pdfbox.pdmodel.PDDocument;
//import org.apache.pdfbox.pdmodel.PDPage;
//import org.apache.pdfbox.pdmodel.PDPageContentStream;
//import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;


public class CameraOperations {

    public static final int PERMISSION_REQUEST_CODE = 1001;

//    public void startCamera1(AppCompatActivity context, Button button, PreviewView previewView){
//        startCamera(context, button, previewView);
//    }

    public void startCamera(AppCompatActivity context, Button button, PreviewView previewView, ImageView imageView) {
        Log.d("tarun", "startCamera: ");
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(context);

        cameraProviderFuture.addListener(() -> {
            try {
                Log.d("tarun", "startCamera:1 ");
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindPreview(context, cameraProvider, button, previewView, imageView);
            } catch (ExecutionException | InterruptedException e) {
                Log.d("tarun", "startCamera: " + e.getMessage());
                // Handle errors
            }
        }, ContextCompat.getMainExecutor(context));
    }

    private void bindPreview(AppCompatActivity context, @NonNull ProcessCameraProvider cameraProvider, Button button, PreviewView previewView,ImageView imageView) {
//        Preview preview = new Preview.Builder().build();
//        CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;
//
//        ImageCapture imageCapture = new ImageCapture.Builder().build();
//        preview.setSurfaceProvider(previewView.getSurfaceProvider());
//
//        Camera camera =  cameraProvider.bindToLifecycle(context, cameraSelector, preview, imageCapture);

        Log.d("tarun", "startCamera:3 ");

       // button.setOnClickListener(v -> {
            Log.d("tarun", "startCamera:4 ");
           takePicture(context, cameraProvider,previewView, button, imageView);
       // });
    }

    public void takePicture(AppCompatActivity context,  @NonNull ProcessCameraProvider cameraProvider, PreviewView previewView, Button button, ImageView imageView){
        Log.d("tarun", "startCamera:5 ");
        Preview preview = new Preview.Builder().build();
        CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;

        int rotation = context.getWindowManager().getDefaultDisplay().getRotation();

        ImageCapture imageCapture = new ImageCapture.Builder().setTargetRotation(rotation).build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        Camera camera =  cameraProvider.bindToLifecycle(context, cameraSelector, preview, imageCapture);
        File photoFile = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "scanned_document.jpg");
        ImageCapture.OutputFileOptions outputOptions = new ImageCapture.OutputFileOptions.Builder(photoFile).build();
        Log.d("tarun", "startCamera:6 ");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(context), new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        Log.d("tarun", "startCamera:7 " + outputFileResults.getSavedUri());
                        Uri savedUri = Uri.fromFile(photoFile);
                        imageView.setImageURI(savedUri);
                      //  performOCR(context,savedUri);  // Perform OCR after capturing the image


                        try {
                            Bitmap correctedBitmap = rotateImageIfRequired(context, savedUri);
                            // Now you have the corrected bitmap which you can use further
                            performOCR(context, null ,correctedBitmap);  // Or save the corrected image
                        } catch (IOException e) {

                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        Log.d("tarun", "onError: " + exception);
                        // Handle errors
                    }
                });
            }
        });


    }

//    private File getOutputDirectory(Context context) {
//        return new File(context.getExternalFilesDir(null), "scanned_images");
//    }

    private void performOCR(Context context,Uri imageUri, Bitmap bitmap) {
        Log.d("tarun", "startCamera: " + imageUri);
      //  try {
//            Bitmap bitmap = null;
//            try{
//                bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
//            }catch (Exception e){
//                Log.d("tarun", "performOCR: " +e.getMessage());
//            }

            assert bitmap != null;
            InputImage image = InputImage.fromBitmap(bitmap, 0);

            TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

            recognizer.process(image)
                    .addOnSuccessListener(visionText -> {
                        String extractedText = visionText.getText();
                    //    Log.d("tarun", "performOCR: " + extractedText);
                        if (!extractedText.isEmpty()) {
//                            Log.d("tarun", "performOCR: 1");
//                            Intent intent = new Intent(context, TextActivity.class);
//                            intent.putExtra("data", extractedText);
//                            context.startActivity(intent);
                           // createBlankPdfWithText(context, extractedText);
                            generatePDF(context,extractedText);


                           // createSearchablePDF(context,imageUri, extractedText, bitmap);  // Convert to searchable PDF
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.d("tarun", "performOCR: " + e.getMessage());
                        // Handle error in text recognition
                    });

//        } catch (Exception e) {
//            Log.d("tarun", "performOCR: " + e.getMessage());
//          //  e.printStackTrace();
//        }
    }


//    public void createBlankPdfWithText(Context context, File outputFile, String customText) {
//        try {
//            // Step 1: Create a blank PDF page
//            PdfProcessorTask task = PdfProcessorTask.fromNewPage(NewPage.emptyPage(NewPage.PAGE_SIZE_A4).build());
//
//            // Step 2: Save the blank PDF to a file
//            PdfProcessor.processDocument(task, outputFile);
//
//            // Step 3: Load the newly created PDF
//            PdfDocument pdfDocument = PdfDocumentLoader.openDocument(context,outputFile.getAbsolutePath());
//
//            // Step 4: Create a text annotation and set its properties
//            TextAnnotation textAnnotation = (TextAnnotation) Annotation.create(AnnotationType.FREETEXT);
//            textAnnotation.setBoundingBox(new RectF(100, 700, 400, 750)); // Set position and size
//            textAnnotation.setContents(customText); // Set your custom text
//
//            // Step 5: Add the annotation to the first page of the document
//            pdfDocument.getAnnotationProvider().addAnnotationToPage(textAnnotation, 0);
//
//            // Step 6: Save the document with the text annotation
//            pdfDocument.saveIfModified();
//
//            System.out.println("PDF created with text annotation successfully!");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


//    public void createBlankPdfAndAddText(Context context, File outputFile, String customText) {
//        try {
//            // Step 1: Create a blank page with desired dimensions
//            NewPage blankPage = NewPage.emptyPage(NewPage.PAGE_SIZE_A4).build();
//
//            // Step 2: Create a new PDF document editor
//            PdfDocumentEditor documentEditor = PdfDocumentEditor.newDocument(context);
//
//            // Step 3: Add the blank page to the document
//            documentEditor.appendPage(blankPage);
//
//            // Step 4: Add custom text to the page
//            documentEditor.writeText(0, customText, 100f, 700f, 16f); // Text on page 0, at (100, 700) with font size 16
//
//            // Step 5: Save the document to the specified output file
//            documentEditor.save(outputFile);
//
//            // Close the document editor to free resources
//            documentEditor.close();
//
//            System.out.println("PDF created and text written successfully!");
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


//    public void createBlankPdfWithText(Context context, String textToWrite) {
//        try {
//
//            File outputFile = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "searchable_documentTarun.pdf");
//            // Step 1: Create a new blank PDF document
//            PDDocument document = new PDDocument();
//
//            // Step 2: Add a blank page to the document
//            PDPage page = new PDPage();
//            document.addPage(page);
//
//            // Step 3: Start writing content (text) to the page
//            PDPageContentStream contentStream = new PDPageContentStream(document, page);
//
//            // Select font and font size
//            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
//
//            // Begin the text content
//            contentStream.beginText();
//
//            // Set text position on the page (coordinates from bottom-left corner)
//            contentStream.newLineAtOffset(100, 700);  // Adjust as needed for position
//
//            // Write the actual text
//            contentStream.showText(textToWrite);
//
//            // End the text content
//            contentStream.endText();
//
//            // Close the content stream
//            contentStream.close();
//
//            // Step 4: Save the document to a file
//            document.save(outputFile);
//
//            // Step 5: Close the document
//            document.close();
//
//            openPdf(context, outputFile);
//            System.out.println("PDF created and text written successfully!");
//        } catch (IOException e) {
//            Log.d("tarun", "createBlankPdfWithText: " + e.getMessage());
//           // e.printStackTrace();
//        }
//    }

    private void createSearchablePDF(Context context, Uri imageUri, String extractedText, Bitmap bitmap) {
        Log.d("tarun", "createSearchablePDF: ");
        try {
            File pdfFile = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "searchable_documentTarun.pdf");
            Log.d("tarun", "createSearchablePDF: " + pdfFile.getAbsolutePath());

            // Load the captured image
          //  Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);

            // Create a new PDF document
            PdfDocument document = new PdfDocument();

            // Create a new page with the dimensions of the image
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), 1).create();
            PdfDocument.Page page = document.startPage(pageInfo);

            // Draw the image on the PDF page
            Canvas canvas = page.getCanvas();
          //  canvas.drawBitmap(bitmap, 0, 0, null);

            // Add recognized text as hidden, searchable text layer
            TextPaint textPaint = new TextPaint();
            textPaint.setColor(Color.RED); // Invisible text
            textPaint.setTextSize(12);

            // Overlay text (text positions need to be calculated based on OCR)
            canvas.drawText(extractedText, 10, 10, textPaint); // Adjust coordinates accordingly

            // Finish the page
            document.finishPage(page);

            // Save the document
            document.writeTo(Files.newOutputStream(pdfFile.toPath()));
            document.close();

            // Open or share the created searchable PDF
            openPdf(context, pdfFile);
        } catch (IOException e) {
            Log.d("tarun", "createSearchablePDF: " + e.getMessage());
          //  e.printStackTrace();
        }
    }

    private void openPdf(Context context, File pdfFile) {
        Log.d("tarun", "openPdf: ");
        Uri pdfUri = Uri.fromFile(pdfFile);
        Intent intent = PdfActivityIntentBuilder.fromUri(context, pdfUri)
                .build();
        //.startActivity(PdfConverter.this);

        context.startActivity(intent);
    }




    private Bitmap rotateImageIfRequired(Context context, Uri uri) throws IOException {
        InputStream input = context.getContentResolver().openInputStream(uri);
        ExifInterface exifInterface = new ExifInterface(input);
        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        Bitmap bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(bitmap, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(bitmap, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(bitmap, 270);
            default:
                return bitmap;
        }
    }

    private Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
    }

    private void generatePDF(Context context, String string) {
        Log.d("tarun", "generatePDF: " + string);
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
        title.setColor(ContextCompat.getColor(context, com.pspdfkit.R.color.cardview_dark_background));

        title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        title.setColor(ContextCompat.getColor(context, com.pspdfkit.R.color.cardview_dark_background));
        title.setTextSize(15);

        // setting our text to center of PDF.
        title.setTextAlign(Paint.Align.CENTER);
        //canvas.drawText("This is sample document which we have created.", 396, 560, title);
        canvas.drawText(string, 209, 500, title);

        // finishing our page.
        pdfDocument.finishPage(myPage);

        // setting the name of our PDF file and its path.
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "GFG.pdf");

        try {
            // writing our PDF file to that location.
            pdfDocument.writeTo(new FileOutputStream(file));

            // printing toast message on completion of PDF generation.
            Toast.makeText(context, "PDF file generated successfully.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            // handling error
            e.printStackTrace();
            Toast.makeText(context, "Failed to generate PDF file.", Toast.LENGTH_SHORT).show();
        }

        // closing our PDF file.
        pdfDocument.close();

        openPdf(context,file);

    }



}
