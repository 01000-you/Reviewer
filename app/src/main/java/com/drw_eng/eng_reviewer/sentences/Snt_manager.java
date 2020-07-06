package com.drw_eng.eng_reviewer.sentences;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.drw_eng.eng_reviewer.DEFINE;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Snt_manager {

    private CSVReader reader;
    private CSVWriter tmp_writer;
    private String[] next_sentence;
    private int sentence_cnt = 1;
    private List<String[]> sentence_list;
    private int num_of_sent;
    private String curr_csv_path;

    public Snt_manager() {
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Snt_manager(String path){
        try {
            this.load_csv(path);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Load_csv", "Fail to read CSV");
        }
    }

    public Snt_manager(String file_path, String file_name, int file_num, int file_order) throws IOException {
        curr_csv_path=file_path + '/' + file_name + ".csv";
        add_csv(file_num, file_order);
    }

    public void init_csv() throws IOException {
        curr_csv_path = Environment.getExternalStorageDirectory()+ DEFINE.EXTERNAL_PATH + "/snt_data0";
        tmp_writer = new CSVWriter(new FileWriter(curr_csv_path + ".csv"), '\t');
        String[] header = ("new list0#0#-10000").split("#");
        String[] hellow_world = (DEFINE.START_SENT+"#Nice to meet you#0").split("#");
        tmp_writer.writeNext(header);
        tmp_writer.writeNext(hellow_world);
        tmp_writer.close();
    }
    public void add_csv(int num, int order) throws IOException {
        tmp_writer = new CSVWriter(new FileWriter(curr_csv_path), '\t');

        String[] header = ("new list"+String.valueOf(num)+"#"+String.valueOf(order)+"#"+"-10000").split("#");
        String[] hellow_world = "문장을 등록해주세요#Please enter your sentence#0".split("#");
        sentence_list = new ArrayList<>();
        sentence_list.add(header);
        sentence_list.add(hellow_world);
        tmp_writer.writeNext(header);
        tmp_writer.writeNext(hellow_world);
        tmp_writer.close();
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void load_csv(String path) throws IOException {
        curr_csv_path = path;
        reader = new CSVReader(new FileReader(curr_csv_path), '\t');
        sentence_list = reader.readAll();
        reader.close();
        num_of_sent = sentence_list.size();
//        Collections.shuffle(sentence_list);
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
            next_sentence = sentence_list.get(sentence_cnt - 1);
            sentence_cnt = sentence_cnt - 1;
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
        CSVWriter writer = new CSVWriter(new FileWriter(curr_csv_path), '\t');
        for (String[] i : sentence_list) {
            writer.writeNext(i);
        }
        writer.close();
    }

    public int add_text(String[] text_arr) {
        int length = text_arr.length;
        if(sentence_list.get(1)[0].equals(DEFINE.NEW_SENT) || sentence_list.get(1)[0].equals(DEFINE.START_SENT) ){
            sentence_list.remove(1);
        }
        if(length % 2 == 0) {
            for (int i = 0; i < length; i += 2) {
                sentence_list.add(new String[]{text_arr[i], text_arr[i + 1], "0"});
                num_of_sent = sentence_list.size();
            }
            return length;
        }
        else{
            return 0;
        }
    }

    public void sub_cnt() {
        sentence_cnt--;
    }

    public void set_eng_sentence(String s) {
        next_sentence[DEFINE.ENG] = s;
    }
    public void set_kor_sentence(String s) {
        next_sentence[DEFINE.KOR] = s;
    }

    public void delete() {
        sentence_list.remove(sentence_cnt--);
        num_of_sent = sentence_list.size();
        next_sentence();
    }
    public List<String[]> getSentenceList(){
        return sentence_list;
    }
    public void setSentenceList(List<String[]> _sentence_list){
        sentence_list = _sentence_list;
    }
    public String getTitle(){
        return sentence_list.get(0)[0];
    }
    public int getOrder(){
        return Integer.parseInt(sentence_list.get(0)[1]);
    }
}
