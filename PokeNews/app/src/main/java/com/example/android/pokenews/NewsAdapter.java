package com.example.android.pokenews;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Roy Li on 15/12/2017.
 */

public class NewsAdapter extends ArrayAdapter<News> {

    private static final String LOG_TAG = NewsAdapter.class.getSimpleName();

    public NewsAdapter(Activity context, ArrayList<News> news) {
        super(context, 0, news);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        News currentNews = getItem(position);
        String titleNews = currentNews.getTitle();
        String authorNews = currentNews.getAuthor();
        if (authorNews == null) {
            authorNews = "......";
        }
        String dateNewsB = currentNews.getDate();
        String[] dateSplit = dateNewsB.split("T");
        String dateNews = dateSplit[0];
        String sourceNews = currentNews.getSource();

        TextView title = listItemView.findViewById(R.id.title);
        TextView author = listItemView.findViewById(R.id.author);
        TextView date = listItemView.findViewById(R.id.date);
        TextView source = listItemView.findViewById(R.id.source);
        title.setText(titleNews);
        author.setText(authorNews);
        date.setText(dateNews);
        source.setText(sourceNews);

        return listItemView;
    }
}
