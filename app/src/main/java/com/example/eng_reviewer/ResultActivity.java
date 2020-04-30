package com.example.eng_reviewer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    TextView TextView_get;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_result);
        TextView_get = findViewById(R.id.TextView_get);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String input = bundle.getString("input");

        TextView_get.setText(input + " // input get");
    }
}
