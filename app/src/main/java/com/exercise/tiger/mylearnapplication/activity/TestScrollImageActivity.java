package com.exercise.tiger.mylearnapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.exercise.tiger.mylearnapplication.R;
import com.exercise.tiger.mylearnapplication.base.BaseActivity;

/**
 * scrollview嵌套imageview
 * create by hzj on 2018/4/14
 **/
public class TestScrollImageActivity extends BaseActivity {
    private ImageView ivTestScroll;
    public static void startActivityByIntent(Context from){
        Intent intent = new Intent(from,TestScrollImageActivity.class);
        from.startActivity(intent);
    }
    @Override
    protected void initView() {
        ivTestScroll = $(R.id.iv_test_scroll);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_test_scrollview;
    }

    @Override
    protected void initData() {
        Glide.with(this).load("file:///android_asset/big.jpg").into(ivTestScroll);
    }

    @Override
    protected void getDataFromIntent(Bundle savedInstanceState) {

    }
}
