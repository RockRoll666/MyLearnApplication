package com.exercise.tiger.mylearnapplication.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.exercise.tiger.mylearnapplication.R;
import com.exercise.tiger.mylearnapplication.base.BaseActivity;
import com.exercise.tiger.mylearnapplication.network.RetrofitFactory;
import com.exercise.tiger.mylearnapplication.network.RetrofitRequestService;
import com.exercise.tiger.mylearnapplication.bean.AddrsBean;
import com.exercise.tiger.mylearnapplication.bean.QueryDouBanMovieTopResult;
import com.exercise.tiger.mylearnapplication.utils.ActivityUtils;
import com.exercise.tiger.mylearnapplication.utils.AppToast;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * 练习rtrofit
 * Created by hzj on 2017/9/13.
 */

public class TestRetrofitActivity extends BaseActivity implements View.OnClickListener{
    public static void startActivityByIntent(Context from){
        ActivityUtils.startNewActivity(from,TestRetrofitActivity.class);
    }
    @Override
    protected void initView() {
        Button testBtn = $(R.id.btn_test);
        testBtn.setOnClickListener(this);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_test_retrofit;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void getDataFromIntent(Bundle savedInstanceState) {

    }

    private void getLocationRequest(){
        Retrofit retrofit = RetrofitFactory.getRetrofit();
        RetrofitRequestService.GetLocationService service = retrofit.create(RetrofitRequestService.GetLocationService.class);
        Call<AddrsBean> call = service.getLocation();
        call.enqueue(new Callback<AddrsBean>() {
            @Override
            public void onResponse(Call<AddrsBean> call, Response<AddrsBean> response) {
                Log.d("onResponse",response.body().toString());
            }

            @Override
            public void onFailure(Call<AddrsBean> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getLocationWithRx(){
        Retrofit retrofit = RetrofitFactory.getRetrofit();
        RetrofitRequestService.GetLocationServiceWithRx serviceWithRx = retrofit.create(RetrofitRequestService.GetLocationServiceWithRx.class);
        serviceWithRx.getLocation().subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AddrsBean>() {
                    private Disposable d;
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                     this.d = d;
                    }

                    @Override
                    public void onNext(@NonNull AddrsBean addrsBean) {
                        AppToast.showShortText(TestRetrofitActivity.this,addrsBean.getLat() + "");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                        AppToast.showShortText(TestRetrofitActivity.this,e.toString());
                        d.dispose();
                    }

                    @Override
                    public void onComplete() {
                        d.dispose();
                    }
                });
    }

    private void getDouBanTop250(int start,int count){
        Retrofit retrofit = RetrofitFactory.getRetrofit();
        RetrofitRequestService.GetDouBanMovieTop250 service = retrofit.create(RetrofitRequestService.GetDouBanMovieTop250.class);
        Call<QueryDouBanMovieTopResult> call = service.getDouBanMovieTop250(start,count);
        call.enqueue(new Callback<QueryDouBanMovieTopResult>() {
            @Override
            public void onResponse(Call<QueryDouBanMovieTopResult> call, Response<QueryDouBanMovieTopResult> response) {
                Log.d("onResponse",response.body().toString());
            }

            @Override
            public void onFailure(Call<QueryDouBanMovieTopResult> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getDouBanTop250WithRx(){
        Retrofit retrofit = RetrofitFactory.getRetrofit();
        RetrofitRequestService.GetDouBanMovieTop250WithRx service = retrofit.create(RetrofitRequestService.GetDouBanMovieTop250WithRx.class);
        service.getDouBanMovieTop250("movie",25,1)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<QueryDouBanMovieTopResult>() {
                    private Disposable d;
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        this.d = d;
                    }

                    @Override
                    public void onNext(@NonNull QueryDouBanMovieTopResult queryDouBanMovieTopResult) {
                        AppToast.showShortText(TestRetrofitActivity.this,queryDouBanMovieTopResult.getTitle());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                        d.dispose();
                    }

                    @Override
                    public void onComplete() {
                        d.dispose();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_test:
                getDouBanTop250WithRx();
                break;
        }
    }
}
