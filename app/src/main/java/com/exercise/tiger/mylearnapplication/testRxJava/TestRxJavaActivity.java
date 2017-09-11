package com.exercise.tiger.mylearnapplication.testRxJava;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

import com.exercise.tiger.mylearnapplication.R;
import com.exercise.tiger.mylearnapplication.base.BaseActivity;

import org.reactivestreams.Subscriber;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;

/**
 * 学习rxjava
 * Created by hzj on 2017/9/11.
 */

public class TestRxJavaActivity extends BaseActivity {
    @Override
    protected void initView() {
        ImageView testIv = $(R.id.iv_test);
        Flowable<String> flow = Flowable.create(new FlowableOnSubscribe<String>(){
            @Override
            public void subscribe(@NonNull FlowableEmitter<String> e) throws Exception {
                e.onNext("123");
                e.onComplete();
            }
        }, BackpressureStrategy.BUFFER);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_test_rxjava;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void getDataFromIntent(Bundle savedInstanceState) {

    }
}
