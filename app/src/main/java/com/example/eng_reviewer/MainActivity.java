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
import com.example.eng_reviewer.Fragment.ReviewerFragment;
import com.example.eng_reviewer.sentences.Snt_manager;

public class MainActivity extends AppCompatActivity {
    Snt_manager sentence;

    public MainActivity(){
        sentence = new Snt_manager();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager pager = findViewById(R.id.pager);
        //캐싱을 해놓을 프래그먼트 개수
        pager.setOffscreenPageLimit(1);

        //getSupportFragmentManager로 프래그먼트 참조가능
        MoviePagerAdapter adapter = new MoviePagerAdapter(getSupportFragmentManager());

        ReviewerFragment fragment1 = new ReviewerFragment(sentence);
        adapter.addItem(fragment1);

        EnrollFragment fragment2 = new EnrollFragment(sentence);
        adapter.addItem(fragment2);
//
//        Fragment3 fragment3 = new Fragment3();
//        adapter.addItem(fragment3);

        pager.setAdapter(adapter);
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
            sentence.save_csv();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

