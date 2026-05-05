package com.example.decisionwheel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.example.decisionwheel.database.UserEntity;
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

        LiveData<UserEntity> userObserver = repository.getUserByUserName(username);
        userObserver.observe(this, user -> {
            if (user != null) {
                if (password.equals(user.getPassword())) {
                    SharedPreferences prefs = getSharedPreferences("decisionWheelPrefs", MODE_PRIVATE);
                    prefs.edit().putInt("userId", user.getId()).apply();

                    if (user.isAdmin()) {
                        startActivity(AdminLandingActivity.newIntent(getApplicationContext()));
                    } else {
                        startActivity(LandingActivity.newIntent(getApplicationContext(), user.getId()));
                    }
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid password", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(LoginActivity.this, username + " is not a valid username", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, LoginActivity.class);
    }
}
