package com.zw.funvideo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zw.funvideo.DailyChoice.DailyChoiceFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DailyChoiceFragment dailyChoiceFragment = new DailyChoiceFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content,dailyChoiceFragment)
                .commit();
    }
}
