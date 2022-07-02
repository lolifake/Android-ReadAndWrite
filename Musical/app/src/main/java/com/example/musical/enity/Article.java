package com.example.musical.enity;

import org.litepal.crud.LitePalSupport;

public class Article extends LitePalSupport {
    public int id;
    private String txt_content;//文章内容
    private String txt_title;//文章标题
    private String txt_author;//文章作者
    private String collect_user;//收藏者

    /*仅作测试使用*//*
    public Article(String txt_title, String txt_author,String txt_content){
        this.txt_author = txt_author;
        this.txt_title = txt_title;
        this.txt_content=txt_content;
    }*/

    public void setTxt_author(String txt_author) {
        this.txt_author = txt_author;
    }

    public void setTxt_content(String txt_content) {
        this.txt_content = txt_content;
    }

    public void setTxt_title(String txt_title) {
        this.txt_title = txt_title;
    }

    public void setCollect_user(String collect_user) {
        this.collect_user = collect_user;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTxt_author() {
        return txt_author;
    }

    public String getTxt_content() {
        return txt_content;
    }

    public String getTxt_title() {
        return txt_title;
    }

    public String getCollect_user() {
        return collect_user;
    }
}
