package com.example.eng_reviewer.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.eng_reviewer.DEFINE;
import com.example.eng_reviewer.R;
import com.example.eng_reviewer.sentences.Snt_manager;

import java.io.IOException;

public class ReviewerFragment extends Fragment {
    Button Button_fail, Button_success, Button_back, Button_next;
    TextView TextView_eng_snt, TextView_kor_snt;
    Snt_manager sentence;
//    CardView Cardview_main;


    String[] edit_sentence = {"한글", "영어"};
    boolean kor_edit_state;
    boolean eng_edit_state;

    int success_button_state = 0;

    public ReviewerFragment(Snt_manager _sentece) {
        sentence = _sentece;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_reviewer, container, false);

        getActivity().setTitle("Review");

        //xml 레이아웃이 인플레이트 되고 자바소스 코드와 연결이된다.
        Button_fail = rootView.findViewById(R.id.Button_fail);
        Button_success = rootView.findViewById(R.id.Button_success);
        Button_back = rootView.findViewById(R.id.Button_back);
        Button_next = rootView.findViewById(R.id.Button_next);

        TextView_eng_snt = rootView.findViewById(R.id.TextView_eng_snt);
        TextView_kor_snt = rootView.findViewById(R.id.TextView_kor_snt);

        try {
            sentence.load_csv();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Load_csv", "Fail to read CSV");
        }
        TextView_kor_snt.setText(sentence.get_cnt() + ". " + sentence.get_kor());

        TextView_kor_snt.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View view) {
                kor_dialog();
                return false;
            }
        });
        TextView_eng_snt.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View view) {
                eng_dialog();
                return false;
            }
        });
        Button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sentence.add_cnt();
                sentence.next_sentence();
                TextView_kor_snt.setText(sentence.get_cnt() + ". " + sentence.get_kor());
                TextView_eng_snt.setText("");
                success_button_state = 0;
                if (sentence.get_eng().equals("The End")) {
                    Button_success.setClickable(false);
                    Button_fail.setClickable(false);
                }
            }
        });
        Button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sentence.before_sentence();
                TextView_kor_snt.setText(sentence.get_cnt() + ". " + sentence.get_kor());
                TextView_eng_snt.setText("");
                success_button_state = 0;
                if (!sentence.get_eng().equals("The End")) {
                    Button_success.setClickable(true);
                    Button_fail.setClickable(true);
                }
            }
        });
        Button_fail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (success_button_state == 1) { // 정답 공개 상태
                    if (!sentence.get_eng().equals("The End")) {
                        sentence.sub_score();
                        sentence.next_sentence();
                        TextView_kor_snt.setText(sentence.get_cnt() + ". " + sentence.get_kor());
                        TextView_eng_snt.setText("");
                        success_button_state = (success_button_state + 1) % 2;
                    } else {
                        Button_success.setClickable(false);
                        Button_fail.setClickable(false);
                    }
                }
            }
        });
        Button_success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (success_button_state == 0) { // 정답 미공개 상태
                    TextView_eng_snt.setText(sentence.get_eng());
                } else { // 정답 공개 상태
                    sentence.add_score();
                    sentence.next_sentence();
                    TextView_kor_snt.setText(sentence.get_cnt() + ". " + sentence.get_kor());
                    TextView_eng_snt.setText("");
                    if (sentence.get_eng().equals("The End")) {
                        Button_success.setClickable(false);
                        Button_fail.setClickable(false);
                    }
                }
                success_button_state = (success_button_state + 1) % 2;
            }
        });
        return rootView;
    }
    void kor_dialog()
    {
        final EditText kor_edittext = new EditText(getContext());
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        kor_edittext.setHint("한글");
        builder.setTitle("Enter the correct sentence.");
//        builder.setMessage("AlertDialog Content");
        builder.setView(kor_edittext);
        builder.setPositiveButton("EDIT",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        sentence.set_kor_sentence(kor_edittext.getText().toString());
                        TextView_kor_snt.setText(sentence.get_cnt() + ". " + sentence.get_kor());
                    }
                });
        builder.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.show();
    }
    void eng_dialog()
    {
        final EditText eng_edittext = new EditText(getContext());
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        eng_edittext.setHint("ENG");
        builder.setTitle("Enter the correct sentence.");
//        builder.setMessage("AlertDialog Content");
        builder.setView(eng_edittext);
        builder.setPositiveButton("EDIT",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        sentence.set_eng_sentence(eng_edittext.getText().toString());
                        TextView_eng_snt.setText(sentence.get_eng());
                    }
                });
        builder.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.show();
    }
}