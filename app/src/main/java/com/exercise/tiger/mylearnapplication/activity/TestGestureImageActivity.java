package com.exercise.tiger.mylearnapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.exercise.tiger.mylearnapplication.R;
import com.exercise.tiger.mylearnapplication.base.BaseActivity;
import com.exercise.tiger.mylearnapplication.customview.GestureImageView;
import com.exercise.tiger.mylearnapplication.customview.LargeImageView;

import java.io.IOException;
import java.io.InputStream;

/**
 * 查看大图页面
 * create by hzj on 2018/4/14
 **/
public class TestGestureImageActivity extends BaseActivity {
    private GestureImageView givTest;

    public static void startActivityByIntent(Context from){
        Intent intent = new Intent(from,TestGestureImageActivity.class);
        from.startActivity(intent);
    }
    @Override
    protected void initView() {
        givTest = $(R.id.giv_test);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_gesture_image;
    }

    @Override
    protected void initData() {
        try {
            InputStream jpgIs = getAssets().open("big.jpg");
            Bitmap bp = BitmapFactory.decodeStream(jpgIs);
            if (null != jpgIs){
                jpgIs.close();
            }
            givTest.setImageBitmap(bp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void getDataFromIntent(Bundle savedInstanceState) {

    }
}
