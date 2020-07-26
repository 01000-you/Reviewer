package com.drw_eng.eng_reviewer;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import com.drw_eng.eng_reviewer.Fragment.EnrollFragment;
import com.drw_eng.eng_reviewer.Fragment.ListFragment;
import com.drw_eng.eng_reviewer.Fragment.ReviewerFragment;
import com.drw_eng.eng_reviewer.sentences.Snt_manager;

import com.drw_eng.eng_reviewer.util.PreferenceManager;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;


public class MainActivity extends AppCompatActivity {
    Snt_manager curr_sentence;

    ListFragment ListFrag = null;
    ReviewerFragment ReviewerFrag = null;
    EnrollFragment EnrollFrag = null;

    private InterstitialAd mInterstitialAd;
    private Context mContext;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public MainActivity() throws IOException {
//        list_adapter = new ListFragment.ListViewAdapter();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-1195074851560755/9871978481");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdClosed(){
                //Load the next interstitial
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });

        setTitle("Review");

        ViewPager pager = findViewById(R.id.pager);
        //캐싱을 해놓을 프래그먼트 개수
        pager.setOffscreenPageLimit(2);

        //getSupportFragmentManager로 프래그먼트 참조가능
        MoviePagerAdapter Fragment_adapter = new MoviePagerAdapter(getSupportFragmentManager());


        try {
            ListFrag = new ListFragment(mContext);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Fragment_adapter.addItem(ListFrag);
        curr_sentence = ListFrag.getCurrentSentence();

        ReviewerFrag = new ReviewerFragment(curr_sentence, getApplicationContext(), mInterstitialAd);
        Fragment_adapter.addItem(ReviewerFrag);
        ListFrag.setReivewer(ReviewerFrag);

        EnrollFrag = new EnrollFragment(curr_sentence);
        Fragment_adapter.addItem(EnrollFrag);
        ListFrag.setEnroller(EnrollFrag);

        pager.setAdapter(Fragment_adapter);
        pager.setCurrentItem(1);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0){
                    setTitle("Playlist");
                    if(ReviewerFrag.play_thread_state){
                        ReviewerFrag.success_button_state=0;
                        ReviewerFrag.play_thread_state = false;
                        ReviewerFrag.play_stop();
                    }
                }
                if(position == 1){
                    setTitle("Review");
                    // ReviewFrag 버튼 상태 초기화
                    ReviewerFrag.ViewCurrSentence();
                    ReviewerFrag.success_button_state = 0;
                    ReviewerFrag.TextView_eng_snt.setText("");
                }
                if(position == 2){
                    setTitle("Register");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    //어댑터 안에서 각각의 아이템을 데이터로서 관리한다
    class MoviePagerAdapter extends FragmentStatePagerAdapter {
        ArrayList<Fragment> items = new ArrayList<Fragment>();
        public MoviePagerAdapter(FragmentManager fm) {
            super(fm);

        }

        public void addItem(Fragment item){
            items.add(item);
        }

        @Override
        public Fragment getItem(int position) {
            return items.get(position);
        }

        @Override
        public int getCount() {
            return items.size();
        }
    }
    protected void onStop() {
        super.onStop();
        Log.d("MainActivity", "On_Stop");
        try {
            ListFrag.getSentenceMng().save_csv();
            PreferenceManager.setInt(mContext,"last_item_idx", ListFrag.getAdapter().getCurrItemIdx());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void onResume() {
        super.onResume();
        // 다시 켤때마다 가끔 한글로 언어가 바뀌는 현상
        ReviewerFrag.tts.setLanguage(Locale.ENGLISH);
    }
    @Override
    // 1.뒤로가기 입력을 감지한다.
    public void onBackPressed() {
        // 2. 다이얼로그를 생성한다.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("알림");
        builder.setMessage("앱을 종료하시겠습니까?");
        builder.setNegativeButton("취소", null);
        builder.setPositiveButton("종료", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                try {
                    ListFrag.getSentenceMng().save_csv();
                    PreferenceManager.setInt(mContext,"last_item_idx", ListFrag.getAdapter().getCurrItemIdx());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
        builder.show();
    }
}

