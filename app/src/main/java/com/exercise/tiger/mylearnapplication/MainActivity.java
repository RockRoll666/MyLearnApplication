package com.exercise.tiger.mylearnapplication;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.exercise.tiger.mylearnapplication.activity.TestGlideActivity;
import com.exercise.tiger.mylearnapplication.activity.TestRetrofitActivity;
import com.exercise.tiger.mylearnapplication.activity.TestRvWithRetrofitActivity;
import com.exercise.tiger.mylearnapplication.activity.TestRxJavaActivity;
import com.exercise.tiger.mylearnapplication.activity.TestScrollImageActivity;
import com.exercise.tiger.mylearnapplication.utils.AppToast;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tv = (TextView) findViewById(R.id.tv_clicked);
        tv.setOnClickListener(this);
        Button retrofitBtn = (Button) findViewById(R.id.btn_goto_retrofit_test);
        retrofitBtn.setOnClickListener(this);
        Button rvBtn = (Button) findViewById(R.id.btn_goto_rv_test);
        rvBtn.setOnClickListener(this);
        Button btnGlide = (Button) findViewById(R.id.btn_glide);
        btnGlide.setOnClickListener(this);
        Button btnScroll = (Button) findViewById(R.id.btn_scroll);
        btnScroll.setOnClickListener(this);
        Button btnRxandroid = (Button) findViewById(R.id.btn_rxandroid);
        btnRxandroid.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_clicked:
                TestRxJavaActivity.startByIntent(this);
//                AppToast.showShortText(this, FormatUtils.decodeFromUnicode("\\u5341\\u4e8c\\u6012\\u6c49"));
                break;
            case R.id.btn_goto_retrofit_test:
                TestRetrofitActivity.startActivityByIntent(this);
                break;
            case R.id.btn_goto_rv_test:
                TestRvWithRetrofitActivity.startActivityByIntent(this);
                break;
            case R.id.btn_glide:
                TestGlideActivity.startActivityByIntent(this);
                break;
            case R.id.btn_scroll:
                TestScrollImageActivity.startActivityByIntent(this);
                break;
            case R.id.btn_rxandroid:
                Observable<String> observable = new Observable<String>() {
                    @Override
                    protected void subscribeActual(Observer<? super String> observer) {
                        String s = "hahaha";
                        observer.onNext(s);
                    }
                };
                Observer<String> observer = new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        AppToast.showLongText(MainActivity.this,s);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                };
                observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
                break;
            default:
                break;
        }
    }
}
