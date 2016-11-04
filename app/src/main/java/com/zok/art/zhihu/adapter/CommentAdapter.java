package com.zok.art.zhihu.adapter;

import android.animation.Animator;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zok.art.zhihu.R;
import com.zok.art.zhihu.bean.CommentBean;
import com.zok.art.zhihu.utils.DateUtil;

import java.util.Locale;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public class CommentAdapter extends BaseListAdapter<CommentBean> implements Animator.AnimatorListener {
    public static final int ITEM_TYPE_LONG = 1;         // flag for long comment title
    public static final int ITEM_TYPE_SHORT = 2;        // flag for short comment title

    private int longCount;              // long comment count
    private int shortCount;             // short comment count
    private OtherViewHolder longHolder; // long comment holder
    private OtherViewHolder shortHolder;// short comment holder

    private boolean isFinish;

    public CommentAdapter(Context context) {
        super(context);
        isFinish = true;
    }

    public int getLongCount() {
        return longCount;
    }

    public void setLongCount(int longCount) {
        this.longCount = longCount;
    }

    public void setShortCount(int shortCount) {
        this.shortCount = shortCount;
    }

    @Override
    protected int getItemLayoutId(int itemViewType) {
        if (itemViewType == ITEM_TYPE_NORMAL) {
            return R.layout.comment_item;
        } else {
            return R.layout.comment_title;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return ITEM_TYPE_LONG;
        else if (position == longCount + 1)
            return ITEM_TYPE_SHORT;
        return ITEM_TYPE_NORMAL;
    }

    @Override
    protected BaseViewHolder createViewHolder(int itemViewType, View itemView) {
        switch (itemViewType) {
            case ITEM_TYPE_NORMAL:
                return new NormalViewHolder(itemView);
            case ITEM_TYPE_LONG:
                if (longHolder == null) {       // avoid recreate view holder when dataSet refresh
                    longHolder = new OtherViewHolder(itemView);
                }
                return longHolder;
            case ITEM_TYPE_SHORT:
                if (shortHolder == null) {      // avoid recreate view holder when dataSet refresh
                    shortHolder = new OtherViewHolder(itemView);
                }
                return shortHolder;
        }
        return null;
    }

    public void animate() {
        ViewPropertyAnimator animator = shortHolder.mFold.animate().rotationBy(180);
        animator.setListener(this);
        animator.start();
    }

    public boolean isFinishOfAnimation() {
        return isFinish;
    }

    @Override
    public void onAnimationStart(Animator animation) {
        isFinish = false;
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        isFinish = true;
    }

    @Override
    public void onAnimationCancel(Animator animation) {
    }

    @Override
    public void onAnimationRepeat(Animator animation) {
    }

    class NormalViewHolder extends BaseViewHolder {
        @BindView(R.id.iv_user_image)
        CircleImageView userIcon;

        @BindView(R.id.tv_user_name)
        TextView userName;

        @BindView(R.id.tv_voted_count)
        TextView voteCount;

        @BindView(R.id.tv_comment_count)
        TextView commentContent;

        @BindView(R.id.tv_comment_time)
        TextView commentTime;

        NormalViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void fillData(CommentBean data, int position) {
            // set name
            userName.setText(data.getAuthor());
            // set comment comment
            commentContent.setText(data.getContent());
            // set vote count
            voteCount.setText(String.valueOf(data.getLikes()));
            // set time of comment
            commentTime.setText(DateUtil.formatTime(data.getTime()));
            // load icon of user
            Picasso.with(getContext()).load(data.getAvatar()).into(userIcon);
        }
    }

    class OtherViewHolder extends BaseViewHolder {
        @BindView(R.id.tv_title)
        TextView mTitle;

        @BindView(R.id.iv_fold)
        ImageView mFold;

        @BindView(R.id.iv_empty)
        ImageView mEmpty;

        OtherViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void fillData(CommentBean data, int position) {
            int type = getItemViewType(position);
            if (type == ITEM_TYPE_LONG) {
                if (longCount == 0) {
                    mEmpty.setVisibility(View.VISIBLE); // show empty image
                }
                mFold.setVisibility(View.GONE);         // no display fold icon
                mTitle.setText(String.format(Locale.CHINA, "%d条长评", longCount));
            } else if (type == ITEM_TYPE_SHORT) {       // no action more
                mTitle.setText(String.format(Locale.CHINA, "%d条短评", shortCount));
                Log.d("tag", mTitle.toString());
            }
        }
    }
}
