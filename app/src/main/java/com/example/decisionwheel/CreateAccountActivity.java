package com.example.decisionwheel;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.example.decisionwheel.databinding.ActivityMakeAccountBinding;

public class CreateAccountActivity extends AppCompatActivity {
    private ActivityMakeAccountBinding binding;
    private UserRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMakeAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = UserRepository.getRepository(getApplication());

        binding.createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount();
            }
        });
    }

    private void createAccount() {
        String username = binding.usernameCreateEntry.getText().toString();
        String password = binding.passwordCreateEntry.getText().toString();

        Log.d("CreateAccount", "Button clicked, username: " + username);

        // validate fields
        if (username.isEmpty()) {
            Toast.makeText(this, "Username cannot be blank", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.isEmpty()) {
            Toast.makeText(this, "Password cannot be blank", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("CreateAccount", "Validation passed, checking username");

        // check if username already exists
        AppDatabase.databaseWriteExecutor.execute(() -> {
            User existingUser = repository.getUserByUserNameNow(username);
            if (existingUser != null) {
                runOnUiThread(() -> Toast.makeText(CreateAccountActivity.this, "Username already taken", Toast.LENGTH_SHORT).show());
            } else {
                repository.insertUser(new User(username, password));
                runOnUiThread(() -> {
                    Toast.makeText(CreateAccountActivity.this, "Account created!", Toast.LENGTH_SHORT).show();
                    startActivity(LoginActivity.loginIntentFactory(getApplicationContext()));
                });
            }
        });


    } // closes createAccount()
    public static Intent createAccountIntentFactory(Context context) {
        return new Intent(context, CreateAccountActivity.class);
    }
}