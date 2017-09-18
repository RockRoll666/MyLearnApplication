package com.exercise.tiger.mylearnapplication.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

/**
 * recycleviewholder基类
 * Created by hzj on 2017/5/10.
 */

public class BaseRecycleViewHolder extends RecyclerView.ViewHolder {
    /**
     * 这个稀疏数组，网上说的是提高效率的
     */
    private final SparseArray<View> views = new SparseArray<View>();
    private View convertView;

    /**
     * 如果用到了，比如我们用glide加载图片的时候，还有其他的需要用到上下文的时候
     */
    private Context mContext;
    public BaseRecycleViewHolder(View itemView) {
        super(itemView);
        convertView = itemView;
        mContext = itemView.getContext();
    }

    public View getRootView(){
        return convertView;
    }

    /**
     * 返回一个具体的view对象
     * 这个就是借鉴的base-adapter-helper中的方法
     *
     * @param viewId
     * @param <T>
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = convertView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }
}
