package com.navify.unibook;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

public class SplashActivity extends AppCompatActivity {

    private VideoView videoView;
    private LottieAnimationView lottieLoader;
    private final int SPLASH_DURATION = 4000; // 3 segundos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Fijar tema oscuro antes de cargar layout para evitar flash blanco
        setTheme(R.style.Theme_NavifyUnibook_Splash);

        setContentView(R.layout.activity_splash);

        videoView = findViewById(R.id.videoView);
        lottieLoader = findViewById(R.id.lottieLoader);

        // Video desde res/raw
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.logo);
        videoView.setVideoURI(videoUri);

        videoView.setOnPreparedListener(mp -> {
            mp.setLooping(false); // No repetir
            videoView.start();
        });

        // Transición a MainActivity después de 4 segundos
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }, SPLASH_DURATION);

        // Lottie se reproduce automáticamente en loop (configurado en XML)
    }
}
