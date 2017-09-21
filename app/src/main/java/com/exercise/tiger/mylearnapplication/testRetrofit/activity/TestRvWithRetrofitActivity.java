package com.exercise.tiger.mylearnapplication.testRetrofit.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.exercise.tiger.mylearnapplication.R;
import com.exercise.tiger.mylearnapplication.base.BaseActivity;
import com.exercise.tiger.mylearnapplication.customview.PullUpLoadMoreListener;
import com.exercise.tiger.mylearnapplication.network.RetrofitRequestServiceFactory;
import com.exercise.tiger.mylearnapplication.testRetrofit.TestRetrofitAdapter;
import com.exercise.tiger.mylearnapplication.testRetrofit.bean.MovieBrief;
import com.exercise.tiger.mylearnapplication.testRetrofit.bean.QueryDouBanMovieTopResult;
import com.exercise.tiger.mylearnapplication.utils.ActivityUtils;
import com.exercise.tiger.mylearnapplication.utils.AppToast;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

/**
 *
 * Created by hzj on 2017/9/18.
 */

public class TestRvWithRetrofitActivity extends BaseActivity {
    private TestRetrofitAdapter adapter;
    private int index = 0;
    private int pageSize = 20;
    private List<MovieBrief> movieBriefs;
    public static void startActivityByIntent(Context from){
        ActivityUtils.startNewActivity(from,TestRvWithRetrofitActivity.class);
    }
    @Override
    protected void initView() {
        RecyclerView recyclerView = $(R.id.rv_content);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this,manager.getOrientation());
        recyclerView.addItemDecoration(itemDecoration);
        PullUpLoadMoreListener loadMoreListener = new PullUpLoadMoreListener() {
            @Override
            public void loadMore() {
                getDouBanTop250();
            }
        };
        recyclerView.addOnScrollListener(loadMoreListener);
        movieBriefs = new ArrayList<>();
        adapter = new TestRetrofitAdapter(this,movieBriefs);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_rv;
    }

    @Override
    protected void initData() {
        getDouBanTop250();
    }

    @Override
    protected void getDataFromIntent(Bundle savedInstanceState) {

    }

    private void getDouBanTop250(){
        startLoading();
        Retrofit retrofit = RetrofitRequestServiceFactory.getInstance().getNewRetrofit();
        RetrofitRequestServiceFactory.GetDouBanMovieTop250WithRx service = retrofit.create(RetrofitRequestServiceFactory.GetDouBanMovieTop250WithRx.class);
        service.getDouBanMovieTop250("movie",index,pageSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<QueryDouBanMovieTopResult>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull QueryDouBanMovieTopResult queryDouBanMovieTopResult) {
                        if (queryDouBanMovieTopResult.getSubjects().size() < pageSize){
                            index = queryDouBanMovieTopResult.getTotal();
                        }else {
                            index = index + pageSize;
                        }
                        if (queryDouBanMovieTopResult.getSubjects().size() > 0){
//                            showAllData(queryDouBanMovieTopResult);
                            showSingularData(queryDouBanMovieTopResult);
                        }else {
                            AppToast.showShortText(TestRvWithRetrofitActivity.this,R.string.load_all);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        finishLoading();
                        AppToast.showShortText(TestRvWithRetrofitActivity.this,R.string.load_fail);
                    }

                    @Override
                    public void onComplete() {
                        finishLoading();
                    }
                });
    }

    private void showAllData(QueryDouBanMovieTopResult queryDouBanMovieTopResult){
        movieBriefs.addAll(queryDouBanMovieTopResult.getSubjects());
        adapter.setmData(movieBriefs);
    }
    private void showSingularData(final QueryDouBanMovieTopResult queryDouBanMovieTopResult){

        Flowable.just(queryDouBanMovieTopResult.getSubjects())
                .subscribeOn(Schedulers.io())
                .map(new Function<List<MovieBrief>, List<MovieBrief>>() {

                    @Override
                    public List<MovieBrief> apply(@NonNull List<MovieBrief> movieBriefs) throws Exception {
                        removeSingular(movieBriefs);
                        return movieBriefs;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<MovieBrief>>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(1);
                    }

                    @Override
                    public void onNext(List<MovieBrief> movieBriefs) {
                        TestRvWithRetrofitActivity.this.movieBriefs.addAll(movieBriefs);
                        adapter.setmData(TestRvWithRetrofitActivity.this.movieBriefs);
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void removeSingular(List<MovieBrief> movieBriefs){
        boolean needShow = true;
        Iterator<MovieBrief> iterator = movieBriefs.iterator();
        while (iterator.hasNext()){
            iterator.next();
            if (!needShow) iterator.remove();
            needShow = !needShow;
        }
    }
}
