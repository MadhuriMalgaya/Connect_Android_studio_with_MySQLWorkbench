package com.example.connect_database;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    Database database;
    Connection connection;
    String DB_NAME, str;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        DB_NAME ="RADARS";
        database= new Database();
        connection= database.conn(MainActivity.this);
    }

    public void CreateDatabase(View view) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(()->{

            connection=database.conn(MainActivity.this);

            try{
                if(connection==null){
                    str= "Error";
                }
                else{
                    String creteDbQuery= "CREATE DATABASE IF NOT EXISTS "+ DB_NAME;
                    //creteDbQuery= "CREATE DATABASE IF NOT EXISTS "+ DB_NAME +"CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ENCRYPTION= 'Y'";
                    Statement stmt= connection.createStatement();
                    stmt.executeUpdate(creteDbQuery);
                    str=" Database Created Successfully";
                }

            }catch (SQLException e){
                str= e.toString();
            }

            runOnUiThread(() -> {
                try{
                    Thread.sleep(1000);
                }catch (InterruptedException e){
                    str= e.toString();
                }

                TextView textView= findViewById(R.id.textview);
                textView.setText(str);

            });


        }
        );

    }

    public void CreateTable(View view) {

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(()->{

            Database.db= DB_NAME;
            connection= database.conn(MainActivity.this);

            try{
                if(connection==null){
                    str="Error";
                }else{
                    Statement stmt= connection.createStatement();
                    String createTableQuery ="CREATE TABLE IF NOT EXISTS USER_INFORMATION ("+
                        "    Userid INT AUTO_INCREMENT PRIMARY KEY," +
                        "    photo mediumblob," +
                        "    User_Name varchar(100)," +
                        "    Age Int,"+
                        "    Blood_Group varchar(3)," +
                        "    Height DECIMAL(5,2)," +
                        "    Weight INT," +
                        "    other_medical_issue varchar(200))";


                    stmt.executeUpdate(createTableQuery);
                    str ="Table Created Successfully";
                }

            }catch (SQLException e){
                str=e.toString();
            }



                    runOnUiThread(() -> {
                        try{
                            Thread.sleep(1000);
                        }catch (InterruptedException e){
                            str= e.toString();
                        }

                        TextView textView= findViewById(R.id.textview);
                        textView.setText(str);

                    });


                }
        );

    }

    public void CreateViews(View view) {

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(()->{

            Database.db= DB_NAME;
            connection= database.conn(MainActivity.this);

            try {
                if (connection == null) {
                    str = "Error";
                }
                else{
                    Statement stmt= connection.createStatement();
                    String createViewQuery= "CREATE VIEW userNameList AS "+
                            "SELECT *FROM USER_INFORMATION WHERE Age = '25';";
                    stmt.executeUpdate(createViewQuery);
                    str="View Created successfully";
                }
            }
                    catch (SQLException e){
                        str=e.toString();
                    }



                    runOnUiThread(() -> {
                        try{
                            Thread.sleep(1000);
                        }catch (InterruptedException e){
                            str= e.toString();
                        }

                        TextView textView= findViewById(R.id.textview);
                        textView.setText(str);

                    });


                }
        );
    }

    public void Create_Stored_procedure(View view) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(()->{

                    Database.db= DB_NAME;
                    connection= database.conn(MainActivity.this);

                    try {
                        if (connection == null) {
                            str = "Error";
                        }
                        else{
                            Statement stmt= connection.createStatement();
                            String createTableQuery= "DROP PROCEDURE IF EXISTS my_procedure;";
                            stmt.executeUpdate(createTableQuery);
                            createTableQuery="CREATE PROCEDURE my_procedure () BEGIN SELECT *FROM USER_INFORMATION; END";
                            stmt.executeUpdate(createTableQuery);
                            str="Stored_procedure Created successfully";
                        }
                    }
                    catch (SQLException e){
                        str=e.toString();
                    }



                    runOnUiThread(() -> {
                        try{
                            Thread.sleep(1000);
                        }catch (InterruptedException e){
                            str= e.toString();
                        }

                        TextView textView= findViewById(R.id.textview);
                        textView.setText(str);

                    });


                }
        );
    }

    public void Retrieve_Data(View view) {
        Intent intent= new Intent(this, retrieveData.class);
        startActivity(intent);
    }

    public void InsertData(View view) {
        Intent intent = new Intent(this, insertData.class);
        startActivity(intent);
    }
}