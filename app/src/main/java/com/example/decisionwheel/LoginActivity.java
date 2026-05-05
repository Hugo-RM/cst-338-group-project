package com.example.decisionwheel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.decisionwheel.database.User;
import com.example.decisionwheel.database.UserRepository;
import com.example.decisionwheel.databinding.LoginActivityBinding;

public class LoginActivity extends AppCompatActivity {
    private LoginActivityBinding binding;
    private UserRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = LoginActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = UserRepository.getRepository(getApplication());

        binding.loginButton.setOnClickListener(view -> verifyUser());
    }

    private void verifyUser() {
        String username = binding.userNameloginEntry.getText().toString().trim();
        String password = binding.passwordLoginEntry.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Username and password cannot be blank", Toast.LENGTH_SHORT).show();
            return;
        }

        // Use a one-time observation or just check the value.
        // To keep it simple and consistent with the project's style, 
        // we'll observe and then remove the observer once we have a result.
        repository.getUserByUserName(username).observe(this, user -> {
            if (user != null) {
                if (password.equals(user.getPassword())) {
                    Log.d("LoginActivity", "Login successful for user: " + username);
                    saveUserAndNavigate(user);
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid password", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.d("LoginActivity", "User not found: " + username);
                Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveUserAndNavigate(User user) {
        SharedPreferences sharedPreferences = getSharedPreferences("decisionWheelPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("userId", user.getId());
        editor.apply();

        Intent intent = new Intent(this, RouletteSelectorActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public static Intent loginIntentFactory(Context context) {
        return new Intent(context, LoginActivity.class);
    }
}
