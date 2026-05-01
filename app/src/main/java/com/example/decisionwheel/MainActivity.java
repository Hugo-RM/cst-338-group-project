package com.example.decisionwheel;

import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.decisionwheel.wheel.Slice;
import com.example.decisionwheel.wheel.Wheel;
import com.example.decisionwheel.wheel.WheelVIew;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Wheel sampleWheel = new Wheel();
        sampleWheel.insertSlice(new Slice("Slice 1", Color.RED));
        sampleWheel.insertSlice(new Slice("Slice 2", Color.GREEN));
        sampleWheel.insertSlice(new Slice("Slice 3", Color.BLUE));

        WheelVIew wheelView = findViewById(R.id.wheelVIew);
        wheelView.setWheel(sampleWheel);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}