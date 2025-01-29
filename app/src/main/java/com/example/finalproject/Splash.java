package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Splash extends AppCompatActivity {

    View nContentView;
    Handler handler;

    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView imageView = findViewById(R.id.splash);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        nContentView = findViewById(R.id.splash);
        nContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View. SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View. SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(user !=null){
                    if(user.isEmailVerified()){
                        startActivity(new Intent(Splash.this, MainActivity.class));
                    }else{
                        startActivity(new Intent(Splash.this, Login.class));
                    }
                }else{
                    startActivity(new Intent(Splash.this, Login.class));
                }
                finish();
            }
        }, 2000);

    }
}