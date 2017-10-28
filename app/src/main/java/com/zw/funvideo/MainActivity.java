package com.zw.funvideo;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.zw.funvideo.DailyChoice.DailyChoiceFragment;
import com.zw.funvideo.home.HomeViewPagerAdapter;

public class MainActivity extends AppCompatActivity {
    SmartTabLayout mTab;
    ViewPager mViewPager;
    HomeViewPagerAdapter mViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTab =  findViewById(R.id.viewpagertab);
        mViewPager = findViewById(R.id.viewpager);
        mViewPagerAdapter = new HomeViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mViewPagerAdapter);
        mTab.setViewPager(mViewPager);



    }
}
