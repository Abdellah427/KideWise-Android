package com.example.kidwise.playing;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kidwise.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SeasonActivity extends AppCompatActivity {

    private ImageView imageView;
    private RadioButton optionOne, optionTwo, optionThree, optionFour;
    private ImageView crossOne, crossTwo, crossThree, crossFour;

    private int currentRound = 0;
    private final int totalRounds = 4;
    private final String[] seasons = {"spring", "summer", "autumn", "winter"};
    private String[] seasonsWithCapital;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_season);

        imageView = findViewById(R.id.imageView);
        optionOne = findViewById(R.id.optionOne);
        optionTwo = findViewById(R.id.optionTwo);
        optionThree = findViewById(R.id.optionThree);
        optionFour = findViewById(R.id.optionFour);

        crossOne = findViewById(R.id.crossOne);
        crossTwo = findViewById(R.id.crossTwo);
        crossThree = findViewById(R.id.crossThree);
        crossFour = findViewById(R.id.crossFour);

        seasonsWithCapital = new String[]{
                getString(R.string.autumn),
                getString(R.string.winter),
                getString(R.string.spring),
                getString(R.string.summer)
        };

        setupQuestion();

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RadioButton selectedRadioButton = (RadioButton) view;
                boolean correct = selectedRadioButton.getText().toString().equals(seasonsWithCapital[currentRound]);
                Log.d("selectedRadioButton", selectedRadioButton.getText().toString());
                Log.d("seasons", seasons[currentRound]);

                handleAnswer(selectedRadioButton, correct);
            }
        };

        optionOne.setOnClickListener(listener);
        optionTwo.setOnClickListener(listener);
        optionThree.setOnClickListener(listener);
        optionFour.setOnClickListener(listener);
    }

    private void handleAnswer(RadioButton selectedRadioButton, boolean correct) {
        deselectAllRadioButtons();
        clearCrosses();
        if (correct) {
            Toast.makeText(SeasonActivity.this, getString(R.string.correct_answer), Toast.LENGTH_SHORT).show();
            currentRound++;
            if (currentRound < totalRounds) {
                setupQuestion();
            } else {
                Intent intent = new Intent(getApplicationContext(), CongratulationActivity.class);
                intent.putExtra(CongratulationActivity.EXTRA_MESSAGE, getString(R.string.congratulations_all_seasons));
                startActivity(intent);
                finish();
            }
        } else {
            ImageView crossImageView = findViewById(getCrossId(selectedRadioButton.getId()));
            if (crossImageView != null) {
                crossImageView.setVisibility(View.VISIBLE);
            }
            Toast.makeText(SeasonActivity.this, getString(R.string.incorrect_try_again), Toast.LENGTH_SHORT).show();
        }
    }


    private void setupQuestion() {
        deselectAllRadioButtons();
        clearCrosses();

        int correctAnswerPosition = new Random().nextInt(4);
        int imageId = getResources().getIdentifier("season_" + seasons[currentRound], "drawable", getPackageName());
        imageView.setImageResource(imageId);

        List<String> options = new ArrayList<>(Arrays.asList(seasonsWithCapital));
        options.remove(currentRound);

        Collections.shuffle(options);

        while (options.size() > 3) {
            options.remove(options.size() - 1);
        }

        options.add(correctAnswerPosition, seasonsWithCapital[currentRound]);

        optionOne.setText(options.get(0));
        optionTwo.setText(options.get(1));
        optionThree.setText(options.get(2));
        optionFour.setText(options.get(3));
    }


    private void clearCrosses() {
        crossOne.setVisibility(View.INVISIBLE);
        crossTwo.setVisibility(View.INVISIBLE);
        crossThree.setVisibility(View.INVISIBLE);
        crossFour.setVisibility(View.INVISIBLE);
    }

    private void deselectAllRadioButtons() {
        optionOne.setChecked(false);
        optionTwo.setChecked(false);
        optionThree.setChecked(false);
        optionFour.setChecked(false);
    }

    private int getCrossId(int radioButtonId) {
        if (radioButtonId == R.id.optionOne) {
            return R.id.crossOne;
        } else if (radioButtonId == R.id.optionTwo) {
            return R.id.crossTwo;
        } else if (radioButtonId == R.id.optionThree) {
            return R.id.crossThree;
        } else if (radioButtonId == R.id.optionFour) {
            return R.id.crossFour;
        }
        return -1;
    }
}
