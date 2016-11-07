package com.zok.art.zhihu.ui.main;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zok.art.zhihu.R;
import com.zok.art.zhihu.base.BaseActivity;
import com.zok.art.zhihu.base.BaseFragment;
import com.zok.art.zhihu.base.BaseFragmentContract;
import com.zok.art.zhihu.bean.SectionBean;
import com.zok.art.zhihu.bean.ThemeItemBean;
import com.zok.art.zhihu.ui.collected.CollectedActivity;
import com.zok.art.zhihu.ui.home.HomeFragment;
import com.zok.art.zhihu.utils.AppUtil;

import butterknife.BindView;

public class MainActivity extends BaseActivity<MainContract.Presenter>
        implements MainContract.View, NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private ActionBarDrawerToggle mToggle;

    @BindView(R.id.toolbar)
    public Toolbar mToolbar;

    @BindView(R.id.status_bar)
    public ImageView mStatusBar;

    @BindView(R.id.left_drawer)
    public DrawerLayout mLeftDrawer;

    @BindView(R.id.nav_menus)
    public NavigationView mNavigationView;

    public ImageView mUserImage;        // 用户头像

    public TextView mLoginTips;

    private FragmentManager mFragmentManager;
    private MenuItem mNightMenuItem;
    private SwitchCompat mNightModeSwitch;


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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
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
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
        mToggle = new ActionBarDrawerToggle(this, mLeftDrawer, R.string.open_drawer_desc, R.string.close_drawer_desc);
        mLeftDrawer.setDrawerListener(mToggle);
        mToggle.syncState();
    }

    private void initDrawer() {
        // 登录部分控件
        View view = mNavigationView.getHeaderView(0);
        mUserImage = (ImageView) view.findViewById(R.id.user_image);
        mLoginTips = (TextView) view.findViewById(R.id.login_tips);

        // 导航监听
        mNavigationView.setNavigationItemSelectedListener(this);

        // 夜景模式菜单
        mNightMenuItem = mNavigationView.getMenu().findItem(R.id.nav_night);
        mNightModeSwitch = (SwitchCompat) mNightMenuItem.getActionView();
        mNightModeSwitch.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int titleId = mNightModeSwitch.isChecked() ?
                R.string.menu_day_tips : R.string.menu_night_tips;
        mNightMenuItem.setTitle(AppUtil.getString(titleId));
        switchNightMode();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                goHome();               // 进入主页列表
                break;
            case R.id.nav_themes:
                goThemes();             // 进入主题列表
                break;
            case R.id.nav_sections:
                goSections();           // 进入栏目列表
                break;
            case R.id.nav_collected:
                goCollectedActivity();  // 进入收藏页面
                break;
            case R.id.nav_setting:
                // TODO: 2016/11/7 设置页面
                break;
            case R.id.nav_about:
                // TODO: 2016/11/7 关于页面
                break;
        }
        return true;
    }

    /*进入主页列表*/
    private void goHome() {
        closeDrawer();
        mPresenter.switchHome();
    }

    /*进入主题列表*/
    private void goThemes() {
        closeDrawer();
        mPresenter.switchThemes();
    }

    /*进入栏目列表*/
    private void goSections() {
        closeDrawer();
        mPresenter.switchSections();
    }

    /*进入收藏页面*/
    private void goCollectedActivity() {
        Intent favorites = new Intent(this, CollectedActivity.class);
        startActivity(favorites);
    }

    /*进入指定主题列表*/
    public void goTheme(ThemeItemBean bean) {
        mPresenter.switchTheme(bean);
    }

    /*进入指定栏目列表*/
    public void goSection(SectionBean bean) {
        closeDrawer();
        mPresenter.switchSection(bean);
    }

    /*关闭侧边导航*/
    private void closeDrawer() {
        mLeftDrawer.closeDrawer(GravityCompat.START);
    }

    /*判断导航是否打开*/
    private boolean isDrawerOpen() {
        return mLeftDrawer.isDrawerOpen(GravityCompat.START);
    }

    @Override
    public void replaceFragment(BaseFragment instance) {
        try {
            String hashCode = Integer.toString(instance.hashCode());

            Fragment addedFragment = getForehand();

            // start transaction
            FragmentTransaction transaction = mFragmentManager.beginTransaction();

            if (addedFragment != null) {
                ((BaseFragment) addedFragment).stopUpdate();
                transaction.hide(addedFragment);
            }

            Fragment cachedFragment = mFragmentManager.findFragmentByTag(hashCode);
            if (cachedFragment == null) {
                cachedFragment = instance;
                transaction.add(R.id.fragment_container, cachedFragment, hashCode);
            } else {
                transaction.show(cachedFragment);
                ((BaseFragmentContract.View) cachedFragment).reStartUpdate();
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
        if (isDrawerOpen()) {
            closeDrawer();
            return;
        }

        // back to the home fragment or finish the activity
        Fragment forehand = getForehand();
        if (forehand == null || forehand.getClass() == HomeFragment.class) {
            finish();
        } else {
            mPresenter.switchHome();
        }
    }


    @Override
    public void updateTitle(String title) {
        mToolbar.setTitle(title);
    }

}
