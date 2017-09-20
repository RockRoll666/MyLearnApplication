package com.exercise.tiger.mylearnapplication.testRetrofit;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.exercise.tiger.mylearnapplication.R;
import com.exercise.tiger.mylearnapplication.base.BaseRecycleViewAdapter;
import com.exercise.tiger.mylearnapplication.base.BaseRecycleViewHolder;
import com.exercise.tiger.mylearnapplication.testRetrofit.bean.MovieBrief;

import java.util.List;

/**
 * 测试retrofit
 * Created by hzj on 2017/9/20.
 */

public class TestRetrofitAdapter extends BaseRecycleViewAdapter<MovieBrief> {
    private Activity parent;
    public TestRetrofitAdapter(Activity parent,List<MovieBrief> mData) {
        super(mData);
        this.parent = parent;
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.cell_rv_douban_movie;
    }

    @Override
    public void convert(BaseRecycleViewHolder holder, MovieBrief movieBrief, int position) {
        ImageView movieIv = holder.getView(R.id.iv_movie_ic);
        RequestOptions requestOptions = new RequestOptions()
                .fitCenter()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher_round)
                .priority(Priority.HIGH);
        Glide.with(parent).load(movieBrief.getImages().getSmall()).apply(requestOptions).into(movieIv);
        TextView titleTv = holder.getView(R.id.tv_title);
        titleTv.setText(movieBrief.getTitle());
    }
}
