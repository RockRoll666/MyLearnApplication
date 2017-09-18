package com.exercise.tiger.mylearnapplication.testRetrofit.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.exercise.tiger.mylearnapplication.R;
import com.exercise.tiger.mylearnapplication.base.BaseActivity;
import com.exercise.tiger.mylearnapplication.customview.PullUpLoadMoreListener;

/**
 *
 * Created by hzj on 2017/9/18.
 */

public class TestRvWithRetrofitActivity extends BaseActivity {
    @Override
    protected void initView() {
        RecyclerView recyclerView = $(R.id.rv_content);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        PullUpLoadMoreListener loadMoreListener = new PullUpLoadMoreListener() {
            @Override
            public void loadMore() {

            }
        };
    }

    @Override
    protected int getLayoutID() {
        return 0;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void getDataFromIntent(Bundle savedInstanceState) {

    }
}
