package com.navify.unibook;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

public class SplashActivity extends AppCompatActivity {

    private VideoView videoView;
    private LottieAnimationView lottieLoader;
    
    private final int LOADING_DELAY = 0; 
    private final int MAX_SPLASH_TIME = 15000; 

    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private final Runnable timeoutRunnable = this::irAMainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setTheme(R.style.Theme_NavifyUnibook_Splash);
        setContentView(R.layout.activity_splash);

        videoView = findViewById(R.id.videoView);
        lottieLoader = findViewById(R.id.lottieLoader);

        lottieLoader.bringToFront();

        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.logo);
        videoView.setVideoURI(videoUri);
        
        // Configuración inicial
        videoView.setZOrderOnTop(false); 
        videoView.setVisibility(View.VISIBLE);
        videoView.setAlpha(0f);

        videoView.setOnPreparedListener(mp -> {
            mp.setLooping(false);
            
            mainHandler.postDelayed(() -> {
                if (!isFinishing()) {
                    mostrarVideo();
                }
            }, LOADING_DELAY);
        });

        videoView.setOnCompletionListener(mp -> {
            mainHandler.removeCallbacks(timeoutRunnable);
            irAMainActivity();
        });

        mainHandler.postDelayed(timeoutRunnable, MAX_SPLASH_TIME);
    }

    private void mostrarVideo() {
        // Mostramos el video
        videoView.setZOrderOnTop(true);
        videoView.setAlpha(1f); 
        videoView.bringToFront();       
        
        videoView.start();

    }

    private void irAMainActivity() {
        if (isFinishing()) return;
        mainHandler.removeCallbacksAndMessages(null);
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainHandler.removeCallbacksAndMessages(null);
    }
}
