package com.uottawa.spier033.seg2105.simplecalc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private enum Operator { none, add, minus, multiply, divide }
    private double data1 = 0, data2 = 0;
    private Operator optr = Operator.none;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onNumberClick(View view) {
        EditText resultField = getResultField();

        int id = view.getId();
        String new_text = resultField.getText().toString();

        if (id == R.id.btnClear)
            new_text = "";
        else if (id == R.id.btnDot)
            new_text += ".";
        else if (id == R.id.btn00)
            new_text += "0";
        else if (id == R.id.btn01)
            new_text += "1";
        else if (id == R.id.btn02)
            new_text += "2";
        else if (id == R.id.btn03)
            new_text += "3";
        else if (id == R.id.btn04)
            new_text += "4";
        else if (id == R.id.btn05)
            new_text += "5";
        else if (id == R.id.btn06)
            new_text += "6";
        else if (id == R.id.btn07)
            new_text += "7";
        else if (id == R.id.btn08)
            new_text += "8";
        else if (id == R.id.btn09)
            new_text += "9";
        else {
            new_text = "ERROR";
            Log.d("Error", "Unknown button press in onNumberClick");
        }

        resultField.setText(new_text);
    }

    public void onOperatorClick(View view) {
        EditText resultField = getResultField();
        int id = view.getId();

        if (id == R.id.btnAdd)
            optr = Operator.add;
        else if (id == R.id.btnMinus)
            optr = Operator.minus;
        else if (id == R.id.btnMultiply)
            optr = Operator.multiply;
        else if (id == R.id.btnDivide)
            optr = Operator.divide;
        else {
            resultField.setText("ERROR");
            Log.d("onOperatorClick", "Unknown button press in onOperatorClick");
            return;
        }

        try {
            data1 = Double.parseDouble(resultField.getText().toString());
        } catch (NumberFormatException nfe) {
            resultField.setText("NaN");
            return;
        }

        resultField.setText("");

    }

    public void onResultClick(View view) {
        if (optr != Operator.none) {
            EditText resultField = getResultField();

            try {
                data2 = Double.parseDouble(resultField.getText().toString());
            } catch (NumberFormatException nfe) {
                resultField.setText("NaN");
                return;
            }

            double result = 0;

            if (optr == Operator.add)
                result = data1 + data2;
            else if (optr == Operator.minus)
                result = data1 - data2;
            else if (optr == Operator.multiply)
                result = data1 * data2;
            else if (optr == Operator.divide) {
                if (data2 == 0) {
                    resultField.setText("NaN");
                    return;
                }
                result = data1 / data2;
            }
            optr = Operator.none;
            data1 = result;
            if ((result - (int)result) != 0)
                resultField.setText(String.valueOf(result));
            else
                resultField.setText(String.valueOf((int)result));
        }
    }

    private EditText getResultField() {
        return findViewById(R.id.resultEditText);
    }
}