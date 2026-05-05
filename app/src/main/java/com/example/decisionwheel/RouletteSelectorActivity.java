package com.example.decisionwheel;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.decisionwheel.database.AppDatabase;
import com.example.decisionwheel.wheel.WheelEntity;

public class RouletteSelectorActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private WheelAdapter adapter;
    private AppDatabase db;
    private int currentUserId;
    private Button btnCreateNew;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.roulette_list);

        db = AppDatabase.getInstance(this);

        SharedPreferences prefs = getSharedPreferences("decisionWheelPrefs", MODE_PRIVATE);
        currentUserId = prefs.getInt("userId", -1);

        if (currentUserId == -1) {
            Toast.makeText(this, "Error: User not logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        btnLogout = findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(v -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.remove("userId");
            editor.apply();

            Intent intent = new Intent(RouletteSelectorActivity.this, WelcomePage.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        recyclerView = findViewById(R.id.recycler_wheels);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new WheelAdapter(new WheelAdapter.OnWheelActionListener() {
            @Override
            public void onUse(WheelEntity wheel) {
                startActivity(MainActivity.newIntent(RouletteSelectorActivity.this, wheel.getId()));
            }

            @Override
            public void onEdit(WheelEntity wheel) {
                Intent intent = new Intent(RouletteSelectorActivity.this, WheelManagementActivity.class);
                intent.putExtra("WHEEL_ID", wheel.getId());
                startActivity(intent);
            }

            @Override
            public void onDelete(WheelEntity wheel) {
                AppDatabase.databaseWriteExecutor.execute(() -> {
                    db.sliceDao().deleteAllForWheel(wheel.getId());
                    db.wheelDao().delete(wheel);
                });
            }
        });
        recyclerView.setAdapter(adapter);

        db.wheelDao().getWheelsForUserLD(currentUserId).observe(this, wheels -> {
            if (wheels != null) adapter.setWheels(wheels);
        });

        btnCreateNew = findViewById(R.id.btn_create_new);
        btnCreateNew.setOnClickListener(v ->
                startActivity(new Intent(this, WheelManagementActivity.class))
        );
    }
}
