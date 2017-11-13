package com.example.androiderp.basic;

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

import com.example.androiderp.R;
import com.example.androiderp.adaper.BasicAdapter;
import com.example.androiderp.bean.AdapterBean;
import com.example.androiderp.bean.HttpPostBean;
import com.example.androiderp.bean.ReceiveParamet;
import com.example.androiderp.interfaces.OerationList;
import com.example.androiderp.listview.Menu;
import com.example.androiderp.listview.MenuItem;
import com.example.androiderp.listview.SlideAndDragListView;
import com.example.androiderp.listview.Utils;
import com.example.androiderp.tools.Common;
import com.example.androiderp.tools.HttpUtil;
import com.example.androiderp.ui.CSearch;
import com.example.androiderp.ui.CSearchBase;
import com.example.androiderp.ui.DataLoadingDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class BasicView extends CSearchBase implements View.OnClickListener,
        AdapterView.OnItemClickListener,
        SlideAndDragListView.OnMenuItemClickListener, SlideAndDragListView.OnItemDeleteListener,OerationList {
    private BasicAdapter adapter;
    private SlideAndDragListView<AdapterBean> listView;
    private DisplayMetrics dm;
    private TextView back, add, tile;
    private CSearch search;
    private ImageView lastCheckedOption;
    private int iPositon = -1;
    private String iName;
    private Menu menu;
    private List<AdapterBean> HttpResponseList = new ArrayList<AdapterBean>();
    private Common common = new Common();
    private HttpPostBean httpPostBean;
    private Dialog dialog;
    private int itemPositionTemp;
    private final ReceiveParamet findParamet=new ReceiveParamet();
    private final ReceiveParamet deleteParamet=new ReceiveParamet();
    private final ReceiveParamet findByNameParamet=new ReceiveParamet();
    private String acivityName,selectName="",isSelect,tableName;

    @Override
    public void iniView() {
        Intent intent = getIntent();
        acivityName = intent.getStringExtra("acivityName");
        isSelect = intent.getStringExtra("isSelect");
        tableName = intent.getStringExtra("tableName");
        httpPostBean = intent.getParcelableExtra("httpPostBean");
        iName = selectName;
        findParamet.setTableName(tableName);
        findAll("findAllByBrand",findParamet);
        setContentView(R.layout.customlistview_category_layout);
        initMenu();
        initUiAndListener();
        back = (TextView) findViewById(R.id.custom_toobar_left);
        tile = (TextView) findViewById(R.id.custom_toobar_midd);
        tile.setText("选择" + acivityName);
        add = (TextView) findViewById(R.id.custom_toobar_right);
        back.setOnClickListener(this);
        add.setOnClickListener(this);
        tile.setOnClickListener(this);
        search = (CSearch) findViewById(R.id.search);

        tile.setCompoundDrawables(null, null, null, null);


        //构造函数第一参数是类的对象，第二个是布局文件，第三个是数据源
        dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);


        search.addTextChangedListener(textWatcher);
        adapter = new BasicAdapter(getApplicationContext(), R.layout.custom_item, HttpResponseList);
        listView.setAdapter(adapter);

    }


    @Override
    public void findAll(String address,ReceiveParamet paramet) {

        showDialog();

        HttpUtil.sendOkHttpRequst(address,paramet, new okhttp3.Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeDialog();

                        Toast.makeText(BasicView.this, "网络连接失败", Toast.LENGTH_SHORT).show();

                    }
                });

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {


                   resultHandler(response.body().string(),1);

            }
        });


    }

    @Override
    public void findByName(String address,ReceiveParamet paramet) {

        showDialog();

        HttpUtil.sendOkHttpRequst(address,paramet, new okhttp3.Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeDialog();

                        Toast.makeText(BasicView.this, "网络连接失败", Toast.LENGTH_SHORT).show();

                    }
                });

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {


                resultHandler(response.body().string(),1);

            }
        });



    }

    @Override
    public void deleteById(String address,ReceiveParamet paramet) {

        showDialog();

        HttpUtil.sendOkHttpRequst(address,paramet, new okhttp3.Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeDialog();

                        Toast.makeText(BasicView.this, "网络连接失败", Toast.LENGTH_SHORT).show();

                    }
                });

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {


                resultHandler(response.body().string(),2);

            }
        });



    }


    @Override
    public void resultHandler(final String result, final int type) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {

                    common.JsonUpdateUi(result, iName,HttpResponseList, type, getApplicationContext(), adapter, R.layout.custom_item, listView);

                    closeDialog();

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "网络连", Toast.LENGTH_SHORT).show();
                    adapter = new BasicAdapter(getApplicationContext(), R.layout.custom_item, HttpResponseList);
                    listView.setAdapter(adapter);
                    closeDialog();

                }

                if(type==2) {
                    if(common.returnResult==-2)
                    {

                    }else {
                        HttpResponseList.remove(itemPositionTemp - listView.getHeaderViewsCount());
                        adapter.notifyDataSetChanged();
                        if (search.getText().toString().isEmpty()) {
                            if (iPositon == itemPositionTemp) {
                                iPositon = -1;
                            }

                            adapter.setSeclection(iPositon);
                            adapter.notifyDataSetChanged();
                        } else {
                            search.setText("");
                        }
                    }

                }

                closeDialog();

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

                        Intent intent = new Intent(BasicView.this, BasicForm.class);
                        intent.putExtra("acivityName",acivityName);
                        intent.putExtra("actionType","edit");
                        intent.putExtra("postdata", HttpResponseList.get(itemPosition));
                        intent.putExtra("httpPostBean", httpPostBean);
                        intent.putExtra("tableName", tableName);
                        startActivityForResult(intent, 1);

                        return Menu.ITEM_NOTHING;
                    case 1:
                        AlertDialog.Builder dialog = new AlertDialog.Builder(BasicView.this);
                        dialog.setTitle("提示");
                        dialog.setMessage("您确认要删除该" + acivityName + "?");
                        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                    itemPositionTemp=itemPosition;
                                    deleteParamet.setTableName(tableName);
                                    deleteParamet.setId(String.valueOf(HttpResponseList.get(itemPosition - listView.getHeaderViewsCount()).getId()));
                                   deleteById("deleteByBrand",deleteParamet);




                            }
                        });
                        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                adapter.notifyDataSetChanged();

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
        if (isSelect.equals("YES")) {
            Intent intent = new Intent();
            intent.putExtra("data_return", HttpResponseList.get(position));
            iName = HttpResponseList.get(position).getName();
            setResult(RESULT_OK, intent);

            if (lastCheckedOption != null) {
                lastCheckedOption.setVisibility(View.INVISIBLE);
            }
            lastCheckedOption = (ImageView) view.findViewById(R.id.custom_item_layout_one_image);
            lastCheckedOption.setVisibility(View.VISIBLE);
            iPositon = position;
            this.finish();
        }
    }

    public void searchItem(String name) {
        findByNameParamet.setTableName(tableName);
        findByNameParamet.setName(name);
        findByName("findNameByBrand",findByNameParamet);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.custom_toobar_left:
                Intent intent = getIntent();
                if (iPositon != -1) {
                    intent.putExtra("data_return", HttpResponseList.get(iPositon).getName());
                }
                setResult(RESULT_OK, intent);
                BasicView.this.finish();
                break;

            case R.id.custom_toobar_midd:


                break;

            case R.id.custom_toobar_right:
                Intent cate = new Intent(BasicView.this, BasicForm.class);
                cate.putExtra("actionType","add");
                cate.putExtra("acivityName",acivityName);
                cate.putExtra("tableName", tableName);
                cate.putExtra("httpPostBean", httpPostBean);
                startActivityForResult(cate, 2);
                break;


        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case 1:
                if (resultCode == RESULT_OK) {
                    findAll("findAllByBrand",findParamet);



                }
                break;

            case 2:
                if (resultCode == RESULT_OK) {
                    findAll("findAllByBrand",findParamet);
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

            searchItem(search.getText().toString());

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
