package com.android.zore3x.newsviewer.activities;

import android.opengl.Visibility;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
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

    private final int mPageStep = 3;

    // переменные необходимые для запроса
    private String mCountry = "ua"; // регион поиска
    private String mCategory = null; // категория поиска
    private String mSearch = null; // ключевое слово
    private int mPage = 1; // страница
    private int mPageSize = mPageStep; // количество записей на странице

    // ответ от сервера
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
                mPageSize += mPageStep;
                if(mPageSize >= mResponse.getTotalResults()) {
                    mPageSize = mResponse.getTotalResults();
                    mShowMoreButton.setVisibility(View.GONE);
                }

                loadMore();
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

        loadMore();

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

    // функция подгрузки новостей
    private void loadMore() {

        App.getTopHeadlinesEndpoint()
                .getTopHeadlines(mPage, mPageSize, mCountry, mSearch, mCategory)
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

    private void search(String search) {
        mSearch = search;

        App.getTopHeadlinesEndpoint()
                .searchTopHeadlines(mPage, mPageSize, mSearch)
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_news_line, menu);


        MenuItem searchItem = menu.findItem(R.id.app_bar_search);
        final SearchView searchView = (SearchView)searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                mPageSize = mPageStep;
                search(s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}
