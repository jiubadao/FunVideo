package com.zw.funvideo.network;

import com.zw.funvideo.constant.Constant;

import java.util.concurrent.TimeUnit;

import io.reactivex.plugins.RxJavaPlugins;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zhangwei on 17/10/27.
 */

public class RetrofitHelper {
    private static RetrofitHelper mInstance;

    public Retrofit getRetrofit() {
        return mRetrofit;
    }

    private Retrofit mRetrofit;
    private RetrofitHelper() {

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();


         mRetrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }


    public static RetrofitHelper getInstance(){
        if(mInstance==null){
            synchronized (RetrofitHelper.class){
                mInstance = new RetrofitHelper();
            }
        }

        return mInstance;

    }
}
