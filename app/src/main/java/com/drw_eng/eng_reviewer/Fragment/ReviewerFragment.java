package com.drw_eng.eng_reviewer.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.drw_eng.eng_reviewer.R;
import com.drw_eng.eng_reviewer.sentences.Snt_manager;
import com.drw_eng.eng_reviewer.util.PreferenceManager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.io.IOException;
import java.util.Locale;
import static android.speech.tts.TextToSpeech.ERROR;
import android.view.WindowManager;

public class ReviewerFragment extends Fragment {

    Handler handler;
    public PlayThread Play_thread;
    public TextToSpeech tts;
    public Boolean play_thread_state;

    ToggleButton ToggleButton_TTS, PlayButton_TTS;
    Button Button_fail, Button_success, Button_back, Button_next;
    public TextView TextView_eng_snt, TextView_kor_snt;
    Snt_manager sentence;

    public int success_button_state = 0; // 0 : 정답 비공개, 1: 정답 공개
    int state_TTS = 0;

    InterstitialAd myAd;
    private AdView mBannerAd;

    public ReviewerFragment(Snt_manager _sentece, Context context, InterstitialAd mInterstitialId) {
        myAd = mInterstitialId;

        sentence = _sentece;
        tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != ERROR) {
                    // 언어를 선택한다.
                    tts.setLanguage(Locale.ENGLISH);
                }
            }
        });
        play_thread_state = false;
    }

    private class PlayThread extends Thread {
        private boolean stopped = false;

        public PlayThread(){


        }
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public void run(){
            try {
                while(true) {
                    if(Integer.parseInt(sentence.get_cnt()) == sentence.getSentenceList().size()) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                PlayButton_TTS.performClick();
                            }
                        });
                    }
                    tts.speak(sentence.get_kor(), TextToSpeech.QUEUE_ADD, null, null);
                    tts.setLanguage(Locale.ENGLISH);
                    sleep(7000);

