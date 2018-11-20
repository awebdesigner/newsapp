package com.example.android.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<News>> {

    private static final String LOG_TAG = NewsLoader.class.getName();

    private String apiUrl;

    public NewsLoader(Context context, String url) {
        super(context);
        apiUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
    @Override
    public List<News> loadInBackground() {
        if (apiUrl == null) return null;
        
        List<News> newsArticles = QueryUtils.fetchNewsData(apiUrl);
        return newsArticles;
    }
}
