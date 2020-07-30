package com.example.notesmasteer.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.notesmasteer.MainActivity;
import com.example.notesmasteer.R;

public class SplashActivity extends AppCompatActivity {
    private static final int TIME_DELAY = 3000;
    TextView tvLogo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//        tvLogo = (TextView) findViewById(R.id.tvLogo) ;
//        // add animation textview and remove splash after 3s
//        Animation animationTextView = AnimationUtils.loadAnimation(SplashActivity.this,R.anim.bounce_animation);
//        tvLogo.startAnimation(animationTextView);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        },TIME_DELAY);
    }
}
