package com.example.decisionwheel;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.decisionwheel.database.AppDatabase;
import com.example.decisionwheel.wheel.Slice;
import com.example.decisionwheel.wheel.WheelEntity;

public class WheelDetailActivity extends AppCompatActivity {
    private static final String EXTRA_WHEEL_ID = "extra_wheel_id";

    private static final int[] SLICE_COLORS = {
        0xFFE53935, 0xFF1E88E5, 0xFF43A047, 0xFFFB8C00,
        0xFF8E24AA, 0xFF00ACC1, 0xFFFFB300, 0xFF6D4C41
    };

    private int wheelId;
    private AppDatabase db;
    private SliceAdapter adapter;
    private int currentSliceCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wheel_detail);

        wheelId = getIntent().getIntExtra(EXTRA_WHEEL_ID, -1);
        if (wheelId == -1) {
            Toast.makeText(this, "Error: no wheel found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        db = AppDatabase.getInstance(this);

        AppDatabase.databaseWriteExecutor.execute(() -> {
            WheelEntity wheel = db.wheelDao().findById(wheelId);
            if (wheel != null) {
                runOnUiThread(() -> {
                    TextView title = findViewById(R.id.wheelDetailTitle);
                    title.setText(wheel.getName());
                });
            }
        });

        RecyclerView recyclerView = findViewById(R.id.slicesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SliceAdapter(slice -> {
            AppDatabase.databaseWriteExecutor.execute(() -> db.sliceDao().delete(slice));
        });
        recyclerView.setAdapter(adapter);

        db.sliceDao().getSlicesForWheelLD(wheelId).observe(this, slices -> {
            currentSliceCount = slices.size();
            adapter.setSlices(slices);
        });

        Button addSliceBtn = findViewById(R.id.btnAddSlice);
        addSliceBtn.setOnClickListener(v -> showAddSliceDialog());

        Button spinBtn = findViewById(R.id.btnSpinWheel);
        spinBtn.setOnClickListener(v ->
                startActivity(MainActivity.newIntent(this, wheelId))
        );
    }

    private void showAddSliceDialog() {
        if (currentSliceCount >= com.example.decisionwheel.wheel.Wheel.MAX_SLICES) {
            Toast.makeText(this, "Wheel is full (max " + com.example.decisionwheel.wheel.Wheel.MAX_SLICES + " slices)", Toast.LENGTH_SHORT).show();
            return;
        }

        EditText objectiveInput = new EditText(this);
        objectiveInput.setHint("Option (e.g. Movie Night)");

        new AlertDialog.Builder(this)
                .setTitle("Add Slice")
                .setView(objectiveInput)
                .setPositiveButton("Add", (dialog, which) -> {
                    String objective = objectiveInput.getText().toString().trim();
                    if (objective.isEmpty()) {
                        Toast.makeText(this, "Option cannot be empty", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    int color = SLICE_COLORS[currentSliceCount % SLICE_COLORS.length];
                    Slice slice = new Slice(objective, "UNASSIGNED", color, wheelId);
                    AppDatabase.databaseWriteExecutor.execute(() -> db.sliceDao().insert(slice));
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    public static Intent newIntent(Context context, int wheelId) {
        Intent intent = new Intent(context, WheelDetailActivity.class);
        intent.putExtra(EXTRA_WHEEL_ID, wheelId);
        return intent;
    }
}
