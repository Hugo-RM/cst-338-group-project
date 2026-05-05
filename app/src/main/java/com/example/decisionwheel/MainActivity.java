package com.example.decisionwheel;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private static final String EXTRA_WHEEL_ID = "extra_wheel_id";

    private Wheel activeWheel = new Wheel();
    private Button spinWheel;
    private MaterialButton wheelConfig;
    private WheelVIew wheelView;
    private TextView textView;
    private ConstraintLayout resultOverlay;
    private TextView overlayResult;
    private Button btnAwesome;

    private SensorManager sensorManager;
    private Sensor accelerometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.tag_category);
        wheelView = findViewById(R.id.wheelVIew);
        spinWheel = findViewById(R.id.spinWheel);
        resultOverlay = findViewById(R.id.resultOverlay);
        overlayResult = findViewById(R.id.overlayResult);
        btnAwesome = findViewById(R.id.btnAwesome);

        if (spinWheel != null) spinWheel.setOnClickListener(v -> spinTheWheel());
        if (btnAwesome != null) btnAwesome.setOnClickListener(v -> hideOverlay());

        wheelConfig = findViewById(R.id.wheel_config);
        if (wheelConfig != null) {
            wheelConfig.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, RouletteSelectorActivity.class)));
        }

        int wheelId = getIntent().getIntExtra(EXTRA_WHEEL_ID, -1);
        if (wheelId != -1) {
            loadWheelFromDb(wheelId);
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

    private void loadWheelFromDb(int wheelId) {
        AppDatabase db = AppDatabase.getInstance(this);
        AppDatabase.databaseWriteExecutor.execute(() -> {
            java.util.List<Slice> slices = db.sliceDao().getSlicesForWheel(wheelId);
            com.example.decisionwheel.wheel.WheelEntity entity = db.wheelDao().findById(wheelId);
            runOnUiThread(() -> {
                for (Slice s : slices) activeWheel.insertSlice(s);
                if (entity != null && textView != null) textView.setText(entity.getName());
                if (wheelView != null) wheelView.setWheel(activeWheel);
            });
        });
    }

    private void setupDefaultWheel() {
        activeWheel.insertSlice(new Slice("Movie Night", Color.RED));
        activeWheel.insertSlice(new Slice("Order Pizza", Color.GREEN));
        activeWheel.insertSlice(new Slice("Go for a Walk", Color.BLUE));
        activeWheel.insertSlice(new Slice("Play Games", Color.YELLOW));
        if (textView != null) textView.setText("FRIDAY NIGHT");
        if (wheelView != null) wheelView.setWheel(activeWheel);
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
        if (sensorManager != null) sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0], y = event.values[1], z = event.values[2];
            double gForce = Math.sqrt(x * x + y * y + z * z) / SensorManager.GRAVITY_EARTH;
            if (gForce > 2.5 && spinWheel != null && spinWheel.isEnabled()) spinTheWheel();
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

    private void showOverlay(String resultText) {
        if (overlayResult != null) overlayResult.setText(resultText);
        if (resultOverlay != null) resultOverlay.setVisibility(android.view.View.VISIBLE);
    }

    private void hideOverlay() {
        if (resultOverlay != null) resultOverlay.setVisibility(android.view.View.GONE);
        if (spinWheel != null) spinWheel.setEnabled(true);
    }

    public static Intent newIntent(Context context, int wheelId) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(EXTRA_WHEEL_ID, wheelId);
        return intent;
    }
}
