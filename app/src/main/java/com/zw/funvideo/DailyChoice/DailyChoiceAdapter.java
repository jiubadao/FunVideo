package com.zw.funvideo.DailyChoice;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zw.funvideo.R;
import com.zw.funvideo.model.ItemList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangwei on 17/10/27.
 */

public class DailyChoiceAdapter extends RecyclerView.Adapter<DailyChoiceAdapter.VideoItemViewHolder> {


    public interface  Callbacks{
        void onVideoItemClicked(ItemList itemList);
    }

    public DailyChoiceAdapter(Callbacks callbacks) {
        this.mCallbacks = callbacks;
    }

    private Callbacks  mCallbacks;
    private List<ItemList> mItems;

    @Override
    public VideoItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video,parent,false);
        return new VideoItemViewHolder(view,mCallbacks);
    }

    @Override
    public void onBindViewHolder(VideoItemViewHolder holder, int position) {
      ItemList itemList =  mItems.get(position);
        holder.bind(itemList);
    }

    public void addData(List<ItemList> itemLists){
        mItems= itemLists;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mItems ==null? 0: mItems.size();
    }

    public static class VideoItemViewHolder extends RecyclerView.ViewHolder{
        private ImageView videoCoverIv;
        private TextView videoDescTv;
        private TextView authorTagTv;
        private static final String VIDEO_TAG = "video";
        private Callbacks mCallbacks;
        private ItemList mItem;
        public VideoItemViewHolder(View itemView,Callbacks callbacks) {
            super(itemView);
            videoCoverIv = itemView.findViewById(R.id.video_cover);
            videoDescTv = itemView.findViewById(R.id.video_desc);
            authorTagTv = itemView.findViewById(R.id.author_tag);
            mCallbacks = callbacks;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                if(mCallbacks!=null&&mItem!=null){
                    mCallbacks.onVideoItemClicked(mItem);
                }
                }
            });
        }


        public void bind(ItemList item){
            mItem = item;
            if (item.type.contains(VIDEO_TAG)) {


            videoDescTv.setText(item.data.title);
            if (item.data.author != null) {
                authorTagTv.setText(item.data.author.name);
            }

            Glide.with(itemView.getContext())
                    .load(item.data.cover.detail)
                    .into(videoCoverIv);
            }
        }
    }
}
