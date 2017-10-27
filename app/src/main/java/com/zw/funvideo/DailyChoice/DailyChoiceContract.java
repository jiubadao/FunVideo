package com.zw.funvideo.DailyChoice;

import com.zw.funvideo.model.ItemList;

import java.util.List;

/**
 * Created by zhangwei on 17/10/27.
 */

public interface DailyChoiceContract {

     interface View{
         void onLoadDataSuccess(List<ItemList> itemLists);

    }

    interface  Prezenter{
        void loadData();

    }



}
