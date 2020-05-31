package com.example.eng_reviewer.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.eng_reviewer.DEFINE;
import com.example.eng_reviewer.Fragment.List.ListViewAdapter;
import com.example.eng_reviewer.R;
import com.example.eng_reviewer.sentences.Snt_manager;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Locale;


public class ListFragment extends Fragment {

    private ListView listview;
    private ListViewAdapter adapter;

    public ListFragment (ListViewAdapter input_adapter){
        adapter = input_adapter;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container, @NonNull Bundle savedInstanceState){
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_themelist, container, false);

        listview = (ListView) rootView.findViewById(R.id.listview);
        listview.setAdapter((ListAdapter) adapter);

        return rootView;
    }
}