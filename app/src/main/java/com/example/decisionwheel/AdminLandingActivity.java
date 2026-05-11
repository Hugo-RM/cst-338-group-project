package com.example.decisionwheel;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.decisionwheel.database.AppDatabase;
import com.example.decisionwheel.database.UserEntity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AdminLandingActivity extends AppCompatActivity {

    private AppDatabase db;
    private UserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_landing);

        db = AppDatabase.getInstance(this);

        RecyclerView recyclerView = findViewById(R.id.usersRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserAdapter();
        recyclerView.setAdapter(adapter);

        adapter.setOnUserActionListener(new UserAdapter.OnUserActionListener() {
            @Override
            public void onUserClick(UserEntity user) {
                startActivity(LandingActivity.newIntent(AdminLandingActivity.this, user.getId()));
            }

            @Override
            public void onEditUsername(UserEntity user) {
                showEditUsernameDialog(user);
            }

            @Override
            public void onEditPassword(UserEntity user) {
                showEditPasswordDialog(user);
            }

            @Override
            public void onDeleteUser(UserEntity user) {
                showDeleteConfirmDialog(user);
            }

            @Override
            public void onToggleAdmin(UserEntity user) {
                user.setAdmin(!user.isAdmin());
                AppDatabase.databaseWriteExecutor.execute(() -> db.userDao().update(user));
            }
        });

        // Observe all users initially
        db.userDao().getAllUsersLD().observe(this, users -> adapter.setUsers(users));

        // Floating Action Button to create a user
        FloatingActionButton fabAddUser = findViewById(R.id.fabAddUser);
        fabAddUser.setOnClickListener(v -> {
            startActivity(CreationActivity.newIntent(AdminLandingActivity.this));
        });

        // Search feature
        EditText searchUsers = findViewById(R.id.searchUsers);
        searchUsers.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateSearch(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void updateSearch(String query) {
        // Remove existing observers to prevent memory leaks and conflicting updates
        db.userDao().getAllUsersLD().removeObservers(this);
        
        if (query.isEmpty()) {
            db.userDao().getAllUsersLD().observe(this, users -> adapter.setUsers(users));
        } else {
            db.userDao().searchUsersLD(query).observe(this, users -> adapter.setUsers(users));
        }
    }

    private void showEditUsernameDialog(UserEntity user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Username");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(user.getUsername());
        builder.setView(input);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String newUsername = input.getText().toString().trim();
            if (!newUsername.isEmpty()) {
                user.setUsername(newUsername);
                AppDatabase.databaseWriteExecutor.execute(() -> db.userDao().update(user));
            } else {
                Toast.makeText(this, "Username cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void showEditPasswordDialog(UserEntity user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Password");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String newPassword = input.getText().toString();
            if (!newPassword.isEmpty()) {
                user.setPassword(newPassword);
                AppDatabase.databaseWriteExecutor.execute(() -> db.userDao().update(user));
            } else {
                Toast.makeText(this, "Password cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void showDeleteConfirmDialog(UserEntity user) {
        new AlertDialog.Builder(this)
                .setTitle("Delete User")
                .setMessage("Are you sure you want to delete " + user.getUsername() + "?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    AppDatabase.databaseWriteExecutor.execute(() -> db.userDao().delete(user));
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, AdminLandingActivity.class);
    }
}
