package com.example.eng_reviewer.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.eng_reviewer.DEFINE;
import com.example.eng_reviewer.List.ListViewItem;
import com.example.eng_reviewer.R;
import com.example.eng_reviewer.sentences.Snt_manager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ListFragment extends Fragment {

    private ListView listview;
    private ListViewAdapter adapter;
    private Snt_manager curr_snt_mng;
    private ReviewerFragment ReviewerFrag;
    private EnrollFragment EnrollFrag;
    private FloatingActionButton FloatingButton_Add;

    public Snt_manager getSentenceMng(){
        return curr_snt_mng;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public ListFragment () throws IOException {
        String mydir_path = Environment.getExternalStorageDirectory()+DEFINE.EXTERNAL_PATH;
        final File mydir = new File(mydir_path);
        if(!mydir.exists()){
            mydir.mkdirs();
            // 파일 만들어주고.
//          new Snt_manager(Environment.getExternalStorageDirectory()+ DEFINE.EXTERNAL_PATH, "snt_data0", 0, 0);
            new Snt_manager().init_csv();
        }
        adapter = new ListFragment.ListViewAdapter();
        curr_snt_mng = new Snt_manager(adapter.get_curr_item().getFilepath());
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container, @NonNull Bundle savedInstanceState){
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_themelist, container, false);

        listview = rootView.findViewById(R.id.listview);
        FloatingButton_Add = rootView.findViewById(R.id.FloatingButton_Add);
        listview.setAdapter(adapter);

        FloatingButton_Add.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                // Make new file
                    //get size of list
                String base_fname="snt_data";
                String file_path = Environment.getExternalStorageDirectory()+DEFINE.EXTERNAL_PATH;
                int file_num = adapter.getFilenum(file_path, base_fname);
                try {
                    String file_name = base_fname+file_num;
                    int get_order = adapter.getNewOrder();
                    Snt_manager new_snt_mng = new Snt_manager(file_path, file_name, file_num, get_order);
                    ListViewItem new_item = new ListViewItem();
                    adapter.addItem(new_item);
                    new_item.setTitle(new_snt_mng.getTitle());
                    new_item.setFilepath(file_path + '/' + file_name + ".csv");
                    new_item.setOrder(get_order);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // Add Item
                adapter.notifyDataSetChanged();
            }
        });
        return rootView;
    }
    public Snt_manager getCurrentSentence(){
        return curr_snt_mng;
    }
    public void setReivewer(ReviewerFragment fragment) {
        ReviewerFrag = fragment;
    }

    public void setEnroller(EnrollFragment fragment) {
        EnrollFrag = fragment;
    }

    public class ListViewAdapter extends BaseAdapter {

        private int curr_item_idx = 0;

        public ArrayList<ListViewItem> listViewItemList = new ArrayList();

        @RequiresApi(api = Build.VERSION_CODES.O)
        public ListViewAdapter() throws IOException {

            File f = new File(Environment.getExternalStorageDirectory() + DEFINE.EXTERNAL_PATH);
            File[] files = f.listFiles(new FileFilter(){
                @Override
                public boolean accept(File pathname) {
                    return pathname.getName().toLowerCase(Locale.US).endsWith(".csv");
                }
            });
            for(int i = 0; i < files.length; i++){
                // 파일 읽어서 첫줄 가져오기
                CSVReader reader = new CSVReader((new FileReader((files[i]))));
                // 파일 제목 불러오기
                String [] Header = reader.readNext();
                if (Header == null){
                    continue;
                }
                BasicFileAttributes attrs;
                attrs = Files.readAttributes(files[i].toPath(), BasicFileAttributes.class);
                FileTime attr_creatiton_time = attrs.creationTime();
                Date creationtime = new Date(attr_creatiton_time.toMillis());
                reader.close();
                this.addItem(Header[0].split("\t")[0].replaceAll("\"",""), files[i].toString(), creationtime, Integer.parseInt(Header[0].split("\t")[1].replaceAll("\"","")));
            }
            // sort
            listViewItemList.sort( new Comparator<ListViewItem>() {
                @Override
                public int compare(ListViewItem t0, ListViewItem t1) {
                    if (t0.getOrder() > t1.getOrder()) {
                        return 1;
                    } else if (t0.getOrder() == t1.getOrder()) {
                        return 0;
                    } else {
                        return -1;
                    }
                }
            });
            this.notifyDataSetChanged();
        }

        @Override
        public int getCount(){
            return listViewItemList.size();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent){

            ListViewItem listViewItem = listViewItemList.get(position);

            if (convertView == null){
                final Context context = parent.getContext();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.listview_item, parent, false);
            }
            TextView titleTextView = convertView.findViewById(R.id.title);
            final ToggleButton ToggleButton_Load = convertView.findViewById(R.id.Button_load);
            Button Button_Edit= convertView.findViewById(R.id.Button_Edit);

            if (position == curr_item_idx){
                ToggleButton_Load.setChecked(true);
                ToggleButton_Load.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_darkblue));
                ToggleButton_Load.setText("Loaded");
            }
            else{
                ToggleButton_Load.setChecked(false);
                ToggleButton_Load.setEnabled(true);
                ToggleButton_Load.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_blue));
                ToggleButton_Load.setText("Load");
            }
            titleTextView.setText(listViewItem.getTitle());

            Button_Edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ListViewItem listViewItem = listViewItemList.get(position);
                    final EditText title_edittext = new EditText(getContext());
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    //파일 읽고
                    CSVReader reader;
                    List<String[]> stn_list;
                    try {
                        reader = new CSVReader(new FileReader(listViewItem.getFilepath()), '\t');
                        stn_list = reader.readAll();
                        title_edittext.setText(stn_list.get(0)[0]);
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    builder.setTitle("Enter the correct sentence.");
                    builder.setView(title_edittext);

                    builder.setNeutralButton("DELETE",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    ListViewItem listViewItem = listViewItemList.get(position);
                                    if(position == curr_item_idx)
                                    {
                                        Toast.makeText(getActivity(), "현재 로드 중인 리스트입니다.",Toast.LENGTH_LONG).show();
                                    }
                                    else if(listViewItemList.size() != 1){

                                        // 파일삭제
                                        File f = new File(listViewItem.getFilepath());
                                        f.delete();
                                        try {
                                            adapter.updateListOrder(listViewItem.getOrder(), position);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        listViewItemList.remove(listViewItem);

                                        adapter.notifyDataSetChanged();
                                        Toast.makeText(getActivity(), "리스트가 삭제되었습니다.", Toast.LENGTH_LONG).show();
                                    }
                                    else {
                                        Toast.makeText(getActivity(), "최소 1개 이상 리스트가 필요합니다.", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                    builder.setPositiveButton("EDIT",
                            new DialogInterface.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.N)
                                public void onClick(DialogInterface dialog, int which) {
                                    Snt_manager snt_mng;
                                    ListViewItem listViewItem = listViewItemList.get(position);
                                    if(position == curr_item_idx)
                                    {
                                        snt_mng = getSentenceMng();
                                    }
                                    else {
                                        snt_mng = new Snt_manager(listViewItem.getFilepath());
                                    }
                                    //첫번째 줄 값 받아서 제목 수정하고
                                    List<String[]> stn_list = snt_mng.getSentenceList();
                                    stn_list.get(0)[0] = title_edittext.getText().toString();
                                    //다시 저장
                                    snt_mng.setSentenceList(stn_list);
                                    try {
                                        snt_mng.save_csv();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    //파일이름 화면 업데이트
                                    listViewItem.setTitle(title_edittext.getText().toString());
                                    adapter.notifyDataSetChanged();
                                    Toast.makeText(getActivity(), "리스트 이름이 수정되었습니다.", Toast.LENGTH_LONG).show();
                                }
                            });
                    builder.setNegativeButton("CANCEL",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                    builder.show();
                }
            });
            ToggleButton_Load.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View view) {
                    ListViewItem listViewItem = listViewItemList.get(position);
                    if (ToggleButton_Load.isChecked()) {
                        setCurrentIdx(position);
                        curr_snt_mng = new Snt_manager(get_curr_item().getFilepath());
                        try {
                            ReviewerFrag.setSentence(curr_snt_mng);
                            EnrollFrag.setSentence(curr_snt_mng);
                            Toast.makeText(getActivity(), "리스트가 로드되었습니다.", Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                    }
                    adapter.notifyDataSetChanged();
                }
            });
            return convertView;
        }

        private void updateListOrder(int input_order, int curr_position) throws IOException {
            // 다음 아이템이 있는지 확인
//            if(listViewItemList.get(curr_position + 1) != null){
            if((curr_position + 1) < listViewItemList.size()){
                ListViewItem listItem = listViewItemList.get(curr_position + 1);
                // 그 아이템의 CSV 파일 읽음
                CSVReader reader = new CSVReader(new FileReader(listItem.getFilepath()),'\t');
                List<String[]> myEntries = reader.readAll();
                reader.close();
                // 파일의 order를 input_order로 변경
                myEntries.get(0)[1] = String.valueOf(input_order);
                CSVWriter writer = new CSVWriter(new FileWriter(listItem.getFilepath()), '\t');
                for (String[] i : myEntries) {
                    writer.writeNext(i);
                }
                writer.close();
                listItem.setOrder(input_order);
            }
        }

        @Override
        public long getItemId(int position){
            return position;
        }

        @Override
        public Object getItem(int position){
            return listViewItemList.get(position);
        }

        public void addItem(String title, String path, Date creation_time, int order) {
            ListViewItem item = new ListViewItem();

            item.setTitle(title);
            item.setFilepath(path);
            item.setCreationTime(creation_time);
            item.setOrder(order);
            listViewItemList.add(item);
        }
        public void addItem(ListViewItem new_item) {
            listViewItemList.add(new_item);
        }
        public ListViewItem get_curr_item(){
            return listViewItemList.get(curr_item_idx);
        }
        public void setCurrentIdx(int idx){
            curr_item_idx =  idx;
        }

        public int getFilenum(String fpath, String fname) {
            int cnt = -1;
            String isfname;
            File f;
            do{
                cnt++;
                isfname = fpath + '/' + fname + String.valueOf(cnt) + ".csv";
                f = new File(isfname);
            }while(f.exists());

            return cnt;
        }

        public int getNewOrder() {
            return listViewItemList.get((listViewItemList.size() - 1)).getOrder() + 1;
        }

    }
}