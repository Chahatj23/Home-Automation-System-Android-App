package com.example.homeautomationsystem;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_CAMERA_PERMISSION = 101;
    private ImageView imageView;
    private Bitmap imageBitmap;

    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    String folderName , deviceId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonCaptureImage = findViewById(R.id.button_capture_image);
        Button buttonUpload = findViewById(R.id.button_upload);
        imageView = findViewById(R.id.image_view);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("name")) {
            folderName = intent.getStringExtra("name");
           Toast.makeText(this, folderName, Toast.LENGTH_SHORT).show();
        }
        if (intent != null && intent.hasExtra("deviceId")) {
            deviceId = intent.getStringExtra("deviceId");
            Toast.makeText(this, deviceId, Toast.LENGTH_SHORT).show();
        }
        // Authenticate anonymously
        mAuth.signInAnonymously().addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                currentUser = mAuth.getCurrentUser();
                // User is signed in
            } else {
                Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
            }
        });

        buttonCaptureImage.setOnClickListener(v -> checkCameraPermission());
        buttonUpload.setOnClickListener(v -> uploadImage());
    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA_PERMISSION);
        } else {
            openCamera();
        }
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Camera permission is required to use the camera", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                imageBitmap = (Bitmap) extras.get("data");
                imageView.setImageBitmap(imageBitmap);
            } else {
                Toast.makeText(this, "Failed to capture image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadImage() {
        if (imageBitmap != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            if (currentUser != null) {
                String userId = currentUser.getUid();
                String imageName = UUID.randomUUID().toString() + ".jpg"; // Generate a unique image name

                StorageReference imageRef = storageReference.child("images/"+deviceId+ "/"+folderName +"/" + userId + "/" + imageName);

                UploadTask uploadTask = imageRef.putBytes(data);
                uploadTask.addOnSuccessListener(taskSnapshot -> {
                    // Image uploaded successfully
                    Toast.makeText(MainActivity.this, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                    // Optionally, you can do something else here, like save the download URL to Firestore
                }).addOnFailureListener(e -> {
                    // Image upload failed
                    Toast.makeText(MainActivity.this, "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    // Print the stack trace for debugging
                    e.printStackTrace();
                });
            } else {
                Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No image captured", Toast.LENGTH_SHORT).show();
        }
    }
}
