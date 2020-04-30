package com.example.eng_reviewer.sentences;
import android.util.Log;

import com.example.eng_reviewer.DEFINE;
import com.opencsv.CSVReader;

import java.io.FileReader;
import java.io.IOException;

public class Snt_manager {

    private CSVReader reader;
    private String[] next_sentence;
    private int sentence_cnt = 0;

    public void load_csv() throws IOException {
        reader = new CSVReader(new FileReader("/sdcard/main_data_test.csv"));
        next_sentence();
    }
    public void next_sentence() throws IOException {
        String[] record;
        String[] null_return = {"미자믹 문장", "The End"};
        add_cnt();
        record = reader.readNext();
        if (record != null) {
            next_sentence = record;
        }
        else
            next_sentence = null_return;
    }
    public String get_kor(){
        return next_sentence[DEFINE.KOR];
    }
    public String get_eng(){
        return next_sentence[DEFINE.ENG];
    }
    public String get_cnt(){
        return String.valueOf(sentence_cnt);
    }
    private void add_cnt(){
        sentence_cnt = sentence_cnt + 1;
    }
//    public String[] next_sentence() throws IOException {
//        String[] record = null;
//        String[] null_return = {"The end", "마지막 문장입니다.", " "};
//
//        record = reader.readNext();
//        if (record != null) {
//            Log.d("load_csv", record[0] + ",");
//            return record;
//        }
//        else
//            return null_return;
//    }
}
