package com.zw.funvideo.api;

import com.zw.funvideo.model.Daily;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author zsj
 */

public interface ApiDaily {

    @GET("v2/feed?num=2")
    Observable<Daily> getDaily(@Query("date") long date);

    @GET("v2/feed?num=2")
    Observable<Daily> getDaily();

}
