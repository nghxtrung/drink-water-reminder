package com.example.drink_water_reminder;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;

public class DrinkDatabase {
    Connection connection = null;

    public Connection ConnectDatabase() {
        String ip = "172.20.10.5", port = "1433", db = "NhacNhoUongNuoc", username = "mylogin", password = "1234";
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String connectURL = null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            connectURL = "jdbc:jtds:sqlserver://" + ip + ":" + port + ";" + "databasename=" + db + ";user=" + username + ";password=" + password + ";";
            connection = DriverManager.getConnection(connectURL);
            Log.e("Thành công", "Kết nối ok");
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }
        return connection;
    }
}
