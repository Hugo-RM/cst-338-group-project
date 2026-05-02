package com.example.decisionwheel;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.decisionwheel.wheel.Slice;
import com.example.decisionwheel.wheel.Wheel;
import com.example.decisionwheel.wheel.WheelVIew;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    //Basic wheel functionality
    private Wheel sampleWheel = new Wheel();
    private Button spinWheel;
    private WheelVIew wheelView;
    private TextView textView;

    //Sensor Mechanism
    private SensorManager sensorManager;
    private Sensor accelerometer;

    //Overlay
    private ConstraintLayout resultOverlay;
    private TextView overlayResult;
    private Button btnAwesome;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        
        // Initialize Overlay Views
        resultOverlay = findViewById(R.id.resultOverlay);
        overlayResult = findViewById(R.id.overlayResult);
        btnAwesome = findViewById(R.id.btnAwesome);

        // Set up overlay button listener
        if (btnAwesome != null) {
            btnAwesome.setOnClickListener(v -> hideOverlay());
        }

        sampleWheel.insertSlice(new Slice("Movie Night", Color.RED));
        sampleWheel.insertSlice(new Slice("Order Pizza", Color.GREEN));
        sampleWheel.insertSlice(new Slice("Go for a Walk", Color.BLUE));
        sampleWheel.insertSlice(new Slice("Play Games", Color.YELLOW));

        sampleWheel.setCategory("FRIDAY NIGHT");

        textView = findViewById(R.id.tag_category);
        if(sampleWheel.getCategory() != null){
            textView.setText(sampleWheel.getCategory());
        } else {
            textView.setText("UNASSIGNED");
        }

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
                showOverlay(result.getObjective());
            }
        });

        animator.start();
    }

    @SuppressLint("SetTextI18n")
    private void showOverlay(String result){
        if (overlayResult != null && resultOverlay != null) {
            overlayResult.setText("Yey, your activity is: " + result + "!");
            resultOverlay.setVisibility(View.VISIBLE);
            resultOverlay.setAlpha(0f);
            resultOverlay.animate()
                    .alpha(1f)
                    .setDuration(500)
                    .setListener(null);
        }
    }

    private void hideOverlay() {
        if (resultOverlay != null) {
            resultOverlay.animate()
                    .alpha(0f)
                    .setDuration(300)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            resultOverlay.setVisibility(View.GONE);
                            if (spinWheel != null) {
                                spinWheel.setEnabled(true);
                            }
                        }
                    });
        }
    }
}
