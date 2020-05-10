package com.example.eng_reviewer;

import android.os.Environment;

public class DEFINE {
    public static final int KOR = 0;
    public static final int ENG = 1;
    public static final int SCORE = 2;
    public static final int UP_POINT = 1;
    public static final int DOWN_POINT = 2;
    public static final String LOAD_PATH = Environment.getExternalStorageDirectory()+"/main_data.csv";
    public static final String SAVE_PATH = Environment.getExternalStorageDirectory()+"/main_data.csv";
}
