package com.drw_eng.eng_reviewer;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import com.drw_eng.eng_reviewer.Fragment.EnrollFragment;
import com.drw_eng.eng_reviewer.Fragment.ListFragment;
import com.drw_eng.eng_reviewer.Fragment.ReviewerFragment;
import com.drw_eng.eng_reviewer.sentences.Snt_manager;

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public MainActivity() throws IOException {
//        list_adapter = new ListFragment.ListViewAdapter();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            ListFrag = new ListFragment();
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
                }
                if(position == 1){
                    setTitle("Review");
                    ReviewerFrag.ViewCurrSentence();
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

