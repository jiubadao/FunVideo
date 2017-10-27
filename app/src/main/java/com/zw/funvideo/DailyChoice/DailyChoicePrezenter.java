package com.zw.funvideo.DailyChoice;

import com.zw.funvideo.api.ApiDaily;
import com.zw.funvideo.model.Daily;
import com.zw.funvideo.model.IssueList;
import com.zw.funvideo.model.ItemList;
import com.zw.funvideo.network.RetrofitHelper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zhangwei on 17/10/27.
 */

public class DailyChoicePrezenter implements DailyChoiceContract.Prezenter {
    private ApiDaily apiDaily;
    private CompositeDisposable compositeDisposable;

    private List<ItemList> mItems= new ArrayList<>();

    WeakReference<DailyChoiceContract.View> mView;


    public DailyChoicePrezenter(DailyChoiceContract.View view) {
        apiDaily = RetrofitHelper.getInstance().getRetrofit().create(ApiDaily.class);
        compositeDisposable = new CompositeDisposable();
        mView = new WeakReference<DailyChoiceContract.View>(view);
    }

    @Override
    public void loadData() {
        apiDaily.getDaily()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Daily>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                         compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull Daily daily) {
                            addData(daily);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }


    private void addData(Daily daily) {
        for (IssueList issueList : daily.issueList) {

            mItems.addAll(issueList.itemList);
        }

        if(getView()!=null){
            getView().onLoadDataSuccess(mItems);
        }


    }

    private DailyChoiceContract.View getView(){

        return mView.get();
    }
}
