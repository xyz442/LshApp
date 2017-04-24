package com.linsh.lshapp.part.home;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.linsh.lshapp.R;
import com.linsh.lshapp.base.BaseViewActivity;
import com.linsh.lshapp.part.home.shiyi.ShiyiFragment;
import com.linsh.lshapp.tools.MainFragmentHelper;

public class MainActivity extends BaseViewActivity implements NavigationView.OnNavigationItemSelectedListener {

    private MainFragmentHelper mHomeFragmentHelper;
    private Toolbar mToolbar;

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        // 初始化ToolBar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        // 初始化菜单栏
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        // 初始化NavigationView
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // 使用FragmentHelper管理Fragment
        mHomeFragmentHelper = new MainFragmentHelper();
        // 默认选择 拾意
        mHomeFragmentHelper.replaceFragment(new ShiyiFragment(), this);
        navigationView.setCheckedItem(R.id.nav_shiyi);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onBackPressed() {
        // 重写返回键，优先关闭菜单
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mHomeFragmentHelper.onCreateOptionsMenu(this, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (mHomeFragmentHelper.onOptionsItemSelected(id)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // 切换Fragment
        mHomeFragmentHelper.onNavigationItemSelected(item, this);
        // 关闭菜单
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
