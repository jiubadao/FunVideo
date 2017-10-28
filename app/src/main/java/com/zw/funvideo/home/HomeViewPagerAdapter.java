package com.zw.funvideo.home;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;

import com.zw.funvideo.DailyChoice.DailyChoiceFragment;
import com.zw.funvideo.R;
import com.zw.funvideo.comment.Global;

/**
 * Created by zhangwei on 17/10/28.
 */

public class HomeViewPagerAdapter extends FragmentStatePagerAdapter {
    public HomeViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if(position ==0){
            return DailyChoiceFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position ==0){
            return Global.getContext().getString(R.string.daily_choice);
        }
        return super.getPageTitle(position);
    }
}
