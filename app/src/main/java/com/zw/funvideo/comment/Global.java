package com.zw.funvideo.comment;

import android.content.Context;

/**
 * Created by zhangwei on 17/10/28.
 */

public class Global {
    public static Context getContext() {
        return mContext;
    }

    public static void setContext(Context mContext) {
        Global.mContext = mContext;
    }

    private static Context mContext;
}
