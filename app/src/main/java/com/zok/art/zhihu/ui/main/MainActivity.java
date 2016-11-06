package com.zok.art.zhihu.ui.main;

import android.content.Intent;
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
import com.zok.art.zhihu.adapter.DrawerListAdapter;
import com.zok.art.zhihu.base.BaseActivity;
import com.zok.art.zhihu.base.BaseFragment;
import com.zok.art.zhihu.base.BaseFragmentContract;
import com.zok.art.zhihu.bean.SectionBean;
import com.zok.art.zhihu.bean.ThemeItemBean;
import com.zok.art.zhihu.config.Constants;
import com.zok.art.zhihu.ui.collected.CollectedActivity;
import com.zok.art.zhihu.ui.home.HomeFragment;
import com.zok.art.zhihu.utils.AppUtil;

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
    private FragmentManager mFragmentManager;


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
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
        int id = item.getItemId();
        // 夜景切换
        if (id == R.id.action_day_night) {
            switchNightMode();
        }
        // 侧边导航显示
        else if (id == android.R.id.home) {
            mToggle.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    protected void requestPermissionSucceed() {

        // 获得Fragment管理器
        mFragmentManager = getSupportFragmentManager();

        // 初始化窗口装饰
        initDecorate();

        // 初始化侧边导航
        initDrawer();

        // 设置监听器
        setListener();

        // 启动PRESENTER层逻辑
        mPresenter.start();
    }


    private void initDecorate() {
        // 设置状态栏高度
        int height = AppUtil.getStatusHeight(this);
        mStatusBar.setMinimumHeight(height);
        mStatusBar.setMaxHeight(height);

        // 设置ActionBar
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);

        // toggle与DrawerLayout关联
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToggle = new ActionBarDrawerToggle(this, mLeftDrawer,
                R.string.open_drawer_desc, R.string.close_drawer_desc);
        mLeftDrawer.setDrawerListener(mToggle);
        mToggle.syncState();
    }

    private void initDrawer() {
        // 为侧边栏添加头部
        View headView = getLayoutInflater().inflate(R.layout.left_drawer_head, null);
        mLvLeftDrawerList.addHeaderView(headView);

        // 为导航列表设置适配器
        mDrawerListAdapter = new DrawerListAdapter(this);
        mLvLeftDrawerList.setAdapter(mDrawerListAdapter);
    }

    private void setListener() {
        // 为侧边导航列表设置适配器
        mLvLeftDrawerList.setOnItemClickListener(this);
    }

    @Override
    public void updateTitle(String title) {
        mToolbar.setTitle(title);
    }

    @Override
    public void updateThemeList(List<ThemeItemBean> themes) {
        // 刷新主题列表
        mDrawerListAdapter.setDataAndRefresh(themes);

        // 根据全局参数替换Fragment
        Integer page = (Integer) AppUtil.getGlobal(Constants.LAST_SCAN_THEME, 0);
        if (page != -1) {
            mLvLeftDrawerList.performItemClick(null, page, 0);
        } else {
            goSections();
        }
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
    public void replaceFragment(BaseFragment instance) {
        try {
            String hashCode = Integer.toString(instance.hashCode());

            Fragment addedFragment = getForehand();

            // start transaction
            FragmentTransaction transaction = mFragmentManager.beginTransaction();

            if (addedFragment != null) {
                ((BaseFragment)addedFragment).stopUpdate();
                transaction.hide(addedFragment);
            }

            Fragment cachedFragment = mFragmentManager.findFragmentByTag(hashCode);
            if (cachedFragment == null) {
                cachedFragment = instance;
                transaction.add(R.id.fragment_container, cachedFragment, hashCode);
            } else {
                transaction.show(cachedFragment);
                ((BaseFragmentContract.View)cachedFragment).reStartUpdate();
            }
            transaction.addToBackStack(hashCode);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private Fragment getForehand() {
        int count = mFragmentManager.getBackStackEntryCount();
        Fragment addedFragment = null;
        FragmentManager.BackStackEntry entryAt;
        if (count > 0) {
            entryAt = mFragmentManager.getBackStackEntryAt(count - 1);
            if (entryAt != null) {
                String hashCode = entryAt.getName();
                addedFragment = mFragmentManager.findFragmentByTag(hashCode);
            }
        }
        return addedFragment;
    }

    @Override
    public void onBackPressed() {

        // close left navigation
        if (mLeftDrawer.isDrawerOpen(GravityCompat.START)) {
            mLeftDrawer.closeDrawer(GravityCompat.START);
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
                goSections();
                break;
            case R.id.ll_left_drawer_favorites:
                Intent favorites = new Intent(this, CollectedActivity.class);
                startActivity(favorites);
                break;
        }
    }

    private void goSections() {
        // close left drawer
        mLeftDrawer.closeDrawer(GravityCompat.START);
        // set global flag variable that mark the last position of themes list
        AppUtil.putGlobal(Constants.LAST_SCAN_THEME, -1);
        // switch sections
        mPresenter.switchSections();
    }

    public void goSection(SectionBean bean) {
        // close left drawer
        mLeftDrawer.closeDrawer(GravityCompat.START);
        // set global flag variable that mark the last position of themes list
        AppUtil.putGlobal(Constants.LAST_SCAN_THEME, -1);
        // switch sections
        mPresenter.switchSection(bean);
    }
}
