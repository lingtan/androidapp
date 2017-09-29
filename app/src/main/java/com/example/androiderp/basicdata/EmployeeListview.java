package com.example.androiderp.basicdata;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.androiderp.R;
import com.example.androiderp.CustomDataClass.Employee;
import com.example.androiderp.CustomDataClass.PostUserData;
import com.example.androiderp.CustomDataClass.ReturnUserData;
import com.example.androiderp.CustomDataClass.SalesOut;
import com.example.androiderp.adaper.CommonAdapterData;
import com.example.androiderp.adaper.CommonListViewAdapter;
import com.example.androiderp.common.Common;
import com.example.androiderp.common.HttpUtil;
import com.example.androiderp.custom.CustomSearch;
import com.example.androiderp.custom.CustomSearchBase;
import com.example.androiderp.form.EmployeeForm;
import com.example.androiderp.listview.Menu;
import com.example.androiderp.listview.MenuItem;
import com.example.androiderp.listview.SlideAndDragListView;
import com.example.androiderp.listview.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class EmployeeListview extends CustomSearchBase implements View.OnClickListener,
        AdapterView.OnItemClickListener,
        SlideAndDragListView.OnMenuItemClickListener, SlideAndDragListView.OnItemDeleteListener {
    private CommonListViewAdapter adapter;
    private SlideAndDragListView<CommonAdapterData> listView;
    private DisplayMetrics dm;
    private List<Employee> employeeList;
    private TextView toobarBack, toobarAdd, toobarTile;
    private CustomSearch customSearch;
    private Menu menu;
    private List<SalesOut> salesOutList =new ArrayList<SalesOut>();
    private int  getEditPositon = -1;
    private PostUserData postDate = new PostUserData();
    private CommonAdapterData editDate = new CommonAdapterData();
    private List<CommonAdapterData> postUserDataList = new ArrayList<CommonAdapterData>();
    @Override
    public void iniView(){
        setContentView(R.layout.customlistview_category_layout);
        initMenu();
        initUiAndListener();
        toobarBack =(TextView)findViewById(R.id.custom_toobar_left) ;
        toobarTile =(TextView)findViewById(R.id.custom_toobar_midd);
        toobarTile.setText("职员");
        toobarAdd =(TextView)findViewById(R.id.custom_toobar_right);
        toobarBack.setOnClickListener(this);
        toobarAdd.setOnClickListener(this);
        toobarTile.setOnClickListener(this);
        customSearch = (CustomSearch) findViewById(R.id.search);
        toobarTile.setCompoundDrawables(null,null,null,null);



        //构造函数第一参数是类的对象，第二个是布局文件，第三个是数据源
        dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        customSearch.addTextChangedListener(textWatcher);

    }

    private void getHttpData(final PostUserData postPostUserData) {


        HttpUtil.sendOkHttpRequst(postPostUserData, new okhttp3.Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        Toast.makeText(EmployeeListview.this, "网络连接失败", Toast.LENGTH_SHORT).show();

                    }
                });

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            if (postPostUserData.getRequestType().equals("select")) {
                                Gson gson = new Gson();
                                postUserDataList = gson.fromJson(response.body().string(), new TypeToken<List<CommonAdapterData>>() {
                                }.getType());

                                if (postUserDataList.size() != 0) {
                                    adapter = new CommonListViewAdapter(EmployeeListview.this, R.layout.custom_item, postUserDataList);
                                    listView.setAdapter(adapter);


                                } else {
                                    adapter = new CommonListViewAdapter(EmployeeListview.this, R.layout.custom_item, postUserDataList);
                                    listView.setAdapter(adapter);

                                    Toast.makeText(EmployeeListview.this, "没有数据", Toast.LENGTH_SHORT).show();

                                }

                            } else {
                                Gson gson = new Gson();
                                ReturnUserData returnUserData = (ReturnUserData) gson.fromJson(response.body().string(), ReturnUserData.class);
                                if (returnUserData.getResult() > 0) {
                                    Toast.makeText(EmployeeListview.this, "操作成功", Toast.LENGTH_SHORT).show();


                                } else {

                                    Toast.makeText(EmployeeListview.this, "操作失败", Toast.LENGTH_SHORT).show();

                                }

                            }


                        } catch (Exception e) {
                            Toast.makeText(EmployeeListview.this, "网络连接失败", Toast.LENGTH_SHORT).show();

                            adapter = new CommonListViewAdapter(EmployeeListview.this, R.layout.custom_item, postUserDataList);
                            listView.setAdapter(adapter);

                        }
                    }
                });


            }
        });


    }
    public void initMenu() {
        menu = new Menu(true);
        menu.addItem(new MenuItem.Builder().setWidth((int) getResources().getDimension(R.dimen.slv_item_bg_btn_width))
                .setBackground(Utils.getDrawable(this, R.drawable.btn_right0))
                .setText("编辑")
                .setDirection(MenuItem.DIRECTION_RIGHT)
                .setTextColor(Color.BLACK)
                .setTextSize(14)
                .build());
        menu.addItem(new MenuItem.Builder().setWidth((int) getResources().getDimension(R.dimen.slv_item_bg_btn_width_img))
                .setBackground(Utils.getDrawable(this, R.drawable.btn_right1))
                .setDirection(MenuItem.DIRECTION_RIGHT)
                .setText("删除")
                .setTextSize(14)
                .build());
    }

    public void initUiAndListener() {
        listView = (SlideAndDragListView) findViewById(R.id.custom_listview_category);
        listView.setMenu(menu);
        listView.setOnItemClickListener(this);
        listView.setOnMenuItemClickListener(this);
        listView.setOnItemDeleteListener(this);
    }
    @Override
    public int onMenuItemClick(View v, final int itemPosition, int buttonPosition, int direction) {
        switch (direction) {
            case MenuItem.DIRECTION_RIGHT:
                switch (buttonPosition) {
                    case 0:
                        Intent intent=new Intent(EmployeeListview.this,EmployeeForm.class);
                        getEditPositon = itemPosition;
                        intent.putExtra("type", "edit");
                        intent.putExtra("postdata", postUserDataList.get(itemPosition));
                        startActivityForResult(intent,1);

                        return Menu.ITEM_NOTHING;
                    case 1:
                        AlertDialog.Builder dialog=new AlertDialog.Builder(EmployeeListview.this);
                        dialog.setTitle("提示");
                        dialog.setMessage("您确认要删除该职员？");
                        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(isCustom(postUserDataList.get(itemPosition - listView.getHeaderViewsCount()).getName().toString()))
                                {
                                    Toast.makeText(EmployeeListview.this,"已经有业务发生，不能删除",Toast.LENGTH_SHORT).show();
                                    adapter.notifyDataSetChanged();
                                }else {

                                    postDate.setName(postUserDataList.get(itemPosition - listView.getHeaderViewsCount()).getName().toString());
                                    postDate.setRequestType("delete");
                                    postDate.setServerIp(Common.ip);
                                    postDate.setServlet("EmployeeOperate");
                                    postDate.setUnitId(postUserDataList.get(itemPosition - listView.getHeaderViewsCount()).getUnitId());
                                    getHttpData(postDate);

                                    postUserDataList.remove(itemPosition - listView.getHeaderViewsCount());
                                    adapter.notifyDataSetChanged();

                                }


                            }
                        });
                        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        dialog.show();
                    return Menu.ITEM_NOTHING;
                }
        }
        return Menu.ITEM_NOTHING;
    }
    @Override
    public void onItemDelete(View view, int position) {


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



    }
    //筛选条件
    public void  searchItem(String name) {
        postDate.setName(name);
        postDate.setRequestType("select");
        postDate.setServerIp(Common.ip);
        postDate.setServlet("EmployeeOperate");
        getHttpData(postDate);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.custom_toobar_left:
                EmployeeListview.this.finish();
                break;

            case R.id.custom_toobar_midd:


                break;

            case R.id.custom_toobar_right:
                Intent cate = new Intent(EmployeeListview.this, EmployeeForm.class);
                cate.putExtra("type","add");
                startActivityForResult(cate,2);
                break;


        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        postDate.setName("");
        postDate.setRequestType("select");
        postDate.setServerIp(Common.ip);
        postDate.setServlet("EmployeeOperate");
        switch (requestCode){
            case 1:
                if (resultCode == RESULT_OK) {
                    editDate = data.getParcelableExtra("customid");
                    if (editDate != null) {
                        postUserDataList.get(getEditPositon).setUnitId(editDate.getUnitId());
                        postUserDataList.get(getEditPositon).setName(editDate.getName());
                        adapter = new CommonListViewAdapter(EmployeeListview.this, R.layout.custom_item, postUserDataList);
                        listView.setAdapter(adapter);
                        Toast.makeText(EmployeeListview.this, "操作成功", Toast.LENGTH_SHORT).show();
                    }

                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    editDate = data.getParcelableExtra("customid");
                    if (editDate != null) {
                        CommonAdapterData commonAdapterData = new CommonAdapterData();
                        commonAdapterData.setUnitId(editDate.getUnitId());
                        commonAdapterData.setName(editDate.getName());
                        postUserDataList.add(commonAdapterData);
                        adapter = new CommonListViewAdapter(EmployeeListview.this, R.layout.custom_item, postUserDataList);
                        listView.setAdapter(adapter);
                        Toast.makeText(EmployeeListview.this, "操作成功", Toast.LENGTH_SHORT).show();
                    }


                }
                break;
            default:
        }
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

            searchItem(customSearch.getText().toString());

        }
    };

    public boolean isCustom(String name)
    {

        salesOutList =DataSupport.where("salesman =?",name).find(SalesOut.class);

        if (salesOutList.size()>0)
        {
            return true;
        }else {
            return false;
        }

    }
    @Override
    protected void onResume() {
        super.onResume();
        postDate.setName("");
        postDate.setRequestType("select");
        postDate.setServerIp(Common.ip);
        postDate.setServlet("EmployeeOperate");
        getHttpData(postDate);
    }
}
