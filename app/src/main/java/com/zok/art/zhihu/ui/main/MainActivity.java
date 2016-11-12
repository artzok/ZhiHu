package com.zok.art.zhihu.ui.main;

import android.content.DialogInterface;
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
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.zok.art.zhihu.R;
import com.zok.art.zhihu.base.BaseActivity;
import com.zok.art.zhihu.base.BaseFragment;
import com.zok.art.zhihu.base.BaseFragmentContract;
import com.zok.art.zhihu.bean.SectionBean;
import com.zok.art.zhihu.bean.ThemeItemBean;
import com.zok.art.zhihu.service.DownloadService;
import com.zok.art.zhihu.ui.about.AboutActivity;
import com.zok.art.zhihu.ui.collected.CollectedActivity;
import com.zok.art.zhihu.ui.home.HomeFragment;
import com.zok.art.zhihu.ui.login.LoginActivity;
import com.zok.art.zhihu.ui.setting.SettingActivity;
import com.zok.art.zhihu.utils.AppUtil;

import butterknife.BindView;

import static android.content.DialogInterface.OnClickListener;

public class MainActivity extends BaseActivity<MainContract.Presenter>
        implements MainContract.View,
        NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener {

    private ActionBarDrawerToggle mToggle;

    @BindView(R.id.toolbar)
    public Toolbar mToolbar;

    @BindView(R.id.left_drawer)
    public DrawerLayout mLeftDrawer;

    @BindView(R.id.nav_menus)
    public NavigationView mNavigationView;

    public ImageView mUserImage;

    public TextView mLoginTips;

    private FragmentManager mFragmentManager;
    private MenuItem mNightMenuItem;


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
        if (id == android.R.id.message) {
            // TODO: 2016/11/8 消息推送功能
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
        // 设置ActionBar
        setToolBar(mToolbar, " ", false);

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
        mLoginTips.setOnClickListener(this);

        // 导航监听
        mNavigationView.setNavigationItemSelectedListener(this);

        // 夜景模式菜单
        mNightMenuItem = mNavigationView.getMenu().findItem(R.id.nav_night);
        SwitchCompat nightModeSwitch = (SwitchCompat) mNightMenuItem.getActionView();
        nightModeSwitch.setOnClickListener(this);
        // 初始化菜单文字
        updateNightModeText();
        nightModeSwitch.setChecked(isNightMode());
    }

    private void updateNightModeText() {
        int titleId = isNightMode() ? R.string.menu_day_tips : R.string.menu_night_tips;
        mNightMenuItem.setTitle(AppUtil.getString(titleId));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.switch_mode:
                switchNightMode();
                break;
            case R.id.login_tips:
                goLoginActivity();
                break;
        }
    }

    private void goLoginActivity() {
        Intent login = new Intent(this, LoginActivity.class);
        startActivity(login);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:         // 进入主页列表
                goHome();
                break;
            case R.id.nav_themes:       // 进入主题列表
                goThemes();
                break;
            case R.id.nav_sections:     // 进入栏目列表
                goSections();
                break;
            case R.id.nav_collected:    // 进入收藏页面
                goCollectedActivity();
                break;
            case R.id.nav_setting:      // 进入设置页面
                goSettingActivity();
                break;
            case R.id.nav_about:        // 进入关于页面
                goAboutActivity();
                break;
            case R.id.nav_download:     // 离线下载内容
                downloadLatestNews();
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

    /*进入设置页面*/
    private void goSettingActivity() {
        // TODO: 2016/11/7 设置页面
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }

    /*进入关于页面*/
    private void goAboutActivity() {
        Intent aboutActivity = new Intent(this, AboutActivity.class);
        startActivity(aboutActivity);
    }
    /*离线下载所有内容*/
    private void downloadLatestNews() {
        Intent download = new Intent(this, DownloadService.class);
        startService(download);
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

            BaseFragment addedFragment = (BaseFragment) getForehand();

            // start transaction
            FragmentTransaction transaction = mFragmentManager.beginTransaction();

            if (addedFragment != null) {
                addedFragment.stopUpdate();
                addedFragment.onPause();
                transaction.hide(addedFragment);
            }

            Fragment cachedFragment = mFragmentManager.findFragmentByTag(hashCode);
            if (cachedFragment == null) {
                cachedFragment = instance;
                transaction.add(R.id.fragment_container, cachedFragment, hashCode);
            } else {
                cachedFragment.onResume();
                transaction.show(cachedFragment);
                ((BaseFragmentContract.View) cachedFragment).reStartUpdate();
            }
            transaction.addToBackStack(hashCode);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*获得前台显示的Fragment实例*/
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
            createExitDialog().show();
        } else {
            goHome();
        }
    }

    @Override
    public void updateTitle(String title) {
        mToolbar.setTitle(title);
    }

    private AlertDialog createExitDialog() {
        TextView view = new TextView(this);
        view.setText(R.string.exit_tips);
        int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                20, AppUtil.getDisplayMetrics());
        view.setPadding(padding, padding, padding, padding);
        view.setTextColor(AppUtil.getColor(R.color.itemFontColor));
        view.setTextSize(16);
        return new AlertDialog.Builder(this)
                .setTitle(R.string.exit_title)
                .setView(view)
                .setNegativeButton("否", null)
                .setPositiveButton("是",
                        new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        }).create();
    }
}
