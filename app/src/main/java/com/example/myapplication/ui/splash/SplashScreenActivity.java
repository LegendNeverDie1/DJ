package com.example.myapplication.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import com.example.myapplication.ui.main.MainActivity;
import com.example.myapplication.R;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // ✅ Android 12+ Splash API (automatically shows your splash theme drawable)
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);

        super.onCreate(savedInstanceState);

        // ✅ Animate splash exit (optional)
        animateSplashExit(splashScreen);

        // ✅ Optional delay before moving to Main Activity
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
            finish();
        }, 2000);  // ✅ 2000 ms = 2 seconds
    }

    private void animateSplashExit(SplashScreen splashScreen) {
        // Animation for smooth fade out
        splashScreen.setOnExitAnimationListener(splashScreenView -> {

            // Fade out the splash screen
            splashScreenView.getView().animate()
                    .alpha(0f)
                    .setDuration(300)  // 300ms fade duration
                    .withEndAction(splashScreenView::remove)
                    .start();
        });
    }
}
