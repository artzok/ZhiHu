package com.zok.art.zhihu.ui.write_comment;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.zok.art.zhihu.R;
import com.zok.art.zhihu.base.BaseActivity;
import com.zok.art.zhihu.bean.CommentItemBean;
import com.zok.art.zhihu.config.Constants;
import com.zok.art.zhihu.utils.AppUtil;
import com.zok.art.zhihu.utils.ToastUtil;

import butterknife.BindView;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public class WriteCommentActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    public Toolbar mToolbar;
    private CommentItemBean mExtra;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_write_comment; // 布局ID
    }

    @Override
    protected void requestPermissionSucceed() {
        // 代码起点
        Intent intent = getIntent();
        mExtra = intent.getParcelableExtra(Constants.EXTRA_INIT_PARAMS);
        String title = mExtra == null ? "写评论" : "回复:" + mExtra.getAuthor();
        setToolBar(mToolbar, title, true);
        // 林华清 开始
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_write_comment, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.send_comment) {
            ToastUtil.show(this, "send comment succeed");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
