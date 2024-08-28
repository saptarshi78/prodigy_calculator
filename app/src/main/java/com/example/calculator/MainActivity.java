package com.example.calculator;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText input;
    private boolean isNewOperation = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input = findViewById(R.id.input);

        GridLayout gridLayout = findViewById(R.id.gridLayout);

        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            View view = gridLayout.getChildAt(i);
            if (view instanceof Button) {
                final Button button = (Button) view;
                button.setOnClickListener(v -> onButtonClick(button.getText().toString()));
            }
        }
    }

    private void onButtonClick(String value) {
        if (value.equals("C")) {
            input.setText("");
            isNewOperation = true;
        } else if (value.equals("=")) {
            String text = input.getText().toString();
            try {
                String result = evaluateExpression(text);
                input.setText(result);
                isNewOperation = true;
            } catch (Exception e) {
                input.setText("Error");
            }
        } else if (value.equals("⌫")) {
            handleBackspace();
        } else if (value.equals("%")) {
            handlePercentage();
        } else {
            if (isNewOperation) {
                input.setText(value);
                isNewOperation = false;
            } else {
                input.append(value);
            }
        }
    }

    private void handleBackspace() {
        String currentText = input.getText().toString();
        if (currentText.length() > 0) {
            input.setText(currentText.substring(0, currentText.length() - 1));
        }
    }

    private void handlePercentage() {
        String text = input.getText().toString();
        if (TextUtils.isEmpty(text)) return;

        // Calculate percentage of the last number
        try {
            // Extract the last number from the expression
            String[] parts = text.split("\\s");
            if (parts.length > 0) {
                String lastPart = parts[parts.length - 1];
                double number = Double.parseDouble(lastPart);
                double percentage = number / 100;

                // Replace the last number with the percentage
                text = text.substring(0, text.length() - lastPart.length()) + percentage;
                input.setText(text);
            }
        } catch (NumberFormatException e) {
            input.setText("Error");
        }
    }

    private String evaluateExpression(String expression) {
        if (TextUtils.isEmpty(expression)) {
            return "";
        }
        try {
            // Evaluate the expression
            double result = new com.example.calculator.Expression(expression).calculate();
            // Format result to avoid unnecessary decimal points
            if (result % 1 == 0) {
                return String.valueOf((int) result);
            } else {
                return String.valueOf(result);
            }
        } catch (Exception e) {
            return "Error";
        }
    }
}
