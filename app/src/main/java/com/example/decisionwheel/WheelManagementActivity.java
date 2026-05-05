package com.example.decisionwheel;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.decisionwheel.database.AppDatabase;
import com.example.decisionwheel.wheel.Slice;
import com.example.decisionwheel.wheel.Wheel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WheelManagementActivity extends AppCompatActivity {

    private EditText editCategory;
    private EditText[] sliceTexts = new EditText[5];
    private AutoCompleteTextView[] colorDropdowns = new AutoCompleteTextView[5];
    private LinearLayout[] sliceContainers = new LinearLayout[5];
    private Button btnAddSlice;
    private Button btnSave;
    private AppDatabase db;
    private int currentUserId;
    private int existingWheelId = -1;
    private int visibleSlices = 2;

    private Map<String, Integer> colorMap;
    private Map<Integer, String> reverseColorMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wheel_management);

        db = AppDatabase.getDatabase(this);
        initializeColorMaps();
        
        SharedPreferences prefs = getSharedPreferences("decisionWheelPrefs", MODE_PRIVATE);
        currentUserId = prefs.getInt("userId", -1);

        if (currentUserId == -1) {
            Toast.makeText(this, "Error: User not logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize UI
        editCategory = findViewById(R.id.edit_category);
        
        sliceContainers[0] = findViewById(R.id.container_slice1);
        sliceContainers[1] = findViewById(R.id.container_slice2);
        sliceContainers[2] = findViewById(R.id.container_slice3);
        sliceContainers[3] = findViewById(R.id.container_slice4);
        sliceContainers[4] = findViewById(R.id.container_slice5);

        sliceTexts[0] = findViewById(R.id.slice1_text);
        sliceTexts[1] = findViewById(R.id.slice2_text);
        sliceTexts[2] = findViewById(R.id.slice3_text);
        sliceTexts[3] = findViewById(R.id.slice4_text);
        sliceTexts[4] = findViewById(R.id.slice5_text);

        colorDropdowns[0] = findViewById(R.id.slice1_color);
        colorDropdowns[1] = findViewById(R.id.slice2_color);
        colorDropdowns[2] = findViewById(R.id.slice3_color);
        colorDropdowns[3] = findViewById(R.id.slice4_color);
        colorDropdowns[4] = findViewById(R.id.slice5_color);

        setupDropdownAdapters();

        btnAddSlice = findViewById(R.id.btn_add_slice);
        btnAddSlice.setOnClickListener(v -> addSliceField());

        btnSave = findViewById(R.id.btn_save_wheel);
        btnSave.setOnClickListener(v -> saveWheel());

        // Check for edit mode
        existingWheelId = getIntent().getIntExtra("WHEEL_ID", -1);
        if (existingWheelId != -1) {
            loadExistingWheel();
        }
    }

    private void initializeColorMaps() {
        colorMap = new HashMap<>();
        reverseColorMap = new HashMap<>();
        
        addColor("Red", Color.RED);
        addColor("Green", Color.GREEN);
        addColor("Blue", Color.BLUE);
        addColor("Yellow", Color.YELLOW);
        addColor("Magenta", Color.MAGENTA);
        addColor("Cyan", Color.CYAN);
    }

    private void addColor(String name, int color) {
        colorMap.put(name, color);
        reverseColorMap.put(color, name);
    }

    private void setupDropdownAdapters() {
        String[] colors = getResources().getStringArray(R.array.wheel_colors);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, colors);
        for (AutoCompleteTextView dropdown : colorDropdowns) {
            if (dropdown != null) {
                dropdown.setAdapter(adapter);
            }
        }
    }

    private void addSliceField() {
        if (visibleSlices < 5) {
            sliceContainers[visibleSlices].setVisibility(View.VISIBLE);
            visibleSlices++;
            if (visibleSlices == 5) {
                btnAddSlice.setVisibility(View.GONE);
            }
        }
    }

    private void loadExistingWheel() {
        db.wheelDAO().getWheelWithSlicesById(existingWheelId).observe(this, wheelWithSlices -> {
            if (wheelWithSlices != null) {
                editCategory.setText(wheelWithSlices.wheel.getCategory());
                List<Slice> slices = wheelWithSlices.slices;
                visibleSlices = Math.max(2, slices.size());
                
                for (int i = 0; i < 5; i++) {
                    if (i < slices.size()) {
                        sliceContainers[i].setVisibility(View.VISIBLE);
                        sliceTexts[i].setText(slices.get(i).getObjective());
                        String colorName = reverseColorMap.getOrDefault(slices.get(i).getColor(), "Red");
                        colorDropdowns[i].setText(colorName, false);
                    } else if (i < 2) {
                        sliceContainers[i].setVisibility(View.VISIBLE);
                    } else {
                        sliceContainers[i].setVisibility(View.GONE);
                    }
                }
                
                if (visibleSlices >= 5) {
                    btnAddSlice.setVisibility(View.GONE);
                } else {
                    btnAddSlice.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void saveWheel() {
        String category = editCategory.getText().toString().trim();
        if (category.isEmpty()) {
            Toast.makeText(this, "Please enter a category", Toast.LENGTH_SHORT).show();
            return;
        }

        List<SliceData> validSlices = new ArrayList<>();
        for (int i = 0; i < visibleSlices; i++) {
            String text = sliceTexts[i].getText().toString().trim();
            if (!text.isEmpty()) {
                String colorName = colorDropdowns[i].getText().toString();
                int colorInt = colorMap.getOrDefault(colorName, Color.GRAY);
                validSlices.add(new SliceData(text, colorInt));
            }
        }

        if (validSlices.size() < 2) {
            Toast.makeText(this, "Please enter at least 2 options", Toast.LENGTH_SHORT).show();
            return;
        }

        AppDatabase.databaseWriteExecutor.execute(() -> {
            if (existingWheelId != -1) {
                // Update existing
                Wheel wheel = new Wheel(category, currentUserId);
                wheel.setId(existingWheelId);
                
                List<Slice> slices = new ArrayList<>();
                for (SliceData data : validSlices) {
                    slices.add(new Slice(data.text, category, data.color, existingWheelId));
                }
                
                db.wheelDAO().deleteSlicesByWheelId(existingWheelId);
                db.wheelDAO().insertWheel(wheel);
                db.wheelDAO().insertSlices(slices);
            } else {
                // Insert new
                Wheel newWheel = new Wheel(category, currentUserId);
                List<Slice> slices = new ArrayList<>();
                for (SliceData data : validSlices) {
                    slices.add(new Slice(data.text, category, data.color, 0));
                }
                db.wheelDAO().insertWheelWithSlices(newWheel, slices);
            }
            
            runOnUiThread(() -> {
                Toast.makeText(WheelManagementActivity.this, "Wheel saved successfully!", Toast.LENGTH_SHORT).show();
                finish();
            });
        });
    }

    private static class SliceData {
        String text;
        int color;
        SliceData(String text, int color) {
            this.text = text;
            this.color = color;
        }
    }
}
