package com.example.androiderp.activities.warehouseform;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androiderp.activities.salesfrom.ShoppingScrennForm;
import com.example.androiderp.bean.AppropriationEnty;
import com.example.androiderp.bean.Product;
import com.example.androiderp.bean.ProductCategory;
import com.example.androiderp.bean.ProductShopping;
import com.example.androiderp.bean.SalesOutEnty;
import com.example.androiderp.bean.ShoppingData;
import com.example.androiderp.bean.Stock;
import com.example.androiderp.bean.StockIniti;
import com.example.androiderp.bean.StockTaking;
import com.example.androiderp.bean.StockTakingEnty;
import com.example.androiderp.R;
import com.example.androiderp.adaper.AppropriationListViewAdapter;
import com.example.androiderp.adaper.CommonAdapterData;
import com.example.androiderp.bean.DataStructure;
import com.example.androiderp.bean.PopuMenuDataStructure;
import com.example.androiderp.activities.purchaseview.ProductStockTakingView;
import com.example.androiderp.activities.warehouseview.StockIntentView;
import com.example.androiderp.tools.Common;
import com.example.androiderp.ui.CSearchBase;
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

public class StockTakingForm extends CSearchBase implements View.OnClickListener, AdapterView.OnItemClickListener,
        SlideAndDragListView.OnMenuItemClickListener, SlideAndDragListView.OnItemDeleteListener {
    private InputMethodManager manager;
    private EditText note;
    private TextView save,toobarTile,toobarBack,toobarAdd,stockname;
    private DisplayMetrics dm;
    private LinearLayout appropriOutLayout,appropriInLayout,productScreenLayout,productAddLayout;
    private Stock  stock;
    private Drawable errorIcon;
    private Common common;
    private List<PopuMenuDataStructure> popuMenuList;
    private List<StockTakingEnty> stockTakingEntyList=new ArrayList<StockTakingEnty>();
    private List<Product> productList;
    private List<ProductShopping> productShoppinglist = new ArrayList<ProductShopping>();
    private List<CommonAdapterData> commonAdapterDataList = new ArrayList<CommonAdapterData>();
    private SlideAndDragListView<CommonAdapterData> listView;
    private AppropriationListViewAdapter adapter;
    private Menu menu;
    private List<Stock> stockList;
    private Calendar calendar;
    private int year,month,day;
    private Intent intent;
    private double quantity;
    private DecimalFormat df = new DecimalFormat("#####0.00");


    public void iniView() {
        setContentView(R.layout.stocktakingform);
        initMenu();
        initUiAndListener();
        dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        final Intent intent=getIntent();
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        stockname=(TextView)findViewById(R.id.stockout);
        productAddLayout=(LinearLayout) findViewById(R.id.add_layout);
        note=(EditText)findViewById(R.id.note);
        productScreenLayout=(LinearLayout) findViewById(R.id.screen_layout);
        save=(TextView)findViewById(R.id.customtoobar_right);
        toobarTile=(TextView)findViewById(R.id.customtoobar_midd);
        toobarBack=(TextView)findViewById(R.id.customtoobar_left);
        toobarAdd=(TextView)findViewById(R.id.customtoobar_r) ;
        appropriInLayout=(LinearLayout)findViewById(R.id.stockin_layout);
        appropriOutLayout=(LinearLayout)findViewById(R.id.stockout_layout);
        appropriInLayout.setOnClickListener(this);
        appropriOutLayout.setOnClickListener(this);
        save.setOnClickListener(this);
        toobarBack.setOnClickListener(this);
        toobarAdd.setOnClickListener(this);
        productAddLayout.setOnClickListener(this);
        save.setCompoundDrawables(null,null,null,null);
        toobarTile.setCompoundDrawables(null,null,null,null);
        errorIcon = getResources().getDrawable(R.drawable.icon_error);
// 设置图片大小
        errorIcon.setBounds(new Rect(0, 0, errorIcon.getIntrinsicWidth(),
                errorIcon.getIntrinsicHeight()));
        save.setText("保存");
        formInit();
        getDate();


        productScreenLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent openCameraIntent = new Intent(StockTakingForm.this,CommonScanActivity.class);
                openCameraIntent.putExtra(Constant.REQUEST_SCAN_MODE,Constant.REQUEST_SCAN_MODE_ALL_MODE);
                startActivityForResult(openCameraIntent, 5);
            }
        });
        toobarTile.setText("盘点作业");

    }
    private void  formInit()
    {


        stock = DataSupport.findFirst(Stock.class);


        if(stock==null)
        {

        }else {
            stockname.setText(stock.getName());
        }










    }
    //获取当前日期
    private void getDate() {
        calendar=Calendar.getInstance();
        year=calendar.get(Calendar.YEAR);       //获取年月日时分秒
        month=calendar.get(Calendar.MONTH);   //获取到的月份是从0开始计数
        day=calendar.get(Calendar.DAY_OF_MONTH);

    }
    public void initMenu() {
        menu = new Menu(true);
        menu.addItem(new MenuItem.Builder().setWidth((int) getResources().getDimension(R.dimen.slv_item_bg_btn_width))
                .setBackground(Utils.getDrawable(this, R.drawable.btn_right0))
                .setText("取消")
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
        listView = (SlideAndDragListView) findViewById(R.id.saleproduct_listview);
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

                        return Menu.ITEM_SCROLL_BACK;
                    case 1:

                                DataStructure.deleteAll(ProductCategory.class,"name = ?", commonAdapterDataList.get(itemPosition - listView.getHeaderViewsCount()).getName().toString());


                        commonAdapterDataList.remove(itemPosition - listView.getHeaderViewsCount());
                                   adapter.notifyDataSetChanged();
                                  setListViewHeightBasedOnChildren(listView);



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


        Intent intent=new Intent(StockTakingForm.this,ShoppingScrennForm.class);
        intent.putExtra("action", "edit");
        intent.putExtra("product_item", String.valueOf(commonAdapterDataList.get(position).getUnitId()));
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
                if (TextUtils.isEmpty(stockname.getText().toString())) {
                    Toast.makeText(StockTakingForm.this,"请选择仓库",Toast.LENGTH_SHORT).show();

                }
                else if (commonAdapterDataList.size()==0) {
                    Toast.makeText(StockTakingForm.this, "请选择产品", Toast.LENGTH_SHORT).show();
                }else

                { SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
                    Date curData = new Date(System.currentTimeMillis());
                    String fdate = format.format(curData);
                    for(int i = 0; i < commonAdapterDataList.size(); i++)
                    {
                        stockCheck(stockname.getText().toString(), commonAdapterDataList.get(i).getName(), commonAdapterDataList.get(i).getNumber(), commonAdapterDataList.get(i).getFqty());

                    }
                    DataSupport.saveAll(stockTakingEntyList);
                    StockTaking stockTaking = new StockTaking();
                    stockTaking.setStockTakingEntyList(stockTakingEntyList);
                    stockTaking.setStock(stockname.getText().toString());
                    stockTaking.setNuber("DBD" + fdate);
                    stockTaking.setStock(stockname.getText().toString());
                    stockTaking.setDate(String.valueOf(year + "-" + (++month) + "-" + day));
                    stockTaking.setNote(note.getText().toString());
                    stockTaking.save();

                        Toast.makeText(StockTakingForm.this, "新增成功", Toast.LENGTH_SHORT).show();
                        save.setVisibility(View.GONE);
                        toobarAdd.setVisibility(View.VISIBLE);


                }

            break;
            case R.id.stockout_layout:

                showEmployeeWindow();
                if(stockList.size()>0) {
                    if (common.mPopWindow == null || !common.mPopWindow.isShowing()) {
                        int xPos = dm.widthPixels / 3;
                        common.mPopWindow.showAsDropDown(stockname, xPos, 5);
                        //mPopWindow.showAtLocation(findViewById(R.id.main), Gravity.BOTTOM, 0, 0);//从底部弹出
                    } else {
                        common.mPopWindow.dismiss();
                    }
                }else {
                    Intent intentstock=new Intent(StockTakingForm.this, StockIntentView.class);
                    intentstock.putExtra("index",stockname.getText().toString());
                    startActivityForResult(intentstock,11);
                }
                break;


            case R.id.customtoobar_left:
                if(commonAdapterDataList.size()>0)
                {
                    AlertDialog.Builder dialog=new AlertDialog.Builder(StockTakingForm.this);
                    dialog.setTitle("提示");
                    dialog.setMessage("单据还没保存，确认要退出？");
                    dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            StockTakingForm.this.finish();

                        }
                    });
                    dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    dialog.show();
                }else {
                    StockTakingForm.this.finish();
                }



             break;

            case R.id.customtoobar_r:
                if( common.mPopWindow==null ||!common.mPopWindow.isShowing())
                {   popuMenuList.clear();

                    PopuMenuDataStructure popuMenub = new PopuMenuDataStructure(R.drawable.poppu_wrie, "销售单新增");
                    popuMenuList.add(popuMenub);
                    PopuMenuDataStructure popuMenua = new PopuMenuDataStructure(R.drawable.poppu_wrie, "销售单复制");
                    popuMenuList.add(popuMenua);
                    int xPos = dm.widthPixels / 3;
                    showPopupWindow(popuMenuList);
                    common.mPopWindow.showAsDropDown(v,0,5);
                    //mPopWindow.showAtLocation(findViewById(R.id.main), Gravity.BOTTOM, 0, 0);//从底部弹出
                }
                else {
                    common.mPopWindow.dismiss();
                }
                break;
            case R.id.add_layout:
                Intent intentbadge=new Intent(StockTakingForm.this, ProductStockTakingView.class);
                intentbadge.putExtra("appropriout",stockname.getText().toString());
                startActivityForResult(intentbadge,6);
                default:

        }
    }
    private void showPopupWindow(final List<PopuMenuDataStructure> popuMenuData) {
        common = new Common();

        common.PopupWindow(StockTakingForm.this, dm, popuMenuData);
        common.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {

                if(popuMenuList.get(position).getName().equals("销售单新增"))
                {
                    intent = new Intent(StockTakingForm.this, StockTakingForm.class);
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

        switch (requestCode){

            case 4:
                if(resultCode==RESULT_OK){
                    Intent intent = new Intent();
                    setResult(RESULT_OK,intent);
                   StockTakingForm.this.finish();
                }
                break;

            case 5:
                if(resultCode==RESULT_OK) {
                    productList=DataStructure.where("number = ?",data.getStringExtra("scanResult")).find(Product.class);

                   if(productList.size()==0){
                       Toast.makeText(StockTakingForm.this,"找不到条码为"+data.getStringExtra("scanResult")+"商品",Toast.LENGTH_LONG).show();
                   }else {

                       for (Product product : productList) {
                           boolean flag=true;
                           double  temQty=0.0;
                           for (CommonAdapterData structure : commonAdapterDataList) {

                               if(structure.getNumber().equals(product.getNumber()))
                               {
                                   structure.setFqty(structure.getFqty()+1);
                                   structure.setSaleamount(structure.getSaleamount()+structure.getSalesprice());
                                   flag=false;
                               }
                               temQty=structure.getFqty();
                           }
                           if(flag==true)
                           {
                               CommonAdapterData commonData = new CommonAdapterData();
                               commonData.setUnitId(product.getId());
                               commonData.setNumber(product.getNumber());
                               commonData.setName(product.getName());
                               commonData.setFqty(1);
                               commonData.setSaleamount(Double.valueOf(product.getSalesPrice()));
                               commonData.setSalesprice(Double.valueOf(product.getSalesPrice()));
                              temQty=commonData.getFqty();
                               commonAdapterDataList.add(commonData);
                           }


                       }

                       adapter = new AppropriationListViewAdapter(StockTakingForm.this, R.layout.saleproduct_item, commonAdapterDataList);
                       listView.setAdapter(adapter);
                       setListViewHeightBasedOnChildren(listView);


                   }
                }

                break;
            case 6:
                if(resultCode==RESULT_OK) {
                    ShoppingData shoppingData=data.getParcelableExtra("shoppingdata");

                    productShoppinglist=shoppingData.getProductShoppingList();
                    for(ProductShopping shopping:productShoppinglist)
                    {
                        for(int i = 0; i < commonAdapterDataList.size(); i++)
                        {
                            if(commonAdapterDataList.get(i).getNumber().equals(shopping.getNumber()))
                            {

                                shopping.setQuantity(shopping.getQuantity()+ commonAdapterDataList.get(i).getFqty());
                                commonAdapterDataList.remove(i);
                                i--;
                            }
                        }
                        CommonAdapterData commonData=new CommonAdapterData();
                        commonData.setUnitId(shopping.getId());
                        commonData.setName(shopping.getName());
                        commonData.setNumber(shopping.getNumber());
                        commonData.setFqty(shopping.getQuantity());
                        commonAdapterDataList.add(commonData);
                    }

                    adapter = new AppropriationListViewAdapter(StockTakingForm.this, R.layout.saleproduct_item, commonAdapterDataList);
                    listView.setAdapter(adapter);
                    setListViewHeightBasedOnChildren(listView);

                }

                break;

            case 7:
                if(resultCode==RESULT_OK) {
                    ProductShopping shopping = (ProductShopping) data.getParcelableExtra("shop_data");
                    for ( CommonAdapterData commonData : commonAdapterDataList)

                    {
                        if (commonData.getNumber().toString().equals(shopping.getNumber().toString())) {
                            commonData.setFqty(shopping.getQuantity());
                            commonData.setSalesprice(shopping.getPrice());
                            commonData.setSaleamount(shopping.getAmount());
                        }


                    }

                    adapter = new AppropriationListViewAdapter(StockTakingForm.this, R.layout.saleproduct_item, commonAdapterDataList);
                    listView.setAdapter(adapter);

                }


                break;

            case 11:
                if(resultCode==RESULT_OK){
                    stockname.setText(data.getStringExtra("data_return"));
                }
                break;

            default:
        }
    }


    private void showEmployeeWindow() {
        common = new Common();
        popuMenuList = new ArrayList<PopuMenuDataStructure>();
        stockList= DataSupport.findAll(Stock.class);
        for(Stock stock:stockList)

        {
            PopuMenuDataStructure popuMenua = new PopuMenuDataStructure(R.drawable.poppu_wrie, stock.getName());
            popuMenuList.add(popuMenua);

        }
        common.PopupWindow(StockTakingForm.this, dm, popuMenuList);
        common.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {

                stockname.setText(popuMenuList.get(position).getName());
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
    private void setListViewHeightBasedOnChildren(SlideAndDragListView<CommonAdapterData> listView) {
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


    private void stockCheck(String stockname,String name,String number,double sfqty)
    {

        double  in=DataSupport.where("stockIn=? and number=?",stockname,number).sum(AppropriationEnty.class,"quantity",double.class);
        double  out=DataSupport.where("stockOut=? and number=?",stockname,number).sum(AppropriationEnty.class,"quantity",double.class);
        double  initis=DataSupport.where("stock=? and number=?",stockname,number).sum(StockIniti.class,"quantity",double.class);
        double  stocktaking=DataSupport.where("stock=? and number=?",stockname,number).sum(StockTakingEnty.class,"quantity",double.class);
        double   salesOut=DataSupport.where("billtype =? and stock=? and number=?","2",stockname,number).sum(SalesOutEnty.class,"quantity",double.class);
        double  supplierin=DataSupport.where("billtype =? and stock=? and number=?","1",stockname,number).sum(SalesOutEnty.class,"quantity",double.class);
        quantity=0.00;
        quantity=initis+supplierin+stocktaking+in-salesOut-out;

        StockTakingEnty stockTakingEnty=new StockTakingEnty();
        stockTakingEnty.setName(name);
        stockTakingEnty.setNumber(number);
        stockTakingEnty.setStock(stockname);
        stockTakingEnty.setQuantity(sfqty-quantity);
        stockTakingEntyList.add(stockTakingEnty);
    }

}
