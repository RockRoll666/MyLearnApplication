package com.exercise.tiger.mylearnapplication.testRetrofit.bean;

/**
 * 电影演员/职员
 * Created by hzj on 2017/9/13.
 */

public class MovieCast {
    private String alt;//个人简介网页
    private Images avatars;//头像
    private String name;
    private Long id;

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public Images getAvatars() {
        return avatars;
    }

    public void setAvatars(Images avatars) {
        this.avatars = avatars;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
