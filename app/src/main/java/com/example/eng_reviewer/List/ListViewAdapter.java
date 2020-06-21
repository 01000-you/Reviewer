//package com.example.eng_reviewer.List;
//
//import android.content.Context;
//import android.os.Build;
//import android.os.Environment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.Button;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import androidx.annotation.RequiresApi;
//
//import com.example.eng_reviewer.DEFINE;
//import com.example.eng_reviewer.R;
//import com.opencsv.CSVReader;
//
//import java.io.File;
//
//import java.io.FileFilter;
//import java.io.FileReader;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.attribute.BasicFileAttributes;
//import java.nio.file.attribute.FileTime;
//import java.util.ArrayList;
//import java.util.Comparator;
//import java.util.Date;
//import java.util.Locale;
//
//public class ListViewAdapter extends BaseAdapter {
//    private TextView titleTextView;
//    private TextView contentTextView;
//    private Button Button_load;
//    private int curr_item_idx = 0;
//
//    public ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>();
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public ListViewAdapter() throws IOException {
//
//        File f = new File(Environment.getExternalStorageDirectory() + DEFINE.EXTERNAL_PATH);
//        File[] files = f.listFiles(new FileFilter(){
//            @Override
//            public boolean accept(File pathname) {
//                return pathname.getName().toLowerCase(Locale.US).endsWith(".csv");
//            }
//        });
//        for(int i = 0; i < files.length; i++){
//            // 파일 읽어서 첫줄 가져오기
//            CSVReader reader = new CSVReader((new FileReader((files[i]))));
//            // 파일 제목 불러오기
//            String [] Header = reader.readNext();
//            BasicFileAttributes attrs;
//            attrs = Files.readAttributes(files[i].toPath(), BasicFileAttributes.class);
//            FileTime attr_creatiton_time = attrs.creationTime();
//            Date creationtime = new Date(attr_creatiton_time.toMillis());
//            reader.close();
//            this.addItem(Header[0].split("\t")[0].replaceAll("\"",""), files[i].toString(), creationtime);
//        }
//        // sort
//        listViewItemList.sort( new Comparator<ListViewItem>() {
//            @Override
//            public int compare(ListViewItem t0, ListViewItem t1) {
//                if (t0.getCreationTime().compareTo(t1.getCreationTime()) > 0) {
//                    return 1;
//                } else if (t0.getCreationTime().compareTo(t1.getCreationTime()) == 0) {
//                    return 0;
//                } else {
//                    return -1;
//                }
//            }
//        });
//        for (int i = 0;  i < listViewItemList.size(); i++) {
//            listViewItemList.get(i).setOrder(i);
//        }
//        this.notifyDataSetChanged();
//    }
//
//    @Override
//    public int getCount(){
//        return listViewItemList.size();
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent){
//        final Context context = parent.getContext();
//
//        if (convertView == null){
//            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            convertView = inflater.inflate(R.layout.listview_item, parent, false);
//        }
//
//        titleTextView = (TextView) convertView.findViewById(R.id.title);
//        contentTextView = (TextView) convertView.findViewById(R.id.content);
//        Button_load= (Button) convertView.findViewById(R.id.Button_load);
//
//
//        final ListViewItem listViewItem = listViewItemList.get(position);
//
//        Button_load.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int current_idx = listViewItem.getOrder();
//                setCurrentIdx(current_idx);
//
//            }
//        });
//        titleTextView.setText(listViewItem.getTitle());
//        contentTextView.setText(listViewItem.getContent());
//
//        return convertView;
//    }
//
//    @Override
//    public long getItemId(int position){
//        return position;
//    }
//
//    @Override
//    public Object getItem(int position){
//        return listViewItemList.get(position);
//    }
//
//    public void addItem(String title, String path, Date creation_time) {
//        ListViewItem item = new ListViewItem();
//
//        item.setTitle(title);
//        item.setFilepath(path);
//        item.setCreationTime(creation_time);
//
//        listViewItemList.add(item);
//    }
//
//    public ListViewItem get_curr_item(){
//        return listViewItemList.get(curr_item_idx);
//    }
//    public void setCurrentIdx(int idx){
//        curr_item_idx =  idx;
//    }
//}
