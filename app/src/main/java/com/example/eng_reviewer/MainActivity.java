package com.example.eng_reviewer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button Button_fail,Button_success, Button_view_ans;
    TextView TextView_eng_snt, TextView_kor_snt;
    EditText EditText_practice;
    String wanted_string = "1234";
    String input_string;
    CSVReader reader;
    String[] next_sentence;
    int sentence_cnt = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button_fail     = findViewById(R.id.Button_fail);
        Button_success  = findViewById(R.id.Button_success);
        Button_view_ans  = findViewById(R.id.Button_view_ans);

        TextView_eng_snt = findViewById(R.id.TextView_eng_snt);
        TextView_kor_snt = findViewById(R.id.TextView_kor_snt);

        EditText_practice = findViewById(R.id.EditText_practice);
        try {
            reader = load_csv();
            next_sentence = next_sentence();
            TextView_kor_snt.setText( String.valueOf(sentence_cnt++) + ", " + next_sentence[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }


//        Button_success.setClickable(true);
        Button_view_ans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                TextView_eng_snt.setText(next_sentence[1]);
            }
        });
        Button_success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    next_sentence = next_sentence();
                    TextView_kor_snt.setText(String.valueOf(sentence_cnt++) + ", " + next_sentence[0]);
                    TextView_eng_snt.setText("");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public CSVReader load_csv() throws IOException {
        CSVReader read = new CSVReader(new FileReader("/sdcard/main_data_test.csv"));
        return read;
    }
    public String[] next_sentence() throws IOException {
        String[] record = null;
        String[] null_return = {"The end", "마지막 문장입니다.", " "};

        record = reader.readNext();
        if (record != null) {
            Log.d("load_csv", record[0] + ",");
            return record;
        }
        else
            return null_return;
    }
}

