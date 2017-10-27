package com.zw.funvideo.DailyChoice;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zw.funvideo.R;
import com.zw.funvideo.model.ItemList;
import com.zw.funvideo.videoplayer.PlayActivity;

import java.util.List;

/**
 * Created by zhangwei on 17/10/27.
 */

public class DailyChoiceFragment extends Fragment implements DailyChoiceContract.View,DailyChoiceAdapter.Callbacks {
    private RecyclerView mRecyclerView;
    private DailyChoiceContract.Prezenter mPrezenter;
    private DailyChoiceAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPrezenter = new DailyChoicePrezenter(this);
        mAdapter = new DailyChoiceAdapter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_daily_choice,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = view.findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mPrezenter.loadData();
    }

    @Override
    public void onLoadDataSuccess(List<ItemList> itemLists) {
        mAdapter.addData(itemLists);
    }

    @Override
    public void onVideoItemClicked(ItemList itemList) {
        Intent intent = new Intent(getContext(), PlayActivity.class);
        intent.putExtra("item", itemList);
        startActivity(intent);
    }
}
