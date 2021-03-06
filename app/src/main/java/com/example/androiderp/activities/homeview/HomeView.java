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

package com.example.androiderp.activities.homeview;

import android.content.Context;
import android.content.Intent;
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

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.androiderp.Fragment.FirstFragment;
import com.example.androiderp.Fragment.FiveFragment;
import com.example.androiderp.Fragment.FouthFragment;
import com.example.androiderp.Fragment.SecondFragment;
import com.example.androiderp.Fragment.ThressFragment;
import com.example.androiderp.R;
import com.example.androiderp.activities.warehouseview.InventorySearchView;
import com.example.androiderp.activities.basicview.ProductSearchView;
import com.example.androiderp.activities.salesview.SaleSearchView;
import com.example.androiderp.activities.purchaseview.PurchaseSearchView;
import com.example.androiderp.ui.CHomeSearch;
import com.example.androiderp.ui.CSearchBase;
import com.example.androiderp.scanning.CommonScanActivity;
import com.example.androiderp.scanning.utils.Constant;

/**
 * TODO
 */
public class HomeView extends CSearchBase implements  BottomNavigationBar.OnTabSelectedListener{
    private BottomNavigationBar bottomNavigationBar;
    private DrawerLayout mDrawerLayout;
    private int lastSelectedPosition = 0;
    private FragmentManager fragmentManager;
    private android.support.v4.app.FragmentTransaction transaction;
    private ActionBar ab;
    private Menu mMenu;
    private String edit;
    private CHomeSearch cHomeSearch;
    private InputMethodManager manager;
    private int searchPosition=0;



    @Override
    public void iniView(){
        setContentView(R.layout.home_layout);//加载主界面

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        final Intent intent=getIntent();
        cHomeSearch =(CHomeSearch)findViewById(R.id.home_search);
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
                cHomeSearch.setHint("输入名称 | 产品货号");
                transaction.commit();
                break;
            case "thress":
                fragmentManager = getSupportFragmentManager();
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.tab, new ThressFragment());
                lastSelectedPosition=2;
                transaction.commit();
                break;
            case "fouth":
                fragmentManager = getSupportFragmentManager();
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.tab, new FouthFragment());
                lastSelectedPosition=3;
                transaction.commit();
                break;

        }
        refresh();
    cHomeSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {

            switch (searchPosition) {
                case 0:
                    if (hasFocus) {
                        Intent    intent = new Intent(HomeView.this, ProductSearchView.class);
                        startActivity(intent);
                        v.clearFocus();
                    }

                 break;
                case 1:
                    if (hasFocus) {
                        Intent    intent = new Intent(HomeView.this, PurchaseSearchView.class);
                        startActivity(intent);
                        v.clearFocus();
                    }

                    break;

                case 2:
                    if (hasFocus) {
                        Intent    intent = new Intent(HomeView.this, SaleSearchView.class);
                        startActivity(intent);
                        v.clearFocus();
                    }

                    break;
                case 3:
                    if (hasFocus) {
                        Intent    intent = new Intent(HomeView.this, InventorySearchView.class);
                        startActivity(intent);
                        v.clearFocus();
                    }

                    break;


                default:
                    break;

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
                .addItem(new BottomNavigationItem(R.drawable.home, "主页").setActiveColorResource(R.color.bottom))
                .addItem(new BottomNavigationItem(R.drawable.purchase, "采购").setActiveColorResource(R.color.bottom))
                .addItem(new BottomNavigationItem(R.drawable.home_sales, "销售").setActiveColorResource(R.color.bottom))
                .addItem(new BottomNavigationItem(R.drawable.hoem_stock, "库存").setActiveColorResource(R.color.bottom))
                .addItem(new BottomNavigationItem(R.drawable.home_report, "报表").setActiveColorResource(R.color.bottom))
                .setFirstSelectedPosition(lastSelectedPosition)
                .setInActiveColor(R.color.bottomNavigationBar)
                .initialise();


    }


    public void onTabSelected(int position) {
        searchPosition=position;
        switch (position) {
            case 0:

                fragmentManager=getSupportFragmentManager();
                transaction=fragmentManager.beginTransaction();
                transaction.replace(R.id.tab,new FirstFragment());
                cHomeSearch.setHint("输入名称 | 产品货号");
                transaction.commit();
                ab.setDisplayHomeAsUpEnabled(true);
                 showEditMenu();
                break;
            case 1:
                fragmentManager=getSupportFragmentManager();
                transaction=fragmentManager.beginTransaction();
                transaction.replace(R.id.tab,new SecondFragment());
                transaction.commit();
                cHomeSearch.setHint("输入供应商 | 单据编号");
                ab.setDisplayHomeAsUpEnabled(true);
                hiddenEditMenu();

                break;
            case 2:
                fragmentManager=getSupportFragmentManager();
                transaction=fragmentManager.beginTransaction();
                transaction.replace(R.id.tab,new ThressFragment());
                cHomeSearch.setHint("输入客户 | 单据编号");
                transaction.commit();
                ab.setDisplayHomeAsUpEnabled(true);
                hiddenEditMenu();
                break;
            case 3:
                fragmentManager=getSupportFragmentManager();
                transaction=fragmentManager.beginTransaction();
                transaction.replace(R.id.tab,new FouthFragment());
                cHomeSearch.setHint("输入名称 | 产品货号");
                transaction.commit();
                ab.setDisplayHomeAsUpEnabled(true);
                hiddenEditMenu();
                break;
            case 4:
                fragmentManager=getSupportFragmentManager();
                transaction=fragmentManager.beginTransaction();
                transaction.replace(R.id.tab,new FiveFragment());
                transaction.commit();
                ab.setDisplayHomeAsUpEnabled(true);
                hiddenEditMenu();
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
                Intent openCameraIntent = new Intent(HomeView.this, CommonScanActivity.class);
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
        //必须要这一句，否则fistfragment的onActivityResult不执行。
        super.onActivityResult(requestCode,resultCode,data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {


                    Intent intent = new Intent(HomeView.this, ProductSearchView.class);
                    intent.putExtra("scanResult",data.getStringExtra("scanResult"));
                    startActivity(intent);
                }
                break;
            default:
        }
    }


}
