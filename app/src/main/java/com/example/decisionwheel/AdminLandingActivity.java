package com.example.decisionwheel;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.decisionwheel.database.AppDatabase;

public class AdminLandingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_landing);

        AppDatabase db = AppDatabase.getInstance(this);

        RecyclerView recyclerView = findViewById(R.id.usersRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        UserAdapter adapter = new UserAdapter();
        recyclerView.setAdapter(adapter);

        db.userDao().getAllUsersLD().observe(this, adapter::setUsers);
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, AdminLandingActivity.class);
    }
}
