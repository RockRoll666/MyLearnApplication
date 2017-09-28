package com.exercise.tiger.mylearnapplication.network;

import com.exercise.tiger.mylearnapplication.testRetrofit.bean.AddrsBean;
import com.exercise.tiger.mylearnapplication.testRetrofit.bean.QueryDouBanMovieTopResult;
import com.exercise.tiger.mylearnapplication.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * 请求接口类
 * Created by hzj on 2017/9/13.
 */

public class RetrofitRequestService {

    public interface GetLocationService {
        @GET("geocoding")
        Call<AddrsBean> getLocation();
    }

    public interface GetLocationServiceWithRx{
        @GET("geocoding")
        Observable<AddrsBean> getLocation();
    }
    public interface GetDouBanMovieTop250{
        @GET("movie/top250")
        Call<QueryDouBanMovieTopResult> getDouBanMovieTop250(@Query("start") int start , @Query("count") int count);
    }
    public interface GetDouBanMovieTop250WithRx{
        @GET("{category}/top250")
        Observable<QueryDouBanMovieTopResult> getDouBanMovieTop250(@Path("category") String category,@Query("start") int start , @Query("count") int count);
    }
}
