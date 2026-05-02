package com.example.decisionwheel;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.decisionwheel.wheel.Slice;
import com.example.decisionwheel.wheel.Wheel;
import com.example.decisionwheel.wheel.WheelVIew;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private Wheel sampleWheel = new Wheel();
    private AlertDialog.Builder builder;
    private Button spinWheel;
    private WheelVIew wheelView;

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private static final float SHAKE_THRESHOLD = 12.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        
        builder = new AlertDialog.Builder(this);

        sampleWheel.insertSlice(new Slice("Slice 1", Color.RED));
        sampleWheel.insertSlice(new Slice("Slice 2", Color.GREEN));
        sampleWheel.insertSlice(new Slice("Slice 3", Color.BLUE));
        sampleWheel.insertSlice(new Slice("Slice 4", Color.YELLOW));

        wheelView = findViewById(R.id.wheelVIew);
        if (wheelView != null) {
            wheelView.setWheel(sampleWheel);
        }
        
        spinWheel = findViewById(R.id.spinWheel);
        if (spinWheel != null) {
            spinWheel.setOnClickListener(v -> spinTheWheel());
        }

        // Initialize Sensor Manager and Accelerometer
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sensorManager != null && accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            // Calculate gForce magnitude
            double gForce = Math.sqrt(x * x + y * y + z * z) / SensorManager.GRAVITY_EARTH;
            
            // Trigger spin if a shake is detected and button is enabled
            if (gForce > 2.5) {
                if (spinWheel != null && spinWheel.isEnabled()) {
                    spinTheWheel();
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not used
    }

    private void spinTheWheel() {
        if (sampleWheel.getSlices().isEmpty()) return;

        spinWheel.setEnabled(false);

        int winningIndex = (int) (Math.random() * sampleWheel.getSlices().size());
        Slice result = sampleWheel.getSlices().get(winningIndex);

        float sweepPerSlice = 360f / sampleWheel.getSlices().size();
        float angleToSlice = (winningIndex * sweepPerSlice) + (sweepPerSlice / 2f);
        float targetRotation = 3600f + (270f - angleToSlice);

        ObjectAnimator animator = ObjectAnimator.ofFloat(wheelView, "rotation", 0f, targetRotation);
        animator.setDuration(3000);
        animator.setInterpolator(new DecelerateInterpolator());
        
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                wheelView.setRotation(targetRotation % 360);
                
                builder.setTitle("Winner!");
                builder.setMessage("The wheel landed on: " + result.getObjective());
                builder.setPositiveButton("OK", (dialog, which) -> spinWheel.setEnabled(true));
                builder.setCancelable(false);
                builder.show();
            }
        });

        animator.start();
    }
}