//                    tts.speak(sentence.get_eng(), TextToSpeech.QUEUE_ADD, null, null);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Button_success.performClick();
                        }
                    });
                    sleep(250);
                    tts.setLanguage(Locale.KOREAN);
                    sleep(4500);

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                          Button_next.performClick();
                        }
                    });
                    sleep(250);
                }
            } catch (InterruptedException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    }
                });
                e.printStackTrace();
            }
        }

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_reviewer, container, false);

        getActivity().setTitle("Review");

        mBannerAd = rootView.findViewById(R.id.adView_review);
        AdRequest adRequest = new AdRequest.Builder().build();
        mBannerAd.loadAd(adRequest);

        ToggleButton_TTS = rootView.findViewById(R.id.ToggleButton_TTS);
        PlayButton_TTS = rootView.findViewById(R.id.PlayButton_TTS);
        PlayButton_TTS.setVisibility(rootView.INVISIBLE);

        Button_fail = rootView.findViewById(R.id.Button_fail);
        Button_success = rootView.findViewById(R.id.Button_success);
        Button_back = rootView.findViewById(R.id.Button_back);
        Button_next = rootView.findViewById(R.id.Button_next);

        TextView_eng_snt = rootView.findViewById(R.id.TextView_eng_snt);
        TextView_kor_snt = rootView.findViewById(R.id.TextView_kor_snt);


        TextView_kor_snt.setText(sentence.get_cnt() + ". " + sentence.get_kor());

        handler = new Handler();

        PlayButton_TTS.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                if(PlayButton_TTS.isChecked()){
                    getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    PlayButton_TTS.setBackgroundDrawable(getResources().getDrawable(R.drawable.auto_pause_button));
                    Play_thread = new PlayThread();
                    tts.setLanguage(Locale.KOREAN);
                    play_thread_state = true;
                    success_button_state = 0;
                    Play_thread.start();
                }
                else {
                    PlayButton_TTS.setBackgroundDrawable(getResources().getDrawable(R.drawable.auto_play_button));
                    play_thread_state = false;
                    Play_thread.interrupt();
                    tts.setLanguage(Locale.ENGLISH);
                }
            }
        });
        ToggleButton_TTS.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                if(ToggleButton_TTS.isChecked()){
                    if (myAd.isLoaded()){
                        myAd.show();
                    }
                    state_TTS = 1;
                    ToggleButton_TTS.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_action_volume_up, null));
                    PlayButton_TTS.setVisibility(view.VISIBLE);
                }
                else{
                    if(play_thread_state) {
                        play_thread_state = false;
                        Play_thread.interrupt();
                        PlayButton_TTS.setChecked(false);
                        PlayButton_TTS.setBackgroundDrawable(getResources().getDrawable(R.drawable.auto_play_button));
                    }
                    state_TTS = 0;
                    ToggleButton_TTS.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_action_volume_off, null));
                    PlayButton_TTS.setVisibility(view.INVISIBLE);
                }
            }
        });
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
                    Button_AllsetClickable(false);
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
                    Button_AllsetClickable(true);
                }
            }
        });
        Button_fail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (success_button_state == 1) { // 정답 공개 상태
                    if (!sentence.get_eng().equals("The End")) {
                        sentence.sub_score();
                        if(!play_thread_state) {
                            sentence.add_cnt();
                            sentence.next_sentence();
                            TextView_kor_snt.setText(sentence.get_cnt() + ". " + sentence.get_kor());
                            TextView_eng_snt.setText("");
                            success_button_state = (success_button_state + 1) % 2;
                        }
                    } else {
                        Button_AllsetClickable(false);
                    }
                }
            }
        });
        Button_success.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                if (success_button_state == 0) { // 정답 미공개 상태
                    TextView_eng_snt.setText(sentence.get_eng());
                    if (play_thread_state){
                        sentence.add_score();
                    }
                    if(state_TTS == 1){
                        tts.speak(sentence.get_eng(),TextToSpeech.QUEUE_ADD, null, null);
                    }
                } else { // 정답 공개 상태
                    sentence.add_cnt();
                    sentence.next_sentence();
                    TextView_kor_snt.setText(sentence.get_cnt() + ". " + sentence.get_kor());
                    TextView_eng_snt.setText("");
                    if (sentence.get_eng().equals("The End")) {
                        Button_AllsetClickable(false);
                    }
                }
                success_button_state = (success_button_state + 1) % 2;
            }
        });
        return rootView;
    }
    public void Button_AllsetClickable(boolean b){
        Button_success.setClickable(b);
        Button_fail.setClickable(b);
        Button_next.setClickable(b);
        TextView_eng_snt.setLongClickable(b);
        TextView_kor_snt.setLongClickable(b);
    }
    void kor_dialog()
    {
        final EditText kor_edittext = new EditText(getContext());
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        kor_edittext.setText(sentence.get_kor());
        builder.setTitle("Enter the correct sentence.");
//        builder.setMessage("AlertDialog Content");
        builder.setView(kor_edittext);
        builder.setNeutralButton("DELETE",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        sentence.delete();
                        TextView_kor_snt.setText((Integer.parseInt(sentence.get_cnt())) + ". " + sentence.get_kor());
                        TextView_eng_snt.setText("");
//                        sentence.add_cnt();
                        Toast.makeText(getActivity(), "문장이 삭제되었습니다.", Toast.LENGTH_LONG).show();
                        if (sentence.get_eng().equals("The End")) {
                            Button_AllsetClickable(false);
                        }
                    }
                });
        builder.setPositiveButton("EDIT",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        sentence.set_kor_sentence(kor_edittext.getText().toString());
                        TextView_kor_snt.setText(sentence.get_cnt() + ". " + sentence.get_kor());
                        Toast.makeText(getActivity(), "한글문장이 수정되었습니다.", Toast.LENGTH_LONG).show();
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
                        Toast.makeText(getActivity(), "영어문장이 수정되었습니다.", Toast.LENGTH_LONG).show();
                    }
                });
        builder.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.show();
    }

    public void setSentence(Snt_manager sentence) throws IOException {
        this.sentence.save_csv();
        this.sentence = sentence;
        TextView_kor_snt.setText(sentence.get_cnt() + ". " + sentence.get_kor());
        TextView_eng_snt.setText("");
    }
    public void ViewCurrSentence(){
        sentence.next_sentence();
        TextView_kor_snt.setText(sentence.get_cnt() + ". " + sentence.get_kor());
        TextView_eng_snt.setText("");
    }

    @Override
    public void onStop() {
        super.onStop();
        if(play_thread_state) {
            success_button_state=0;
            play_thread_state = false;
            Play_thread.interrupt();
        }
    }

    public void onResume() {
        super.onResume();
        PlayButton_TTS.setChecked(false);
        PlayButton_TTS.setBackgroundDrawable(getResources().getDrawable(R.drawable.auto_play_button));
    }

    public void play_stop(){
        Play_thread.interrupt();
        PlayButton_TTS.setBackgroundDrawable(getResources().getDrawable(R.drawable.auto_play_button));
    }
}