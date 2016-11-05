package com.zok.art.zhihu.db;

import android.content.Context;

import com.zok.art.zhihu.bean.StoryListItemBean;
import com.zok.art.zhihu.db.bean.ReadStateBean;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public class RealmManager {
    private static final String DB_NAME = "zhihu.db";
    private static RealmConfiguration sConfig;
    private static RealmManager instance;
    private static int code;
    private Realm mainRealm;

    /**
     * 初始化Realm配置
     *
     * @param appContext
     */
    public static void initRealm(Context appContext) {
        Realm.init(appContext);
        sConfig = new RealmConfiguration.Builder()
                .name(DB_NAME).build();
        RealmManager instance = getInstance();
        code = instance.mainRealm.hashCode();
    }

    private RealmManager() {
        mainRealm = Realm.getInstance(sConfig);
    }

    /**
     * 获得主线程查询实例
     *
     * @return
     */
    public static RealmManager getInstance() {
        if (instance == null) {
            synchronized (RealmManager.class) {
                if (instance == null)
                    instance = new RealmManager();
            }
        }
        return instance;
    }

    /**
     * The async instance must call {@code close()} method release memory after operation.
     *
     * @return
     */
    public static RealmManager getAsyncInstance() {
        return new RealmManager();
    }

    /**
     * 关闭异步线程的Realm实例
     */
    public void close() {
        if (mainRealm.hashCode() == code) {
            throw new RuntimeException("主线程实例不能关闭");
        }
        mainRealm.close();
    }

    /**
     * 查询指定新闻是否已经阅读
     *
     * @param id
     * @return
     */
    public boolean isRead(long id) {
        RealmResults<ReadStateBean> results = mainRealm
                .where(ReadStateBean.class).equalTo("id", id)
                .equalTo("isRead", true).findAll();
        return results.size() > 0;
    }

    /**
     * 查询指定新闻是否已经被收藏
     *
     * @param id
     * @return
     */
    public boolean isCollected(long id) {
        RealmResults<ReadStateBean> results = mainRealm
                .where(ReadStateBean.class).equalTo("id", id)
                .equalTo("isCollected", true).findAll();
        return results.size() > 0;
    }

    /**
     * 查询指定新闻是否被点赞
     *
     * @param id
     * @return
     */
    public boolean isPraised(long id) {
        RealmResults<ReadStateBean> results = mainRealm
                .where(ReadStateBean.class).equalTo("id", id)
                .equalTo("isPraised", true).findAll();
        return results.size() > 0;
    }

    /**
     * 设置指定新闻的阅读状态
     *
     * @param story
     */
    public void setRead(ReadStateBean story) {
        ReadStateBean bean = queryReadState(story.getId());

        mainRealm.beginTransaction();
        if (bean != null) {
            bean.setRead(true);
            mainRealm.copyToRealm(bean);
        } else {
            story.setRead(true);
            mainRealm.copyToRealm(story);
        }
        mainRealm.commitTransaction();

    }

    public void setRead(StoryListItemBean bean) {
        setRead(getReadState(bean));
    }

    /**
     * 设置指定新闻的收藏状态
     *
     * @param story
     * @param collected
     */
    public void setCollected(ReadStateBean story, boolean collected) {
        ReadStateBean bean = queryReadState(story.getId());

        mainRealm.beginTransaction();
        if (bean != null) {
            bean.setCollected(collected);
            mainRealm.copyToRealm(bean);
        } else {
            story.setRead(true);
            story.setCollected(collected);
            mainRealm.copyToRealm(story);
        }
        mainRealm.commitTransaction();
    }

    public void setCollected(StoryListItemBean bean, boolean collected) {
        setCollected(getReadState(bean), collected);
    }

    /**
     * 设置指定新闻的点赞状态
     *
     * @param story
     * @param praised
     */
    public void setPraised(ReadStateBean story, boolean praised) {
        ReadStateBean bean = queryReadState(story.getId());

        mainRealm.beginTransaction();
        if (bean != null) {
            bean.setPraised(praised);
            mainRealm.copyToRealm(bean);
        } else {
            story.setRead(true);
            story.setPraised(praised);
            mainRealm.copyToRealm(story);
        }
        mainRealm.commitTransaction();
    }

    public void setPraised(StoryListItemBean bean, boolean praised) {
        setPraised(getReadState(bean), praised);
    }

    /**
     * 查询指定新闻的阅读状态
     *
     * @param id
     * @return
     */
    public ReadStateBean queryReadState(long id) {
        return mainRealm.where(ReadStateBean.class).equalTo("id", id).findFirst();
    }

    /**
     * 查询所有新闻的阅读状态
     *
     * @return
     */
    public List<ReadStateBean> queryAllReadState() {
        return mainRealm.where(ReadStateBean.class).findAll();
    }

    public List<ReadStateBean> queryAllCollected() {
        return mainRealm.where(ReadStateBean.class).equalTo("isCollected", true).findAll();
    }

    public static ReadStateBean getReadState(StoryListItemBean bean) {
        ReadStateBean state = new ReadStateBean();
        state.setTitle(bean.getTitle());
        state.setGa_prefix(bean.getGa_prefix());
        state.setId(bean.getId());
        state.setType(bean.getType());
        List<String> urls = bean.getImageUrls();
        if (urls != null && urls.size() > 0)
            state.setImageUrl(urls.get(0));
        return state;
    }

    public static StoryListItemBean getListItem(ReadStateBean state) {
        StoryListItemBean bean = new StoryListItemBean();
        bean.setId(state.getId());
        bean.setTitle(state.getTitle());
        bean.setGa_prefix(state.getGa_prefix());
        bean.setType(state.getType());
        ArrayList<String> imageUrls = new ArrayList<>();
        imageUrls.add(state.getImageUrl());
        bean.setImageUrls(imageUrls);
        return bean;
    }
}
