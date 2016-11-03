package com.zok.art.zhihu.db;

import io.realm.RealmObject;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public class ReadStateBean extends RealmObject {
    private long id;    // new id

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
