package com.example.eng_reviewer.sentences;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.eng_reviewer.DEFINE;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Snt_manager {

    private CSVReader reader;
    private String[] next_sentence;
    private int sentence_cnt = 0;
    private List<String[]> sentence_list;
    private int num_of_sent;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void load_csv() throws IOException {
        reader = new CSVReader(new FileReader(DEFINE.LOAD_PATH), '\t');
        sentence_list = reader.readAll();
        num_of_sent = sentence_list.size();
        Collections.shuffle(sentence_list);
        sentence_list.sort(new Comparator<String[]>() {
            @Override
            public int compare(String[] t0, String[] t1) {
                if (Integer.parseInt(t0[DEFINE.SCORE]) > Integer.parseInt(t1[DEFINE.SCORE])){
                    return 1;
                }
                else if(Integer.parseInt(t0[DEFINE.SCORE]) == Integer.parseInt(t1[DEFINE.SCORE]))
                    return 0;
                else
                    return -1;
            }
        });
        next_sentence();
    }

    public void before_sentence(){
        if(sentence_cnt != 1){
            sentence_cnt = sentence_cnt - 1;
            next_sentence = sentence_list.get(sentence_cnt - 1);
        }
    }
    public void next_sentence() {
        String[] null_return = {"마지막 문장", "The End"};
        if (sentence_cnt == num_of_sent){
            next_sentence = null_return;
        }
        else
            next_sentence = sentence_list.get(sentence_cnt);
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
    public void sub_score() {
        next_sentence[DEFINE.SCORE] = String.valueOf(Integer.parseInt(next_sentence[DEFINE.SCORE]) - DEFINE.DOWN_POINT);
    }
    public void add_score() {
        next_sentence[DEFINE.SCORE] = String.valueOf(Integer.parseInt(next_sentence[DEFINE.SCORE]) + DEFINE.UP_POINT);
    }
    public void add_cnt(){
        sentence_cnt = sentence_cnt + 1;
    }
    public void save_csv() throws IOException {
        CSVWriter writer = new CSVWriter(new FileWriter(DEFINE.SAVE_PATH), '\t');
        for (String[] i : sentence_list) {
            writer.writeNext(i);
        }
        writer.close();
    }

    public int add_text(String[] text_arr) {
        int length = text_arr.length;
        if(length % 2 == 0) {
            for (int i = 0; i < length; i += 2) {
                sentence_list.add(new String[]{text_arr[i], text_arr[i + 1], "0"});
            }
            return length;
        }
        else{
            return 0;
        }
    }
    public void set_eng_sentence(String s) {
        next_sentence[DEFINE.ENG] = s;
    }
    public void set_kor_sentence(String s) {
        next_sentence[DEFINE.KOR] = s;
    }

    public void delete() {
        sentence_list.remove(--sentence_cnt);
        num_of_sent = sentence_list.size();
        next_sentence();
    }
}
