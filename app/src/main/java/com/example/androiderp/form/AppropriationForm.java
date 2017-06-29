package com.example.androiderp.form;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androiderp.CustomDataClass.Appropriation;
import com.example.androiderp.CustomDataClass.AppropriationEnty;
import com.example.androiderp.CustomDataClass.Consignment;
import com.example.androiderp.CustomDataClass.Custom;
import com.example.androiderp.CustomDataClass.Employee;
import com.example.androiderp.CustomDataClass.Product;
import com.example.androiderp.CustomDataClass.ProductCategory;
import com.example.androiderp.CustomDataClass.ProductShopping;
import com.example.androiderp.CustomDataClass.SalesOut;
import com.example.androiderp.CustomDataClass.SalesOutEnty;
import com.example.androiderp.CustomDataClass.ShoppingData;
import com.example.androiderp.CustomDataClass.Stock;
import com.example.androiderp.R;
import com.example.androiderp.adaper.AppropriationListViewAdapter;
import com.example.androiderp.adaper.CommonDataStructure;
import com.example.androiderp.adaper.DataStructure;
import com.example.androiderp.adaper.PopuMenuDataStructure;
import com.example.androiderp.adaper.SaleProductListViewAdapter;
import com.example.androiderp.basicdata.ConsignmentListview;
import com.example.androiderp.basicdata.ProductAppropriationListView;
import com.example.androiderp.basicdata.ProductBadgeListView;
import com.example.androiderp.basicdata.SelectCustomListView;
import com.example.androiderp.common.Common;
import com.example.androiderp.custom.CustomSearchBase;
import com.example.androiderp.listview.Menu;
import com.example.androiderp.listview.MenuItem;
import com.example.androiderp.listview.SlideAndDragListView;
import com.example.androiderp.listview.Utils;
import com.example.androiderp.scanning.CommonScanActivity;
import com.example.androiderp.scanning.utils.Constant;

import org.litepal.crud.DataSupport;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by lingtan on 2017/5/15.
 */

