package com.exercise.tiger.mylearnapplication.network;

import com.exercise.tiger.mylearnapplication.bean.AddrsBean;
import com.exercise.tiger.mylearnapplication.bean.QueryDouBanMovieTopResult;

import io.reactivex.Observable;
import retrofit2.Call;
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
