package com.example.decisionwheel;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Persistent Login Check
        SharedPreferences prefs = getSharedPreferences("decisionWheelPrefs", MODE_PRIVATE);
        int userId = prefs.getInt("userId", -1);
        if (userId != -1) {
            Intent intent = new Intent(this, RouletteSelectorActivity.class);
            startActivity(intent);
            finish(); // Prevent coming back to welcome page
            return;
        }

        setContentView(R.layout.welcome_activity);

        findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(LoginActivity.loginIntentFactory(getApplicationContext()));
            }
        });

        findViewById(R.id.makeAccButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // todo: create account
            }
        });
    }
}