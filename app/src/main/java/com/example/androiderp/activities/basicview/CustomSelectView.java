package com.example.androiderp.activities.basicview;

import android.app.Dialog;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androiderp.adaper.BasicAdapter;
import com.example.androiderp.bean.AcivityPostBean;
import com.example.androiderp.bean.AdapterBean;
import com.example.androiderp.bean.Custom;
import com.example.androiderp.bean.HttpPostBean;
import com.example.androiderp.bean.PostUserData;
import com.example.androiderp.R;
import com.example.androiderp.adaper.CommonAdapterData;
import com.example.androiderp.tools.Common;
import com.example.androiderp.tools.GlobalVariable;
import com.example.androiderp.tools.HttpUtil;
import com.example.androiderp.ui.CSearch;
import com.example.androiderp.ui.CSearchBase;
import com.example.androiderp.activities.basicfrom.CustomForm;
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

public class CustomSelectView extends CSearchBase implements View.OnClickListener {
    private List<CommonAdapterData> commonAdapterDataList = new ArrayList<CommonAdapterData>();
    private BasicAdapter rightAdapter;
    private BasicAdapter leftAdapter;
    private ListView rightListView;
    private ListView leftListView;
    private DisplayMetrics dm;
    private List<Custom> customList;
    private TextView back, add, tile;
    private CSearch search;
    private String selectCategory;
    private ImageView lastCheckedOption;
    private int rPositon;
    private int lPositon=0;
    private String iName;
    private AcivityPostBean acivityPostBen=new AcivityPostBean();
    private HttpPostBean httpPostBean=new HttpPostBean();
    private  List<AdapterBean>  HttpResponseCategory=new ArrayList<>();
    private  List<AdapterBean>  HttpResponseCategoryTemp=new ArrayList<>();
    private   List<AdapterBean>  HttpResponseCustom=new ArrayList<>();
    private PostUserData postDate = new PostUserData();
    private Dialog dialog;


