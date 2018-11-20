package com.example.android.newsapp;

import java.util.Date;

public class News {
    
    private String articleTitle;

    private String articleSection;
    private String articleAuthor;

    private String articleDate;
    private String apiUrl;

    public News(String title,String author, String section, String date, String url) {
        articleTitle = title;
        articleAuthor = author;
        articleSection = section;
        articleDate = date;
        apiUrl = url;
    }

    public String getArticleTitle() {
        return articleTitle;
    }
    
    public String getArticleSection() {
        return articleSection;
    }

    public String getArticleDate() {
        return articleDate;
    }

    public String getArticleUrl() {
        return apiUrl;
    }

    public String getArticleAuthor(){return articleAuthor;}
}
