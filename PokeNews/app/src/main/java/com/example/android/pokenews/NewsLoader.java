package com.example.android.pokenews;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by Roy Li on 15/12/2017.
 */

public class NewsLoader extends AsyncTaskLoader<List<News>> {

    private static final String LOG_TAG = NewsLoader.class.getSimpleName();

    private String Url;

    public NewsLoader (Context context,String url) {
        super(context);
        Url = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<News> loadInBackground() {
        if (Url == null) {
            return null;
        }

        List<News> news = QueryUtils.extractNews(Url);
        return news;
    }

}
