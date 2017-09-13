package com.exercise.tiger.mylearnapplication.testRetrofit.bean;

import java.util.List;

/**
 * 豆瓣电影top250查询结果
 * Created by hzj on 2017/9/13.
 */

public class QueryDouBanMovieTopResult {
    private Integer count;//结果数量
    private Integer start;//开始下标
    private Integer total;//总数
    private List<MovieBrief> subjects;//电影简介
    private String title;//标题

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<MovieBrief> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<MovieBrief> subjects) {
        this.subjects = subjects;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
