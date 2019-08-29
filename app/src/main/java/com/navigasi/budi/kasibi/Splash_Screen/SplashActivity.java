package com.navigasi.budi.kasibi.Splash_Screen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.navigasi.budi.kasibi.Database.DatabaseSQLHelper;
import com.navigasi.budi.kasibi.R;
import com.navigasi.budi.kasibi.WelcomeActivity;
import com.navigasi.budi.kasibi.coba;

import org.andresoviedo.util.android.AndroidURLStreamHandlerFactory;

import java.net.URL;

public class SplashActivity extends AppCompatActivity {

    DatabaseSQLHelper dbHelper;

    static {
        System.setProperty("java.protocol.handler.pkgs", "org.andresoviedo.util.android");
        URL.setURLStreamHandlerFactory(new AndroidURLStreamHandlerFactory());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);

        //initialize database
        dbHelper = new DatabaseSQLHelper(this);

        try {
            dbHelper.createDataBase();
        }
        catch (Exception ioe) {
            Toast.makeText(getApplicationContext(), "Gagal", Toast.LENGTH_LONG).show();
        }

        Intent demoIntent = new Intent(this.getApplicationContext(), WelcomeActivity.class);
        demoIntent.putExtra("immersiveMode", "false");
        demoIntent.putExtra("item", "");

        startActivity(demoIntent);
        finish();
    }

    @SuppressWarnings("unused")
    private void init() {
        startActivity(new Intent(this.getApplicationContext(), coba.class));
        finish();
    }
}
