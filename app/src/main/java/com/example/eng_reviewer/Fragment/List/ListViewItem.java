package com.example.eng_reviewer.Fragment.List;

public class ListViewItem {

    private String contentStr;
    private String titleStr;

    public void setTitle(String title) {
        titleStr = title;
    }
    public void setContent(String content){
        contentStr = content;
    }

    public String getContent(){
        return this.contentStr;
    }
    public String getTitle(){
        return this.titleStr;
    }
}
