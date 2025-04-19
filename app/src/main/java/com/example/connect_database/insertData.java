package com.example.connect_database;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.concurrent.Executors;

public class insertData extends AppCompatActivity {

    EditText name, age, height, weight, medicalIssue;
    Spinner bloodGroupSpinner;
    Button saveBtn;
    ImageView photo;

    Uri selectedImageUri;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_data);

        // Bind Views
        name = findViewById(R.id.name);
        age = findViewById(R.id.age);
        height = findViewById(R.id.height);
        weight = findViewById(R.id.weight);
        medicalIssue = findViewById(R.id.medicalIssue);
        bloodGroupSpinner = findViewById(R.id.bloodGroupSpinner);
        saveBtn = findViewById(R.id.save);
        photo = findViewById(R.id.photo);

        // Set Spinner Values
        String[] bloodGroups = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, bloodGroups);
        bloodGroupSpinner.setAdapter(adapter);

        // Photo picker
        photo.setOnClickListener(this::onClickPhoto);

        // Save Button Action
        saveBtn.setOnClickListener(v -> insertUserData());
    }

    private void insertUserData() {
        try {
            // Get values
            String userName = name.getText().toString().trim();
            int userAge = Integer.parseInt(age.getText().toString().trim());
            String userBloodGroup = bloodGroupSpinner.getSelectedItem().toString();
            double userHeight = Double.parseDouble(height.getText().toString().trim());
            int userWeight = Integer.parseInt(weight.getText().toString().trim());
            String userMedical = medicalIssue.getText().toString().trim();

            // Convert image to byte[]
            byte[] finalPhotoBytes = null;
            if (bitmap != null) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                finalPhotoBytes = stream.toByteArray();
            } else {
                Toast.makeText(this, "❗ Please select a photo", Toast.LENGTH_SHORT).show();
                return;
            }

            // Run in background thread
            byte[] finalBytes = finalPhotoBytes;
            Executors.newSingleThreadExecutor().execute(() -> {
                try {
                    Connection conn = new Database().conn(this);
                    if (conn != null) {
                        String insertQuery = "INSERT INTO USER_INFORMATION (Photo, User_Name, Age, Blood_Group, Height, Weight, other_medical_issue) VALUES (?, ?, ?, ?, ?, ?, ?)";
                        PreparedStatement stmt = conn.prepareStatement(insertQuery);

                        stmt.setBytes(1, finalBytes);
                        stmt.setString(2, userName);
                        stmt.setInt(3, userAge);
                        stmt.setString(4, userBloodGroup);
                        stmt.setDouble(5, userHeight);
                        stmt.setInt(6, userWeight);
                        stmt.setString(7, userMedical);

                        int rows = stmt.executeUpdate();

                        runOnUiThread(() -> {
                            if (rows > 0) {
                                Toast.makeText(this, "✅ Data inserted successfully!", Toast.LENGTH_SHORT).show();
                                clearFields();
                            } else {
                                Toast.makeText(this, "⚠️ Failed to insert data!", Toast.LENGTH_SHORT).show();
                            }
                        });

                        stmt.close();
                        conn.close();
                    } else {
                        runOnUiThread(() -> Toast.makeText(this, "❌ Failed to connect to DB!", Toast.LENGTH_SHORT).show());
                    }
                } catch (Exception e) {
                    runOnUiThread(() -> Toast.makeText(this, "❌ Error: " + e.getMessage(), Toast.LENGTH_LONG).show());
                    e.printStackTrace();
                }
            });

        } catch (Exception e) {
            Toast.makeText(this, "⚠️ Please fill all fields correctly", Toast.LENGTH_SHORT).show();
        }
    }

    private void onClickPhoto(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                photo.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "⚠️ Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void clearFields() {
        name.setText("");
        age.setText("");
        height.setText("");
        weight.setText("");
        medicalIssue.setText("");
        bloodGroupSpinner.setSelection(0);
        photo.setImageResource(R.drawable.baseline_account_box_24); // Set your default image
        bitmap = null;
    }
}
