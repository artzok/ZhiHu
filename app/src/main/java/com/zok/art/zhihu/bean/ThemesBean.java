package com.zok.art.zhihu.bean;

import java.util.List;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public class ThemesBean {
    public List<ThemeBean> others;

    public List<ThemeBean> getOthers() {
        return others;
    }

    public void setOthers(List<ThemeBean> others) {
        this.others = others;
    }

    @Override
    public String toString() {
        return "ThemesBean{" +
                "others=" + others +
                '}';
    }
}
