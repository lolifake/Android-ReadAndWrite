package com.example.musical.enity;

import org.litepal.crud.LitePalSupport;

public class User extends LitePalSupport {
    private int id;
    private String niki_name;//用户名（昵称）
    private String sexual;//用户性别
    private String note;//用户签名
    private String email;//用户邮箱
    private String password;//用户密码

    public void setId(int id) {
        this.id = id;
    }

    public void setNiki_name(String niki_name) {
        this.niki_name = niki_name;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setSexual(String sexual) {
        this.sexual = sexual;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getNiki_name() {
        return niki_name;
    }

    public String getNote() {
        return note;
    }

    public String getSexual() {
        return sexual;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
