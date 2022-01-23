package com.example.assestmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.GregorianCalendar;

public class SplashScreen extends AppCompatActivity {
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        imageView = findViewById(R.id.imageView2);
        GregorianCalendar expDate = new GregorianCalendar(2022, 0, 21); // midnight
        GregorianCalendar now = new GregorianCalendar();

        boolean isExpired = now.after(expDate);
        if (isExpired) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View dailogbox = LayoutInflater.from(this).inflate(R.layout.expirealertdialog, null);

            builder.setView(dailogbox);
            builder.setCancelable(false);
            builder.show();


        } else {
            imageView.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {

// Using handler with postDelayed called runnable run method

                @Override

                public void run() {

                    Intent i = new Intent(SplashScreen.this, MainActivity.class);

                    startActivity(i);

                    // close this activity

                    finish();

                }

            }, 3 * 1000);
        }
    }
}