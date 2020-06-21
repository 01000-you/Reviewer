package com.example.eng_reviewer.List;

import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ListViewItem {

    private String titleStr;
    private String filepath;
    private Date creation_time;
    private int order;


    public ListViewItem(){

    }


    public void setTitle(String title) {
        titleStr = title;
    }
//    public void setContent(String content){
//        contentStr = content;
//    }
    public void setFilepath(String path) { filepath = path;}
    public void setCreationTime(Date _creation_time){ creation_time = _creation_time;}
    public void setOrder(int idx){ order = idx;}

//    public String getContent(){
//        return this.contentStr;
//    }
    public String getTitle(){ return this.titleStr;}
    public String getFilepath(){ return this.filepath;}
    public int getOrder(){return this.order;}
}

