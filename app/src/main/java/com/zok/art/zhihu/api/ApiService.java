package com.zok.art.zhihu.api;

import com.zok.art.zhihu.bean.BeforeStoryBean;
import com.zok.art.zhihu.bean.CommentsBean;
import com.zok.art.zhihu.bean.NewsExtraBean;
import com.zok.art.zhihu.bean.ThemeNewsBean;
import com.zok.art.zhihu.bean.ThemesBean;
import com.zok.art.zhihu.bean.LatestStoriesBean;
import com.zok.art.zhihu.bean.NewsDetailBean;
import com.zok.art.zhihu.bean.SplashBean;
import com.zok.art.zhihu.bean.VersionBean;

import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public interface ApiService {
    String BASE_URL = "http://news-at.zhihu.com/api/4/";

    @GET
    Observable<ResponseBody> getRaw(@Url String url);

    @GET("start-image/1080*1776")
    Observable<SplashBean> getSplash();//Splash page

    @GET("version/android/{curVersion}")
    Observable<VersionBean> latestVersion(@Path("curVersion") String curVersion);
    //http://news-at.zhihu.com/api/4/version/android/2.3.0

    @GET("news/latest")
    Observable<LatestStoriesBean> latestNewsStories();
    //http://news-at.zhihu.com/api/4/news/latest
    @GET("news/{id}")
    Observable<NewsDetailBean> newsDetails(@Path("id") long id);
    //http://news-at.zhihu.com/api/4/news/3892357

    @GET("news/before/{date}")
    Observable<BeforeStoryBean> beforeNews(@Path("date") String date);
    //http://news.at.zhihu.com/api/4/news/before/20131119

    @GET("story-extra/{id}")
    Observable<NewsExtraBean> getNewsExtra(@Path("id") long id);
    // http://news-at.zhihu.com/api/4/story-extra/{id}

    @GET("story/{id}/long-comments")
    Observable<CommentsBean> getLongComment(@Path("id") long id);
    //http://news-at.zhihu.com/api/4/story/4232852/long-comments

    @GET("story/{id}/short-comments")
    Observable<CommentsBean> getShortComment(@Path("id") long id);
    //http://news-at.zhihu.com/api/4/story/4232852/short-comments

    @GET("themes")
    Observable<ThemesBean> newestThemes();
    //http://news-at.zhihu.com/api/4/themes

    //http://news-at.zhihu.com/api/4/theme/11
    @GET("theme/{id}")
    Observable<ThemeNewsBean> getTheTheme(@Path("id") int id);
    //http://news-at.zhihu.com/api/3/news/hot
    //http://news-at.zhihu.com/api/3/promotion/android
    //http://news-at.zhihu.com/api/3/sections

    //http://news-at.zhihu.com/api/3/section/1
    //http://news-at.zhihu.com/api/4/story/#{id}/recommenders
    //http://news-at.zhihu.com/api/4/section/#{section id}/before/#{timestamp}
    //http://news-at.zhihu.com/api/4/editor/#{id}/profile-page/android

    @GET
    Observable<ResponseBody> getData(@Url String url);
}
