package com.example.android.pokenews;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.content.Loader;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final String NEWS_REQUEST_URL = "http://content.guardianapis.com/search?api-key=b1ee2550-7c50-40f1-8e12-e743b2ecf362";

    private NewsAdapter adapter;

    private static final int NEWS_LOADER_ID = 0;

    private TextView emptyTextView;

    private ProgressBar progressBar;

    //private boolean UserInfoLoaderInitialized = false;

    private SwipeRefreshLayout swipe;

    private NetworkInfo activeNetwork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView newsListView = (ListView) findViewById(R.id.list_item);
        adapter = new NewsAdapter(this, new ArrayList<News>());
        newsListView.setAdapter(adapter);
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                News newsItem = adapter.getItem(position);
                Uri newsUri = Uri.parse(newsItem.getUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);
                startActivity(websiteIntent);
            }
        });
        emptyTextView = (TextView) findViewById(R.id.empty_view);
        newsListView.setEmptyView(emptyTextView);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.INVISIBLE);

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            progressBar.setVisibility(View.VISIBLE);
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
            //UserInfoLoaderInitialized = true;
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            emptyTextView.setText(R.string.no_internet);
        }

        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //if (UserInfoLoaderInitialized) {
                    if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
                        LoaderManager loaderManager = getLoaderManager();
                        loaderManager.restartLoader(NEWS_LOADER_ID, null, MainActivity.this);
                    } else {
                        progressBar.setVisibility(View.INVISIBLE);
                        emptyTextView.setText(R.string.no_internet);
                    }
//                } else {
//                    if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
//                        LoaderManager loaderManager = getLoaderManager();
//                        loaderManager.initLoader(NEWS_LOADER_ID, null, MainActivity.this);
//                        UserInfoLoaderInitialized = true;
//                    } else {
//                        progressBar.setVisibility(View.INVISIBLE);
//                        emptyTextView.setText(R.string.no_internet);
//                    }
//                }
            }
        });
    }

    //inflate the menu, and respond when users click on our menu item
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //define corresponding method for Loader
    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String topic = sharedPrefs.getString(
                getString(R.string.settings_topic_key),
                getString(R.string.settings_topic_default));
        String startDate = sharedPrefs.getString(
                getString(R.string.settings_start_date_key),
                getString(R.string.settings_start_date_default)
        );
//        String endDate = sharedPrefs.getString(
//                getString(R.string.settings_end_date_key),
//                getString(R.string.settings_end_date_default)
//        );
        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );
        Uri baseUri = Uri.parse(NEWS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("q", topic);
        uriBuilder.appendQueryParameter("from-date", startDate);
        //uriBuilder.appendQueryParameter("to-date", endDate);
        uriBuilder.appendQueryParameter("order-by", orderBy);
        uriBuilder.appendQueryParameter("show-tags", "contributor");

        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {
        progressBar.setVisibility(View.INVISIBLE);
        swipe.setRefreshing(false);

        emptyTextView.setText(R.string.no_news);
        adapter.clear();
        if (news != null && !news.isEmpty()) {
            adapter.addAll(news);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        adapter.clear();
    }
}
