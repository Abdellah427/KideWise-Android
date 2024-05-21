package com.example.kidwise.playing;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kidwise.ContinueDialog;
import com.example.kidwise.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DigitMemoryActivity extends AppCompatActivity {
    private TextView instructionTextView;
    private EditText answerEditText;
    private Button startButton;
    private Button submitButton;
    private Button resetButton;

    private final List<Integer> digitSequence = new ArrayList<>();
    private int currentIndex = 0;

    private static final int NUMBEROFNUMBER = 5;
    private Handler handler = new Handler();
    private final int delayMillis = 2000;
    private int correctCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_digit_memory);

        instructionTextView = findViewById(R.id.instructionTextView);
        answerEditText = findViewById(R.id.answerEditText);
        startButton = findViewById(R.id.startButton);
        submitButton = findViewById(R.id.submitAnswerButton);
        resetButton = new Button(this);
        resetButton.setText(getString(R.string.reset_button_text));

        if (correctCount == 0) {
            instructionTextView.setText(getString(R.string.instructions_forward));
        } else {
            instructionTextView.setText(getString(R.string.instructions_backward));
        }


        startButton.setOnClickListener(v -> startSequence());
        submitButton.setOnClickListener(v -> checkAnswer());
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetSequence();
            }
        });


    }

    private void startSequence() {
        startButton.setVisibility(View.GONE);
        generateDigitSequence();
        displayNextDigit();
    }

    private void displayNextDigit() {
        if (currentIndex < digitSequence.size()) {
            instructionTextView.setText(digitSequence.get(currentIndex).toString());
            instructionTextView.setTextSize(60);
            currentIndex++;
            handler.postDelayed(this::displayNextDigit, delayMillis);
        } else {
            showAnswerField();

        }
    }


    private void showAnswerField() {
        instructionTextView.setTextSize(20);
        if (correctCount == 0) {
            instructionTextView.setText(getString(R.string.enter_digits_forward));
        } else {
            instructionTextView.setText(getString(R.string.enter_digits_backward));
        }

        answerEditText.setVisibility(View.VISIBLE);
        submitButton.setVisibility(View.VISIBLE);
    }

    public void checkAnswer() {
        String userAnswer = answerEditText.getText().toString().trim();
        Boolean test;
        if(correctCount==1){
            userAnswer=new StringBuilder(userAnswer).reverse().toString();
        }

        if (userAnswer.equals(getSequenceAsString())) {
            correctCount++;
            if (correctCount >= 2) {
                showDialog();
            } else {
                Toast.makeText(this, getString(R.string.correct_next_sequence), Toast.LENGTH_SHORT).show();
                resetSequence();
                startSequence();
            }
        } else {
            Toast.makeText(this, getString(R.string.incorrect_try_again), Toast.LENGTH_SHORT).show();
        }

    }

    private String getSequenceAsString() {
        StringBuilder sequenceBuilder = new StringBuilder();
        for (Integer digit : digitSequence) {
            sequenceBuilder.append(digit);
        }
        return sequenceBuilder.toString();
    }

    private void showDialog() {
        String message = getString(R.string.congratulations_message_number);
        ContinueDialog.showContinueDialog(this, message, resetButton);
        resetSequence();
    }

    private void resetSequence() {
        digitSequence.clear();
        currentIndex = 0;
        correctCount = 0;
        answerEditText.setText("");
        answerEditText.setVisibility(View.INVISIBLE);
        submitButton.setVisibility(View.INVISIBLE);
        startButton.setVisibility(View.VISIBLE);
        if (correctCount == 0) {
            instructionTextView.setText(getString(R.string.instructions_forward));
        } else {
            instructionTextView.setText(getString(R.string.instructions_backward));
        }
        generateDigitSequence();
    }


    private void generateDigitSequence() {
        Random random = new Random();
        digitSequence.clear();
        int previousDigit = -1;
        for (int i = 0; i < NUMBEROFNUMBER; i++) {
            int newDigit = random.nextInt(10);
            while (newDigit == previousDigit) {
                newDigit = random.nextInt(10);
            }
            digitSequence.add(newDigit);
            previousDigit = newDigit;
        }
    }

}
