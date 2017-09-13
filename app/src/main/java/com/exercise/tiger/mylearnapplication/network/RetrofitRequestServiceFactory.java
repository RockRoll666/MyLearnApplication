package com.exercise.tiger.mylearnapplication.network;

import com.exercise.tiger.mylearnapplication.testRetrofit.bean.AddrsBean;
import com.exercise.tiger.mylearnapplication.testRetrofit.bean.QueryDouBanMovieTopResult;
import com.exercise.tiger.mylearnapplication.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 请求接口类
 * Created by hzj on 2017/9/13.
 */

public class RetrofitRequestServiceFactory {

    private final Gson mGsonDateFormat;

    private static class SingletonHolder{
        private static final RetrofitRequestServiceFactory INSTANCE = new RetrofitRequestServiceFactory();
    }


    public static RetrofitRequestServiceFactory getInstance(){
        return SingletonHolder.INSTANCE;
    }

    public RetrofitRequestServiceFactory(){
        mGsonDateFormat = new GsonBuilder()
                .setDateFormat(Constants.DATE_FORMAT_PATTERN)
                .create();
    }

    public Retrofit getNewRetrofit(){
        //声明日志类
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        //设定日志级别
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //自定义OkHttpClient
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        //添加拦截器
        okHttpClient.addInterceptor(httpLoggingInterceptor);
        return new Retrofit.Builder()
                .baseUrl(Constants.HTTP_URL)
                .addConverterFactory(GsonConverterFactory.create(mGsonDateFormat))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient.build())
                .build();
    }
    public interface GetLocationService {
        @GET("geocoding")
        Call<AddrsBean> getLocation();
    }
    public interface GetDouBanMovieTop250{
        @GET("movie/top250")
        Call<QueryDouBanMovieTopResult> getDouBanMovieTop250(@Query("start") int start , @Query("count") int count);
    }
}
