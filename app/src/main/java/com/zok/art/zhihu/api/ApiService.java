package com.zok.art.zhihu.api;

import com.zok.art.zhihu.bean.CommentListBean;
import com.zok.art.zhihu.bean.NewsDetailBean;
import com.zok.art.zhihu.bean.NewsExtraBean;
import com.zok.art.zhihu.bean.SectionListBean;
import com.zok.art.zhihu.bean.SplashBean;
import com.zok.art.zhihu.bean.StoriesBeforeBean;
import com.zok.art.zhihu.bean.StoriesLatestBean;
import com.zok.art.zhihu.bean.ThemeListBean;
import com.zok.art.zhihu.bean.ThemeNewsBean;
import com.zok.art.zhihu.bean.VersionBean;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Url;
import rx.Observable;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public interface ApiService {
    String BASE_URL = "http://news-at.zhihu.com/api/";

    @GET
    Observable<ResponseBody> getRaw(@Url String url);

    @GET("4/start-image/1080*1776")
    Observable<SplashBean> getSplash();//Splash page

    @GET("4/version/android/{curVersion}")
    Observable<VersionBean> latestVersion(@Path("curVersion") String curVersion);
    //http://news-at.zhihu.com/api/4/version/android/2.3.0

    @GET("4/news/latest")
    Observable<StoriesLatestBean> latestNewsStories();

    //http://news-at.zhihu.com/api/4/news/latest
    @GET("4/news/{id}")
    Observable<NewsDetailBean> newsDetails(@Path("id") long id);
    //http://news-at.zhihu.com/api/4/news/3892357

    @GET("4/news/before/{date}")
    Observable<StoriesBeforeBean> beforeNews(@Path("date") String date);
    //http://news.at.zhihu.com/api/4/news/before/20131119

    @GET("4/story-extra/{id}")
    Observable<NewsExtraBean> getNewsExtra(@Path("id") long id);
    // http://news-at.zhihu.com/api/4/story-extra/{id}

    @GET("4/story/{id}/long-comments")
    Observable<CommentListBean> getLongComment(@Path("id") long id);
    //http://news-at.zhihu.com/api/4/story/4232852/long-comments

    @GET("4/story/{id}/short-comments")
    Observable<CommentListBean> getShortComment(@Path("id") long id);
    //http://news-at.zhihu.com/api/4/story/4232852/short-comments

    @GET("4/themes")
    Observable<ThemeListBean> newestThemes();
    //http://news-at.zhihu.com/api/4/themes

    @GET("4/theme/{id}")
    Observable<ThemeNewsBean> getTheTheme(@Path("id") int id);
    //http://news-at.zhihu.com/api/4/theme/11

    @GET("3/sections")
    Observable<SectionListBean> getSections();
    //http://news-at.zhihu.com/api/3/sections

    //http://news-at.zhihu.com/api/3/section/1
    //http://news-at.zhihu.com/api/4/story/#{id}/recommenders
    //http://news-at.zhihu.com/api/4/section/#{section id}/before/#{timestamp}
    //http://news-at.zhihu.com/api/4/editor/#{id}/profile-page/android

    @GET
    Observable<ResponseBody> getData(@Url String url);
}
