package com.example.androiderp.utili;
import android.app.Dialog;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androiderp.basicdata.CustomListView;
import com.example.androiderp.common.Common;
import com.example.androiderp.pull.XmlPullParser;
import com.example.androiderp.CustomDataClass.User;
import com.example.androiderp.adaper.CustomAdapter;
import com.example.androiderp.adaper.DataStructure;
import com.example.androiderp.adaper.PopuMenuDataStructure;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import com.example.androiderp.R;
import org.litepal.crud.DataSupport;

public class SenHome extends AppCompatActivity implements SearchView.OnQueryTextListener,View.OnClickListener  {
    private List<DataStructure> fruitList = new ArrayList<DataStructure>();
    private List<DataStructure> fruit = new ArrayList<DataStructure>();
    private List<PopuMenuDataStructure> popuMenuDatas;
    private CustomAdapter adapter;
    private ListView plistView;
    private SearchView searchView;
    private TextView mMenuTv;
    private DisplayMetrics dm;
    private User user;
    private List<DataStructure> fruitListback;
    private  String url;
    private Common common;
    private Dialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_mail);
        fruitList= DataSupport.findAll(DataStructure.class);
         url="http://api.androidhive.info/music/music.xml";
        //构造函数第一参数是类的对象，第二个是布局文件，第三个是数据源
        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDefaultDisplayHomeAsUpEnabled(true);
        plistView = (ListView) findViewById(R.id.list_view);
        plistView.setTextFilterEnabled(true);
        dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        user=(User) getIntent().getParcelableExtra("user_data");
        showPopupWindow();
        plistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {

                if( common.mPopWindow==null ||!common.mPopWindow.isShowing())
                {   DisplayMetrics dm = new DisplayMetrics();

                    getWindowManager().getDefaultDisplay().getMetrics(dm);
                    int xPos = dm.widthPixels / 3;
                    common.mPopWindow.showAsDropDown(view,xPos,5);
                    //mPopWindow.showAtLocation(findViewById(R.id.main), Gravity.BOTTOM, 0, 0);//从底部弹出
                }
                else {

                    common.mPopWindow.dismiss();
                }
            }
        });
        if(fruitList.size()!=0||fruitList!=null) {
            adapter = new CustomAdapter(SenHome.this, R.layout.home_item, fruitList);
            plistView.setAdapter(adapter);

        }else {


        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.searchview,menu);
        MenuItem menuItem=menu.findItem(R.id.search);//
        searchView= (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(SenHome.this);//为搜索框设置监听事件
        searchView.setSubmitButtonEnabled(false);//设置是否显示搜索按钮
        searchView.setQueryHint("请输入产品或客户");//设置提示信息
        searchView.setIconifiedByDefault(true);//设置搜索默认为图标
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //非常重要
        mMenuTv=(TextView) findViewById(R.id.setting);
        switch (item.getItemId()) {

            case R.id.setting:
                if( common.mPopWindow==null ||!common.mPopWindow.isShowing())
                {
                    int xPos = dm.widthPixels / 3;
                    common.mPopWindow.showAsDropDown(mMenuTv,0,5);
                    //mPopWindow.showAtLocation(findViewById(R.id.main), Gravity.BOTTOM, 0, 0);//从底部弹出
                }
                else {
                    common.mPopWindow.dismiss();
                }
                break;
            case R.id.search:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public boolean onQueryTextChange(String newText) {
        Object[] obj = searchItem(newText);
        updateLayout(obj);
        return false;
    }
    //筛选条件
    public Object[] searchItem(String name) {
         fruitListback = new ArrayList<DataStructure>();
        for (int i = 0; i < fruitList.size(); i++) {
            int index = fruitList.get(i).getName().indexOf(name);
            // 存在匹配的数据
            if (index != -1) {
                fruitListback.add(fruitList.get(i));
            }
        }
        return fruitListback.toArray();
    }
//adapter刷新,重写Filter方式会出现BUG.
    public void updateLayout(Object[] obj) {
        if(fruitListback!=null) {
            adapter = new CustomAdapter(SenHome.this, R.layout.home_item, fruitListback);
            plistView.setAdapter(adapter);
        }
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        if (TextUtils.isEmpty(query)){
            plistView.clearTextFilter();
        }else{
            }
        return true;
    }
    private void showPopupWindow() {
        common = new Common();
        popuMenuDatas = new ArrayList<PopuMenuDataStructure>();
        PopuMenuDataStructure popuMenua = new PopuMenuDataStructure(android.R.drawable.ic_menu_edit, "客户管理");
        popuMenuDatas.add(popuMenua);
        PopuMenuDataStructure popuMenub = new PopuMenuDataStructure(android.R.drawable.ic_menu_edit, "供应商管理");
        popuMenuDatas.add(popuMenub);
        common.PopupWindow(SenHome.this, dm, popuMenuDatas);
        common.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {

                searchView.setQueryHint(popuMenuDatas.get(position).getName());
                if(popuMenuDatas.get(position).getName().equals("客户管理"))
                {
                    Intent intent = new Intent(SenHome.this, CustomListView.class);
                    startActivity(intent);
                }
                common.mPopWindow.dismiss();
            }
        });
    }

    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU && event.getRepeatCount() == 0
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (common.mPopWindow != null && common.mPopWindow.isShowing()) {
                common.mPopWindow.dismiss();
            }
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {

    }
    private void queryFromServer(String address) {
        showDialog();
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                XmlPullParser xmlPullParser=new XmlPullParser();
                fruit=xmlPullParser.parseXMLWithPull(responseText);

                if (fruit!=null) {
                    SenHome.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeDialog();
                           fruitList=fruit;
                            adapter = new CustomAdapter(SenHome.this, R.layout.home_item, fruitList);
                            plistView.setAdapter(adapter);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                // 通过runOnUiThread()方法回到主线程处理逻辑
                SenHome.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeDialog();
                        Toast.makeText(SenHome.this, "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    /**
     * 显示进度对话框
     */
    private void showDialog() {

        mDialog = common.createLoadingDialog(this, "正在加载中...");
        mDialog.setCancelable(true);//允许返回
        mDialog.show();//显示

    }

    /**
     * 关闭进度对话框
     */
    private void closeDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }
    
}
