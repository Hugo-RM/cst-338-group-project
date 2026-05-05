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
import com.example.decisionwheel.wheel.Wheel;

import java.util.ArrayList;

public class RouletteSelectorActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private WheelAdapter adapter;
    private AppDatabase db;
    private int currentUserId;
    private Button btnCreateNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.roulette_list);

        db = AppDatabase.getDatabase(this);

        SharedPreferences prefs = getSharedPreferences("decisionWheelPrefs", MODE_PRIVATE);
        currentUserId = prefs.getInt("userId", -1);

        if (currentUserId == -1) {
            Toast.makeText(this, "Error: User not logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        recyclerView = findViewById(R.id.recycler_wheels);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        adapter = new WheelAdapter(new WheelAdapter.OnWheelActionListener() {
            @Override
            public void onUse(Wheel wheel) {
                Intent intent = new Intent(RouletteSelectorActivity.this, MainActivity.class);
                intent.putExtra("WHEEL_ID", wheel.getId());
                startActivity(intent);
            }

            @Override
            public void onEdit(Wheel wheel) {
                Intent intent = new Intent(RouletteSelectorActivity.this, WheelManagementActivity.class);
                intent.putExtra("WHEEL_ID", wheel.getId());
                startActivity(intent);
            }

            @Override
            public void onDelete(Wheel wheel) {
                AppDatabase.databaseWriteExecutor.execute(() -> {
                    db.wheelDAO().deleteSlicesByWheelId(wheel.getId());
                    db.wheelDAO().deleteWheel(wheel);
                });
            }
        });

        recyclerView.setAdapter(adapter);

        db.wheelDAO().getWheelsByUserId(currentUserId).observe(this, wheels -> {
            if (wheels != null) {
                adapter.setWheels(wheels);
            }
        });

        btnCreateNew = findViewById(R.id.btn_create_new);
        btnCreateNew.setOnClickListener(v -> {
            Intent intent = new Intent(RouletteSelectorActivity.this, WheelManagementActivity.class);
            startActivity(intent);
        });
    }
}
