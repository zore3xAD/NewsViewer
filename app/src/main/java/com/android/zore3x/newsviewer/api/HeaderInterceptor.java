package com.android.zore3x.newsviewer.api;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HeaderInterceptor implements Interceptor {

    private String mXApiKey;

    public HeaderInterceptor(String XApiKey) {
        mXApiKey = XApiKey;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        request = request.newBuilder()
                .addHeader("Authorization", "X-Api-Key " + mXApiKey)
                .build();

        return chain.proceed(request);
    }
}
