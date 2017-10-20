/*
功能：商品信息、商品扫描搜索、商品搜索、商品新增、商品修改、网络状态更变提醒
主要控件：ListView、Adapter、ustomSearch、TextView
数据模型：Product、ProductCategory、CommonAdapterData
 */

package com.example.androiderp.activities.basicview;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.androiderp.adaper.BasicAdapter;
import com.example.androiderp.bean.AcivityPostBen;
import com.example.androiderp.bean.AdapterBean;
import com.example.androiderp.bean.PostProductData;
import com.example.androiderp.bean.PostUserData;
import com.example.androiderp.bean.Product;
import com.example.androiderp.R;
import com.example.androiderp.adaper.ProductAdapter;
import com.example.androiderp.tools.Common;
import com.example.androiderp.tools.GlobalVariable;
import com.example.androiderp.tools.HttpUtil;
import com.example.androiderp.ui.CSearch;
import com.example.androiderp.ui.CSearchBase;
import com.example.androiderp.activities.basicfrom.ProductForm;
import com.example.androiderp.scanning.CommonScanActivity;
import com.example.androiderp.scanning.utils.Constant;
import com.example.androiderp.ui.DataLoadingDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class ProductMoreView extends CSearchBase implements View.OnClickListener {
    private ProductAdapter rightAdapter;
    private BasicAdapter leftAdapter;
    private ListView rightListView;
    private ListView leftListView;
    private DisplayMetrics dm;
    private TextView toobarBack, toobarAdd, toobarTile, countShow, toobarScreen;
    private CSearch search;
    private Intent  intentScreen;
    private String scanResult;
    private IntentFilter intentFilter;
    private NetworkChangeReceiver networkChangeReceiver;
    private AcivityPostBen acivityPostBen=new AcivityPostBen();
    private  List<AdapterBean>  HttpResponseCategory=new ArrayList<>();
    private  List<AdapterBean>  HttpResponseCategoryTemp=new ArrayList<>();
    private   List<Product>  HttpResponseCustom=new ArrayList<>();
    private PostProductData postDate = new PostProductData();
    private Dialog dialog;
    private  int selectPositon=0;
    private String selectCategory;

    @Override
    public void iniView(){
        setContentView(R.layout.product_listview_layout);
        DataInit();
        widgetInit();
        widgetSet();
        widgetListenerSet();
        if(scanResult!=null)
        {

            search.requestFocusFromTouch();
            search.setText(scanResult);


        }


    }
//注册广播类型
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intentFilter=new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        intentFilter.addAction("android.net.wifi.STATE_CHANGE");
        networkChangeReceiver=new NetworkChangeReceiver();
        registerReceiver(networkChangeReceiver,intentFilter);

    }
    //读取数据库
    private void DataInit()
    {
        Intent intent=getIntent();
        acivityPostBen=intent.getParcelableExtra("acivityPostBen");
        postDate.setName("");
        postDate.setRequestType(GlobalVariable.cmvCusmtAndCategory);
        postDate.setServerIp(Common.ip);
        postDate.setClassType(1);
        postDate.setServlet("ProductOperate");
        showDialog();
        getHttpData(postDate);
    }
//控件初始化
    private void widgetInit()
    {
        toobarBack =(TextView)findViewById(R.id.custom_toobar_left) ;
        toobarTile =(TextView)findViewById(R.id.custom_toobar_midd);
        toobarAdd =(TextView)findViewById(R.id.custom_toobar_right);
        toobarScreen =(TextView)findViewById(R.id.customtoobar_screen);
        search = (CSearch) findViewById(R.id.search);
        countShow =(TextView)findViewById(R.id.product_item_layout_count) ;
        leftListView=(ListView) findViewById(R.id.left_list);
        rightListView = (ListView) findViewById(R.id.right_list);
    }
//控件设置
    private void widgetSet()
    {

        toobarTile.setText("商品信息");
        countShow.setText(String.valueOf(HttpResponseCustom.size()));//统计商品数量并显示
        dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        intentScreen =getIntent();
        scanResult= intentScreen.getStringExtra("scanResult");//获取扫描返回结果
        toobarTile.setCompoundDrawables(null,null,null,null);//取消标题图片
        toobarScreen.setOnClickListener(this);
        toobarBack.setOnClickListener(this);
        toobarAdd.setOnClickListener(this);
        toobarTile.setOnClickListener(this);
        leftListView.setTextFilterEnabled(true);
        rightListView.setTextFilterEnabled(true);
        search.addTextChangedListener(textWatcher);


    }

//控件设置事件，点击编辑、获取左类别的对应的位置及名称
    private void widgetListenerSet()
    {
        leftListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showDialog();
                selectPositon=position;
                leftAdapter.setSeclection(selectPositon);
                leftAdapter.notifyDataSetChanged();
                selectCategory=HttpResponseCategory.get(position).getName();
                postDate.setName(search.getText().toString());
                if(HttpResponseCategory.get(position).getName().equals("全部")) {
                    postDate.setCategory_name("");

                }else {
                    postDate.setCategory_name(HttpResponseCategory.get(position).getName());
                }
                postDate.setName("");
                postDate.setServerIp(Common.ip);
                postDate.setRequestType(GlobalVariable.cfCatetorySelect);
                postDate.setClassType(1);
                postDate.setServlet("ProductOperate");
                getHttpData(postDate);
                countShow.setText(String.valueOf(HttpResponseCustom.size()));
            }
        });
        rightListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {

                Intent  intent= new Intent(getApplicationContext(), ProductForm.class);
                intent.putExtra("type", "edit");
                intent.putExtra("postdata", HttpResponseCustom.get(position));
                intent.putExtra("acivityPostBen",acivityPostBen);
                startActivityForResult(intent,1);


            }
        });

    }


