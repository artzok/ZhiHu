package com.zok.art.zhihu.ui.detail;

import com.zok.art.zhihu.base.BasePresenter;
import com.zok.art.zhihu.base.BaseView;
import com.zok.art.zhihu.bean.BasicStoryBean;
import com.zok.art.zhihu.bean.NewsExtraBean;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public interface DetailContract {
    interface View extends BaseView {
        /**
         * 显示加载进度条，关闭进度条由WebView实现
         */
        void showProgress();

        /**
         * 更新头部图片
         *
         * @param url 图片url
         */
        void updateHeaderImage(String url);

        /**
         * 更新头部标题
         *
         * @param title 标题字符串
         */
        void updateTitle(String title);

        /**
         * 更新头部图片版权信息
         *
         * @param copyRight 版权信息
         */
        void updateCopyRight(String copyRight);

        /**
         * 更新WebView正文内容
         *
         * @param html
         */
        void updateWebView(String html);

        /**
         * 更新额外信息：如评论数，点赞数
         *
         * @param bean
         */
        void updateExtra(NewsExtraBean bean);

    }

    interface Presenter extends BasePresenter<View> {
        /**
         * 返回新闻信息
         *
         * @return
         */
        BasicStoryBean getStoryBean();

        /**
         * 返回新闻额外信息
         *
         * @return
         */
        NewsExtraBean getNewsExtraInfo();

        boolean isCollected();

        boolean isPraised();

        void setCollected(boolean collected);

        void setPraised(boolean praised);
    }

}
