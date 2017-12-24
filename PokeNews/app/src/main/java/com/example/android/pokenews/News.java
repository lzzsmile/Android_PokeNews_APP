package com.example.android.pokenews;

/**
 * Created by Roy Li on 15/12/2017.
 */

public class News {

    private String url;
    private String title;
    private String author;
    private String date;
    private String source;

    public News(String t, String a, String d, String s, String u) {
        title = t;
        author = a;
        date = d;
        source = s;
        url = u;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getDate() {
        return date;
    }

    public String getSource() {
        return source;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "Title: " + title + " Author: " + author + " Date: " + date + " Source: " + source + " Url: " + url;
    }
}
