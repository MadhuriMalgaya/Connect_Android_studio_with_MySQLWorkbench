package com.example.connect_database;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class retrieveData extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve_data);

        // ✅ Initialize RecyclerView first
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 🔄 Load data in background
        Executors.newSingleThreadExecutor().execute(() -> {
            List<userModel> users = fetchUsersFromDatabase();

            // ✅ Update adapter on UI thread
            runOnUiThread(() -> {
                recyclerView.setAdapter(new itemAdapter(this, users));
            });
        });
    }

    private List<userModel> fetchUsersFromDatabase() {
        List<userModel> userList = new ArrayList<>();
        try {
            Connection conn = new Database().conn(this);
            if (conn != null) {
                String query = "SELECT Photo, User_Name, Age, Blood_Group, Height, Weight, other_medical_issue FROM USER_INFORMATION";
                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    byte[] photo = rs.getBytes("Photo");
                    String name = rs.getString("User_Name");
                    int age = rs.getInt("Age");
                    String bloodGroup = rs.getString("Blood_Group");
                    double height = rs.getDouble("Height");
                    int weight = rs.getInt("Weight");
                    String medicalIssue = rs.getString("other_medical_issue");

                    userList.add(new userModel(photo, name, age, height, weight, bloodGroup, medicalIssue));
                }

                rs.close();
                stmt.close();
                conn.close();
            } else {
                runOnUiThread(() ->
                        Toast.makeText(this, "❌ Failed to connect to DB!", Toast.LENGTH_SHORT).show()
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
            runOnUiThread(() ->
                    Toast.makeText(this, "❗ Error: " + e.getMessage(), Toast.LENGTH_LONG).show()
            );
        }

        return userList;
    }
}
