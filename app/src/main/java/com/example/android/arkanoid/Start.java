package com.example.android.arkanoid;

import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class Start extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start2);
        int timeout = 1000;
        Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_up);

// Start animation
        findViewById(R.id.imageView2).startAnimation(slide_up);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(Start.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        },timeout);
    }
}
