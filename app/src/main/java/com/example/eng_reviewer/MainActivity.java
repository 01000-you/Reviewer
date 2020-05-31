package com.example.eng_reviewer;

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

import com.example.eng_reviewer.Fragment.EnrollFragment;
import com.example.eng_reviewer.Fragment.List.ListViewAdapter;
import com.example.eng_reviewer.Fragment.ListFragment;
import com.example.eng_reviewer.Fragment.ReviewerFragment;
import com.example.eng_reviewer.sentences.Snt_manager;

public class MainActivity extends AppCompatActivity {
    Snt_manager curr_sentence;
    private ListViewAdapter list_adapter;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public MainActivity(){
        list_adapter = new ListViewAdapter();
        curr_sentence = new Snt_manager(list_adapter.get_curr_item().getFilepath());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Review");

        ViewPager pager = findViewById(R.id.pager);
        //캐싱을 해놓을 프래그먼트 개수
        pager.setOffscreenPageLimit(2);

        //getSupportFragmentManager로 프래그먼트 참조가능
        MoviePagerAdapter Fragment_adapter = new MoviePagerAdapter(getSupportFragmentManager());

        ListFragment fragment0 = new ListFragment(list_adapter);
        Fragment_adapter.addItem(fragment0);

        ReviewerFragment fragment1 = new ReviewerFragment(curr_sentence, getApplicationContext());
        Fragment_adapter.addItem(fragment1);

        EnrollFragment fragment2 = new EnrollFragment(curr_sentence);
        Fragment_adapter.addItem(fragment2);

        pager.setAdapter(Fragment_adapter);
        pager.setCurrentItem(1);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0){
                    setTitle("Theme list");
                }
                if(position == 1){
                    setTitle("Review");
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
            curr_sentence.save_csv();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

