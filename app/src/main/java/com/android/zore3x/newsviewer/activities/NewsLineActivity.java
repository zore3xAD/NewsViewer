package com.android.zore3x.newsviewer.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.zore3x.newsviewer.App;
import com.android.zore3x.newsviewer.R;
import com.android.zore3x.newsviewer.adapters.NewsRecyclerViewAdapter;
import com.android.zore3x.newsviewer.api.endpoints.TopHeadlinesEndpoint;
import com.android.zore3x.newsviewer.api.models.Response;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class NewsLineActivity extends AppCompatActivity {

    private int mPageSize = 3;

    private Response mResponse;

    private RecyclerView mNewsLineRecyclerView;
    private NewsRecyclerViewAdapter mAdapter;
    private TextView mShowMoreButton;
    private TextView mResultsCountTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_line);

        mResultsCountTextView = findViewById(R.id.resultsCount_textView);

        mShowMoreButton = findViewById(R.id.button_showMore);
        mShowMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPageSize += 3;
                if(mPageSize >= mResponse.getTotalResults()) {
                    mPageSize = mResponse.getTotalResults();
                    mShowMoreButton.setVisibility(View.GONE);
                }
                App.getTopHeadlinesEndpoint()
                        .getTopHeadlines(1, mPageSize, "ru", null, null)
                        .enqueue(new Callback<Response>() {

                            @Override
                            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                                mResponse = response.body();
                                update(mResponse);
                                mShowMoreButton.setVisibility(View.GONE);
                            }

                            @Override
                            public void onFailure(Call<Response> call, Throwable t) {

                            }
                        });
            }
        });

        mNewsLineRecyclerView = findViewById(R.id.newsLine_recyclerView);
        mNewsLineRecyclerView.setLayoutManager(new LinearLayoutManager(getApplication()));
        mNewsLineRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(mPageSize >= mResponse.getTotalResults()) {
                    mShowMoreButton.setVisibility(View.GONE);
                } else {
                    if (!recyclerView.canScrollVertically(1)) {
                        mShowMoreButton.setVisibility(View.VISIBLE);
                    } else {
                        mShowMoreButton.setVisibility(View.GONE);
                    }
                }
            }
        });

        App.getTopHeadlinesEndpoint()
                .getTopHeadlines(1, mPageSize, "ru", null, null)
                .enqueue(new Callback<Response>() {

            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                mResponse = response.body();
                update(mResponse);
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {

            }
        });
    }

    private void update(Response response) {
        if(mAdapter == null) {
            mAdapter = new NewsRecyclerViewAdapter(mResponse);
            mNewsLineRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setResponse(mResponse);
            mAdapter.notifyDataSetChanged();
        }
        mResultsCountTextView.setText(String.format("%d / %d",mPageSize, response.getTotalResults()));
    }
}
