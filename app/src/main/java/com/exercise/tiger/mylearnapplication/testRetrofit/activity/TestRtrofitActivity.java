package com.exercise.tiger.mylearnapplication.testRetrofit.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.exercise.tiger.mylearnapplication.R;
import com.exercise.tiger.mylearnapplication.base.BaseActivity;
import com.exercise.tiger.mylearnapplication.network.RetrofitRequestServiceFactory;
import com.exercise.tiger.mylearnapplication.testRetrofit.bean.AddrsBean;
import com.exercise.tiger.mylearnapplication.testRetrofit.bean.QueryDouBanMovieTopResult;
import com.exercise.tiger.mylearnapplication.utils.ActivityUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * 练习rtrofit
 * Created by hzj on 2017/9/13.
 */

public class TestRtrofitActivity extends BaseActivity implements View.OnClickListener{
    public static void startActivityByIntent(Context from){
        ActivityUtils.startNewActivity(from,TestRtrofitActivity.class);
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
        Retrofit retrofit = RetrofitRequestServiceFactory.getInstance().getNewRetrofit();
        RetrofitRequestServiceFactory.GetLocationService service = retrofit.create(RetrofitRequestServiceFactory.GetLocationService.class);
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

    private void getDouBanTop250(int start,int count){
        Retrofit retrofit = RetrofitRequestServiceFactory.getInstance().getNewRetrofit();
        RetrofitRequestServiceFactory.GetDouBanMovieTop250 service = retrofit.create(RetrofitRequestServiceFactory.GetDouBanMovieTop250.class);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_test:
                getDouBanTop250(25,2);
                break;
        }
    }
}
