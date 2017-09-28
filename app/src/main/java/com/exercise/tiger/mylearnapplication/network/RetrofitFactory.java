package com.exercise.tiger.mylearnapplication.network;

import android.app.Application;
import android.content.Context;

import com.exercise.tiger.mylearnapplication.BuildConfig;
import com.exercise.tiger.mylearnapplication.base.MyApplication;
import com.exercise.tiger.mylearnapplication.utils.Constants;
import com.exercise.tiger.mylearnapplication.utils.FileUtil;
import com.exercise.tiger.mylearnapplication.utils.NetworkUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hzj on 2017/9/28.
 */

public class RetrofitFactory {
    private static Retrofit retrofit;
    private static final File cacheFile = new File(FileUtil.WORK_DIRECTORY_CACHE + File.separator + "data" + File.separator + "NetCache");
    private static final Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);
    private static final Interceptor cacheInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!NetworkUtil.isNetworkAvailable(MyApplication.getContextObject())) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }
            Response response = chain.proceed(request);
            if (NetworkUtil.isNetworkAvailable(MyApplication.getContextObject())) {
                int maxAge = 0;
                // 有网络时, 不缓存, 最大保存时长为0
                response.newBuilder()
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .removeHeader("Pragma")
                        .build();
            } else {
                // 无网络时，设置超时为4周
                int maxStale = 60 * 60 * 24 * 28;
                response.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .removeHeader("Pragma")
                        .build();
            }
            return response;
        }
    };
    private static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .readTimeout(120, TimeUnit.SECONDS)
            .connectTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120,TimeUnit.SECONDS)
            .addNetworkInterceptor(cacheInterceptor)
            .addInterceptor(cacheInterceptor)
            .cache(cache)
            .addInterceptor(new HttpLoggingInterceptor().setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE))
            .addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
//                String sign_request_body = getOriginalSign(request);
                    request = request
                            .newBuilder()
//                        .addHeader("Version", getHeader(VERSION, ""))
//                        .addHeader("Timestamp", getHeader(TIMESTAMP, ""))
//                        .addHeader("Token", getHeader(TOKEN, ""))
//                        .addHeader("Sign", getHeader(SIGN, sign_request_body))
                            .build();
                    return chain.proceed(request);
                }
            })
            .build();
    private static final CallAdapter.Factory rxJavaCallAdapterFactory = RxJava2CallAdapterFactory.create();
    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .callFactory(okHttpClient)
                    .baseUrl(Constants.HTTP_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
        }
        return retrofit;
    }

    public static <S> S createService(Class<S> serviceClass) {
        return getRetrofit().create(serviceClass);
    }
    private final Gson mGsonDateFormat;

    public RetrofitFactory() {
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
}
