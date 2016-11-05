package com.zok.art.zhihu.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zok.art.zhihu.config.Constants;
import com.zok.art.zhihu.utils.AppUtil;
import com.zok.art.zhihu.utils.NetWorkUtil;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public class ApiManager {

    private Retrofit mRetrofit;
    private static ApiManager instance;

    private static ApiManager getInstance() {
        if (instance == null) {
            instance = new ApiManager();
        }
        return instance;
    }

    private ApiManager() {
        OkHttpClient httpClient = getClientBuilder().build();
        Gson gson = getGsonBuilder().create();

        // create unique instance
        mRetrofit = new Retrofit.Builder()
                .baseUrl(ApiService.BASE_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    public static ApiService getApiService() {
        return getInstance().mRetrofit.create(ApiService.class);
    }

    private OkHttpClient.Builder getClientBuilder() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        // logging
        if (Constants.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(loggingInterceptor);
        }

        // http cache
        File cacheFile = new File(AppUtil.getCacheDir() + File.pathSeparator + "httpCache");
        builder.cache(new Cache(cacheFile, 10 * 1024 * 1024));// 10MB

        // cache control
        CacheInterceptor interceptor = new CacheInterceptor();
        builder.addNetworkInterceptor(interceptor);
        builder.addInterceptor(interceptor);

        // time out
        builder.connectTimeout(20, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(15, TimeUnit.SECONDS);

        // retry mechanism
        builder.retryOnConnectionFailure(true);
        return builder;
    }

    private GsonBuilder getGsonBuilder() {
        return new GsonBuilder()
                .setDateFormat("yyyyMMdd")
                .disableInnerClassSerialization()
                .generateNonExecutableJson()
                .disableHtmlEscaping()
                .setPrettyPrinting();
    }

    private class CacheInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            // get request
            Request request = chain.request();

            // build request of force cache
            Request cacheRequest = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();

            Response response;
            if (!NetWorkUtil.isNetWorkAvailable(AppUtil.getAppContext())) {
                response = chain.proceed(cacheRequest);
            } else {
                response = chain.proceed(request);
            }

            // cache mechanism
            if (NetWorkUtil.isNetWorkAvailable(AppUtil.getAppContext())) {
                int maxAge = 60 * 10;
                response.newBuilder()
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .removeHeader("Pragma")
                        .build();
            } else {
                int maxStale = 60 * 60 * 24 * 28;
                response.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .removeHeader("Pragma")
                        .build();
            }
            return response;
        }
    }
}
