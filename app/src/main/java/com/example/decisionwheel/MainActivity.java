package com.example.decisionwheel;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.decisionwheel.database.AppDatabase;
import com.example.decisionwheel.wheel.Slice;
import com.example.decisionwheel.wheel.Wheel;
import com.example.decisionwheel.wheel.WheelVIew;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private Wheel activeWheel = new Wheel();
    private Button spinWheel;
    private MaterialButton wheelConfig;
    private WheelVIew wheelView;
    private TextView textView;
    private AppDatabase db;

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
        setContentView(R.layout.activity_main);
        
        db = AppDatabase.getDatabase(this);

        // Initialize Overlay Views
        resultOverlay = findViewById(R.id.resultOverlay);
        overlayResult = findViewById(R.id.overlayResult);
        btnAwesome = findViewById(R.id.btnAwesome);

        if (btnAwesome != null) {
            btnAwesome.setOnClickListener(v -> hideOverlay());
        }

        textView = findViewById(R.id.tag_category);
        wheelView = findViewById(R.id.wheelVIew);
        spinWheel = findViewById(R.id.spinWheel);
        
        if (spinWheel != null) {
            spinWheel.setOnClickListener(v -> spinTheWheel());
        }

        wheelConfig = findViewById(R.id.wheel_config);
        if (wheelConfig != null) {
            wheelConfig.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, RouletteSelectorActivity.class);
                startActivity(intent);
            });
        }

        int wheelId = getIntent().getIntExtra("WHEEL_ID", -1);
        if (wheelId != -1) {
            loadWheel(wheelId);
        } else {
            setupDefaultWheel();
        }

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

    private void setupDefaultWheel() {
        activeWheel = new Wheel();
        activeWheel.insertSlice(new Slice("Movie Night", "Fun", Color.RED, 0));
        activeWheel.insertSlice(new Slice("Order Pizza", "Food", Color.GREEN, 0));
        activeWheel.insertSlice(new Slice("Go for a Walk", "Exercise", Color.BLUE, 0));
        activeWheel.insertSlice(new Slice("Play Games", "Fun", Color.YELLOW, 0));
        activeWheel.setCategory("SAMPLE WHEEL");
        
        updateUI();
    }

    private void loadWheel(int wheelId) {
        db.wheelDAO().getWheelWithSlicesById(wheelId).observe(this, wheelWithSlices -> {
            if (wheelWithSlices != null) {
                activeWheel = wheelWithSlices.wheel;
                activeWheel.setSlices(new ArrayList<>(wheelWithSlices.slices));
                updateUI();
            }
        });
    }

    private void updateUI() {
        if (textView != null) {
            textView.setText(activeWheel.getCategory());
        }
        if (wheelView != null) {
            wheelView.setWheel(activeWheel);
        }
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

            double gForce = Math.sqrt(x * x + y * y + z * z) / SensorManager.GRAVITY_EARTH;
            
            if (gForce > 2.5) {
                if (spinWheel != null && spinWheel.isEnabled()) {
                    spinTheWheel();
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    private void spinTheWheel() {
        if (activeWheel.getSlices().isEmpty()) return;

        spinWheel.setEnabled(false);

        int winningIndex = (int) (Math.random() * activeWheel.getSlices().size());
        Slice result = activeWheel.getSlices().get(winningIndex);

        float sweepPerSlice = 360f / activeWheel.getSlices().size();
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
