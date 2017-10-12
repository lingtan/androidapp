package com.example.androiderp.basic;

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

import com.example.androiderp.adaper.BasicAdapter;
import com.example.androiderp.bean.AcivityPostBen;
import com.example.androiderp.bean.AdapterBean;
import com.example.androiderp.bean.PostUserData;
import com.example.androiderp.bean.Product;
import com.example.androiderp.bean.Unit;
import com.example.androiderp.R;
import com.example.androiderp.tools.Common;
import com.example.androiderp.tools.HttpUtil;
import com.example.androiderp.ui.CSearch;
import com.example.androiderp.ui.CSearchBase;
import com.example.androiderp.listview.Menu;
import com.example.androiderp.listview.MenuItem;
import com.example.androiderp.listview.SlideAndDragListView;
import com.example.androiderp.listview.Utils;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class BasicView extends CSearchBase implements View.OnClickListener,
        AdapterView.OnItemClickListener,
        SlideAndDragListView.OnMenuItemClickListener, SlideAndDragListView.OnItemDeleteListener {
    private BasicAdapter adapter;
    private SlideAndDragListView<AdapterBean> listView;
    private DisplayMetrics dm;
    private List<Unit> unitList;
    private TextView toobarBack, toobarAdd, toobarTile;
    private CSearch search;
    private ImageView lastCheckedOption;
    private int indexPositon=-1,getEditPositon = -1;
    private String indexName, searchVale;
    private Menu menu;
    private PostUserData postDate = new PostUserData();
    private AdapterBean editDate = new AdapterBean();
    private List<AdapterBean> HttpResponseList = new ArrayList<AdapterBean>();
    private Common common=new Common();
    private AcivityPostBen acivityPostBen;
    @Override
    public void iniView(){
        Intent intent=getIntent();
        acivityPostBen=intent.getParcelableExtra("acivityPostBen");
        indexName =acivityPostBen.getName();
        postDate.setName("");
        postDate.setRequestType("select");
        postDate.setServerIp(Common.ip);
        postDate.setClassType(acivityPostBen.getSetClassType());
        postDate.setServlet(acivityPostBen.getRequestServlet());

        getHttpData(postDate);
        setContentView(R.layout.customlistview_category_layout);
        initMenu();
        initUiAndListener();
        toobarBack =(TextView)findViewById(R.id.custom_toobar_left) ;
        toobarTile =(TextView)findViewById(R.id.custom_toobar_midd);
        toobarTile.setText("选择"+acivityPostBen.getAcivityName());
        toobarAdd =(TextView)findViewById(R.id.custom_toobar_right);
        toobarBack.setOnClickListener(this);
        toobarAdd.setOnClickListener(this);
        toobarTile.setOnClickListener(this);
        search = (CSearch) findViewById(R.id.search);

        toobarTile.setCompoundDrawables(null,null,null,null);



        //构造函数第一参数是类的对象，第二个是布局文件，第三个是数据源
        dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);


        search.addTextChangedListener(textWatcher);




    }

    private void getHttpData(final PostUserData postPostUserData) {


        HttpUtil.sendOkHttpRequst(postPostUserData, new okhttp3.Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        Toast.makeText(BasicView.this, "网络连接失败", Toast.LENGTH_SHORT).show();

                    }
                });

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            indexPositon = -1;
                            common.JsonUpdateUi(response.body().string(),indexName, postPostUserData.getRequestType(), getApplicationContext(), adapter, R.layout.custom_item, listView);
                            HttpResponseList =common.HttpResponseList;
                            indexPositon=common.indexPositon;


                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "网络连接失败", Toast.LENGTH_SHORT).show();

                            adapter = new BasicAdapter(getApplicationContext(), R.layout.custom_item, HttpResponseList);
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
                        Intent intent=new Intent(BasicView.this,BasicForm.class);
                        getEditPositon = itemPosition;
                        intent.putExtra("type", "edit");
                        intent.putExtra("postdata", HttpResponseList.get(itemPosition));
                        intent.putExtra("acivityPostBen",acivityPostBen);
                        startActivityForResult(intent,1);

                        return Menu.ITEM_NOTHING;
                    case 1:
                        AlertDialog.Builder dialog=new AlertDialog.Builder(BasicView.this);
                        dialog.setTitle("提示");
                        dialog.setMessage("您确认要删除该"+acivityPostBen.getAcivityName()+"?");
                        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                List<Product> productList=DataSupport.where("unit =?", HttpResponseList.get(itemPosition - listView.getHeaderViewsCount()).getName().toString()).find(Product.class);
                                if(productList.size()>0)
                                {
                                    adapter.notifyDataSetChanged();
                                    Toast.makeText(BasicView.this,"业务已经发生不能删除",Toast.LENGTH_SHORT).show();
                                }else {
                                    postDate.setName(HttpResponseList.get(itemPosition - listView.getHeaderViewsCount()).getName().toString());
                                    postDate.setRequestType("delete");
                                    postDate.setServerIp(Common.ip);
                                    postDate.setServlet(acivityPostBen.getRequestServlet());
                                    postDate.setUnitId(HttpResponseList.get(itemPosition - listView.getHeaderViewsCount()).getUnitId());
                                    getHttpData(postDate);
                                    HttpResponseList.remove(itemPosition - listView.getHeaderViewsCount());
                                    adapter = new BasicAdapter(getApplicationContext(), R.layout.custom_item, HttpResponseList);
                                    listView.setAdapter(adapter);
                                    if (search.getText().toString().isEmpty()) {
                                        if (indexPositon == itemPosition) {
                                            indexPositon = -1;
                                        }

                                        adapter.setSeclection(indexPositon);
                                        adapter.notifyDataSetChanged();
                                    } else {
                                        search.setText("");
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
        if(!indexName.isEmpty()) {
            Intent intent = new Intent(BasicView.this, BasicForm.class);

            intent.putExtra("data_return", String.valueOf(HttpResponseList.get(position).getName()));
            indexName = HttpResponseList.get(position).getName();

            setResult(RESULT_OK, intent);

            if (lastCheckedOption != null) {
                lastCheckedOption.setVisibility(View.INVISIBLE);
            }
            lastCheckedOption = (ImageView) view.findViewById(R.id.custom_item_layout_one_image);
            lastCheckedOption.setVisibility(View.VISIBLE);
            indexPositon = position;
            this.finish();
        }
    }
    public void  searchItem(String name) {

        postDate.setName(name);
        postDate.setRequestType("select");
        postDate.setServerIp(Common.ip);
        postDate.setServlet(acivityPostBen.getRequestServlet());
        getHttpData(postDate);
    }
    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.custom_toobar_left:
                Intent intent=getIntent();
                if(indexPositon!=-1) {
                    intent.putExtra("data_return", HttpResponseList.get(indexPositon).getName());
                }
                setResult(RESULT_OK,intent);
                BasicView.this.finish();
                break;

            case R.id.custom_toobar_midd:


                break;

            case R.id.custom_toobar_right:
                Intent cate = new Intent(BasicView.this, BasicForm.class);
                cate.putExtra("type","add");
                cate.putExtra("acivityPostBen",acivityPostBen);
                startActivityForResult(cate,2);
                break;


        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){

            case 1:
                if(resultCode==RESULT_OK)
                {    editDate = data.getParcelableExtra("getPostData");
                    if (editDate != null) {
                        HttpResponseList.get(getEditPositon).setUnitId(editDate.getUnitId());
                        HttpResponseList.get(getEditPositon).setName(editDate.getName());
                        HttpResponseList.get(getEditPositon).setNote(editDate.getNote());
                        adapter = new BasicAdapter(BasicView.this, R.layout.custom_item, HttpResponseList);
                        adapter.setSeclection(indexPositon);
                        listView.setAdapter(adapter);
                        Toast.makeText(BasicView.this, "操作成功", Toast.LENGTH_SHORT).show();
                    }else {
                        adapter = new BasicAdapter(BasicView.this, R.layout.custom_item, HttpResponseList);
                        adapter.setSeclection(indexPositon);
                        listView.setAdapter(adapter);
                    }


                }
                break;

            case 2:
                if(resultCode==RESULT_OK)
                {
                    editDate = data.getParcelableExtra("getPostData");
                    if (editDate != null) {
                        HttpResponseList.add(editDate);
                        adapter = new BasicAdapter(BasicView.this, R.layout.custom_item, HttpResponseList);
                        adapter.setSeclection(indexPositon);
                        listView.setAdapter(adapter);
                        Toast.makeText(BasicView.this, "操作成功", Toast.LENGTH_SHORT).show();
                    }else {
                        adapter = new BasicAdapter(BasicView.this, R.layout.custom_item, HttpResponseList);
                        adapter.setSeclection(indexPositon);
                        listView.setAdapter(adapter);
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

            searchItem(search.getText().toString());
            searchVale= search.getText().toString();



        }
    };

}
