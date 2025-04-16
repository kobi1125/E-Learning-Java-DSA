package com.example.elearningapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        TextView welcomeText = findViewById(R.id.tvWelcomeMessage);
        Button btnStartLearning = findViewById(R.id.btnStartLearning);

        // Optional: get user email from login
        String email = getIntent().getStringExtra("email");
        if (email != null) {
            welcomeText.setText("Welcome to the E-learning app, " + email + "!");
        }

        // 🧭 Go to LearningModulesActivity on button click
        btnStartLearning.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, LearningModulesActivity.class);
            startActivity(intent);
        });
    }
}
