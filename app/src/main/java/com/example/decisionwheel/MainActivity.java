package com.example.decisionwheel;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.decisionwheel.wheel.Slice;
import com.example.decisionwheel.wheel.Wheel;
import com.example.decisionwheel.wheel.WheelVIew;

public class MainActivity extends AppCompatActivity {

    Wheel sampleWheel = new Wheel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        sampleWheel.insertSlice(new Slice("Slice 1", Color.RED));
        sampleWheel.insertSlice(new Slice("Slice 2", Color.GREEN));
        sampleWheel.insertSlice(new Slice("Slice 3", Color.BLUE));
        sampleWheel.insertSlice(new Slice("Slice 4", Color.YELLOW));

        // Fix: Use findViewById to get the view from the layout instead of creating a new one
        WheelVIew wheelView = findViewById(R.id.wheelVIew);
        if (wheelView != null) {
            wheelView.setWheel(sampleWheel);
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
