package com.android.zore3x.newsviewer.api.endpoints;

import android.support.annotation.Nullable;

import com.android.zore3x.newsviewer.api.models.Response;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 *
 * returns breaking news headlines for a country and category,
 * or currently running on a single or multiple sources.
 * This is perfect for use with news tickers
 * or anywhere you want to display live up-to-date news headlines and images
 *
 */

public interface TopHeadlinesEndpoint {

    @GET("/v2/top-headlines")
    Call<Response> getTopHeadlines(@Query("page") int page, @Query("pageSize") int pageSize,
                                   @Query("country") String country, @Query("q") @Nullable String q,
                                   @Query("category") @Nullable String category);

    @GET("/v2/top-headlines")
    Call<Response> searchTopHeadlines(@Query("page") int page, @Query("pageSize") int pageSize,
                                      @Query("q") String q);


}
