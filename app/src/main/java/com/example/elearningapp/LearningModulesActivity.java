package com.example.elearningapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log; // <-- ADD THIS LINE
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser; // <-- ADD THIS LINE
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LearningModulesActivity extends AppCompatActivity {

    private TextView tvProgressText;
    private ProgressBar progressBar;
    private Button btnCertificate;

    private final String[] modules = {
            "Introduction to Java",
            "Object-Oriented Programming",
            "Data Structures Basics",
            "Arrays & Linked Lists",
            "Stack, Queue, Trees",
            "Sorting & Searching Algorithms",
            "Java Practice Challenges"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning_modules);

        tvProgressText = findViewById(R.id.tvProgressText);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(100);

        btnCertificate = findViewById(R.id.btnCertificate);
        btnCertificate.setVisibility(View.GONE); // Hide by default

        ListView listView = findViewById(R.id.listViewModules);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, modules);
        listView.setAdapter(adapter);

        // Handle module click
        listView.setOnItemClickListener((adapterView, view, position, id) -> {
            Intent intent = new Intent(LearningModulesActivity.this, ModuleDetailActivity.class);
            intent.putExtra("moduleTitle", modules[position]);
            startActivity(intent);
        });

        // Handle certificate button click
        btnCertificate.setOnClickListener(v -> {
            Intent intent = new Intent(LearningModulesActivity.this, CertificateSuccessActivity.class);
            startActivity(intent);
        });

        checkCurrentUser(); // ✅ Make sure the user is logged in and print userId
    }

    private void checkCurrentUser() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            Log.d("USER_ID", "Current user ID: " + userId);
            Toast.makeText(this, "User ID: " + userId, Toast.LENGTH_LONG).show(); // 👈

            loadProgress(userId);
        } else {
            tvProgressText.setText("User not logged in");
        }
    }


    private void loadProgress(String userId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users")
                .document(userId)
                .collection("progress")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    int completed = 0;
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Boolean isDone = doc.getBoolean("completed");
                        if (isDone != null && isDone) {
                            completed++;
                        }
                    }

                    int total = modules.length;
                    int percent = (int) ((completed * 100.0f) / total);

                    progressBar.setProgress(percent);
                    tvProgressText.setText("Progress: " + completed + " / " + total + " modules completed");

                    if (completed == total) {
                        Toast.makeText(this, "🎉 All modules completed!", Toast.LENGTH_SHORT).show();

                        db.collection("users").document(userId).get()
                                .addOnSuccessListener(userDoc -> {
                                    Boolean issued = userDoc.getBoolean("certificateIssued");

                                    // 💡 Always show the button
                                    btnCertificate.setVisibility(View.VISIBLE);

                                    // If not issued, update Firestore
                                    if (issued == null || !issued) {
                                        db.collection("users").document(userId)
                                                .update("certificateIssued", true)
                                                .addOnSuccessListener(aVoid -> {
                                                    Toast.makeText(this, "🎓 Certificate unlocked!", Toast.LENGTH_SHORT).show();
                                                });
                                    }
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    tvProgressText.setText("Failed to load progress");
                });
    }
}