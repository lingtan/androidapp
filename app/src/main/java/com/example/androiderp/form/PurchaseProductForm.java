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
import com.example.androiderp.basicdata.ProductBadgeListView;
import com.example.androiderp.basicdata.SelectSupplierListView;
import com.example.androiderp.basicdata.StockIntentListview;
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
    private LinearLayout productAddLayout;
    private TextView toobarSave, toobarTile, toobarBack, toobarAdd,category,name,number,data, consign, totalAmout, totalQuantity;
    private DisplayMetrics dm;
    private LinearLayout categoryLayout,customLayout,stockLayout,dataLayout,consignmentLayout,screenLayout,totalLayout;
    private Supplier supplier;
    private Stock stock;
    private Employee employee;
    private Consignment consignment;
    private String customid;
    private Drawable errorIcon;
    private Common common;
    private List<PopuMenuDataStructure> popuMenuDatas;
    private List<Product> productList;
    private List<SalesOutEnty> salesOutEntyList=new ArrayList<SalesOutEnty>();
    private List<ProductShopping> productShoppingList = new ArrayList<ProductShopping>();
    private List<CommonDataStructure> listdatas = new ArrayList<CommonDataStructure>();
    private SlideAndDragListView<CommonDataStructure> listView;
    private SaleProductListViewAdapter adapter;
    private Menu menu;
    private List<Stock> stockList;
    private List<Employee> employeeList;
    private Calendar calendar;
    private int year,month,day;
    private int countall;
    private double amountCount;
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
        name=(TextView)findViewById(R.id.stockout);
        productAddLayout =(LinearLayout) findViewById(R.id.saleproduct_add);
        number=(TextView)findViewById(R.id.stockin);
        data=(TextView)findViewById(R.id.businessdata);
        consign =(TextView)findViewById(R.id.billnumber);
        note=(EditText)findViewById(R.id.note);
        screenLayout=(LinearLayout) findViewById(R.id.number_screen);
        category=(TextView)findViewById(R.id.documentmaker);
        toobarSave =(TextView)findViewById(R.id.customtoobar_right);
        toobarTile =(TextView)findViewById(R.id.customtoobar_midd);
        toobarBack =(TextView)findViewById(R.id.customtoobar_left);
        categoryLayout=(LinearLayout)findViewById(R.id.documentmaker_layout);
        toobarAdd =(TextView)findViewById(R.id.customtoobar_r) ;
        stockLayout=(LinearLayout)findViewById(R.id.stockin_layout);
        customLayout=(LinearLayout)findViewById(R.id.stockout_layout);
        dataLayout=(LinearLayout)findViewById(R.id.businessdata_layout);
        consignmentLayout=(LinearLayout)findViewById(R.id.billnumber_layout);
        totalQuantity =(TextView)findViewById(R.id.product_totalfqty);
        totalAmout =(TextView)findViewById(R.id.product_totalamount);
        totalLayout=(LinearLayout)findViewById(R.id.product_total_layout);
        stockLayout.setOnClickListener(this);
        customLayout.setOnClickListener(this);
        dataLayout.setOnClickListener(this);
        consignmentLayout.setOnClickListener(this);
        toobarSave.setOnClickListener(this);
        toobarBack.setOnClickListener(this);
        categoryLayout.setOnClickListener(this);
        toobarAdd.setOnClickListener(this);
        productAddLayout.setOnClickListener(this);
        toobarSave.setCompoundDrawables(null,null,null,null);
        toobarTile.setCompoundDrawables(null,null,null,null);
        errorIcon = getResources().getDrawable(R.drawable.icon_error);
