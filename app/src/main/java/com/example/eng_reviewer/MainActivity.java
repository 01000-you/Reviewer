package com.example.eng_reviewer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

import com.example.eng_reviewer.sentences.Snt_manager;

public class MainActivity extends AppCompatActivity {

    Button Button_fail,Button_success, Button_view_ans;
    TextView TextView_eng_snt, TextView_kor_snt;
    EditText EditText_practice;
    Snt_manager sentence = new Snt_manager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button_fail = findViewById(R.id.Button_fail);
        Button_success = findViewById(R.id.Button_success);
        Button_view_ans = findViewById(R.id.Button_view_ans);

        TextView_eng_snt = findViewById(R.id.TextView_eng_snt);
        TextView_kor_snt = findViewById(R.id.TextView_kor_snt);

        EditText_practice = findViewById(R.id.EditText_practice);
        try {
            sentence.load_csv();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Load_csv", "CSV읽기 실패");
        }
        TextView_kor_snt.setText(sentence.get_cnt() + ". " + sentence.get_kor());
//        sentence.add_cnt();

//        Button_success.setClickable(true);
            Button_view_ans.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TextView_eng_snt.setText(sentence.get_eng());
                    if(sentence.get_eng() == "The End"){
                        Button_success.setClickable(false);
                    }
                }
            });
            Button_success.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    sentence.add_cnt();
                    try {
                        sentence.next_sentence();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    TextView_kor_snt.setText(sentence.get_cnt() + ". " + sentence.get_kor());
                    TextView_eng_snt.setText("");
                }
            });
    }
}

