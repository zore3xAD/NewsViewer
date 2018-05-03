package com.android.zore3x.newsviewer;

import android.app.Application;

import com.android.zore3x.newsviewer.api.HeaderInterceptor;
import com.android.zore3x.newsviewer.api.endpoints.TopHeadlinesEndpoint;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {

    private String mApiKey = "414de4e48911427ea31809811dbdfbbc";

    private static TopHeadlinesEndpoint mTopHeadlinesEndpoint;
    Retrofit mRetrofit;

    @Override
    public void onCreate() {
        super.onCreate();

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new HeaderInterceptor(mApiKey)).build();

        mRetrofit = new Retrofit.Builder()
                .baseUrl("https://newsapi.org")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mTopHeadlinesEndpoint = mRetrofit.create(TopHeadlinesEndpoint.class);

    }

    public static TopHeadlinesEndpoint getTopHeadlinesEndpoint() {
        return mTopHeadlinesEndpoint;
    }
}
