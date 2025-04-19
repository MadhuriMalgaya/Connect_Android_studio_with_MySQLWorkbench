package com.example.connect_database;

import android.content.Context;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Objects;

public class Database {

    protected static String db="";
    protected  static String ip= "10.0.2.2"; //ip use for emulator= 10.0.2.2 and for real android, use your system ip address
    protected static String port= "your port";
    protected static String username="your username";
    protected static String password="your password";

    public Connection conn(Context context){
        Connection connection =null;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            String connectionString = "jdbc:mysql://" + ip + ":" + port + "/" + db + "?allowPublicKeyRetrieval=true&useSSL=false";


            connection= DriverManager.getConnection(connectionString,username,password);

            Log.i("DB", "✅ Connected Successfully!");
        } catch (Exception e) {
            Log.e("DB Error", Objects.requireNonNull(e.getMessage()));
            // Don't call Toast here — not a UI thread
        }
        return connection;
    }




}
