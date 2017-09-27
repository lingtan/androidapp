package com.example.androiderp.basicdata;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androiderp.CustomDataClass.Product;
import com.example.androiderp.CustomDataClass.ResultData;
import com.example.androiderp.CustomDataClass.TestUser;
import com.example.androiderp.R;
import com.example.androiderp.adaper.CommonAdapterData;
import com.example.androiderp.adaper.CommonListViewAdapter;
import com.example.androiderp.common.Common;
import com.example.androiderp.common.HttpUtil;
import com.example.androiderp.custom.CustomSearch;
import com.example.androiderp.custom.CustomSearchBase;
import com.example.androiderp.form.TestUserForm;
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

public class TestUserListView extends CustomSearchBase implements View.OnClickListener,
        AdapterView.OnItemClickListener,
        SlideAndDragListView.OnMenuItemClickListener, SlideAndDragListView.OnItemDeleteListener {
    private List<CommonAdapterData> listdatas = new ArrayList<CommonAdapterData>();
    private CommonListViewAdapter adapter;
    private SlideAndDragListView<CommonAdapterData> listView;
    private DisplayMetrics dm;
    private List<TestUser> testUserList = new ArrayList<TestUser>();
    private TextView toobarBack, toobarAdd, toobarTile;
    private CustomSearch customSearch;
    private ImageView lastCheckedOption;
    private int indexPositon = -1, editPositon = -1;
    private String indexName;
    private Menu menu;
    private Dialog dialog;
    private TestUser postDate = new TestUser();
    private CommonAdapterData editDate = new CommonAdapterData();


    @Override
    public void iniView() {
        setContentView(R.layout.customlistview_category_layout);
        initMenu();
        initUiAndListener();
        toobarBack = (TextView) findViewById(R.id.custom_toobar_left);
        toobarTile = (TextView) findViewById(R.id.custom_toobar_midd);
        toobarAdd = (TextView) findViewById(R.id.custom_toobar_right);
        toobarBack.setOnClickListener(this);
        toobarAdd.setOnClickListener(this);
        toobarTile.setOnClickListener(this);
        customSearch = (CustomSearch) findViewById(R.id.search);
        Intent intent = getIntent();
        indexName = intent.getStringExtra("index");

        toobarTile.setCompoundDrawables(null, null, null, null);
        toobarTile.setText("选择品牌");


        //构造函数第一参数是类的对象，第二个是布局文件，第三个是数据源
        dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);


        customSearch.addTextChangedListener(textWatcher);


    }

    private void getHttpData(final TestUser postTestUser) {


        HttpUtil.sendOkHttpRequst(postTestUser, new okhttp3.Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        Toast.makeText(TestUserListView.this, "网络连接失败", Toast.LENGTH_SHORT).show();

                    }
                });

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            if (postTestUser.getRequestType().equals("select")) {
                                indexPositon = -1;
                                Gson gson = new Gson();
                                testUserList = gson.fromJson(response.body().string(), new TypeToken<List<TestUser>>() {
                                }.getType());
                                listdatas.clear();
                                for (TestUser testUser : testUserList)

                                {
                                    if (testUser.getName().equals(indexName)) {
                                        indexPositon = testUserList.indexOf(testUser);
                                    }
                                    CommonAdapterData commonData = new CommonAdapterData();
                                    commonData.setName(testUser.getName());
                                    commonData.setId(testUser.getUnitId());


                                    commonData.setImage(R.drawable.seclec_arrow);
                                    listdatas.add(commonData);


                                }
                                if (listdatas.size() != 0) {
                                    adapter = new CommonListViewAdapter(TestUserListView.this, R.layout.custom_item, listdatas);
                                    adapter.setSeclection(indexPositon);
                                    listView.setAdapter(adapter);


                                } else {
                                    Toast.makeText(TestUserListView.this, "没有数据", Toast.LENGTH_SHORT).show();

                                }

                            } else {
                                Gson gson = new Gson();
                                ResultData resultData = (ResultData) gson.fromJson(response.body().string(), ResultData.class);
                                if (resultData.getResult() > 0) {
                                    Toast.makeText(TestUserListView.this, "操作成功", Toast.LENGTH_SHORT).show();


                                } else {

                                    Toast.makeText(TestUserListView.this, "操作失败", Toast.LENGTH_SHORT).show();

                                }

                            }


                        } catch (Exception e) {
                            Toast.makeText(TestUserListView.this, "网络连接失败", Toast.LENGTH_SHORT).show();

                            adapter = new CommonListViewAdapter(TestUserListView.this, R.layout.custom_item, listdatas);
                            adapter.setSeclection(indexPositon);
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
                        Intent intent = new Intent(TestUserListView.this, TestUserForm.class);
                        editPositon = itemPosition;
                        intent.putExtra("action", "edit");
                        intent.putExtra("customid", listdatas.get(itemPosition));
                        startActivityForResult(intent, 1);

                        return Menu.ITEM_NOTHING;
                    case 1:
                        AlertDialog.Builder dialog = new AlertDialog.Builder(TestUserListView.this);
                        dialog.setTitle("提示");
                        dialog.setMessage("您确认要删除该品牌？");
                        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                List<Product> productList = DataSupport.where("brand =?", listdatas.get(itemPosition - listView.getHeaderViewsCount()).getName().toString()).find(Product.class);
                                if (productList.size() > 0) {
                                    adapter.notifyDataSetChanged();
                                    Toast.makeText(TestUserListView.this, "业务已经发生不能删除", Toast.LENGTH_SHORT).show();
                                } else {
                                    postDate.setName(listdatas.get(itemPosition - listView.getHeaderViewsCount()).getName().toString());
                                    postDate.setRequestType("delete");
                                    postDate.setServerIp(Common.ip);
                                    postDate.setServlet("UnitOperate");
                                    postDate.setUnitId(listdatas.get(itemPosition - listView.getHeaderViewsCount()).getId());
                                    getHttpData(postDate);
                                    listdatas.remove(itemPosition - listView.getHeaderViewsCount());
                                    if (customSearch.getText().toString().isEmpty()) {
                                        if (indexPositon == itemPosition) {
                                            indexPositon = -1;
                                        }

                                        adapter.setSeclection(indexPositon);
                                        adapter.notifyDataSetChanged();
                                    } else {
                                        customSearch.setText("");
                                    }

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


        Intent intent = getIntent();
        intent.putExtra("data_return", String.valueOf(listdatas.get(position).getName()));
        indexName = listdatas.get(position).getName();

        setResult(RESULT_OK, intent);

        if (lastCheckedOption != null) {
            lastCheckedOption.setVisibility(View.INVISIBLE);
        }
        lastCheckedOption = (ImageView) view.findViewById(R.id.custom_item_layout_one_image);
        lastCheckedOption.setVisibility(View.VISIBLE);
        indexPositon = position;
        this.finish();
    }

    //筛选条件
    public void searchItem(String name) {
        if (listdatas.size() > 0) {
            listdatas.clear();
        }
        postDate.setName(name);
        postDate.setRequestType("select");
        postDate.setServerIp(Common.ip);
        postDate.setServlet("UnitOperate");
        getHttpData(postDate);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.custom_toobar_left:
                Intent intent = getIntent();
                if (indexPositon != -1) {
                    intent.putExtra("data_return", listdatas.get(indexPositon).getName());
                }
                setResult(RESULT_OK, intent);
                TestUserListView.this.finish();
                break;

            case R.id.custom_toobar_midd:


                break;

            case R.id.custom_toobar_right:
                Intent cate = new Intent(TestUserListView.this, TestUserForm.class);
                cate.putExtra("action", "add");
                startActivityForResult(cate, 2);
                break;


        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        postDate.setName("");
        postDate.setRequestType("select");
        postDate.setServerIp(Common.ip);
        postDate.setServlet("UnitOperate");
        switch (requestCode) {

            case 1:
                if (resultCode == RESULT_OK) {
                    editDate = data.getParcelableExtra("customid");
                    if (editDate != null) {
                        listdatas.get(editPositon).setId(editDate.getId());
                        listdatas.get(editPositon).setName(editDate.getName());
                        adapter = new CommonListViewAdapter(TestUserListView.this, R.layout.custom_item, listdatas);
                        adapter.setSeclection(indexPositon);
                        listView.setAdapter(adapter);
                        Toast.makeText(TestUserListView.this, "操作成功", Toast.LENGTH_SHORT).show();
                    }

                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    editDate = data.getParcelableExtra("customid");
                    if (editDate != null) {
                        CommonAdapterData commonAdapterData = new CommonAdapterData();
                        commonAdapterData.setId(editDate.getId());
                        commonAdapterData.setName(editDate.getName());
                        listdatas.add(commonAdapterData);
                        adapter = new CommonListViewAdapter(TestUserListView.this, R.layout.custom_item, listdatas);
                        adapter.setSeclection(indexPositon);
                        listView.setAdapter(adapter);
                        Toast.makeText(TestUserListView.this, "操作成功", Toast.LENGTH_SHORT).show();
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

            searchItem(customSearch.getText().toString().trim());
            customSearch.getText().toString();

        }
    };

    /**
     * 显示进度对话框
     */
    private void showDialog() {

        dialog = Common.createLoadingDialog(this, "正在加载中...");
        dialog.setCancelable(true);//允许返回
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


    @Override
    protected void onResume() {
        super.onResume();
        postDate.setName("");
        postDate.setRequestType("select");
        postDate.setServerIp(Common.ip);
        postDate.setServlet("UnitOperate");
        getHttpData(postDate);
    }
}
