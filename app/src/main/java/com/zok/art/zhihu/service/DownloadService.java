package com.zok.art.zhihu.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.squareup.picasso.Picasso;
import com.zok.art.zhihu.R;
import com.zok.art.zhihu.api.ApiManager;
import com.zok.art.zhihu.api.ApiService;
import com.zok.art.zhihu.bean.NewsDetailBean;
import com.zok.art.zhihu.bean.StoriesLatestBean;
import com.zok.art.zhihu.bean.StoryListItemBean;
import com.zok.art.zhihu.utils.AppUtil;
import com.zok.art.zhihu.utils.StringUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public class DownloadService extends IntentService {

    private ApiService mApiService;

    private int mCurrProgress;
    private int mMaxProgress;
    private NotificationCompat.Builder mNotifyBuilder;
    private NotificationManager mNotifyManager;
    private String notifyMessage;

    public DownloadService() {
        super("download latest news !");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mApiService = ApiManager.getApiService();
        mNotifyManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notifyMessage = "正在下载最新内容%d";
        mNotifyBuilder = new NotificationCompat
                .Builder(getApplicationContext())
                .setSmallIcon(R.drawable.ic_logo)
                .setContentTitle("正在下载")
                .setContentText(String.format(notifyMessage, 0))
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        Observable<StoriesLatestBean> latestNews = mApiService.getLatestNews();
        latestNews.observeOn(Schedulers.immediate())
                .filter(new Func1<StoriesLatestBean, Boolean>() {
                    @Override
                    public Boolean call(StoriesLatestBean storiesLatestBean) {
                        if (storiesLatestBean == null) return false;
                        List<StoryListItemBean> listStories = storiesLatestBean.getListStories();
                        return listStories != null && listStories.size() > 0;
                    }
                })
                .flatMap(new Func1<StoriesLatestBean, Observable<StoryListItemBean>>() {
                    @Override
                    public Observable<StoryListItemBean> call(StoriesLatestBean storiesLatestBean) {
                        // update size
                        mCurrProgress = 0;
                        mMaxProgress = storiesLatestBean.getListStories().size();
                        return Observable.from(storiesLatestBean.getListStories());
                    }
                })
                .flatMap(new Func1<StoryListItemBean, Observable<NewsDetailBean>>() {
                    @Override
                    public Observable<NewsDetailBean> call(StoryListItemBean storyListItemBean) {
                        return mApiService.getNewsDetails(storyListItemBean.getId());
                    }
                })
                .subscribe(new Action1<NewsDetailBean>() {
                    @Override
                    public void call(NewsDetailBean bean) {
                        Picasso picasso = Picasso.with(DownloadService.this);
                        // download author icon
                        picasso.load(bean.getImageAuthor()).fetch();
                        // download large image
                        picasso.load(bean.getLagerImageUrl()).fetch();
                        // download images
                        for (String url : bean.getImages()) {
                            picasso.load(url).fetch();
                        }
                        // download css
                        List<String> css = bean.getCss();
                        if (css != null) {
                            for (String cs : css) {
                                final File cssFile = AppUtil.getCacheFile(StringUtil.MD5(cs));
                                mApiService.getRaw(cs).subscribe(new Action1<ResponseBody>() {
                                    @Override
                                    public void call(ResponseBody responseBody) {
                                        try {
                                            String string = responseBody.string();
                                            StringUtil.writeFileFromString(cssFile, string, false);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new Action1<Throwable>() {
                                    @Override
                                    public void call(Throwable throwable) {
                                        Log.d("tag", "load css error!");
                                        throwable.printStackTrace();
                                    }
                                });
                            }
                        }
                        // update notify
                        mNotifyBuilder
                                .setProgress(mMaxProgress, mCurrProgress++, false)
                                .setContentText(String.format(notifyMessage, mCurrProgress))
                                .setNumber(mMaxProgress);
                        mNotifyManager.notify(0, mNotifyBuilder.build());

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.d("tag", "load cache error!");
                        throwable.printStackTrace();
                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        mNotifyManager.cancel(0);
                        Log.d("tag", "download finish!");
                    }
                });
    }
}
