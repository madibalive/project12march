package com.example.madiba.venualpha;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.madiba.venualpha.onboard.BaseOnboardActivity;
import com.parse.ParseUser;

public class LaunchActivity extends AppCompatActivity {
    int SPLASH_SCREEN_TIMEOUT=5000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch2);
        scheduleRedirect();
    }

    private void scheduleRedirect() {
        new Handler().postDelayed(() -> {

            if (ParseUser.getCurrentUser() != null) {
//                GeneralService.startActionStartUp(LaunchActivity.this.getApplicationContext());
                startActivity(new Intent(LaunchActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
            }else
                startActivity(new Intent(LaunchActivity.this, BaseOnboardActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        }, SPLASH_SCREEN_TIMEOUT);
    }
}
