package com.exercise.tiger.mylearnapplication.customview;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * 上拉加载更多的监听
 * Created by hzj on 2017/9/18.
 */

public abstract class PullUpLoadMoreListener extends RecyclerView.OnScrollListener {
    private int lastVisibleItem;
    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager){
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager)recyclerView.getLayoutManager();
            lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
        }

    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == recyclerView.getAdapter().getItemCount()){
            loadMore();
        }
    }

    public abstract void loadMore();
}
