package com.example.decisionwheel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences("decisionWheelPrefs", MODE_PRIVATE);
        int userId = prefs.getInt("userId", -1);
        if (userId != -1) {
            startActivity(LandingActivity.newIntent(this, userId));
            finish();
            return;
        }

        setContentView(R.layout.welcome_activity);

        findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(LoginActivity.newIntent(getApplicationContext()));
            }
        });

        findViewById(R.id.makeAccButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(CreationActivity.newIntent(getApplicationContext()));
            }
        });
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, WelcomePage.class);
    }
}
