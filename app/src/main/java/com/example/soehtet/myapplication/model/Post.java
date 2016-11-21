package com.example.soehtet.myapplication.model;

/**
 * Created by soehtet on 13/10/16.
 */

public class Post {

    int userid;
    String username;
    String text;
    String pathImage;
    String date;
    String ava;

    public Post(int userid, String username, String text, String pathImage, String date, String ava) {
        this.userid = userid;
        this.username = username;
        this.text = text;
        this.pathImage = pathImage;
        this.date = date;
        this.ava = ava;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPathImage() {
        return pathImage;
    }

    public void setPathImage(String pathImage) {
        this.pathImage = pathImage;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAva() {
        return ava;
    }

    public void setAva(String ava) {
        this.ava = ava;
    }
}
