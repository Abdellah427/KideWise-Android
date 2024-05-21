package com.example.kidwise.playing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collections;

import com.example.kidwise.R;

public class MonthsOfYearActivity extends AppCompatActivity {

    private LinearLayout left_container, right_container;
    private  String[] monthsOfYear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_months_of_year);

        left_container = findViewById(R.id.left_container);
        right_container = findViewById(R.id.right_container);

        monthsOfYear = new String[]{
                getString(R.string.january),
                getString(R.string.february),
                getString(R.string.march),
                getString(R.string.april),
                getString(R.string.may),
                getString(R.string.june),
                getString(R.string.july),
                getString(R.string.august),
                getString(R.string.september),
                getString(R.string.october),
                getString(R.string.november),
                getString(R.string.december)
        };

        setupButtons();
    }

    private void setupButtons() {
        ArrayList<String> monthsList = new ArrayList<>();
        for (String month : monthsOfYear) {
            monthsList.add(month);
        }
        Collections.shuffle(monthsList);

        for (String month : monthsList) {
            Button monthButton = new Button(this);
            monthButton.setText(month);
            monthButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    moveButtonToRightContainer((Button) v);
                }
            });
            left_container.addView(monthButton);
        }
    }

    private void moveButtonToRightContainer(Button button) {
        if (left_container == ((LinearLayout) button.getParent())) {
            left_container.removeView(button);
            right_container.addView(button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    moveButtonToLeftContainer((Button) v);
                }
            });
        } else {
            moveButtonToLeftContainer(button);
        }
        checkOrder();
    }

    private void moveButtonToLeftContainer(Button button) {
        right_container.removeView(button);
        left_container.addView(button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveButtonToRightContainer((Button) v);
            }
        });
    }

    private void checkOrder() {
        boolean isInOrder = true;
        int count = right_container.getChildCount();
        for (int i = 0; i < count; i++) {
            Button button = (Button) right_container.getChildAt(i);
            if (!button.getText().toString().equals(monthsOfYear[i])) {
                isInOrder = false;
                break;
            }
        }

        if (isInOrder && count == monthsOfYear.length) {
            resetOrder();
            Intent intent = new Intent(getApplicationContext(), CongratulationActivity.class);
            intent.putExtra(CongratulationActivity.EXTRA_MESSAGE, getString(R.string.months_correct_order_message));
            startActivity(intent);
            finish();
        } else if (count == monthsOfYear.length) {
            Toast.makeText(this, getString(R.string.months_incorrect_order_message), Toast.LENGTH_LONG).show();
            resetOrder();
        }

    }

    private void resetOrder() {
        left_container.removeAllViews();
        right_container.removeAllViews();
        setupButtons();
    }
}
