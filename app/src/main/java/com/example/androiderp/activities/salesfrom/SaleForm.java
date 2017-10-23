package com.example.androiderp.activities.salesfrom;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androiderp.basic.BasicView;
import com.example.androiderp.bean.AcivityPostBean;
import com.example.androiderp.bean.AppropriationEnty;
import com.example.androiderp.bean.BalanceAccount;
import com.example.androiderp.bean.Custom;
import com.example.androiderp.bean.Employee;
import com.example.androiderp.bean.PostUserData;
import com.example.androiderp.bean.Product;
import com.example.androiderp.bean.ProductCategory;
import com.example.androiderp.bean.ProductShopping;
import com.example.androiderp.bean.SalesOut;
import com.example.androiderp.bean.SalesOutEnty;
import com.example.androiderp.bean.ShoppingData;
import com.example.androiderp.bean.Stock;
import com.example.androiderp.bean.StockIniti;
import com.example.androiderp.bean.StockTakingEnty;
import com.example.androiderp.bean.Tally;
import com.example.androiderp.R;
import com.example.androiderp.adaper.CommonAdapterData;
import com.example.androiderp.bean.DataStructure;
import com.example.androiderp.bean.PopBean;
import com.example.androiderp.adaper.SaleProductListViewAdapter;
import com.example.androiderp.activities.accountsview.BalanceAccountView;
import com.example.androiderp.activities.basicview.ProductSelectView;
import com.example.androiderp.activities.basicview.CustomSelectView;
import com.example.androiderp.tools.Common;
import com.example.androiderp.tools.HttpUtil;
import com.example.androiderp.ui.CSearchBase;
import com.example.androiderp.listview.Menu;
import com.example.androiderp.listview.MenuItem;
import com.example.androiderp.listview.SlideAndDragListView;
import com.example.androiderp.listview.Utils;
import com.example.androiderp.scanning.CommonScanActivity;
import com.example.androiderp.scanning.utils.Constant;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by lingtan on 2017/5/15.
 */

