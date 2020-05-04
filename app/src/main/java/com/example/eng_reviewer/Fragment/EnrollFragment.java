package com.example.eng_reviewer.Fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.eng_reviewer.R;
import com.example.eng_reviewer.sentences.Snt_manager;

import java.io.IOException;


public class EnrollFragment extends Fragment {

    Snt_manager sentence;
    Button Button_save, Button_cancel;
    EditText EditText_sentences;
    public EnrollFragment(Snt_manager _sentence){
        sentence = _sentence;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_enroll, container, false);

        Button_cancel = rootView.findViewById(R.id.Button_cancel);
        Button_save = rootView.findViewById(R.id.Button_save);

        EditText_sentences = rootView.findViewById(R.id.EditText_sentences);

        Button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Text = EditText_sentences.getText().toString();
                if (Text.equals("")) {
                    String[] Text_arr = Text.split("\n");

                    sentence.add_text(Text_arr);
                    EditText_sentences.setText("");
                    try {
                        sentence.save_csv();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        Button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText_sentences.setText("");
            }
        });
        return rootView;
    }
}