//处理返回结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {


                    postDate.setName("");
                    postDate.setServerIp(Common.ip);
                    postDate.setRequestType(GlobalVariable.cmvCusmtAndCategory);
                    postDate.setClassType(acivityPostBen.getSetClassType());
                    postDate.setServlet(acivityPostBen.getRequestServlet());
                    showDialog();
                    HttpResponseCustom.clear();
                    rightAdapter.notifyDataSetChanged();
                    getHttpData(postDate);


                }

            case 2:
                if (resultCode == RESULT_OK) {
                   search.requestFocusFromTouch();
                    search.setText(data.getStringExtra("scanResult"));

                }
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {   //返回
            case R.id.custom_toobar_left:
                ProductMoreView.this.finish();
                break;
            //新增按钮
            case R.id.custom_toobar_right:
                Intent  intent= new Intent(getApplicationContext(), ProductForm.class);
                acivityPostBen.setAcivityName(acivityPostBen.getAcivityName()+"资料");
                acivityPostBen.setRequestServlet("ProductOperate");
                acivityPostBen.setSetClassType(acivityPostBen.getSetClassType());
                intent.putExtra("acivityPostBen",acivityPostBen);
                intent.putExtra("type","add");
                startActivityForResult(intent,1);
                break;
            //扫描
            case R.id.customtoobar_screen:
                Intent openCameraIntent = new Intent(ProductMoreView.this, CommonScanActivity.class);
                openCameraIntent.putExtra(Constant.REQUEST_SCAN_MODE,Constant.REQUEST_SCAN_MODE_ALL_MODE);
                startActivityForResult(openCameraIntent, 2);

                break;


        }

    }
    public void  searchItem(String name) {

        postDate.setName(name);
        if(selectCategory.equals("全部"))
        {
            postDate.setCategory_name("");
        }else
        {
            postDate.setCategory_name(selectCategory);
        }

        postDate.setRequestType(GlobalVariable.cfCatetorySelect);
        postDate.setClassType(acivityPostBen.getSetClassType());
        postDate.setServlet(acivityPostBen.getRequestServlet());
        showDialog();
        getHttpData(postDate);
    }
    private void getHttpData(final PostProductData postPostUserData) {



        HttpUtil.sendProductRequst(postPostUserData, new okhttp3.Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        closeDialog();
                        Toast.makeText(getApplicationContext(), "网络连接失败", Toast.LENGTH_SHORT).show();

                    }
                });

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            if(postDate.getRequestType().equals(GlobalVariable.cmvCusmtAndCategory)) {
                                JSONObject jsonObject = new JSONObject(response.body().string());
                                JSONArray jsonArray = jsonObject.getJSONArray("custom");
                                JSONArray jsonArray1 = jsonObject.getJSONArray("customcategory");
                                Gson gson = new Gson();
                                HttpResponseCategory.clear();
                                HttpResponseCategoryTemp = gson.fromJson(jsonArray1.toString(), new TypeToken<List<AdapterBean>>() {
                                }.getType());
                                AdapterBean adapterBean=new AdapterBean();
                                adapterBean.setName("全部");
                                HttpResponseCategory.add(adapterBean);
                                HttpResponseCategory.addAll(HttpResponseCategoryTemp);
                                HttpResponseCustom= gson.fromJson(jsonArray.toString(), new TypeToken<List<Product>>() {
                                }.getType());
                                if(HttpResponseCategory!=null&&HttpResponseCustom!=null) {
                                    leftAdapter = new BasicAdapter(getApplicationContext(), R.layout.custom_item, HttpResponseCategory);
                                    leftAdapter.setSeclection(selectPositon);
                                    selectCategory=HttpResponseCategory.get(0).getName();
                                    leftListView.setAdapter(leftAdapter);
                                    rightAdapter = new ProductAdapter(getApplicationContext(), R.layout.product_item, HttpResponseCustom);
                                    rightListView.setAdapter(rightAdapter);
                                }


                            }else
                            {

                                Gson gson = new Gson();
                                HttpResponseCustom = gson.fromJson(response.body().string(), new TypeToken<List<Product>>() {
                                }.getType());
                                if(HttpResponseCustom!=null) {
                                    if (HttpResponseCustom.size() != 0) {


                                        rightAdapter = new ProductAdapter(getApplicationContext(), R.layout.product_item, HttpResponseCustom);
                                        rightListView.setAdapter(rightAdapter);


                                    } else {

                                        rightAdapter = new ProductAdapter(getApplicationContext(), R.layout.product_item, HttpResponseCustom);
                                        rightListView.setAdapter(rightAdapter);
                                        Toast.makeText(getApplicationContext(), "没有数据", Toast.LENGTH_SHORT).show();

                                    }
                                }else
                                {
                                    HttpResponseCustom.clear();
                                    rightAdapter.notifyDataSetChanged();
                                }

                            }
                            closeDialog();

                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "网络", Toast.LENGTH_SHORT).show();
                            closeDialog();
                        }
                    }
                });


            }
        });


    }

    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {


        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {



        }

        @Override
        public void afterTextChanged(Editable s) {


            searchItem(search.getText().toString());

        }
    };
//自定义广播类型
    class NetworkChangeReceiver extends BroadcastReceiver {
//重写onReceive方法
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectionManager = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {

            } else {
                Toast.makeText(context, "没有网络，请确认WIFI是否打开",
                        Toast.LENGTH_SHORT).show();
            }

        }

    }
//反注册广播
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkChangeReceiver);
    }

    /**
     * 显示进度对话框
     */
    private void showDialog() {

        dialog = new DataLoadingDialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();//显示

    }

    /**
     * 关闭进度对话框
     */
    private void closeDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
