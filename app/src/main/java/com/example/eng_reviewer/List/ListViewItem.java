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

    public TextView titleTextView;
    public ToggleButton ToggleButton_Load;
    public Button Button_Edit;

    public ListViewItem(){

    }

    public void setTitleTextView(TextView textview){
        titleTextView = textview;
    }
    public void setTextButton_load(ToggleButton button){
        ToggleButton_Load = button;
    }
    public void setTextButton_edit(Button button){
        Button_Edit = button;
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
    public Date getCreationTime(){return this.creation_time;};
    public int getOrder(){return this.order;}
}

