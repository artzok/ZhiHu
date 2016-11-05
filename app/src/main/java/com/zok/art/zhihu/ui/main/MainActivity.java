package com.zok.art.zhihu.ui.main;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zok.art.zhihu.R;
import com.zok.art.zhihu.adapter.DrawerListAdapter;
import com.zok.art.zhihu.adapter.SectionsAdapter;
import com.zok.art.zhihu.base.BaseActivity;
import com.zok.art.zhihu.bean.SectionBean;
import com.zok.art.zhihu.bean.ThemeItemBean;
import com.zok.art.zhihu.config.Constants;
import com.zok.art.zhihu.ui.collected.CollectedActivity;
import com.zok.art.zhihu.ui.home.HomeFragment;
import com.zok.art.zhihu.ui.sections.SectionsActivity;
import com.zok.art.zhihu.utils.AppUtil;
import com.zok.art.zhihu.utils.ToastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity<MainContract.Presenter>
        implements MainContract.View, AdapterView.OnItemClickListener {

    private ActionBarDrawerToggle mToggle;

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

    private DrawerListAdapter mDrawerListAdapter;
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
        mDrawerListAdapter = new DrawerListAdapter(this);
        mLvLeftDrawerList.setAdapter(mDrawerListAdapter);
    }

    public void initEvent() {
        mLvLeftDrawerList.setOnItemClickListener(this);
    }

    @Override
    public void showError(String msg, Throwable e) {
        ToastUtil.show(this, msg + e.getMessage());
        log.d(msg + ":" + e.getMessage());

    }

    @Override
    public void updateTitle(String title) {
        mToolbar.setTitle(title);
    }

    @Override
    public void updateThemeList(List<ThemeItemBean> themes) {
        mDrawerListAdapter.setDataAndRefresh(themes);
        // go to theme page or home page after theme list has been updated
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
            mPresenter.switchHome();
        else
            mPresenter.switchTheme(position - 1);
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
            }
        }
        return addedFragment;
    }

    @Override
    public void onBackPressed() {

        // close left navigation
        if (mLeftDrawer.isDrawerOpen(Gravity.LEFT)) {
            mLeftDrawer.closeDrawer(Gravity.LEFT);
            return;
        }

        // back to the home fragment or finish the activity
        Fragment forehand = getForehand();
        if (forehand == null || forehand.getClass() == HomeFragment.class) {
            finish();
        } else {
            // mPresenter.switchHome();
            mLvLeftDrawerList.performItemClick(null, 0, 0);
        }
    }

    @OnClick({R.id.btn_sections, R.id.ll_left_drawer_favorites})
    public void onNavigateClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sections:
                Intent sections = new Intent(this, SectionsActivity.class);
                startActivity(sections);
                break;
            case R.id.ll_left_drawer_favorites:
            Intent favorites = new Intent(this, CollectedActivity.class);
            startActivity(favorites);
                break;
        }
    }
}
