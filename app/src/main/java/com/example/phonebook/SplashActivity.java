package com.example.phonebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

    private  static int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefFile",0);
                boolean hasLoggedIn = sharedPreferences.getBoolean("hasLoggedIn",false);

                Intent intent = new Intent(SplashActivity.this,RegisterActivity.class);
                startActivity(intent);
                finish();

//                if(hasLoggedIn){
//                    Intent intent = new Intent(SplashActivity.this,FirstActivity.class);
//                    startActivity(intent);
//                    finish();
//                }else{
//                    Intent intent = new Intent(SplashActivity.this,RegisterActivity.class);
//                    startActivity(intent);
//                    finish();
//                }
            }
        },SPLASH_TIME_OUT);
    }
}