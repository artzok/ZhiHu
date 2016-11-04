package com.zok.art.zhihu.ui.main;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zok.art.zhihu.R;
import com.zok.art.zhihu.adapter.LeftDrawerAdapter;
import com.zok.art.zhihu.base.BaseActivity;
import com.zok.art.zhihu.bean.ThemeBean;
import com.zok.art.zhihu.config.Constants;
import com.zok.art.zhihu.ui.homepage.HomePageFragment;
import com.zok.art.zhihu.utils.AppUtil;
import com.zok.art.zhihu.utils.ToastUtil;

import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity<MainContract.Presenter>
        implements MainContract.View, AdapterView.OnItemClickListener {
    // 设置ActionBar
    private ActionBarDrawerToggle mToggle;
    /**
     * toolbar
     */
    @BindView(R.id.toolbar)
    public Toolbar mToolbar;

    @BindView(R.id.status_bar)
    public ImageView mStatusBar;

    @BindView(R.id.left_drawer)
    public DrawerLayout mLeftDrawer;

    @BindView(R.id.lv_left_drawer_list)
    public ListView mLvLeftDrawerList;          // 主题列表

    @BindView(R.id.iv_left_drawer_image)
    public ImageView mAccountImage;             // 头像

    @BindView(R.id.tv_left_drawer_login_tip)
    public TextView mLoginTips;                 // 请登录

    @BindView(R.id.ll_left_drawer_favorites)    // 我的收藏
    public LinearLayout mFavoritesBtn;

    @BindView(R.id.ll_left_drawer_download)
    public LinearLayout mDownloadBtn;           // 离线下载

    private LeftDrawerAdapter mLeftDrawerAdapter;
    private FragmentManager mManager;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void requestImmersion() {
        // 禁用沉浸式
    }

    @Override
    protected int getPermissionArrId() {
        return R.array.main_permissions;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_day_night);
        if (item != null) {
            boolean isNight = isNightMode();
            item.setTitle(isNight ? R.string.action_day : R.string.action_night);
            return true;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_day_night:
                switchNightMode();
                break;
            case android.R.id.home:
                mToggle.onOptionsItemSelected(item);
                break;
        }
        return true;
    }

    @Override
    protected void requestPermissionSucceed() {
        mManager = getSupportFragmentManager();
        initDecorate();
        initDrawer();
        initEvent();
        // load themes
        mPresenter.start();
    }

    private void initDecorate() {
        // Set height of status bar
        int height = AppUtil.getStatusHeight(this);
        mStatusBar.setMinimumHeight(height);
        mStatusBar.setMaxHeight(height);
        // 设置ToolBar
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
        // 设置toggle Button与DrawerLayout关联
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToggle = new ActionBarDrawerToggle(this, mLeftDrawer,
                R.string.open_drawer_desc, R.string.close_drawer_desc);
        mLeftDrawer.setDrawerListener(mToggle);
        mToggle.syncState();
    }

    public void initDrawer() {
        // left drawer head view
        View headView = getLayoutInflater().inflate(R.layout.left_drawer_head, null);
        mLvLeftDrawerList.addHeaderView(headView);
        mLeftDrawerAdapter = new LeftDrawerAdapter(this);
        mLvLeftDrawerList.setAdapter(mLeftDrawerAdapter);
    }

    public void initEvent() {
        mLvLeftDrawerList.setOnItemClickListener(this);
    }

    @Override
    public void showError(String msg, Throwable e) {
        ToastUtil.show(this, msg);
        log.d(msg + ":" + e.getMessage());
    }

    @Override
    public void updateActionBarTitle(String title) {
        mToolbar.setTitle(title);
    }

    @Override
    public void updateThemeList(List<ThemeBean> themes) {
        mLeftDrawerAdapter.setDataAndRefresh(themes);
        // go theme or home page after theme list has been updated
        Integer global = (Integer) AppUtil.getGlobal(Constants.LAST_SCAN_THEME, 0);
        mLvLeftDrawerList.performItemClick(null, global, 0);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // update theme list's select state
        mLvLeftDrawerList.setSelection(position);
        // set global flag variable that mark the last position of themes list
        AppUtil.putGlobal(Constants.LAST_SCAN_THEME, position);
        // close left drawer
        mLeftDrawer.closeDrawer(GravityCompat.START);
        // notify presenter
        if (position == 0)
            mPresenter.changedHome();
        else
            mPresenter.changedTheme(position - 1);
    }

    @Override
    public void replaceFragment(Fragment instance) {
        try {
            String hashCode = Integer.toString(instance.hashCode());

            Fragment addedFragment = getForehand();

            // start transaction
            FragmentTransaction transaction = mManager.beginTransaction();

            if (addedFragment != null) {
                if (Integer.toString(addedFragment.hashCode()).equals(hashCode)) {
                    transaction.commit();
                    return;
                }
                addedFragment.onPause();
                transaction.hide(addedFragment);
            }

            Fragment cachedFragment = mManager.findFragmentByTag(hashCode);
            if (cachedFragment == null) {
                cachedFragment = instance;
                transaction.add(R.id.fragment_container, cachedFragment, hashCode);
            } else {
                transaction.show(cachedFragment);
                cachedFragment.onResume();
            }
            transaction.addToBackStack(hashCode);
            transaction.commit();

            log.e("hash", "replace hash code:" + hashCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Fragment getForehand() {
        int count = mManager.getBackStackEntryCount();
        Fragment addedFragment = null;
        FragmentManager.BackStackEntry entryAt;
        if (count > 0) {
            entryAt = mManager.getBackStackEntryAt(count - 1);
            if (entryAt != null) {
                String hashCode = entryAt.getName();
                addedFragment = mManager.findFragmentByTag(hashCode);
                log.e("hash", "find hash code:" + hashCode);
            }
        }
        return addedFragment;
    }

    @Override
    public void onBackPressed() {
        Fragment forehand = getForehand();
        if (forehand == null || forehand.getClass() == HomePageFragment.class) {
            finish();
        } else {
            mPresenter.changedHome();
        }
    }


}
