package com.exercise.tiger.mylearnapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.exercise.tiger.mylearnapplication.R;
import com.exercise.tiger.mylearnapplication.base.BaseActivity;
import com.exercise.tiger.mylearnapplication.customview.LargeImageView;

import java.io.IOException;
import java.io.InputStream;

/**
 * 查看大图页面
 * create by hzj on 2018/4/14
 **/
public class TestLargeImageActivity extends BaseActivity {
    private LargeImageView livTest;

    public static void startActivityByIntent(Context from){
        Intent intent = new Intent(from,TestLargeImageActivity.class);
        from.startActivity(intent);
    }
    @Override
    protected void initView() {
        livTest = $(R.id.liv_test);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_large_image;
    }

    @Override
    protected void initData() {
        try {
            InputStream jpgIs = getAssets().open("big.jpg");
            livTest.setInputStream(jpgIs);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void getDataFromIntent(Bundle savedInstanceState) {

    }
}