    @Override
    public void iniView(){
        setContentView(R.layout.custom_listview_layout);
        back =(TextView)findViewById(R.id.custom_toobar_left) ;
        tile =(TextView)findViewById(R.id.custom_toobar_midd);
        add =(TextView)findViewById(R.id.custom_toobar_right);
        back.setOnClickListener(this);
        add.setOnClickListener(this);
        tile.setOnClickListener(this);
        Intent intent=getIntent();
        iName =intent.getStringExtra("index");
        acivityPostBen=intent.getParcelableExtra("acivityPostBen");
        httpPostBean=intent.getParcelableExtra("httpPostBean");
        postDate.setName("");
        postDate.setRequestType(GlobalVariable.cmvCusmtAndCategory);
        postDate.setServerIp(Common.ip);
        postDate.setClassType(httpPostBean.getClassType());
        postDate.setServlet(httpPostBean.getServlet());
        showDialog();
        getHttpData(postDate);
        search = (CSearch) findViewById(R.id.search);
        tile.setText(acivityPostBen.getAcivityName());
        selectCategory="全部";

        //构造函数第一参数是类的对象，第二个是布局文件，第三个是数据源
        leftListView=(ListView) findViewById(R.id.left_list);
        leftListView.setTextFilterEnabled(true);
        rightListView = (ListView) findViewById(R.id.right_list);
        rightListView.setTextFilterEnabled(true);
        leftListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showDialog();
                lPositon=position;
                leftAdapter.setSeclection(lPositon);
                leftAdapter.notifyDataSetChanged();
                selectCategory=HttpResponseCategory.get(position).getName();
                postDate.setName(search.getText().toString());
                if(HttpResponseCategory.get(position).getName().equals("全部")) {
                    postDate.setCategory_name("");

                }else {
                    postDate.setCategory_name(HttpResponseCategory.get(position).getName());
                }
                postDate.setServerIp(Common.ip);
                postDate.setRequestType(GlobalVariable.cfCatetorySelect);
                postDate.setClassType(httpPostBean.getClassType());
                postDate.setServlet(httpPostBean.getServlet());
                getHttpData(postDate);
            }
        });
        dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        if(iName.isEmpty())
        {
            rPositon =-1;
        }
        rightListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {
                Intent intent=getIntent();


                            intent.putExtra("data_return", String.valueOf(HttpResponseCustom.get(position).getName()));
                            iName = HttpResponseCustom.get(position).getName();

                if(lastCheckedOption != null){
                    lastCheckedOption.setVisibility(View.INVISIBLE);
                }
                lastCheckedOption = (ImageView)view.findViewById(R.id.custom_item_layout_one_image);
                lastCheckedOption.setVisibility(View.VISIBLE);
                setResult(RESULT_OK,intent);
                finish();


            }
        });




        search.addTextChangedListener(textWatcher);

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
        postDate.setClassType(httpPostBean.getClassType());
        postDate.setServlet(httpPostBean.getServlet());
        showDialog();
        getHttpData(postDate);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if(resultCode==RESULT_OK)
                {
                    postDate.setName("");
                    postDate.setServerIp(Common.ip);
                    postDate.setRequestType(GlobalVariable.cmvCusmtAndCategory);
                    postDate.setClassType(httpPostBean.getClassType());
                    postDate.setServlet(httpPostBean.getServlet());
                    showDialog();
                    HttpResponseCustom.clear();
                    rightAdapter.notifyDataSetChanged();
                    getHttpData(postDate);



                }
                break;

            default:
                }
        }

    private void getHttpData(final PostUserData postPostUserData) {



        HttpUtil.sendOkHttpRequst(postPostUserData, new okhttp3.Callback() {

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
                                HttpResponseCustom = gson.fromJson(jsonArray.toString(), new TypeToken<List<AdapterBean>>() {
                                }.getType());
                                if(HttpResponseCategory!=null&&HttpResponseCustom!=null) {
                                    for(AdapterBean custom: HttpResponseCustom)

                                    {
                                        if(custom.getName().equals(iName))
                                        {
                                            rPositon = HttpResponseCustom.indexOf(custom);

                                        }
                                        custom.setSelectImage(R.drawable.seclec_arrow);


                                    }
                                    leftAdapter = new BasicAdapter(getApplicationContext(), R.layout.custom_item, HttpResponseCategory);
                                    leftAdapter.setSeclection(lPositon);
                                    selectCategory=HttpResponseCategory.get(0).getName();
                                    leftListView.setAdapter(leftAdapter);
                                    rightAdapter = new BasicAdapter(getApplicationContext(), R.layout.custom_item, HttpResponseCustom);
                                    rightAdapter.setSeclection(rPositon);
                                    rightListView.setAdapter(rightAdapter);
                                }


                            }else
                            {

                                Gson gson = new Gson();
                                rPositon =-1;
                                HttpResponseCustom = gson.fromJson(response.body().string(), new TypeToken<List<AdapterBean>>() {
                                }.getType());
                                if(HttpResponseCustom!=null) {
                                    if (HttpResponseCustom.size() != 0) {

                                        for(AdapterBean custom: HttpResponseCustom)

                                        {
                                            if(custom.getName().equals(iName))
                                            {
                                                rPositon = HttpResponseCustom.indexOf(custom);

                                            }
                                            custom.setSelectImage(R.drawable.seclec_arrow);


                                        }
                                        rightAdapter = new BasicAdapter(getApplicationContext(), R.layout.custom_item, HttpResponseCustom);
                                        rightAdapter.setSeclection(rPositon);
                                        rightListView.setAdapter(rightAdapter);


                                    } else {

                                        rightAdapter = new BasicAdapter(getApplicationContext(), R.layout.custom_item, HttpResponseCustom);
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


    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.custom_toobar_left:
                CustomSelectView.this.finish();
                break;

            case R.id.custom_toobar_right:

                Intent  intent= new Intent(getApplicationContext(), CustomForm.class);
                acivityPostBen.setAcivityName(acivityPostBen.getAcivityName()+"资料");
                httpPostBean.setServlet("ContactOperate");
                httpPostBean.setClassType(httpPostBean.getClassType());
                intent.putExtra("acivityPostBen",acivityPostBen);
                intent.putExtra("httpPostBean",httpPostBean);
                intent.putExtra("type","add");
                startActivityForResult(intent,1);
                break;


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
