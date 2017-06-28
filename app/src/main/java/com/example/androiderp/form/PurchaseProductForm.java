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
import com.example.androiderp.CustomDataClass.Supplier;
import com.example.androiderp.R;
import com.example.androiderp.adaper.CommonDataStructure;
import com.example.androiderp.adaper.DataStructure;
import com.example.androiderp.adaper.PopuMenuDataStructure;
import com.example.androiderp.adaper.SaleProductListViewAdapter;
import com.example.androiderp.basicdata.ConsignmentListview;
import com.example.androiderp.basicdata.EmployeeListview;
import com.example.androiderp.basicdata.ProductBadgeListView;
import com.example.androiderp.basicdata.SelectCustomListView;
import com.example.androiderp.basicdata.SelectSupplierListView;
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

public class PurchaseProductForm extends CustomSearchBase implements View.OnClickListener, AdapterView.OnItemClickListener,
        SlideAndDragListView.OnMenuItemClickListener, SlideAndDragListView.OnItemDeleteListener {
    private InputMethodManager manager;
    private EditText note;
    private LinearLayout saleproduct_add;
    private TextView save,toobar_tile,toobar_back,toobar_add,category,name,number,data,consignment,totalamout,totalfqty;
    private DisplayMetrics dm;
    private LinearLayout categoryLayout,customLayout,stockLayout,dataLayout,consignmentLayout,screenLayout,totalLayout;
    private Supplier customlist;
    private Stock  stockList;
    private Employee employeelist;
    private Consignment consignmentList;
    private String customid;
    private Drawable errorIcon;
    private Common common;
    private Intent  intentback;
    private List<PopuMenuDataStructure> popuMenuDatas;
    private List<Product> findNumber;
    private List<SalesOutEnty> salesOutEntyList=new ArrayList<SalesOutEnty>();
    private List<ProductShopping> shoppinglist = new ArrayList<ProductShopping>();
    private List<CommonDataStructure> listdatas = new ArrayList<CommonDataStructure>();
    private SlideAndDragListView<CommonDataStructure> plistView;
    private SaleProductListViewAdapter adapter;
    private Menu mMenu;
    private List<Stock> stocks;
    private List<Employee> employees;
    private Calendar cal;
    private int year,month,day;
    private int countall;
    private double countamount;
    private Intent intent;
    public void iniView() {
        setContentView(R.layout.purchaseproductform);
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
        data=(TextView)findViewById(R.id.product_data);
        consignment=(TextView)findViewById(R.id.product_consignment);
        note=(EditText)findViewById(R.id.product_note);
        screenLayout=(LinearLayout) findViewById(R.id.number_screen);
        category=(TextView)findViewById(R.id.product_category);
        save=(TextView)findViewById(R.id.customtoobar_right);
        toobar_tile=(TextView)findViewById(R.id.customtoobar_midd);
        toobar_back=(TextView)findViewById(R.id.customtoobar_left);
        categoryLayout=(LinearLayout)findViewById(R.id.product_category_layout);
        toobar_add=(TextView)findViewById(R.id.customtoobar_r) ;
        stockLayout=(LinearLayout)findViewById(R.id.product_stock_layout);
        customLayout=(LinearLayout)findViewById(R.id.product_custom_layout);
        dataLayout=(LinearLayout)findViewById(R.id.product_data_layout);
        consignmentLayout=(LinearLayout)findViewById(R.id.product_consignment_layout);
        totalfqty=(TextView)findViewById(R.id.product_totalfqty);
        totalamout=(TextView)findViewById(R.id.product_totalamount);
        totalLayout=(LinearLayout)findViewById(R.id.product_total_layout);
        stockLayout.setOnClickListener(this);
        customLayout.setOnClickListener(this);
        dataLayout.setOnClickListener(this);
        consignmentLayout.setOnClickListener(this);
        save.setOnClickListener(this);
        toobar_back.setOnClickListener(this);
        categoryLayout.setOnClickListener(this);
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

                Intent openCameraIntent = new Intent(PurchaseProductForm.this,CommonScanActivity.class);
                openCameraIntent.putExtra(Constant.REQUEST_SCAN_MODE,Constant.REQUEST_SCAN_MODE_ALL_MODE);
                startActivityForResult(openCameraIntent, 5);
            }
        });
        toobar_tile.setText("采购单");

    }
    private void  formInit()
    {

            customlist = DataSupport.find(Supplier.class, 1);
        stockList = DataSupport.find(Stock.class, 1);
        employeelist = DataSupport.find(Employee.class, 1);
        consignmentList = DataSupport.find(Consignment.class, 1);
        if(customlist==null)
        {

        }else {
            name.setText(customlist.getName());
        }

        if(stockList==null)
        {

        }else {
            number.setText(stockList.getName());
        }
        if(employeelist==null)
        {

        }else {
            category.setText(employeelist.getName());
        }
        if(consignmentList==null)
        {

        }else {
            consignment.setText(consignmentList.getName());
        }



    }
    //获取当前日期
    private void getDate() {
        cal=Calendar.getInstance();
        year=cal.get(Calendar.YEAR);       //获取年月日时分秒
        month=cal.get(Calendar.MONTH);   //获取到的月份是从0开始计数
        day=cal.get(Calendar.DAY_OF_MONTH);
        data.setText(year+"-"+(++month)+"-"+day);
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
                        if(countamount!=0) {
                            totalLayout.setVisibility(View.VISIBLE);
                            totalamout.setText("¥"+df.format(countamount));
                            totalfqty.setText(String.valueOf(countall));
                        }else {
                            totalLayout.setVisibility(View.GONE);
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


        Intent intent=new Intent(PurchaseProductForm.this,ScrennProductShoppingForm.class);
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
                    Toast.makeText(PurchaseProductForm.this,"请选择客户",Toast.LENGTH_SHORT).show();

                }else if (TextUtils.isEmpty(number.getText().toString()))
                {
                    Toast.makeText(PurchaseProductForm.this,"请选择仓库",Toast.LENGTH_SHORT).show();
                }
                else if (listdatas.size()==0)
                {
                    Toast.makeText(PurchaseProductForm.this,"请选择产品",Toast.LENGTH_SHORT).show();
                }else

                {
                    SimpleDateFormat format=new SimpleDateFormat("yyyyMMddHHmmss");
                    Date curData=new Date(System.currentTimeMillis());
                    String fdate=format.format(curData);
                    for(int i = 0; i < listdatas.size(); i++)
                    {

                        SalesOutEnty salesOutEnty=new SalesOutEnty();
                        salesOutEnty.setItemname(listdatas.get(i).getName());
                        salesOutEnty.setItemnumber(listdatas.get(i).getNumber());
                        salesOutEnty.setItemprice(listdatas.get(i).getSalesprice());
                        salesOutEnty.setItemfqty(listdatas.get(i).getFqty());
                        salesOutEnty.setItemamount(listdatas.get(i).getSaleamount());
                        salesOutEntyList.add(salesOutEnty);
                    }
                    DataSupport.saveAll(salesOutEntyList);
                    SalesOut salesOut =new SalesOut();
                    salesOut.setSalesOutEntyList(salesOutEntyList);
                    salesOut.setCustomer(name.getText().toString());
                    salesOut.setNuber("CGRK"+ fdate);
                    salesOut.setFdate(data.getText().toString());
                    salesOut.setStock(number.getText().toString());
                    salesOut.setSaleamount(countamount);
                    salesOut.setSalefqty(countall);
                    salesOut.setSalesman(category.getText().toString());
                    salesOut.setConsignment(consignment.getText().toString());
                    salesOut.setNote(note.getText().toString().trim());
                    salesOut.setBilltype("1");
                    salesOut.save();
                    Toast.makeText(PurchaseProductForm.this,"新增成功",Toast.LENGTH_SHORT).show();
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
                Intent intentcustom=new Intent(PurchaseProductForm.this, SelectSupplierListView.class);
                startActivityForResult(intentcustom,8);


                break;
            case R.id.customtoobar_left:

                    PurchaseProductForm.this.finish();

             break;
            case R.id.product_category_layout:

                showEmployeeWindow();

                if( common.mPopWindow==null ||!common.mPopWindow.isShowing())
                {
                    int xPos = dm.widthPixels / 3;
                    common.mPopWindow.showAsDropDown(category,xPos,5);
                    //mPopWindow.showAtLocation(findViewById(R.id.main), Gravity.BOTTOM, 0, 0);//从底部弹出
                }
                else {
                    common.mPopWindow.dismiss();
                }
                break;
            case R.id.product_data_layout:

                DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        data.setText(year+"-"+(++month)+"-"+day);
                    }
                };
                DatePickerDialog dialog=new DatePickerDialog(PurchaseProductForm.this, 0,listener,year,month,day);//后边三个参数为显示dialog时默认的日期，月份从0开始，0-11对应1-12个月
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE,"取消",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();
                break;
            case R.id.product_consignment_layout:
                Intent intentconsignment=new Intent(PurchaseProductForm.this, ConsignmentListview.class);
                intentconsignment.putExtra("index",consignment.getText().toString());
                startActivityForResult(intentconsignment,9);
                break;
            case R.id.customtoobar_r:
                if( common.mPopWindow==null ||!common.mPopWindow.isShowing())
                {   popuMenuDatas.clear();

                    PopuMenuDataStructure popuMenub = new PopuMenuDataStructure(R.drawable.poppu_wrie, "采购单新增");
                    popuMenuDatas.add(popuMenub);
                    PopuMenuDataStructure popuMenua = new PopuMenuDataStructure(R.drawable.poppu_wrie, "采购单复制");
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
                Intent intentbadge=new Intent(PurchaseProductForm.this, ProductBadgeListView.class);
                startActivityForResult(intentbadge,6);
                default:

        }
    }
    private void showPopupWindow(final List<PopuMenuDataStructure> popuMenuData) {
        common = new Common();

        common.PopupWindow(PurchaseProductForm.this, dm, popuMenuData);
        common.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {

                if(popuMenuDatas.get(position).getName().equals("采购单新增"))
                {
                    intent = new Intent(PurchaseProductForm.this, SupplierForm.class);
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
            case 1:
                if(resultCode==RESULT_OK){
                    category.setText(data.getStringExtra("data_return"));
                }
                break;
            case 9:
                if(resultCode==RESULT_OK){
                    consignment.setText(data.getStringExtra("data_return"));
                }
                break;
            case 4:
                if(resultCode==RESULT_OK){
                    Intent intent = new Intent();
                    setResult(RESULT_OK,intent);
                   PurchaseProductForm.this.finish();
                }
                break;

            case 5:
                if(resultCode==RESULT_OK) {

                    countall=0;
                    countamount=0.00;
                    findNumber=DataStructure.where("number = ?",data.getStringExtra("scanResult")).find(Product.class);

                   if(findNumber.size()==0){
                       Toast.makeText(PurchaseProductForm.this,"找不到条码为"+data.getStringExtra("scanResult")+"商品",Toast.LENGTH_LONG).show();
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
                       adapter = new SaleProductListViewAdapter(PurchaseProductForm.this, R.layout.saleproduct_item, listdatas);
                       plistView.setAdapter(adapter);
                       setListViewHeightBasedOnChildren(plistView);
                       if(countamount!=0) {
                           totalLayout.setVisibility(View.VISIBLE);
                           totalamout.setText("¥"+df.format(countamount));
                           totalfqty.setText(String.valueOf(countall));
                       }else {
                           totalLayout.setVisibility(View.GONE);
                       }

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
                                shopping.setSaleamount(shopping.getSaleamount()+listdatas.get(i).getSaleamount());
                                listdatas.remove(i);
                                i--;
                            }
                        }
                        CommonDataStructure commonData=new CommonDataStructure();
                        commonData.setId(shopping.getId());
                        commonData.setName(shopping.getSalename());
                        commonData.setNumber(shopping.getSalenumber());
                        commonData.setFqty(shopping.getSalefqty());
                        commonData.setSaleamount(shopping.getSaleamount());
                        commonData.setSalesprice(shopping.getSalesprice());
                        listdatas.add(commonData);
                    }
                    for(int i = 0; i < listdatas.size(); i++)
                    {

                        countall+= listdatas.get(i).getFqty();
                        countamount+=listdatas.get(i).getSaleamount();
                    }

                    adapter = new SaleProductListViewAdapter(PurchaseProductForm.this, R.layout.saleproduct_item, listdatas);
                    plistView.setAdapter(adapter);
                    setListViewHeightBasedOnChildren(plistView);
                    if(countamount!=0) {
                        totalLayout.setVisibility(View.VISIBLE);
                        totalamout.setText("¥"+df.format(countamount));
                        totalfqty.setText(String.valueOf(countall));
                    }else {
                        totalLayout.setVisibility(View.GONE);
                    }
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

                    adapter = new SaleProductListViewAdapter(PurchaseProductForm.this, R.layout.saleproduct_item, listdatas);
                    plistView.setAdapter(adapter);

                    for(int i = 0; i < listdatas.size(); i++)
                    {

                        countall+= listdatas.get(i).getFqty();
                        countamount+=listdatas.get(i).getSaleamount();
                    }

                    if(countamount!=0) {
                        totalLayout.setVisibility(View.VISIBLE);
                        totalamout.setText("¥"+df.format(countamount));
                        totalfqty.setText(String.valueOf(countall));
                    }else {
                        totalLayout.setVisibility(View.GONE);
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
        common.PopupWindow(PurchaseProductForm.this, dm, popuMenuDatas);
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
        employees= DataSupport.findAll(Employee.class);
        for(Employee employee:employees)

        {
            PopuMenuDataStructure popuMenua = new PopuMenuDataStructure(R.drawable.poppu_wrie, employee.getName());
            popuMenuDatas.add(popuMenua);

        }
        common.PopupWindow(PurchaseProductForm.this, dm, popuMenuDatas);
        common.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {

                category.setText(popuMenuDatas.get(position).getName());
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
