package com.zok.art.zhihu.db;

import android.content.Context;
import android.util.Log;

import java.security.PrivateKey;

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
    private Realm mainRealm;

    public static void initRealm(Context appContext) {
        Realm.init(appContext);
        sConfig = new RealmConfiguration.Builder()
                .name(DB_NAME).build();
        getInstance();
    }

    public RealmManager() {
        mainRealm = Realm.getInstance(sConfig);
    }

    public void close() {
        mainRealm.close();
    }

    public static RealmManager getInstance() {
        if (instance == null) {
            synchronized (RealmManager.class) {
                if (instance == null)
                    instance = new RealmManager();
            }
        }
        return instance;
    }

    public boolean queryNewsReadState(long id) {
        RealmResults<ReadStateBean> results = mainRealm
                .where(ReadStateBean.class).equalTo("id", id).findAll();
        return results.size() > 0;
    }

    public void insertNewsReadState(long id) {
        mainRealm.beginTransaction();
        ReadStateBean bean = mainRealm.createObject(ReadStateBean.class);
        bean.setId(id);
        mainRealm.commitTransaction();
    }
}
