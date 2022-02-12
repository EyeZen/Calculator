package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    TextView view_input, view_result;
    private HashMap<String, Button> keys = new HashMap<>();
    private CalcBuffer buffer = new CalcBuffer();
    private static final String KEYS[] = {
            "parenthesis_open", "parenthesis_close", "power", "scientific",
            "ac", "del", "plusminus","divide",
            "7", "8", "9", "multiply",
            "4", "5", "6", "minus",
            "1", "2", "3", "plus",
            "percent","0","dot","equals"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        view_input = findViewById(R.id.view_input);
        view_result = findViewById(R.id.view_result);
        // row1
        keys.put("parenthesis_open", (Button)findViewById(R.id.key_parenthesis_open));
        keys.put("parenthesis_close", (Button)findViewById(R.id.key_parenthesis_close));
        keys.put("power", (Button)findViewById(R.id.key_power));
        keys.put("scientific", (Button)findViewById(R.id.key_scientific));
        // row2
        keys.put("ac", (Button)findViewById(R.id.key_ac));
        keys.put("del", (Button)findViewById(R.id.key_del));
        keys.put("plusminus", (Button)findViewById(R.id.key_plusminus));
        keys.put("divide", (Button)findViewById(R.id.key_divide));
        // row3
        keys.put("7", (Button)findViewById(R.id.key_7));
        keys.put("8", (Button)findViewById(R.id.key_8));
        keys.put("9", (Button)findViewById(R.id.key_9));
        keys.put("multiply", (Button)findViewById(R.id.key_multiply));
        // row4
        keys.put("4", (Button)findViewById(R.id.key_4));
        keys.put("5", (Button)findViewById(R.id.key_5));
        keys.put("6", (Button)findViewById(R.id.key_6));
        keys.put("minus", (Button)findViewById(R.id.key_minus));
        // row5
        keys.put("1", (Button)findViewById(R.id.key_1));
        keys.put("2", (Button)findViewById(R.id.key_2));
        keys.put("3", (Button)findViewById(R.id.key_3));
        keys.put("plus", (Button)findViewById(R.id.key_plus));
        // row6
        keys.put("percent", (Button)findViewById(R.id.key_percent));
        keys.put("0", (Button)findViewById(R.id.key_0));
        keys.put("dot", (Button)findViewById(R.id.key_dot));
        keys.put("equals", (Button)findViewById(R.id.key_equals));

        // set click listener for all buttons
        for (String key: KEYS) {
            keys.get(key).setOnClickListener(this::handleInput);
        }
    }

    void handleInput(View v) {
        char ch=' ';
        switch(v.getId()) {
            case R.id.key_parenthesis_open: ch='(';
                break;
            case R.id.key_parenthesis_close: ch=')';
                break;
            case R.id.key_power: ch='^';
                break;
            case R.id.key_scientific: // TODO: implement scientific calculator
                break;

            case R.id.key_plus: ch='+';
                break;
            case R.id.key_minus: ch='-';
                break;
            case R.id.key_divide: ch='/';
                break;
            case R.id.key_multiply: ch='x';
                break;
            case R.id.key_percent: ch='%';
                break;

            case R.id.key_0: ch='0';
                break;
            case R.id.key_1: ch='1';
                break;
            case R.id.key_2: ch='2';
                break;
            case R.id.key_3: ch='3';
                break;
            case R.id.key_4: ch='4';
                break;
            case R.id.key_5: ch='5';
                break;
            case R.id.key_6: ch='6';
                break;
            case R.id.key_7: ch='7';
                break;
            case R.id.key_8: ch='8';
                break;
            case R.id.key_9: ch='9';
                break;
            case R.id.key_dot: ch='.';
                break;

            case R.id.key_ac:
                buffer.clear();
                break;
            case R.id.key_del: buffer.removeLast();
                break;
            case R.id.key_plusminus:  buffer.changeSign();
                break;
            case R.id.key_equals:
                String result = buffer.getResult();
                buffer.clear();
                buffer.append(result);
                break;

            default:
        }

        if(ch!=' ') buffer.append(ch);
        view_input.setText(buffer.toString());
        view_result.setText(buffer.getResult());
    }
}