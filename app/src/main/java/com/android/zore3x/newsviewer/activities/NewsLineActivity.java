package com.android.zore3x.newsviewer.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.zore3x.newsviewer.App;
import com.android.zore3x.newsviewer.R;
import com.android.zore3x.newsviewer.api.endpoints.TopHeadlinesEndpoint;
import com.android.zore3x.newsviewer.api.models.Response;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class NewsLineActivity extends AppCompatActivity {

    private Response mResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_line);

        App.getTopHeadlinesEndpoint()
                .getTopHeadlines(1, 20, "ru", null, null)
                .enqueue(new Callback<Response>() {

            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                mResponse = response.body();
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {

            }
        });

    }
}
