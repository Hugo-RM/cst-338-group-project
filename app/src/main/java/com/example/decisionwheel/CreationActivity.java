package com.example.decisionwheel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.decisionwheel.database.AppDatabase;
import com.example.decisionwheel.database.UserEntity;
import com.example.decisionwheel.databinding.CreatetionActivityBinding;

public class CreationActivity extends AppCompatActivity {

    private CreatetionActivityBinding binding;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = CreatetionActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = AppDatabase.getInstance(this);

        binding.createButton.setOnClickListener(v -> createAccount());
    }

    private void createAccount() {
        String username = binding.usernameEntry.getText().toString().trim();
        String usernameConfirm = binding.usernameEntryConfirm.getText().toString().trim();
        String password = binding.passwordEntry.getText().toString().trim();
        String passwordConfirm = binding.passwordEntryConfirm.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!username.equals(usernameConfirm)) {
            Toast.makeText(this, "Usernames do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(passwordConfirm)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        AppDatabase.databaseWriteExecutor.execute(() -> {
            UserEntity existingUser = db.userDao().findByUsername(username);
            if (existingUser != null) {
                runOnUiThread(() -> Toast.makeText(CreationActivity.this, "Username already exists", Toast.LENGTH_SHORT).show());
                return;
            }

            UserEntity newUser = new UserEntity(username, password);
            long userId = db.userDao().insert(newUser);

            runOnUiThread(() -> {
                SharedPreferences prefs = getSharedPreferences("decisionWheelPrefs", MODE_PRIVATE);
                prefs.edit().putInt("userId", (int) userId).apply();

                Toast.makeText(CreationActivity.this, "Account created successfully!", Toast.LENGTH_SHORT).show();
                startActivity(LandingActivity.newIntent(CreationActivity.this, (int) userId));
                finish();
            });
        });
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, CreationActivity.class);
    }
}
