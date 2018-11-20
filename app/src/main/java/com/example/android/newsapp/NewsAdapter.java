package com.example.android.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {

    public NewsAdapter(Context context, List<News> news) {
        super(context, 0, news);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        News currentNewsArticle = getItem(position);

        TextView title = listItemView.findViewById(R.id.article_title);
        title.setText(currentNewsArticle.getArticleTitle());

        TextView section = listItemView.findViewById(R.id.article_section);
        section.setText(currentNewsArticle.getArticleSection());

        String unformattedDate = currentNewsArticle.getArticleDate();
        String substringDate = unformattedDate.substring(0,16);
        String dateFormatted = substringDate.replace("-", "/");
        dateFormatted = dateFormatted.replace("T"," ");

        TextView date = listItemView.findViewById(R.id.article_date);
        date.setText(dateFormatted);

        TextView author = listItemView.findViewById(R.id.article_author);
        author.setText(currentNewsArticle.getArticleAuthor());

        return listItemView;
    }
}
