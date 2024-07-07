package com.example.homeautomationsystem;



import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.ArrayList;
   // Parse Dependencies
  import com.parse.Parse;
   import com.parse.ParseInstallation;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import com.parse.ParseInstallation;
import java.util.HashMap;
import java.util.Map;

public class SetupActivity extends AppCompatActivity {

    private EditText editTextFlatNo, editTextFloor, editTextBlock;
    private EditText editTextName, editTextPhoneNo, editTextDeviceId;
    private Button buttonSetup;

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);


        editTextFlatNo = findViewById(R.id.edit_text_flat_number);
        editTextFloor = findViewById(R.id.edit_text_floor);
        editTextBlock = findViewById(R.id.edit_text_block);
        editTextName = findViewById(R.id.edit_text_name);
        editTextPhoneNo = findViewById(R.id.edit_text_phone_number);
        editTextDeviceId = findViewById(R.id.edit_text_device_id);
        buttonSetup = findViewById(R.id.button_setup);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        buttonSetup.setOnClickListener(v -> setupHouse());
    }

    private void setupHouse() {
        String flatNo = editTextFlatNo.getText().toString().trim();
        String floor = editTextFloor.getText().toString().trim();
        String block = editTextBlock.getText().toString().trim();
        String name = editTextName.getText().toString().trim();
        String phoneNo = editTextPhoneNo.getText().toString().trim();
        String deviceId = editTextDeviceId.getText().toString().trim();

        if (TextUtils.isEmpty(flatNo) || TextUtils.isEmpty(floor) || TextUtils.isEmpty(block) ||
                TextUtils.isEmpty(name) || TextUtils.isEmpty(phoneNo) || TextUtils.isEmpty(deviceId)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInAnonymously().addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser != null) {
                    saveHouseDetails(name, flatNo, floor, block, name, phoneNo, deviceId);
                }
            } else {
                Toast.makeText(SetupActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveHouseDetails(String userId, String flatNo, String floor, String block, String name, String phoneNo, String deviceId) {
        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("flatNo", flatNo);
        userDetails.put("floor", floor);
        userDetails.put("block", block);
        userDetails.put("name", name);
        userDetails.put("phoneNo", phoneNo);
        userDetails.put("deviceId", deviceId);
        userDetails.put("role", "owner");

        firestore.collection("users").document(userId)
                .set(userDetails)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(SetupActivity.this, "House setup completed", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SetupActivity.this, MainActivity.class);
                        intent.putExtra("deviceId", deviceId);
                        intent.putExtra("name", name);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(SetupActivity.this, "Failed to save house details", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
