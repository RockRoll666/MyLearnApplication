package com.exercise.tiger.mylearnapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.exercise.tiger.mylearnapplication.R;
import com.exercise.tiger.mylearnapplication.base.BaseActivity;

import java.io.IOException;
import java.io.InputStream;

/**
 * *create by hzj on 2018/4/14
 **/
public class TestGlideActivity extends BaseActivity {
    private ImageView ivWhole;
    private ImageView ivLittle;
    private ImageView ivGesture;

    public static void startActivityByIntent(Context from){
        Intent intent = new Intent(from,TestGlideActivity.class);
        from.startActivity(intent);
    }

    @Override
    protected void initView() {
        ivWhole = $(R.id.iv_whole);
        ivWhole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestLargeImageActivity.startActivityByIntent(TestGlideActivity.this);
            }
        });
        ivLittle = $(R.id.iv_little);
        ivGesture = $(R.id.iv_gesture);
        ivGesture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestGestureImageActivity.startActivityByIntent(TestGlideActivity.this);
            }
        });
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_test_glide;
    }

    @Override
    protected void initData() {
        Glide.with(this).load("file:///android_asset/big.jpg").into(ivWhole);
        Glide.with(this).load("file:///android_asset/big.jpg").into(ivGesture);
        try {
            InputStream jpgIs = getAssets().open("big.jpg");
            //获得图片宽高
            BitmapFactory.Options tmOptions = new BitmapFactory.Options();
            tmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(jpgIs,null,tmOptions);
            int width = tmOptions.outWidth;
            int height = tmOptions.outHeight;

            //选择用来显示的bitmap部分
            BitmapRegionDecoder bitmapRegionDecoder = BitmapRegionDecoder.newInstance(jpgIs,false);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            Bitmap jpg = bitmapRegionDecoder.decodeRegion(new Rect(width / 2 - 100, height / 2 - 100, width / 2 + 100, height / 2 + 100), options);
            Log.d("jpgwidth",""+jpg.getWidth());
            ivLittle.setImageBitmap(jpg);
            if (null != jpgIs){
                jpgIs.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void getDataFromIntent(Bundle savedInstanceState) {

    }
}
