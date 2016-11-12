package com.zok.art.zhihu.api;

import com.zok.art.zhihu.bean.CommentListBean;
import com.zok.art.zhihu.bean.NewsDetailBean;
import com.zok.art.zhihu.bean.NewsExtraBean;
import com.zok.art.zhihu.bean.SectionListBean;
import com.zok.art.zhihu.bean.SectionNewsBean;
import com.zok.art.zhihu.bean.SplashBean;
import com.zok.art.zhihu.bean.StoriesBeforeBean;
import com.zok.art.zhihu.bean.StoriesLatestBean;
import com.zok.art.zhihu.bean.ThemeListBean;
import com.zok.art.zhihu.bean.ThemeNewsBean;

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
    /*知乎根API*/
    String BASE_URL = "http://news-at.zhihu.com/api/";

    /*bing每日图片地址*/
    String BIND_SPLASH_URL = "http://www.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1";

    /*获取知乎的闪屏图片*/
    @GET("4/start-image/1080*1776")
    Observable<SplashBean> getSplashImage();

    /*获得最新新闻*/
    @GET("4/news/latest")
    Observable<StoriesLatestBean> getLatestNews();
    //http://news-at.zhihu.com/api/4/news/latest

    /*获得新闻详情*/
    @GET("4/news/{id}")
    Observable<NewsDetailBean> getNewsDetails(@Path("id") long id);
    //http://news-at.zhihu.com/api/4/news/3892357

    /*获得以前的新闻*/
    @GET("4/news/before/{date}")
    Observable<StoriesBeforeBean> getBeforeNews(@Path("date") String date);
    //http://news.at.zhihu.com/api/4/news/before/20131119

    /*获得新闻额外信息：如评论，点赞信息*/
    @GET("4/story-extra/{id}")
    Observable<NewsExtraBean> getNewsExtra(@Path("id") long id);
    // http://news-at.zhihu.com/api/4/story-extra/{id}

    /*获得指定id的长评论*/
    @GET("4/story/{id}/long-comments")
    Observable<CommentListBean> getLongComment(@Path("id") long id);
    //http://news-at.zhihu.com/api/4/story/4232852/long-comments

    /*获得指定id的长评论*/
    @GET("4/story/{id}/long-comments/{userId}")
    Observable<CommentListBean> getLongCommentMore(@Path("id") long id, @Path("userId") long userId);
    //http://news-at.zhihu.com/api/4/story/4232852/long-comments

    /*获得指定id的短评*/
    @GET("4/story/{id}/short-comments")
    Observable<CommentListBean> getShortComment(@Path("id") long id);
    //http://news-at.zhihu.com/api/4/story/4232852/short-comments

    /*获得指定id的短评*/
    @GET("4/story/{id}/short-comments/before/{userId}")
    Observable<CommentListBean> getShortCommentMore(@Path("id") long id, @Path("userId") long userId);
    //http://news-at.zhihu.com/api/4/story/4232852/short-comments

    /*获得主题列表*/
    @GET("4/themes")
    Observable<ThemeListBean> getLatestThemes();
    //http://news-at.zhihu.com/api/4/themes

    /*获得指定主题的新闻列表*/
    @GET("4/theme/{id}")
    Observable<ThemeNewsBean> getSpecificTheme(@Path("id") long id);
    //http://news-at.zhihu.com/api/4/theme/11

    /*获得栏目列表*/
    @GET("3/sections")
    Observable<SectionListBean> getSectionList();
    //http://news-at.zhihu.com/api/3/sections

    /*获得指定栏目的新闻列表*/
    @GET("3/section/{id}")
    Observable<SectionNewsBean> getSectionNews(@Path("id") long id);
    //http://news-at.zhihu.com/api/3/section/1

    /*获得指定栏目之前的新闻*/
    @GET("4/section/{id}/before/{timestamp}")
    Observable<StoriesBeforeBean> getSectionNewsBefore(@Path("id") long id, @Path("timestamp") long date);
    //http://news-at.zhihu.com/api/4/section/#{section id}/before/#{timestamp}

    /*访问全路径API并返回原生数据*/
    @GET
    Observable<ResponseBody> getRaw(@Url String url);
}
