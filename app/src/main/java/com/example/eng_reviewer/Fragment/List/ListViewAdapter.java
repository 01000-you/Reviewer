package com.example.eng_reviewer.Fragment.List;

import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.eng_reviewer.DEFINE;
import com.example.eng_reviewer.R;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Locale;

public class ListViewAdapter extends BaseAdapter {
    private TextView titleTextView;
    private TextView contentTextView;
    private int curr_item_idx = 0;

    public ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>();

    public ListViewAdapter(){

        File f = new File(Environment.getExternalStorageDirectory() + DEFINE.EXTERNAL_PATH);
        File[] files = f.listFiles(new FileFilter(){
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().toLowerCase(Locale.US).endsWith(".csv");
            }
        });
        for(int i = 0; i < files.length; i++){
            this.addItem("제목" + String.valueOf(i), files[i].toString());
        }
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount(){
        return listViewItemList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
        }

        titleTextView = (TextView) convertView.findViewById(R.id.title);
        contentTextView = (TextView) convertView.findViewById(R.id.content);

        ListViewItem listViewItem = listViewItemList.get(position);

        titleTextView.setText(listViewItem.getTitle());
        contentTextView.setText(listViewItem.getContent());

        return convertView;
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public Object getItem(int position){
        return listViewItemList.get(position);
    }

    public void addItem(String title, String path) {
        ListViewItem item = new ListViewItem();

        item.setTitle(title);
        item.setFilepath(path);

        listViewItemList.add(item);
    }

    public ListViewItem get_curr_item(){
        return listViewItemList.get(curr_item_idx);
    }
}