public class AppropriationForm extends CustomSearchBase implements View.OnClickListener, AdapterView.OnItemClickListener,
        SlideAndDragListView.OnMenuItemClickListener, SlideAndDragListView.OnItemDeleteListener {
    private InputMethodManager manager;
    private EditText note;
    private LinearLayout saleproduct_add;
    private TextView save,toobar_tile,toobar_back,toobar_add,name,number;
    private DisplayMetrics dm;
    private LinearLayout customLayout,stockLayout,screenLayout;
    private Custom customlist;
    private Stock  stockList;
    private String customid;
    private Drawable errorIcon;
    private Common common;
    private Intent  intentback;
    private List<PopuMenuDataStructure> popuMenuDatas;
    private List<Product> findNumber;
    private List<AppropriationEnty> salesOutEntyList=new ArrayList<AppropriationEnty>();
    private List<ProductShopping> shoppinglist = new ArrayList<ProductShopping>();
    private List<CommonDataStructure> listdatas = new ArrayList<CommonDataStructure>();
    private SlideAndDragListView<CommonDataStructure> plistView;
    private AppropriationListViewAdapter adapter;
    private Menu mMenu;
    private List<Stock> stocks;
    private List<Employee> employees;
    private Calendar cal;
    private int year,month,day;
    private int countall;
    private double countamount;
    private Intent intent;
    public void iniView() {
        setContentView(R.layout.appropriationform);
        initMenu();
        initUiAndListener();
        dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        final Intent intent=getIntent();
        customid=intent.getStringExtra("product_item");
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        name=(TextView)findViewById(R.id.product_custom);
        saleproduct_add=(LinearLayout) findViewById(R.id.saleproduct_add);
        number=(TextView)findViewById(R.id.product_stock);
        note=(EditText)findViewById(R.id.product_note);
        screenLayout=(LinearLayout) findViewById(R.id.number_screen);
        save=(TextView)findViewById(R.id.customtoobar_right);
        toobar_tile=(TextView)findViewById(R.id.customtoobar_midd);
        toobar_back=(TextView)findViewById(R.id.customtoobar_left);
        toobar_add=(TextView)findViewById(R.id.customtoobar_r) ;
        stockLayout=(LinearLayout)findViewById(R.id.product_stock_layout);
        customLayout=(LinearLayout)findViewById(R.id.product_custom_layout);
        stockLayout.setOnClickListener(this);
        customLayout.setOnClickListener(this);
        save.setOnClickListener(this);
        toobar_back.setOnClickListener(this);
        toobar_add.setOnClickListener(this);
        saleproduct_add.setOnClickListener(this);
        save.setCompoundDrawables(null,null,null,null);
        toobar_tile.setCompoundDrawables(null,null,null,null);
        errorIcon = getResources().getDrawable(R.drawable.icon_error);
// 设置图片大小
        errorIcon.setBounds(new Rect(0, 0, errorIcon.getIntrinsicWidth(),
                errorIcon.getIntrinsicHeight()));
        save.setText("保存");
        formInit();
        getDate();

        screenLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent openCameraIntent = new Intent(AppropriationForm.this,CommonScanActivity.class);
                openCameraIntent.putExtra(Constant.REQUEST_SCAN_MODE,Constant.REQUEST_SCAN_MODE_ALL_MODE);
                startActivityForResult(openCameraIntent, 5);
            }
        });
        toobar_tile.setText("调拨单");

    }
    private void  formInit()
    {


        stockList = DataSupport.find(Stock.class, 1);


        if(stockList==null)
        {

        }else {
            name.setText(stockList.getName());
        }










    }
    //获取当前日期
    private void getDate() {
        cal=Calendar.getInstance();
        year=cal.get(Calendar.YEAR);       //获取年月日时分秒
        month=cal.get(Calendar.MONTH);   //获取到的月份是从0开始计数
        day=cal.get(Calendar.DAY_OF_MONTH);

    }
    public void initMenu() {
        mMenu = new Menu(true);
        mMenu.addItem(new MenuItem.Builder().setWidth((int) getResources().getDimension(R.dimen.slv_item_bg_btn_width))
                .setBackground(Utils.getDrawable(this, R.drawable.btn_right0))
                .setText("取消")
                .setDirection(MenuItem.DIRECTION_RIGHT)
                .setTextColor(Color.BLACK)
                .setTextSize(14)
                .build());
        mMenu.addItem(new MenuItem.Builder().setWidth((int) getResources().getDimension(R.dimen.slv_item_bg_btn_width_img))
                .setBackground(Utils.getDrawable(this, R.drawable.btn_right1))
                .setDirection(MenuItem.DIRECTION_RIGHT)
                .setText("删除")
                .setTextSize(14)
                .build());
    }

    public void initUiAndListener() {
        plistView = (SlideAndDragListView) findViewById(R.id.saleproduct_listview);
        plistView.setMenu(mMenu);
        plistView.setOnItemClickListener(this);
        plistView.setOnMenuItemClickListener(this);
        plistView.setOnItemDeleteListener(this);
    }
    @Override
    public int onMenuItemClick(View v, final int itemPosition, int buttonPosition, int direction) {
        switch (direction) {
            case MenuItem.DIRECTION_RIGHT:
                switch (buttonPosition) {
                    case 0:

                        return Menu.ITEM_SCROLL_BACK;
                    case 1:

                                DataStructure.deleteAll(ProductCategory.class,"name = ?",listdatas.get(itemPosition - plistView.getHeaderViewsCount()).getName().toString());


                                  listdatas.remove(itemPosition - plistView.getHeaderViewsCount());
                                   adapter.notifyDataSetChanged();
                                  setListViewHeightBasedOnChildren(plistView);
                        countall=0;
                        countamount=0.00;
                        DecimalFormat df = new DecimalFormat("#####0.00");
                        for(int i = 0; i < listdatas.size(); i++)
                        {

                            countall+= listdatas.get(i).getFqty();
                            countamount+=listdatas.get(i).getSaleamount();
                        }


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


        Intent intent=new Intent(AppropriationForm.this,ScrennProductShoppingForm.class);
        intent.putExtra("action", "edit");
        intent.putExtra("product_item", String.valueOf(listdatas.get(position).getId()));
        startActivityForResult(intent,7);
    }

    private void hintKbTwo() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm.isActive()&&getCurrentFocus()!=null){
            if (getCurrentFocus().getWindowToken()!=null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId())

        {
            case R.id.customtoobar_right:
                if (TextUtils.isEmpty(name.getText().toString())) {
                    Toast.makeText(AppropriationForm.this,"请选择客户",Toast.LENGTH_SHORT).show();

                }else if (TextUtils.isEmpty(number.getText().toString()))
                {
                    Toast.makeText(AppropriationForm.this,"请选择仓库",Toast.LENGTH_SHORT).show();
                }
                else if (listdatas.size()==0)
                {
                    Toast.makeText(AppropriationForm.this,"请选择产品",Toast.LENGTH_SHORT).show();
                }else

                {
                    SimpleDateFormat format=new SimpleDateFormat("yyyyMMddHHmmss");
                    Date curData=new Date(System.currentTimeMillis());
                    String fdate=format.format(curData);
                    for(int i = 0; i < listdatas.size(); i++)
                    {

                        AppropriationEnty salesOutEnty=new AppropriationEnty();
                        salesOutEnty.setItemname(listdatas.get(i).getName());
                        salesOutEnty.setItemnumber(listdatas.get(i).getNumber());
                        salesOutEnty.setItemfqty(listdatas.get(i).getFqty());
                        salesOutEnty.setInstock(name.getText().toString());
                        salesOutEnty.setOutstock(number.getText().toString());
                        salesOutEntyList.add(salesOutEnty);
                    }
                    DataSupport.saveAll(salesOutEntyList);
                    Appropriation salesOut =new Appropriation();
                    salesOut.setSalesOutEntyList(salesOutEntyList);
                    salesOut.setInstock(name.getText().toString());
                    salesOut.setNuber("DBD"+ fdate);
                    salesOut.setOutstock(number.getText().toString());
                    salesOut.setFdate(year+"-"+(++month)+"-"+day);
                    salesOut.save();
                    Toast.makeText(AppropriationForm.this,"新增成功",Toast.LENGTH_SHORT).show();
                    save.setVisibility(View.GONE);
                    toobar_add.setVisibility(View.VISIBLE);


                }

            break;
            case R.id.product_stock_layout:
                showStockWindow();
                if( common.mPopWindow==null ||!common.mPopWindow.isShowing())
                {
                    int xPos = dm.widthPixels / 3;
                    common.mPopWindow.showAsDropDown(number,xPos,5);
                    //mPopWindow.showAtLocation(findViewById(R.id.main), Gravity.BOTTOM, 0, 0);//从底部弹出
                }
                else {
                    common.mPopWindow.dismiss();
                }

                break;

            case R.id.product_custom_layout:

                showEmployeeWindow();

                if( common.mPopWindow==null ||!common.mPopWindow.isShowing())
                {
                    int xPos = dm.widthPixels / 3;
                    common.mPopWindow.showAsDropDown(name,xPos,5);
                    //mPopWindow.showAtLocation(findViewById(R.id.main), Gravity.BOTTOM, 0, 0);//从底部弹出
                }
                else {
                    common.mPopWindow.dismiss();
                }
                break;


            case R.id.customtoobar_left:

                    AppropriationForm.this.finish();

             break;

            case R.id.customtoobar_r:
                if( common.mPopWindow==null ||!common.mPopWindow.isShowing())
                {   popuMenuDatas.clear();

                    PopuMenuDataStructure popuMenub = new PopuMenuDataStructure(R.drawable.poppu_wrie, "销售单新增");
                    popuMenuDatas.add(popuMenub);
                    PopuMenuDataStructure popuMenua = new PopuMenuDataStructure(R.drawable.poppu_wrie, "销售单复制");
                    popuMenuDatas.add(popuMenua);
                    int xPos = dm.widthPixels / 3;
                    showPopupWindow(popuMenuDatas);
                    common.mPopWindow.showAsDropDown(v,0,5);
                    //mPopWindow.showAtLocation(findViewById(R.id.main), Gravity.BOTTOM, 0, 0);//从底部弹出
                }
                else {
                    common.mPopWindow.dismiss();
                }
                break;
            case R.id.saleproduct_add:
                Intent intentbadge=new Intent(AppropriationForm.this, ProductAppropriationListView.class);
                startActivityForResult(intentbadge,6);
                default:

        }
    }
    private void showPopupWindow(final List<PopuMenuDataStructure> popuMenuData) {
        common = new Common();

        common.PopupWindow(AppropriationForm.this, dm, popuMenuData);
        common.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {

                if(popuMenuDatas.get(position).getName().equals("销售单新增"))
                {
                    intent = new Intent(AppropriationForm.this, AppropriationForm.class);
                    startActivity(intent);
                }
                else

                {

                }
                common.mPopWindow.dismiss();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        DecimalFormat df = new DecimalFormat("#####0.00");
        switch (requestCode){

            case 4:
                if(resultCode==RESULT_OK){
                    Intent intent = new Intent();
                    setResult(RESULT_OK,intent);
                   AppropriationForm.this.finish();
                }
                break;

            case 5:
                if(resultCode==RESULT_OK) {

                    countall=0;
                    countamount=0.00;
                    findNumber=DataStructure.where("number = ?",data.getStringExtra("scanResult")).find(Product.class);

                   if(findNumber.size()==0){
                       Toast.makeText(AppropriationForm.this,"找不到条码为"+data.getStringExtra("scanResult")+"商品",Toast.LENGTH_LONG).show();
                   }else {

                       for (Product product : findNumber) {
                           boolean flag=true;
                           for (CommonDataStructure structure : listdatas) {

                               if(structure.getNumber().equals(product.getNumber()))
                               {
                                   structure.setFqty(structure.getFqty()+1);
                                   structure.setSaleamount(structure.getSaleamount()+structure.getSalesprice());
                                   flag=false;
                               }
                           }
                           if(flag==true)
                           {
                               CommonDataStructure commonData = new CommonDataStructure();
                               commonData.setId(product.getId());
                               commonData.setNumber(product.getNumber());
                               commonData.setName(product.getName());
                               commonData.setFqty(1);
                               commonData.setSaleamount(Double.valueOf(product.getSalesprice()));
                               commonData.setSalesprice(Double.valueOf(product.getSalesprice()));

                               listdatas.add(commonData);
                           }

                       }

                       for(int i = 0; i < listdatas.size(); i++)
                       {

                           countall+= listdatas.get(i).getFqty();
                           countamount+=listdatas.get(i).getSaleamount();
                       }
                       adapter = new AppropriationListViewAdapter(AppropriationForm.this, R.layout.saleproduct_item, listdatas);
                       plistView.setAdapter(adapter);
                       setListViewHeightBasedOnChildren(plistView);


                   }
                }

                break;
            case 6:
                if(resultCode==RESULT_OK) {
                    countall=0;
                    countamount=0.00;

                    ShoppingData shoppingData=data.getParcelableExtra("shoppingdata");

                    shoppinglist=shoppingData.getShoppingdata();
                    for(ProductShopping shopping:shoppinglist)
                    {
                        for(int i = 0; i < listdatas.size(); i++)
                        {
                            if(listdatas.get(i).getNumber().equals(shopping.getSalenumber()))
                            {

                                shopping.setSalefqty(shopping.getSalefqty()+listdatas.get(i).getFqty());
                                listdatas.remove(i);
                                i--;
                            }
                        }
                        CommonDataStructure commonData=new CommonDataStructure();
                        commonData.setId(shopping.getId());
                        commonData.setName(shopping.getSalename());
                        commonData.setNumber(shopping.getSalenumber());
                        commonData.setFqty(shopping.getSalefqty());
                        listdatas.add(commonData);
                    }
                    for(int i = 0; i < listdatas.size(); i++)
                    {

                        countall+= listdatas.get(i).getFqty();
                        countamount+=listdatas.get(i).getSaleamount();
                    }

                    adapter = new AppropriationListViewAdapter(AppropriationForm.this, R.layout.saleproduct_item, listdatas);
                    plistView.setAdapter(adapter);
                    setListViewHeightBasedOnChildren(plistView);

                }

                break;

            case 7:
                if(resultCode==RESULT_OK) {
                    countall=0;
                    countamount=0.00;
                    ProductShopping shopping = (ProductShopping) data.getParcelableExtra("shop_data");
                    for ( CommonDataStructure commonData : listdatas)

                    {
                        if (commonData.getNumber().toString().equals(shopping.getSalenumber().toString())) {
                            commonData.setFqty(shopping.getSalefqty());
                            commonData.setSalesprice(shopping.getSalesprice());
                            commonData.setSaleamount(shopping.getSaleamount());
                        }
                    }

                    adapter = new AppropriationListViewAdapter(AppropriationForm.this, R.layout.saleproduct_item, listdatas);
                    plistView.setAdapter(adapter);

                    for(int i = 0; i < listdatas.size(); i++)
                    {

                        countall+= listdatas.get(i).getFqty();
                        countamount+=listdatas.get(i).getSaleamount();
                    }


                }


                break;

            case 8:
                if(resultCode==RESULT_OK){
                    name.setText(data.getStringExtra("data_return"));
                }
                break;

            default:
        }
    }
    private void showStockWindow() {
        common = new Common();
        popuMenuDatas = new ArrayList<PopuMenuDataStructure>();
        stocks= DataSupport.findAll(Stock.class);
        for(Stock stock:stocks)

        {
            PopuMenuDataStructure popuMenua = new PopuMenuDataStructure(R.drawable.poppu_wrie, stock.getName());
            popuMenuDatas.add(popuMenua);

        }
        common.PopupWindow(AppropriationForm.this, dm, popuMenuDatas);
        common.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {

                number.setText(popuMenuDatas.get(position).getName());
                common.mPopWindow.dismiss();
            }
        });
    }

    private void showEmployeeWindow() {
        common = new Common();
        popuMenuDatas = new ArrayList<PopuMenuDataStructure>();
        stocks= DataSupport.findAll(Stock.class);
        for(Stock stock:stocks)

        {
            PopuMenuDataStructure popuMenua = new PopuMenuDataStructure(R.drawable.poppu_wrie, stock.getName());
            popuMenuDatas.add(popuMenua);

        }
        common.PopupWindow(AppropriationForm.this, dm, popuMenuDatas);
        common.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {

                name.setText(popuMenuDatas.get(position).getName());
                common.mPopWindow.dismiss();
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
//根据内容动态测量listview实际高度,动态显示listview内容，此方法，适配器中 getView 方法如果是 RelativeLayout 则显示不正常
    private void setListViewHeightBasedOnChildren(SlideAndDragListView<CommonDataStructure> listView) {
        if (listView == null) {
            return;
        }
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

}