public class SaleForm extends CSearchBase implements View.OnClickListener, AdapterView.OnItemClickListener,
        SlideAndDragListView.OnMenuItemClickListener, SlideAndDragListView.OnItemDeleteListener {
    private InputMethodManager manager;
    private EditText note;
    private LinearLayout productAddLayout;
    private TextView save, tile, back, add, balanceAccount, category,popwin, name, number, data, consign, totalAmout, totalQuantity;
    private DisplayMetrics dm;
    private LinearLayout categoryLayout, customLayout, stockLayout, dataLayout, balanceAccountLayout, consignmentLayout, screenLayout, totalLayout;
    private Custom custom;
    private Stock stock;
    private Employee employee;
    private Drawable errorIcon;
    private Common common;
    private List<PopBean> popBeanList;
    private List<Product> productList;
    private List<SalesOutEnty> salesOutEntyList = new ArrayList<SalesOutEnty>();
    private List<ProductShopping> productShoppingList = new ArrayList<ProductShopping>();
    private List<CommonAdapterData> listdatas = new ArrayList<CommonAdapterData>();
    private SlideAndDragListView<CommonAdapterData> listView;
    private SaleProductListViewAdapter adapter;
    private Menu menu;
    private BalanceAccount balanceAccountList;
    private Calendar calendar;
    private int year, month, day;
    private double quantityCount;
    private double amountCount;
    private Intent intent;
    private List<PostUserData> postUserDataList = new ArrayList<PostUserData>();
    private PostUserData postDate = new PostUserData();
    private double quantity;
    private int stockCheck = 1;
    private List<Integer> stockCheckList = new ArrayList<Integer>();
    private AcivityPostBean acivityPostBen=new AcivityPostBean();

    public void iniView() {
        setContentView(R.layout.saleproductform);
        initMenu();
        initUiAndListener();
        dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        name = (TextView) findViewById(R.id.stockout);
        productAddLayout = (LinearLayout) findViewById(R.id.saleproduct_add);
        number = (TextView) findViewById(R.id.stockin);
        data = (TextView) findViewById(R.id.businessdata);
        consign = (TextView) findViewById(R.id.billnumber);
        balanceAccount = (TextView) findViewById(R.id.balanceaccount);
        note = (EditText) findViewById(R.id.note);
        screenLayout = (LinearLayout) findViewById(R.id.number_screen);
        category = (TextView) findViewById(R.id.documentmaker);
        save = (TextView) findViewById(R.id.customtoobar_right);
        tile = (TextView) findViewById(R.id.customtoobar_midd);
        back = (TextView) findViewById(R.id.customtoobar_left);
        categoryLayout = (LinearLayout) findViewById(R.id.documentmaker_layout);
        add = (TextView) findViewById(R.id.customtoobar_r);
        stockLayout = (LinearLayout) findViewById(R.id.stockin_layout);
        customLayout = (LinearLayout) findViewById(R.id.stockout_layout);
        dataLayout = (LinearLayout) findViewById(R.id.businessdata_layout);
        consignmentLayout = (LinearLayout) findViewById(R.id.billnumber_layout);
        balanceAccountLayout = (LinearLayout) findViewById(R.id.balanceaccount_layout);
        balanceAccountLayout.setOnClickListener(this);
        totalQuantity = (TextView) findViewById(R.id.product_totalfqty);
        totalAmout = (TextView) findViewById(R.id.product_totalamount);
        totalLayout = (LinearLayout) findViewById(R.id.product_total_layout);
        stockLayout.setOnClickListener(this);
        customLayout.setOnClickListener(this);
        dataLayout.setOnClickListener(this);
        consignmentLayout.setOnClickListener(this);
        save.setOnClickListener(this);
        back.setOnClickListener(this);
        categoryLayout.setOnClickListener(this);
        add.setOnClickListener(this);
        productAddLayout.setOnClickListener(this);
        save.setCompoundDrawables(null, null, null, null);
        tile.setCompoundDrawables(null, null, null, null);
        errorIcon = getResources().getDrawable(R.drawable.icon_error);
// 设置图片大小
        errorIcon.setBounds(new Rect(0, 0, errorIcon.getIntrinsicWidth(),
                errorIcon.getIntrinsicHeight()));
        save.setText("保存");
        formInit();
        popBeanList = new ArrayList<PopBean>();
        PopBean popuMenua = new PopBean(android.R.drawable.ic_menu_edit, "美的");
        popBeanList.add(popuMenua);
        PopBean popuMenub = new PopBean(android.R.drawable.ic_menu_edit, "松下");
        popBeanList.add(popuMenub);
        showPopupWindow(popBeanList);
        getDate();

        screenLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent openCameraIntent = new Intent(SaleForm.this, CommonScanActivity.class);
                openCameraIntent.putExtra(Constant.REQUEST_SCAN_MODE, Constant.REQUEST_SCAN_MODE_ALL_MODE);
                startActivityForResult(openCameraIntent, 5);
            }
        });
        tile.setText("销售单");

    }

    private void formInit() {

        custom = DataSupport.findFirst(Custom.class);
        stock = DataSupport.findFirst(Stock.class);
        employee = DataSupport.findFirst(Employee.class);
        balanceAccountList = DataSupport.findFirst(BalanceAccount.class);

        if (custom == null) {

        } else {
            name.setText(custom.getName());
        }

        if (stock == null) {

        } else {
            number.setText(stock.getName());
        }
        if (employee == null) {

        } else {
            category.setText(employee.getName());
        }

        if (balanceAccountList == null) {

        } else {
            balanceAccount.setText(balanceAccountList.getName());
        }


    }

    //获取当前日期
    private void getDate() {
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);       //获取年月日时分秒
        month = calendar.get(Calendar.MONTH);   //获取到的月份是从0开始计数
        day = calendar.get(Calendar.DAY_OF_MONTH);
        data.setText(year + "-" + (++month) + "-" + day);
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

                        DataStructure.deleteAll(ProductCategory.class, "name = ?", listdatas.get(itemPosition - listView.getHeaderViewsCount()).getName().toString());


                        listdatas.remove(itemPosition - listView.getHeaderViewsCount());
                        adapter.notifyDataSetChanged();
                        setListViewHeightBasedOnChildren(listView);
                        quantityCount = 0.00;
                        amountCount = 0.00;
                        DecimalFormat df = new DecimalFormat("#####0.00");
                        for (int i = 0; i < listdatas.size(); i++) {

                            quantityCount += listdatas.get(i).getFqty();
                            amountCount += listdatas.get(i).getSaleamount();
                        }
                        if (amountCount != 0) {
                            totalLayout.setVisibility(View.VISIBLE);
                            totalAmout.setText("¥" + df.format(amountCount));
                            totalQuantity.setText(df.format(quantityCount));
                        } else {
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


        Intent intent = new Intent(SaleForm.this, ShoppingScrennForm.class);
        intent.putExtra("action", "edit");
        intent.putExtra("product_item", String.valueOf(listdatas.get(position).getUnitId()));
        startActivityForResult(intent, 7);
    }

    private void hintKbTwo() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && getCurrentFocus() != null) {
            if (getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    @Override
    public void onClick(View v) {
        postDate.setName("");
        postDate.setRequestType("select");
        postDate.setServerIp(Common.ip);
        switch (v.getId())

        {
            case R.id.customtoobar_right:

                if (TextUtils.isEmpty(name.getText().toString())) {
                    Toast.makeText(SaleForm.this, "请选择客户", Toast.LENGTH_SHORT).show();

                } else if (TextUtils.isEmpty(number.getText().toString())) {
                    Toast.makeText(SaleForm.this, "请选择仓库", Toast.LENGTH_SHORT).show();
                } else if (listdatas.size() == 0) {
                    Toast.makeText(SaleForm.this, "请选择产品", Toast.LENGTH_SHORT).show();
                } else

                {
                    stockCheckList.clear();

                    for (int i = 0; i < listdatas.size(); i++) {
                        stockCheck(number.getText().toString(), listdatas.get(i).getNumber(), listdatas.get(i).getFqty());

                    }
                    if (stockCheckList.size() > 0) {
                        Toast.makeText(SaleForm.this, "有产品库存不足,不能保存", Toast.LENGTH_LONG).show();
                    } else {
                        DecimalFormat df = new DecimalFormat("#####0.00");
                        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
                        Date curData = new Date(System.currentTimeMillis());
                        String fdate = format.format(curData);
                        for (int i = 0; i < listdatas.size(); i++) {

                            SalesOutEnty salesOutEnty = new SalesOutEnty();
                            salesOutEnty.setName(listdatas.get(i).getName());
                            salesOutEnty.setNumber(listdatas.get(i).getNumber());
                            salesOutEnty.setPrice(listdatas.get(i).getSalesprice());
                            salesOutEnty.setQuantity(listdatas.get(i).getFqty());
                            salesOutEnty.setAmount(listdatas.get(i).getSaleamount());
                            salesOutEnty.setStock(number.getText().toString());
                            salesOutEnty.setBilltype("2");
                            salesOutEntyList.add(salesOutEnty);
                        }
                        DataSupport.saveAll(salesOutEntyList);
                        SalesOut salesOut = new SalesOut();
                        salesOut.setSalesOutEntyList(salesOutEntyList);
                        salesOut.setCustomer(name.getText().toString());
                        salesOut.setNuber("XSCK" + fdate);
                        salesOut.setDate(data.getText().toString());
                        salesOut.setStock(number.getText().toString());
                        salesOut.setAmount(amountCount);
                        salesOut.setQuantity(quantityCount);
                        salesOut.setSalesman(category.getText().toString());
                        salesOut.setConsignment(consign.getText().toString());
                        salesOut.setNote(note.getText().toString().trim());
                        salesOut.setBilltype("2");
                        salesOut.save();
                        Tally tally = new Tally();
                        tally.setNumber("JZLS" + fdate);
                        tally.setBalanceAccount(balanceAccount.getText().toString());
                        tally.setAccounts("客户");
                        tally.setDealings(name.getText().toString());
                        tally.setDate(fdate);
                        tally.setAmount(amountCount);
                        tally.setNote(note.getText().toString());
                        tally.save();
                        Toast.makeText(SaleForm.this, "新增成功", Toast.LENGTH_SHORT).show();
                        save.setVisibility(View.GONE);
                        add.setVisibility(View.VISIBLE);

                    }
                }

                break;
            case R.id.stockin_layout:

                postDate.setServlet("StockOperate");
                popwin=number;
                getHttpData(postDate);


                break;

            case R.id.stockout_layout:

                Intent intentcustom = new Intent(SaleForm.this, CustomSelectView.class);
                intentcustom.putExtra("index", name.getText().toString());
                startActivityForResult(intentcustom, 8);


                break;
            case R.id.balanceaccount_layout:
                Intent intentBalance = new Intent(SaleForm.this, BalanceAccountView.class);
                intentBalance.putExtra("index", balanceAccount.getText().toString());
                startActivityForResult(intentBalance, 13);


                break;
            case R.id.customtoobar_left:
                if (listdatas.size() > 0) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(SaleForm.this);
                    dialog.setTitle("提示");
                    dialog.setMessage("单据还没保存，确认要退出？");
                    dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            SaleForm.this.finish();

                        }
                    });
                    dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    dialog.show();
                } else {
                    SaleForm.this.finish();
                }


                break;
            case R.id.documentmaker_layout:

                postDate.setServlet("EmployeeOperate");
                popwin=category;
                getHttpData(postDate);


                break;
            case R.id.businessdata_layout:

                DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        data.setText(year + "-" + (++month) + "-" + dayOfMonth);
                    }
                };
                DatePickerDialog dialog = new DatePickerDialog(SaleForm.this, 0, listener, year, month, day);//后边三个参数为显示dialog时默认的日期，月份从0开始，0-11对应1-12个月
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();
                break;
            case R.id.billnumber_layout:
                acivityPostBen.setAcivityName("发货方式");
                acivityPostBen.setRequestServlet("BrandOperate");
                acivityPostBen.setName(consign.getText().toString());
                acivityPostBen.setSetClassType(6);
                acivityPostBen.setIsSelect("YES");
                Intent intentconsignment=new Intent(SaleForm.this, BasicView.class);
                intentconsignment.putExtra("acivityPostBen",acivityPostBen);
                startActivityForResult(intentconsignment,9);
                break;
            case R.id.customtoobar_r:
                if (common.mPopWindow == null || !common.mPopWindow.isShowing()) {
                    popBeanList.clear();

                    PopBean popuMenub = new PopBean(R.drawable.poppu_wrie, "销售单新增");
                    popBeanList.add(popuMenub);
                    PopBean popuMenua = new PopBean(R.drawable.poppu_wrie, "销售单复制");
                    popBeanList.add(popuMenua);
                    int xPos = dm.widthPixels / 3;
                    showPopupWindow(popBeanList);
                    common.mPopWindow.showAsDropDown(v, 0, 5);
                    //mPopWindow.showAtLocation(findViewById(R.id.main), Gravity.BOTTOM, 0, 0);//从底部弹出
                } else {
                    common.mPopWindow.dismiss();
                }
                break;
            case R.id.saleproduct_add:
                Intent intentbadge = new Intent(SaleForm.this, ProductSelectView.class);
                startActivityForResult(intentbadge, 6);
            default:

        }
    }

    private void showPopupWindow(final List<PopBean> popuMenuData) {
        common = new Common();

        common.PopupWindow(SaleForm.this, dm, popuMenuData);
        common.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {

                if (popBeanList.get(position).getName().equals("销售单新增")) {
                    intent = new Intent(SaleForm.this, SaleForm.class);
                    startActivity(intent);
                    SaleForm.this.finish();
                } else

                {

                }
                common.mPopWindow.dismiss();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        DecimalFormat df = new DecimalFormat("#####0.00");
        DecimalFormat quantityDf = new DecimalFormat("#####0.##");
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    category.setText(data.getStringExtra("data_return"));
                }
                break;
            case 9:
                if (resultCode == RESULT_OK) {
                    consign.setText(data.getStringExtra("data_return"));
                }
                break;
            case 11:
                if (resultCode == RESULT_OK) {
                    number.setText(data.getStringExtra("data_return"));
                }
                break;
            case 8:
                if (resultCode == RESULT_OK) {
                    name.setText(data.getStringExtra("data_return"));
                }
                break;
            case 12:
                if (resultCode == RESULT_OK) {
                    category.setText(data.getStringExtra("data_return"));
                }
                break;
            case 13:
                if (resultCode == RESULT_OK) {
                    balanceAccount.setText(data.getStringExtra("data_return"));
                }
                break;
            case 4:
                if (resultCode == RESULT_OK) {
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    SaleForm.this.finish();
                }
                break;

            case 5:
                if (resultCode == RESULT_OK) {


                    quantityCount = 0;
                    amountCount = 0.00;
                    productList = DataStructure.where("number = ?", data.getStringExtra("scanResult")).find(Product.class);

                    if (productList.size() == 0) {
                        Toast.makeText(SaleForm.this, "找不到条码为" + data.getStringExtra("scanResult") + "商品", Toast.LENGTH_LONG).show();
                    } else {
                        //产品重复就累加，否则就添加。
                        for (Product product : productList) {
                            boolean flag = true;
                            for (CommonAdapterData structure : listdatas) {

                                if (structure.getNumber().equals(product.getNumber())) {
                                    structure.setFqty(structure.getFqty() + 1);
                                    structure.setSaleamount(structure.getSaleamount() + structure.getSalesprice());
                                    flag = false;
                                }
                            }
                            if (flag == true) {
                                CommonAdapterData commonData = new CommonAdapterData();
                                commonData.setUnitId(product.getProduct_id());
                                commonData.setNumber(product.getNumber());
                                commonData.setName(product.getName());
                                commonData.setFqty(1);
                                commonData.setSaleamount(Double.valueOf(product.getSalesPrice()));
                                commonData.setSalesprice(Double.valueOf(product.getSalesPrice()));

                                listdatas.add(commonData);
                            }

                        }
                        //计算与库存盘点
                        for (int i = 0; i < listdatas.size(); i++)

                        {
                            if (stockCheck(number.getText().toString(), listdatas.get(i).getNumber(), listdatas.get(i).getFqty()) == 0) {
                                Toast.makeText(SaleForm.this, "仓库数量不足，实际库存为：" + df.format(quantity), Toast.LENGTH_SHORT).show();
                            }
                            quantityCount += listdatas.get(i).getFqty();
                            amountCount += listdatas.get(i).getSaleamount();


                        }
                        adapter = new SaleProductListViewAdapter(SaleForm.this, R.layout.saleproduct_item, listdatas);
                        listView.setAdapter(adapter);
                        //计算listView实际内容高度
                        setListViewHeightBasedOnChildren(listView);
                        //总数量与金额显示
                        if (amountCount != 0) {
                            totalLayout.setVisibility(View.VISIBLE);
                            totalAmout.setText("¥" + df.format(amountCount));
                            totalQuantity.setText(quantityDf.format(quantityCount));
                        } else {
                            totalLayout.setVisibility(View.GONE);
                        }

                    }
                }

                break;
            case 6:
                if (resultCode == RESULT_OK) {
                    quantityCount = 0;
                    amountCount = 0.00;

                    ShoppingData shoppingData = data.getParcelableExtra("shoppingdata");

                    productShoppingList = shoppingData.getProductShoppingList();
                    for (ProductShopping shopping : productShoppingList) {
                        for (int i = 0; i < listdatas.size(); i++) {
                            if (listdatas.get(i).getNumber().equals(shopping.getNumber())) {

                                shopping.setQuantity(shopping.getQuantity() + listdatas.get(i).getFqty());
                                shopping.setAmount(shopping.getAmount() + listdatas.get(i).getSaleamount());
                                listdatas.remove(i);
                                i--;
                            }
                        }
                        CommonAdapterData commonData = new CommonAdapterData();
                        commonData.setUnitId(shopping.getId());
                        commonData.setName(shopping.getName());
                        commonData.setNumber(shopping.getNumber());
                        commonData.setFqty(shopping.getQuantity());
                        commonData.setSaleamount(shopping.getAmount());
                        commonData.setSalesprice(shopping.getPrice());
                        listdatas.add(commonData);
                    }
                    for (int i = 0; i < listdatas.size(); i++) {

                        quantityCount += listdatas.get(i).getFqty();
                        amountCount += listdatas.get(i).getSaleamount();
                    }

                    adapter = new SaleProductListViewAdapter(SaleForm.this, R.layout.saleproduct_item, listdatas);
                    listView.setAdapter(adapter);
                    setListViewHeightBasedOnChildren(listView);
                    if (amountCount != 0) {
                        totalLayout.setVisibility(View.VISIBLE);
                        totalAmout.setText("¥" + df.format(amountCount));
                        totalQuantity.setText(quantityDf.format(quantityCount));
                    } else {
                        totalLayout.setVisibility(View.GONE);
                    }
                }

                break;

            case 7:
                if (resultCode == RESULT_OK) {
                    quantityCount = 0;
                    amountCount = 0.00;
                    ProductShopping shopping = data.getParcelableExtra("shop_data");
                    for (CommonAdapterData commonData : listdatas)

                    {

                        if (commonData.getNumber().toString().equals(shopping.getNumber().toString())) {
                            commonData.setFqty(shopping.getQuantity());
                            commonData.setSalesprice(shopping.getPrice());
                            commonData.setSaleamount(shopping.getAmount());
                        }
                    }

                    adapter = new SaleProductListViewAdapter(SaleForm.this, R.layout.saleproduct_item, listdatas);
                    listView.setAdapter(adapter);

                    for (int i = 0; i < listdatas.size(); i++) {
                        if (stockCheck(number.getText().toString(), listdatas.get(i).getNumber(), listdatas.get(i).getFqty()) == 0) {
                            Toast.makeText(SaleForm.this, "仓库数量不足，实际库存为：" + df.format(quantity), Toast.LENGTH_SHORT).show();
                        }

                        quantityCount += listdatas.get(i).getFqty();
                        amountCount += listdatas.get(i).getSaleamount();
                    }

                    if (amountCount != 0) {
                        totalLayout.setVisibility(View.VISIBLE);
                        totalAmout.setText("¥" + df.format(amountCount));
                        totalQuantity.setText(quantityDf.format(quantityCount));
                    } else {
                        totalLayout.setVisibility(View.GONE);
                    }

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


                        Toast.makeText(SaleForm.this, "网络连接失败", Toast.LENGTH_SHORT).show();

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
                                postUserDataList = gson.fromJson(response.body().string(), new TypeToken<List<PostUserData>>() {
                                }.getType());

                                if (postUserDataList.size() >0) {
                                    common = new Common();
                                    popBeanList = new ArrayList<PopBean>();

                                    for (PostUserData postUserData : postUserDataList)

                                    {
                                        PopBean popuMenua = new PopBean(R.drawable.poppu_wrie, postUserData.getName());
                                        popBeanList.add(popuMenua);

                                    }
                                    common.PopupWindow(SaleForm.this, dm, popBeanList);
                                    common.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                        @Override
                                        public void onItemClick(AdapterView<?> adapterView, View view,
                                                                int position, long id) {

                                            popwin.setText(popBeanList.get(position).getName());
                                            common.mPopWindow.dismiss();
                                        }
                                    });

                                    if (common.mPopWindow == null || !common.mPopWindow.isShowing()) {
                                        int xPos = dm.widthPixels / 3;
                                        common.mPopWindow.showAsDropDown(popwin, xPos, 5);
                                        //mPopWindow.showAtLocation(findViewById(R.id.main), Gravity.BOTTOM, 0, 0);//从底部弹出
                                    } else {
                                        common.mPopWindow.dismiss();
                                    }




                                }else {
                                    Toast.makeText(SaleForm.this, "没有数据", Toast.LENGTH_SHORT).show();
                                }

                            }

                        } catch (Exception e) {
                            Toast.makeText(SaleForm.this, "网络连接失败", Toast.LENGTH_SHORT).show();

                        }
                    }
                });


            }
        });


    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
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


    private int stockCheck(String stockname, String number, double sfqty) {

        double in = DataSupport.where("stockIn=? and number=?", stockname, number).sum(AppropriationEnty.class, "quantity", double.class);
        double out = DataSupport.where("stockOut=? and number=?", stockname, number).sum(AppropriationEnty.class, "quantity", double.class);
        double initis = DataSupport.where("stock=? and number=?", stockname, number).sum(StockIniti.class, "quantity", double.class);
        double stocktaking = DataSupport.where("stock=? and number=?", stockname, number).sum(StockTakingEnty.class, "quantity", double.class);
        double salesOut = DataSupport.where("billtype =? and stock=? and number=?", "2", stockname, number).sum(SalesOutEnty.class, "quantity", double.class);
        double supplierin = DataSupport.where("billtype =? and stock=? and number=?", "1", stockname, number).sum(SalesOutEnty.class, "quantity", double.class);
        quantity = 0.00;
        quantity = initis + supplierin + in + stocktaking - salesOut - out;

        if (sfqty > quantity) {
            stockCheck = 0;
            stockCheckList.add(stockCheck);
        } else {
            stockCheck = 1;
        }
        return stockCheck;
    }


}
