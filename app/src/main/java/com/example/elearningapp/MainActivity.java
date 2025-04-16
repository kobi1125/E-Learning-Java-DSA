package com.example.elearningapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Get current user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();



        if (user == null) {
            // No user is signed in, navigate to the login activity
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            // User is already signed in, navigate to the home screen or dashboard
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);  // HomeActivity is the target activity
            startActivity(intent);
            finish();  // Close the MainActivity to avoid the user returning to it after navigating
        }
    }
}
