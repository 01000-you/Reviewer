package com.example.eng_reviewer;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

import com.example.eng_reviewer.sentences.Snt_manager;

public class MainActivity extends AppCompatActivity {

    Button Button_fail,Button_success, Button_back;
    TextView TextView_eng_snt, TextView_kor_snt;
    EditText EditText_practice;
    Snt_manager sentence = new Snt_manager();
    int success_button_state = 0;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button_fail = findViewById(R.id.Button_fail);
        Button_success = findViewById(R.id.Button_success);
        Button_back = findViewById(R.id.Button_back);

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

            Button_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sentence.before_sentence();
                    TextView_kor_snt.setText(sentence.get_cnt() + ". " + sentence.get_kor());
                    TextView_eng_snt.setText("");
                    success_button_state = 0;
                }
            });
            Button_fail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (success_button_state == 1){ // 정답 공개 상태
                        if(sentence.get_eng().equals("The End")) {
                            sentence.next_sentence();
                            TextView_kor_snt.setText(sentence.get_cnt() + ". " + sentence.get_kor());
                            TextView_eng_snt.setText("");
                            success_button_state = (success_button_state + 1) % 2;
                            sentence.sub_score();
                        }
                    }
                }
            });
            Button_success.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (success_button_state == 0){ // 정답 미공개 상태
                        TextView_eng_snt.setText(sentence.get_eng());
                        if(sentence.get_eng().equals("The End")){
                            Button_success.setClickable(false);
                        }
                    }
                    else { // 정답 공개 상태
                        sentence.next_sentence();
                        TextView_kor_snt.setText(sentence.get_cnt() + ". " + sentence.get_kor());
                        TextView_eng_snt.setText("");
                        sentence.add_score();
                    }
                    success_button_state = (success_button_state + 1) % 2;
                }
            });
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("MainActivity", "On_Stop");
        try {
            sentence.save_csv();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