// 设置图片大小
        errorIcon.setBounds(new Rect(0, 0, errorIcon.getIntrinsicWidth(),
                errorIcon.getIntrinsicHeight()));
        toobarSave.setText("保存");
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
        toobarTile.setText("采购单");

    }
    private void  formInit()
    {

            supplier = DataSupport.find(Supplier.class, 1);
        stock = DataSupport.find(Stock.class, 1);
        employee = DataSupport.find(Employee.class, 1);
        consignment = DataSupport.find(Consignment.class, 1);
        if(supplier ==null)
        {

        }else {
            name.setText(supplier.getName());
        }

        if(stock ==null)
        {

        }else {
            number.setText(stock.getName());
        }
        if(employee ==null)
        {

        }else {
            category.setText(employee.getName());
        }
        if(consignment ==null)
        {

        }else {
            consign.setText(consignment.getName());
        }



    }
    //获取当前日期
    private void getDate() {
        calendar =Calendar.getInstance();
        year= calendar.get(Calendar.YEAR);       //获取年月日时分秒
        month= calendar.get(Calendar.MONTH);   //获取到的月份是从0开始计数
        day= calendar.get(Calendar.DAY_OF_MONTH);
        data.setText(year+"-"+(++month)+"-"+day);
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

                                DataStructure.deleteAll(ProductCategory.class,"name = ?",listdatas.get(itemPosition - listView.getHeaderViewsCount()).getName().toString());


                                  listdatas.remove(itemPosition - listView.getHeaderViewsCount());
                                   adapter.notifyDataSetChanged();
                                  setListViewHeightBasedOnChildren(listView);
                        countall=0;
                        amountCount =0.00;
                        DecimalFormat df = new DecimalFormat("#####0.00");
                        for(int i = 0; i < listdatas.size(); i++)
                        {

                            countall+= listdatas.get(i).getFqty();
                            amountCount +=listdatas.get(i).getSaleamount();
                        }
                        if(amountCount !=0) {
                            totalLayout.setVisibility(View.VISIBLE);
                            totalAmout.setText("¥"+df.format(amountCount));
                            totalQuantity.setText(String.valueOf(countall));
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
                        salesOutEnty.setName(listdatas.get(i).getName());
                        salesOutEnty.setNumber(listdatas.get(i).getNumber());
                        salesOutEnty.setPrice(listdatas.get(i).getSalesprice());
                        salesOutEnty.setQuantity(listdatas.get(i).getFqty());
                        salesOutEnty.setAmount(listdatas.get(i).getSaleamount());
                        salesOutEnty.setStock(number.getText().toString());
                        salesOutEnty.setBilltype("1");
                        salesOutEntyList.add(salesOutEnty);
                    }
                    DataSupport.saveAll(salesOutEntyList);
                    SalesOut salesOut =new SalesOut();
                    salesOut.setSalesOutEntyList(salesOutEntyList);
                    salesOut.setCustomer(name.getText().toString());
                    salesOut.setNuber("CGRK"+ fdate);
                    salesOut.setDate(data.getText().toString());
                    salesOut.setStock(number.getText().toString());
                    salesOut.setAmount(amountCount);
                    salesOut.setQuantity(countall);
                    salesOut.setSalesman(category.getText().toString());
                    salesOut.setConsignment(consign.getText().toString());
                    salesOut.setNote(note.getText().toString().trim());
                    salesOut.setBilltype("1");
                    salesOut.save();
                    Toast.makeText(PurchaseProductForm.this,"新增成功",Toast.LENGTH_SHORT).show();
                    toobarSave.setVisibility(View.GONE);
                    toobarAdd.setVisibility(View.VISIBLE);


                }

            break;
            case R.id.stockin_layout:
                showStockWindow();
                if(stockList.size()>0) {
                    if (common.mPopWindow == null || !common.mPopWindow.isShowing()) {
                        int xPos = dm.widthPixels / 3;
                        common.mPopWindow.showAsDropDown(number, xPos, 5);
                        //mPopWindow.showAtLocation(findViewById(R.id.main), Gravity.BOTTOM, 0, 0);//从底部弹出
                    } else {
                        common.mPopWindow.dismiss();
                    }
                }else {
                    Intent intentstock=new Intent(PurchaseProductForm.this, StockIntentListview.class);
                    intentstock.putExtra("index",number.getText().toString());
                    startActivityForResult(intentstock,11);
                }
                break;

            case R.id.stockout_layout:
                Intent intentcustom=new Intent(PurchaseProductForm.this, SelectSupplierListView.class);
                intentcustom.putExtra("index",name.getText().toString());
                startActivityForResult(intentcustom,8);


                break;
            case R.id.customtoobar_left:

                    PurchaseProductForm.this.finish();

             break;
            case R.id.documentmaker_layout:

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
            case R.id.businessdata_layout:

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
            case R.id.billnumber_layout:
                Intent intentconsignment=new Intent(PurchaseProductForm.this, ConsignmentListview.class);
                intentconsignment.putExtra("index", consign.getText().toString());
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
                    consign.setText(data.getStringExtra("data_return"));
                }
                break;
            case 11:
                if(resultCode==RESULT_OK){
                    number.setText(data.getStringExtra("data_return"));
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
                    amountCount =0.00;
                    productList =DataStructure.where("number = ?",data.getStringExtra("scanResult")).find(Product.class);

                   if(productList.size()==0){
                       Toast.makeText(PurchaseProductForm.this,"找不到条码为"+data.getStringExtra("scanResult")+"商品",Toast.LENGTH_LONG).show();
                   }else {

                       for (Product product : productList) {
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
                               commonData.setSaleamount(Double.valueOf(product.getSalesPrice()));
                               commonData.setSalesprice(Double.valueOf(product.getSalesPrice()));

                               listdatas.add(commonData);
                           }

                       }

                       for(int i = 0; i < listdatas.size(); i++)
                       {

                           countall+= listdatas.get(i).getFqty();
                           amountCount +=listdatas.get(i).getSaleamount();
                       }
                       adapter = new SaleProductListViewAdapter(PurchaseProductForm.this, R.layout.saleproduct_item, listdatas);
                       listView.setAdapter(adapter);
                       setListViewHeightBasedOnChildren(listView);
                       if(amountCount !=0) {
                           totalLayout.setVisibility(View.VISIBLE);
                           totalAmout.setText("¥"+df.format(amountCount));
                           totalQuantity.setText(String.valueOf(countall));
                       }else {
                           totalLayout.setVisibility(View.GONE);
                       }

                   }
                }

                break;
            case 6:
                if(resultCode==RESULT_OK) {
                    countall=0;
                    amountCount =0.00;

                    ShoppingData shoppingData=data.getParcelableExtra("shoppingdata");

                    productShoppingList =shoppingData.getProductShoppingList();
                    for(ProductShopping shopping: productShoppingList)
                    {
                        for(int i = 0; i < listdatas.size(); i++)
                        {
                            if(listdatas.get(i).getNumber().equals(shopping.getNumber()))
                            {

                                shopping.setQuantity(shopping.getQuantity()+listdatas.get(i).getFqty());
                                shopping.setAmount(shopping.getAmount()+listdatas.get(i).getSaleamount());
                                listdatas.remove(i);
                                i--;
                            }
                        }
                        CommonDataStructure commonData=new CommonDataStructure();
                        commonData.setId(shopping.getId());
                        commonData.setName(shopping.getName());
                        commonData.setNumber(shopping.getNumber());
                        commonData.setFqty(shopping.getQuantity());
                        commonData.setSaleamount(shopping.getAmount());
                        commonData.setSalesprice(shopping.getPrice());
                        listdatas.add(commonData);
                    }
                    for(int i = 0; i < listdatas.size(); i++)
                    {

                        countall+= listdatas.get(i).getFqty();
                        amountCount +=listdatas.get(i).getSaleamount();
                    }

                    adapter = new SaleProductListViewAdapter(PurchaseProductForm.this, R.layout.saleproduct_item, listdatas);
                    listView.setAdapter(adapter);
                    setListViewHeightBasedOnChildren(listView);
                    if(amountCount !=0) {
                        totalLayout.setVisibility(View.VISIBLE);
                        totalAmout.setText("¥"+df.format(amountCount));
                        totalQuantity.setText(String.valueOf(countall));
                    }else {
                        totalLayout.setVisibility(View.GONE);
                    }
                }

                break;

            case 7:
                if(resultCode==RESULT_OK) {
                    countall=0;
                    amountCount =0.00;
                    ProductShopping shopping = (ProductShopping) data.getParcelableExtra("shop_data");
                    for ( CommonDataStructure commonData : listdatas)

                    {
                        if (commonData.getNumber().toString().equals(shopping.getNumber().toString())) {
                            commonData.setFqty(shopping.getQuantity());
                            commonData.setSalesprice(shopping.getPrice());
                            commonData.setSaleamount(shopping.getAmount());
                        }
                    }

                    adapter = new SaleProductListViewAdapter(PurchaseProductForm.this, R.layout.saleproduct_item, listdatas);
                    listView.setAdapter(adapter);

                    for(int i = 0; i < listdatas.size(); i++)
                    {

                        countall+= listdatas.get(i).getFqty();
                        amountCount +=listdatas.get(i).getSaleamount();
                    }

                    if(amountCount !=0) {
                        totalLayout.setVisibility(View.VISIBLE);
                        totalAmout.setText("¥"+df.format(amountCount));
                        totalQuantity.setText(String.valueOf(countall));
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
        stockList = DataSupport.findAll(Stock.class);
        for(Stock stock: stockList)

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
        employeeList = DataSupport.findAll(Employee.class);
        for(Employee employee: employeeList)

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
