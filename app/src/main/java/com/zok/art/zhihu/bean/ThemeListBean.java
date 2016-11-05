package com.zok.art.zhihu.bean;

import java.util.List;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public class ThemeListBean {
    public List<ThemeItemBean> others;

    public List<ThemeItemBean> getOthers() {
        return others;
    }

    public void setOthers(List<ThemeItemBean> others) {
        this.others = others;
    }

    @Override
    public String toString() {
        return "ThemeListBean{" +
                "others=" + others +
                '}';
    }
}
