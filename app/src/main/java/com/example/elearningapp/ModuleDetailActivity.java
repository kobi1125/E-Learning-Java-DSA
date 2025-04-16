package com.example.elearningapp;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.widget.Button;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collections;

public class ModuleDetailActivity extends AppCompatActivity {

    TextView tvModuleTitle, tvExplanation, tvSampleCode, tvKeyConcepts;
    Button btnTakeQuiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_detail);

        // Link views
        tvModuleTitle = findViewById(R.id.tvModuleTitle);
        tvExplanation = findViewById(R.id.tvExplanation);
        tvSampleCode = findViewById(R.id.tvSampleCode);
        tvKeyConcepts = findViewById(R.id.tvKeyConcepts);
        btnTakeQuiz = findViewById(R.id.btnTakeQuiz);

        // Get the module title
        String moduleTitle = getIntent().getStringExtra("moduleTitle");
        tvModuleTitle.setText(moduleTitle);

        // 🔥 Save progress in Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("users")
                .document(userId)
                .collection("progress")
                .document(moduleTitle)
                .set(Collections.singletonMap("completed", true));

        // Only show quiz for the final module
        if ("Java Practice Challenges".equals(moduleTitle)) {
            btnTakeQuiz.setVisibility(View.VISIBLE);
        } else {
            btnTakeQuiz.setVisibility(View.GONE);
        }

        // Set onClick listener
        btnTakeQuiz.setOnClickListener(v -> {
            Intent intent = new Intent(ModuleDetailActivity.this, QuizActivity.class);
            startActivity(intent);
        });

        // Set module details
        switch (moduleTitle) {
            case "Introduction to Java":
                tvExplanation.setText("Java is a popular, platform-independent programming language used for building mobile, web, and enterprise applications.");
                tvSampleCode.setText("public class HelloWorld {\n    public static void main(String[] args) {\n        System.out.println(\"Hello, Java!\");\n    }\n}");
                tvKeyConcepts.setText("• Java Syntax\n• JVM & JDK\n• Hello World Program");
                break;

            case "Object-Oriented Programming":
                tvExplanation.setText("OOP is a programming paradigm based on the concept of 'objects' that contain data and methods.");
                tvSampleCode.setText("class Car {\n    String model;\n    void drive() {\n        System.out.println(\"Driving...\");\n    }\n}");
                tvKeyConcepts.setText("• Classes & Objects\n• Inheritance\n• Polymorphism");
                break;

            case "Data Structures Basics":
                tvExplanation.setText("Data structures help in organizing and storing data efficiently.");
                tvSampleCode.setText("int[] numbers = {1, 2, 3, 4};\nSystem.out.println(numbers[0]);");
                tvKeyConcepts.setText("• Arrays\n• Lists\n• Maps");
                break;

            // Add more modules here...

            default:
                tvExplanation.setText("More content coming soon...");
                tvSampleCode.setText("// Sample code coming soon...");
                tvKeyConcepts.setText("• Stay tuned!");
                break;
        }
    }
}
