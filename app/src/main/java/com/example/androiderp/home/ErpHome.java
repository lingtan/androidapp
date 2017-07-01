/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.androiderp.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.ashokvarma.bottomnavigation.BadgeItem;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.androiderp.Fragment.FirstFragment;
import com.example.androiderp.Fragment.SecondFragment;
import com.example.androiderp.Fragment.ThressFragment;
import com.example.androiderp.R;
import com.example.androiderp.basicdata.CustomSearchListView;
import com.example.androiderp.basicdata.ProductTowListView;
import com.example.androiderp.custom.CustomHomeSearch;
import com.example.androiderp.custom.CustomSearchBase;
import com.example.androiderp.scanning.CommonScanActivity;
import com.example.androiderp.scanning.utils.Constant;

/**
 * TODO
 */
public class ErpHome extends CustomSearchBase implements  BottomNavigationBar.OnTabSelectedListener{
    private BottomNavigationBar bottomNavigationBar;
    private DrawerLayout mDrawerLayout;
    private int lastSelectedPosition = 0;
    private FragmentManager fragmentManager;
    private android.support.v4.app.FragmentTransaction transaction;
    private ActionBar ab;
    private Menu mMenu;
    private String edit;
    private CustomHomeSearch customHomeSearch;
    private InputMethodManager manager;
    private BadgeItem  badgeItem;


    @Override
    public void iniView(){
        setContentView(R.layout.home_layout);//加载主界面

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        final Intent intent=getIntent();
        customHomeSearch=(CustomHomeSearch)findViewById(R.id.home_search);
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        edit=intent.getStringExtra("fragment");
        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar.setTabSelectedListener(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);//加载工具栏
        ab = getSupportActionBar();//创建活动工具栏
        ab.setHomeAsUpIndicator(R.drawable.home_toobar_menu);//设置主页按钮
        ab.setDisplayHomeAsUpEnabled(true);//显示主页按钮
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);//加载滑动布置
        badgeItem = new BadgeItem()
                .setBorderWidth(4)
                .setBackgroundColorResource(R.color.holo_red_light)
                .setText("0");
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);//加载导向内容
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }
        switch (edit) {
            case "send":
                fragmentManager=getSupportFragmentManager();
                transaction=fragmentManager.beginTransaction();
                transaction.replace(R.id.tab,new SecondFragment());
                lastSelectedPosition=1;
                transaction.commit();
                break;
            case "fisrt":
                fragmentManager = getSupportFragmentManager();
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.tab, new FirstFragment());
                lastSelectedPosition=0;
                customHomeSearch.setHint("查找商品");
                transaction.commit();
                break;
            case "thress":
                fragmentManager = getSupportFragmentManager();
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.tab, new ThressFragment());
                lastSelectedPosition=2;
                transaction.commit();
                break;

        }
        refresh();
    customHomeSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {

            if (hasFocus) {
            Intent    intent = new Intent(ErpHome.this, CustomSearchListView.class);
                startActivity(intent);
                v.clearFocus();
            }
        }
    });
    }

    private void refresh() {

        bottomNavigationBar.clearAll();
//        bottomNavigationBar.setFab(fabHome, BottomNavigationBar.FAB_BEHAVIOUR_TRANSLATE_AND_STICK);
        bottomNavigationBar
                .setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.home, "主页").setActiveColorResource(R.color.bottom).setBadgeItem(badgeItem))
                .addItem(new BottomNavigationItem(R.drawable.purchase, "采购").setActiveColorResource(R.color.bottom))
                .addItem(new BottomNavigationItem(R.drawable.home_sales, "销售").setActiveColorResource(R.color.bottom))
                .addItem(new BottomNavigationItem(R.drawable.hoem_stock, "库存").setActiveColorResource(R.color.bottom))
                .addItem(new BottomNavigationItem(R.drawable.home_report, "报表").setActiveColorResource(R.color.bottom))
                .setFirstSelectedPosition(lastSelectedPosition)
                .setInActiveColor(R.color.bottomNavigationBar)
                .initialise();


    }


    public void onTabSelected(int position) {

        switch (position) {
            case 0:
                fragmentManager=getSupportFragmentManager();
                transaction=fragmentManager.beginTransaction();
                transaction.replace(R.id.tab,new FirstFragment());
                transaction.commit();
                ab.setDisplayHomeAsUpEnabled(true);
                if (badgeItem != null) {
                    badgeItem.setText(Integer.toString(position));
                }
                 showEditMenu();
                break;
            case 1:

                fragmentManager=getSupportFragmentManager();
                transaction=fragmentManager.beginTransaction();
                transaction.replace(R.id.tab,new SecondFragment());
                if (badgeItem != null) {
                    badgeItem.setText(Integer.toString(position));
                }
                transaction.commit();
                ab.setDisplayHomeAsUpEnabled(false);
                hiddenEditMenu();

                break;
            case 2:
                fragmentManager=getSupportFragmentManager();
                transaction=fragmentManager.beginTransaction();
                transaction.replace(R.id.tab,new ThressFragment());
                if (badgeItem != null) {
                    badgeItem.setText(Integer.toString(position));
                }
                transaction.commit();
                ab.setDisplayHomeAsUpEnabled(false);
                hiddenEditMenu();
                break;
            case 3:
                if (badgeItem != null) {
                    badgeItem.setText(Integer.toString(position));
                }
                break;
            case 4:
                if (badgeItem != null) {
                    badgeItem.setText(Integer.toString(position));
                }
                break;
            default:
                break;
        }

    }

    @Override
    public void onTabUnselected(int position) {
    }

    @Override
    public void onTabReselected(int position) {

    }

    private void setMessageText(String messageText) {

    }
//创建night_mode菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu=menu;

        switch (edit) {
            case "send":
                hiddenEditMenu();
                break;
            case "fisrt":
                getMenuInflater().inflate(R.menu.toobar_nemu, menu);

        }

        return true;
    }
    private void hiddenEditMenu(){
        if(null != mMenu){
            mMenu.clear();
            ab.setDisplayHomeAsUpEnabled(false);
            getMenuInflater().inflate(R.menu.custom_menu, mMenu);
            }

    }

    private void showEditMenu(){
        if(null != mMenu){

            mMenu.clear();
            getMenuInflater().inflate(R.menu.toobar_nemu, mMenu);


        }
    }
//只执行一次
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;

            case R.id.toobar_nemu_night:
                Intent openCameraIntent = new Intent(ErpHome.this, CommonScanActivity.class);
                openCameraIntent.putExtra(Constant.REQUEST_SCAN_MODE,Constant.REQUEST_SCAN_MODE_ALL_MODE);
                startActivityForResult(openCameraIntent, 1);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            if(getCurrentFocus()!=null && getCurrentFocus().getWindowToken()!=null){
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.onTouchEvent(event);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {


                    Intent intent = new Intent(ErpHome.this, ProductTowListView.class);
                    intent.putExtra("scanResult",data.getStringExtra("scanResult"));
                    startActivity(intent);
                }
                break;
            default:
        }
    }
}
