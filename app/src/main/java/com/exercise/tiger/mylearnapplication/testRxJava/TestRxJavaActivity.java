package com.exercise.tiger.mylearnapplication.testRxJava;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.exercise.tiger.mylearnapplication.R;
import com.exercise.tiger.mylearnapplication.base.BaseActivity;
import com.exercise.tiger.mylearnapplication.utils.AppToast;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * 学习rxjava
 * Created by hzj on 2017/9/11.
 */

public class TestRxJavaActivity extends BaseActivity implements View.OnClickListener{
    private Flowable<String> flowable;
    private Subscriber<String> subscriber;

    public static void startByIntent(Context from){
        from.startActivity(new Intent(from,TestRxJavaActivity.class));
    }
    @Override
    protected void initView() {
        ImageView testIv = $(R.id.iv_test);
        Button testBtn = $(R.id.btn_test);
        testBtn.setOnClickListener(this);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_test_rxjava;
    }

    @Override
    protected void initData() {
        flowable = initFlowable();
        subscriber = initSubscriber();
    }

    @Override
    protected void getDataFromIntent(Bundle savedInstanceState) {

    }

    private Flowable<String> initFlowable(){
        return Flowable.create(new FlowableOnSubscribe<String>(){
            @Override
            public void subscribe(@NonNull FlowableEmitter<String> e) throws Exception {
                e.onNext("123");
                e.onComplete();
            }
        }, BackpressureStrategy.BUFFER);
    }

    private Subscriber<String> initSubscriber(){
        return new Subscriber<String>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(1);
            }

            @Override
            public void onNext(String s) {
                AppToast.showShortText(TestRxJavaActivity.this,"do:" + s);
            }

            @Override
            public void onError(Throwable t) {
                Log.e("onError","onError" + t);
            }

            @Override
            public void onComplete() {
                Log.d("onComplete","onComplete");
            }
        };
    }

    private void testFlowableJust(){
        Flowable.just("123")
                .map(new Function<String, String>() {
                    @Override
                    public String apply(@NonNull String s) throws Exception {
                        return s + "456";
                    }
                }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                AppToast.showShortText(TestRxJavaActivity.this,"do:" + s);
            }
        });
    }

    private void testFlowableList(){
        List<Integer> data = new ArrayList<>();
        data.add(1);
        data.add(2);
        data.add(3);
        Flowable.fromIterable(data)
                .take(2)
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(@NonNull Integer integer) throws Exception {
                        return integer > 1;
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d("testFlowableList",integer +"");
                    }
                });
    }

    private void testFlatMap(){
        List<Integer> data = new ArrayList<>();
        data.add(1);
        data.add(2);
        data.add(3);
        Flowable.just(data)
                .flatMap(new Function<List<Integer>, Publisher<Integer>>() {
                    @Override
                    public Publisher<Integer> apply(@NonNull List<Integer> integers) throws Exception {
                        return Flowable.fromIterable(integers);
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d("testFlatMap",integer +"");
                    }
                });
    }

    private void testReactAction(){
        Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<String> e) throws Exception {
                e.onNext("将会在3秒后显示");
                SystemClock.sleep(3000);
                e.onNext("123446");
                e.onComplete();
            }
        }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.d("testReactAction",s);
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_test:
                testReactAction();
                break;
            default:
                break;
        }
    }
}
