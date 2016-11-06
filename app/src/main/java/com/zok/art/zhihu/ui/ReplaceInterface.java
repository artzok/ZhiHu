package com.zok.art.zhihu.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public interface ReplaceInterface {

    Fragment toFragment();
    void setInitParams(Bundle bundle);
}
