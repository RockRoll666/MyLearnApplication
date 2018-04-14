package com.exercise.tiger.mylearnapplication.bean;

import java.util.List;

/**
 * 豆瓣top250电影简介
 * Created by hzj on 2017/9/13.
 */

public class MovieBrief {
    private MovieRate rating;
    private List<String> genres;//电影类型
    private String title;//中文名称
    private List<MovieCast> casts;//演员
    private Integer collect_count;//收藏数
    private String original_title;//原名
    private String subtype;//子类别
    private List<MovieCast> directors;//导演
    private Integer year;//年份
    private Images images;
    private String alt;//电影简介网址
    private Long id;

    public MovieRate getRating() {
        return rating;
    }

    public void setRating(MovieRate rating) {
        this.rating = rating;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<MovieCast> getCasts() {
        return casts;
    }

    public void setCasts(List<MovieCast> casts) {
        this.casts = casts;
    }

    public Integer getCollect_count() {
        return collect_count;
    }

    public void setCollect_count(Integer collect_count) {
        this.collect_count = collect_count;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public List<MovieCast> getDirectors() {
        return directors;
    }

    public void setDirectors(List<MovieCast> directors) {
        this.directors = directors;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
