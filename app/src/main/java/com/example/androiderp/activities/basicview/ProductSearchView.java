package com.example.androiderp.activities.basicview;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.androiderp.bean.AcivityPostBean;
import com.example.androiderp.bean.HttpPostBean;
import com.example.androiderp.bean.PostProductData;
import com.example.androiderp.bean.Product;
import com.example.androiderp.R;
import com.example.androiderp.adaper.ProductAdapter;
import com.example.androiderp.tools.Common;
import com.example.androiderp.tools.GlobalVariable;
import com.example.androiderp.tools.HttpUtil;
import com.example.androiderp.ui.CHomeSearch;
import com.example.androiderp.ui.CSearchBase;
import com.example.androiderp.activities.basicfrom.ProductForm;
import com.example.androiderp.scanning.CommonScanActivity;
import com.example.androiderp.scanning.utils.Constant;
import com.example.androiderp.ui.DataLoadingDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class ProductSearchView extends CSearchBase {
    private ProductAdapter adapter;
    private ListView listView;
    private DisplayMetrics dm;
    private Common common;
    private CHomeSearch cHomeSearch;
    private Intent iScreen;
    private ActionBar ab;
    private String scanResult;
    private AcivityPostBean postAcivityPostBen = new AcivityPostBean();
    private HttpPostBean httpPostBean= new HttpPostBean();
    private List<Product> HttpResponseCustom = new ArrayList<>();
    private Dialog dialog;

    @Override
    public void iniView(){
        setContentView(R.layout.custom_search_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.custom_search_toolbar);
        setSupportActionBar(toolbar);//加载工具栏
        ab = getSupportActionBar();//创建活动工具栏
        ab.setHomeAsUpIndicator(R.drawable.prudect_screen);//设置主页按钮
        ab.setDisplayHomeAsUpEnabled(true);//显示主页按钮
        cHomeSearch = (CHomeSearch) findViewById(R.id.home_custom_search);
        cHomeSearch.setVisibility(View.VISIBLE);
        cHomeSearch.setHint("输入名称 | 产品货号");
        iScreen =getIntent();
        scanResult= iScreen.getStringExtra("scanResult");
        //构造函数第一参数是类的对象，第二个是布局文件，第三个是数据源
        listView = (ListView) findViewById(R.id.list);
        listView.setTextFilterEnabled(true);
        dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {

                httpPostBean.setServlet("ProductOperate");
                httpPostBean.setClassType(1);
                Intent intent = new Intent(getApplicationContext(), ProductForm.class);
                intent.putExtra("type", "edit");
                intent.putExtra("postdata", HttpResponseCustom.get(position));
                intent.putExtra("acivityPostBen", postAcivityPostBen);
                startActivityForResult(intent, 1);


            }
        });

        cHomeSearch.addTextChangedListener(textWatcher);
        if(scanResult!=null) {

            cHomeSearch.requestFocusFromTouch();
            cHomeSearch.setText(scanResult);

        }else {
            PostProductData postDate = new PostProductData();
            postDate.setName("");
            postDate.setCategory_name("");
            postDate.setServerIp(Common.ip);
            postDate.setRequestType(GlobalVariable.cfCatetorySelect);
            postDate.setClassType(1);
            postDate.setServlet("ProductOperate");
            getHttpData(postDate);
        }



    }



    public void searchItem(String name) {
        PostProductData postDate = new PostProductData();
        postDate.setName(name);
        postDate.setCategory_name("");

        postDate.setServerIp(Common.ip);
        postDate.setRequestType(GlobalVariable.cfCatetorySelect);
        postDate.setClassType(1);
        postDate.setServlet("ProductOperate");
        getHttpData(postDate);
    }
    private void getHttpData(final PostProductData postPostUserData) {
        showDialog();


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



                                Gson gson = new Gson();
                                HttpResponseCustom = gson.fromJson(response.body().string(), new TypeToken<List<Product>>() {
                                }.getType());
                                if (HttpResponseCustom != null) {
                                    if (HttpResponseCustom.size() != 0) {


                                        adapter = new ProductAdapter(getApplicationContext(), R.layout.product_item, HttpResponseCustom);
                                        listView.setAdapter(adapter);


                                    } else {

                                        adapter = new ProductAdapter(getApplicationContext(), R.layout.product_item, HttpResponseCustom);
                                        listView.setAdapter(adapter);
                                        Toast.makeText(getApplicationContext(), "没有数据", Toast.LENGTH_SHORT).show();

                                    }
                                } else {
                                    HttpResponseCustom.clear();
                                    adapter.notifyDataSetChanged();
                                }


                            closeDialog();

                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "网络", Toast.LENGTH_SHORT).show();
                            Log.d("lingtana", e.toString());
                            closeDialog();
                        }
                    }
                });


            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:

                PostProductData postDate = new PostProductData();
                postDate.setName("");
                postDate.setCategory_name("");
                postDate.setServerIp(Common.ip);
                postDate.setRequestType(GlobalVariable.cfCatetorySelect);
                postDate.setClassType(1);
                postDate.setServlet("ProductOperate");
                getHttpData(postDate);

                break;
            case 2:
                if(resultCode==RESULT_OK) {

                    cHomeSearch.requestFocusFromTouch();
                    cHomeSearch.setText(data.getStringExtra("scanResult"));

                }
                break;
            default:
                break;
                }
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
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toobar_search_nemu, menu);


        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toobar_search_nemu_night:
                this.finish();
                return true;

            case android.R.id.home:
                Intent openCameraIntent = new Intent(ProductSearchView.this,CommonScanActivity.class);
                openCameraIntent.putExtra(Constant.REQUEST_SCAN_MODE,Constant.REQUEST_SCAN_MODE_ALL_MODE);
                startActivityForResult(openCameraIntent, 2);
                return true;
        }
        return super.onOptionsItemSelected(item);
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
            searchItem(cHomeSearch.getText().toString());


        }
    };

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
