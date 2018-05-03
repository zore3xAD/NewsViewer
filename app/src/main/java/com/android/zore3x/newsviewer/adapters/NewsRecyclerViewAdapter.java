package com.android.zore3x.newsviewer.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.zore3x.newsviewer.R;
import com.android.zore3x.newsviewer.api.models.Article;
import com.android.zore3x.newsviewer.api.models.Response;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsRecyclerViewAdapter extends RecyclerView.Adapter<NewsRecyclerViewAdapter.ViewHolder> {


    private Response mResponse;

    public  NewsRecyclerViewAdapter(Response response) {
        mResponse = response;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news_line, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.bind(mResponse.getArticles().get(position));

    }

    @Override
    public int getItemCount() {
        return mResponse.getArticles().size();
    }

    public void setResponse(Response response) {
        mResponse = response;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final ImageView mNewsImageView;
        public final TextView mNewsTitleTextView;
        public final TextView mNewsDescriptionTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            mNewsImageView = itemView.findViewById(R.id.itemImage_imageView);
            mNewsDescriptionTextView = itemView.findViewById(R.id.itemDescription_textView);
            mNewsTitleTextView = itemView.findViewById(R.id.itemTitle_textView);
        }

        public void bind(Article article) {

            mNewsTitleTextView.setText(article.getTitle());
            mNewsDescriptionTextView.setText(article.getDescription());
            Picasso.get().load(article.getUrlToImage()).into(mNewsImageView);

        }
    }
}
