package com.example.decisionwheel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.decisionwheel.database.AppDatabase;
import com.example.decisionwheel.database.UserEntity;
import com.example.decisionwheel.wheel.WheelEntity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class LandingActivity extends AppCompatActivity {
    private static final String EXTRA_USER_ID = "extra_user_id";

    private int userId;
    private AppDatabase db;
    private WheelAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        userId = getIntent().getIntExtra(EXTRA_USER_ID, -1);
        if (userId == -1) {
            Toast.makeText(this, "Error: no user found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        db = AppDatabase.getInstance(this);

        TextView usernameText = findViewById(R.id.landingUsername);
        Button adminBtn = findViewById(R.id.btnAdminPanel);
        Button logoutBtn = findViewById(R.id.btnLogout);

        db.userDao().findByIdLD(userId).observe(this, (UserEntity user) -> {
            if (user != null) {
                usernameText.setText("Logged in as: " + user.getUsername());
                if (user.isAdmin()) {
                    adminBtn.setVisibility(View.VISIBLE);
                } else {
                    adminBtn.setVisibility(View.INVISIBLE);
                }
            }
        });

        adminBtn.setOnClickListener(v ->
                startActivity(AdminLandingActivity.newIntent(this))
        );

        logoutBtn.setOnClickListener(v -> {
            getSharedPreferences("decisionWheelPrefs", MODE_PRIVATE)
                    .edit().remove("userId").apply();
            startActivity(WelcomePage.newIntent(this));
            finish();
        });

        RecyclerView recyclerView = findViewById(R.id.wheelsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new WheelAdapter(wheel ->
                startActivity(WheelDetailActivity.newIntent(this, wheel.getId()))
        );
        recyclerView.setAdapter(adapter);

        db.wheelDao().getWheelsForUserLD(userId).observe(this, wheels -> adapter.setWheels(wheels));

        FloatingActionButton fab = findViewById(R.id.fabAddWheel);
        fab.setOnClickListener(v -> showNewWheelDialog());
    }

    private void showNewWheelDialog() {
        EditText input = new EditText(this);
        input.setHint("Wheel name");

        new AlertDialog.Builder(this)
                .setTitle("New Wheel")
                .setView(input)
                .setPositiveButton("Create", (dialog, which) -> {
                    String name = input.getText().toString().trim();
                    if (name.isEmpty()) {
                        Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    AppDatabase.databaseWriteExecutor.execute(() -> {
                        long wheelId = db.wheelDao().insert(new WheelEntity(name, userId));
                        runOnUiThread(() ->
                                startActivity(WheelDetailActivity.newIntent(this, (int) wheelId))
                        );
                    });
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    public static Intent newIntent(Context context, int userId) {
        Intent intent = new Intent(context, LandingActivity.class);
        intent.putExtra(EXTRA_USER_ID, userId);
        return intent;
    }
}
