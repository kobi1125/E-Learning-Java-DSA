package com.example.elearningapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.airbnb.lottie.LottieAnimationView;


import androidx.appcompat.app.AppCompatActivity;

public class QuizActivity extends AppCompatActivity {

    TextView tvQuestion;
    RadioGroup radioGroup;
    RadioButton option1, option2, option3;
    Button btnSubmit;

    LottieAnimationView lottieCongrats;

    int currentQuestionIndex = 0;
    int score = 0;

    // Define questions, options, and answers
    String[] questions = {
            "Which line prints 'Hello, World!' in Java?",
            "Which keyword is used to inherit a class?",
            "Which data structure uses LIFO?"
    };

    String[][] options = {
            {"echo 'Hello, World!';", "System.out.println(\"Hello, World!\");", "printf(\"Hello, World!\");"},
            {"inherits", "extends", "implements"},
            {"Queue", "Stack", "Array"}
    };

    String[] correctAnswers = {
            "System.out.println(\"Hello, World!\");",
            "extends",
            "Stack"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        tvQuestion = findViewById(R.id.tvQuestion);
        radioGroup = findViewById(R.id.radioGroup);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        btnSubmit = findViewById(R.id.btnSubmit);


        lottieCongrats = findViewById(R.id.lottieCongrats);

        loadQuestion();

        btnSubmit.setOnClickListener(v -> {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            if (selectedId == -1) {
                Toast.makeText(QuizActivity.this, "Please select an answer!", Toast.LENGTH_SHORT).show();
                return;
            }

            RadioButton selectedOption = findViewById(selectedId);
            String selectedText = selectedOption.getText().toString();

            if (selectedText.equals(correctAnswers[currentQuestionIndex])) {
                score++;
            }

            currentQuestionIndex++;

            if (currentQuestionIndex < questions.length) {
                loadQuestion();
            } else {
                showResult();
            }
        });
    }

    private void loadQuestion() {
        tvQuestion.setText(questions[currentQuestionIndex]);
        option1.setText(options[currentQuestionIndex][0]);
        option2.setText(options[currentQuestionIndex][1]);
        option3.setText(options[currentQuestionIndex][2]);
        radioGroup.clearCheck();
    }

    private void showResult() {
        String message = "Your Score: " + score + "/" + questions.length;
        tvQuestion.setText(message);

        option1.setVisibility(View.GONE);
        option2.setVisibility(View.GONE);
        option3.setVisibility(View.GONE);
        btnSubmit.setVisibility(View.GONE);

        if (score == questions.length) {
            // Show animation if all answers are correct
            lottieCongrats.setVisibility(View.VISIBLE);
            Toast.makeText(this, "🎉 Congratulations! You nailed it!", Toast.LENGTH_LONG).show();
        }
    }
}
