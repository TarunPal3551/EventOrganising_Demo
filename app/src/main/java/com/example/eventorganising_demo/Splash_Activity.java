package com.example.eventorganising_demo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class Splash_Activity extends AppCompatActivity {
    private Handler mWaitHandler = new Handler();
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_);
        mAuth = FirebaseAuth.getInstance();
        mWaitHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                //The following code will execute after the 5 seconds.
                if (mAuth.getCurrentUser() != null) {
                    Intent intent = new Intent(Splash_Activity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(Splash_Activity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        }, 3000);  // Give a 5 seconds delay.

    }
}