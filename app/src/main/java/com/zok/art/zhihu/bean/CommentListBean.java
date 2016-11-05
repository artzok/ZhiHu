package com.zok.art.zhihu.bean;

import java.util.List;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public class CommentListBean {

    private List<CommentItemBean> comments;

    public List<CommentItemBean> getComments() {
        return comments;
    }

    public void setComments(List<CommentItemBean> comments) {
        this.comments = comments;
    }
}
