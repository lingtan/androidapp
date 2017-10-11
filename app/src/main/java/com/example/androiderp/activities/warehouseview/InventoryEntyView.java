package com.example.androiderp.activities.warehouseview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.androiderp.bean.AppropriationEnty;
import com.example.androiderp.bean.Product;
import com.example.androiderp.bean.SalesOutEnty;
import com.example.androiderp.bean.Stock;
import com.example.androiderp.bean.StockIniti;
import com.example.androiderp.bean.StockTakingEnty;
import com.example.androiderp.R;
import com.example.androiderp.adaper.AppropriationListViewAdapter;
import com.example.androiderp.adaper.CommonAdapterData;
import com.example.androiderp.bean.PopuMenuDataStructure;
import com.example.androiderp.tools.Common;
import com.example.androiderp.ui.CSearchBase;
import com.example.androiderp.listview.Menu;
import com.example.androiderp.listview.MenuItem;
import com.example.androiderp.listview.SlideAndDragListView;

import org.litepal.crud.DataSupport;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lingtan on 2017/5/15.
 */

public class InventoryEntyView extends CSearchBase implements View.OnClickListener, AdapterView.OnItemClickListener,
        SlideAndDragListView.OnMenuItemClickListener, SlideAndDragListView.OnItemDeleteListener {
    private InputMethodManager manager;
    private TextView note,save,toobarTile,toobarBack,toobarAdd,name,number,data,consignment,totalQuantity;
    private DisplayMetrics dm;
    private Product product;
    private String productId;
    private Drawable errorIcon;
    private Common common;
    private double quantityCount;
    private Intent  intentBack;
    private List<PopuMenuDataStructure> popuMenuDatas;
    private List<CommonAdapterData> listdatas = new ArrayList<CommonAdapterData>();
    private SlideAndDragListView<CommonAdapterData> listView;
    private AppropriationListViewAdapter adapter;
    private Menu menu;
    private List<Stock> stockList;
    private double quantity;
    public void iniView() {
        setContentView(R.layout.inventoryentyform);
        initMenu();
        initUiAndListener();
        dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        intentBack= new Intent(InventoryEntyView.this, InventoryEntyView.class);
        final Intent intent=getIntent();
        productId =intent.getStringExtra("product_item");
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        name=(TextView)findViewById(R.id.stockout);
        number=(TextView)findViewById(R.id.stockin);
        data=(TextView)findViewById(R.id.businessdata);
        consignment=(TextView)findViewById(R.id.billnumber);
        note=(TextView)findViewById(R.id.note);
        save=(TextView)findViewById(R.id.customtoobar_right);
        toobarTile=(TextView)findViewById(R.id.customtoobar_midd);
        toobarBack=(TextView)findViewById(R.id.customtoobar_left);
        toobarAdd=(TextView)findViewById(R.id.customtoobar_r) ;
        totalQuantity=(TextView)findViewById(R.id.totalquantity);
        save.setOnClickListener(this);
        toobarBack.setOnClickListener(this);
        toobarAdd.setOnClickListener(this);
        Drawable more= getResources().getDrawable(R.drawable.toobar_more);
        more.setBounds(0, 0, more.getMinimumWidth(), more.getMinimumHeight());
        save.setCompoundDrawables(more,null,null,null);
        toobarTile.setCompoundDrawables(null,null,null,null);
        errorIcon = getResources().getDrawable(R.drawable.icon_error);
// 设置图片大小
        errorIcon.setBounds(new Rect(0, 0, errorIcon.getIntrinsicWidth(),
                errorIcon.getIntrinsicHeight()));
        save.setText("");
        name.setText("产品名称");
        number.setText("产品编码");
        formInit();


        toobarTile.setText("库存状况");

    }
    private void  formInit()
    {  quantityCount=0;
        DecimalFormat df = new DecimalFormat("#####0.00");
        if(productId !=null) {
            product = DataSupport.find(Product.class, Long.parseLong(productId),true);
            stockList=DataSupport.findAll(Stock.class);
            name.setText(product.getName());
            number.setText(product.getNumber());
            data.setText(product.getModel());
            consignment.setText(product.getCategory());
            note.setText(product.getNote());
          for(Stock stock:stockList) {
              if(stockInventory(stock.getName(),product.getNumber())>0) {
                  CommonAdapterData commonData = new CommonAdapterData();
                  commonData.setName(stock.getName());
                  commonData.setFqty(stockInventory(stock.getName(),product.getNumber()));
                  quantityCount +=commonData.getFqty();
                  listdatas.add(commonData);
              }
          }

            totalQuantity.setText(df.format(quantityCount));
            adapter = new AppropriationListViewAdapter(InventoryEntyView.this, R.layout.saleproduct_item, listdatas);
            listView.setAdapter(adapter);
            setListViewHeightBasedOnChildren(listView);

        }
    }
    public void initMenu() {
        menu = new Menu(true);

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


            break;
            case R.id.customtoobar_left:

                    InventoryEntyView.this.finish();

             break;
            case R.id.customtoobar_r:
                if( common.mPopWindow==null ||!common.mPopWindow.isShowing())
                {   popuMenuDatas.clear();

                    PopuMenuDataStructure popuMenub = new PopuMenuDataStructure(R.drawable.poppu_wrie, "商品新增");
                    popuMenuDatas.add(popuMenub);
                    PopuMenuDataStructure popuMenua = new PopuMenuDataStructure(R.drawable.poppu_wrie, "商品复制");
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
                default:

        }
    }
    private void showPopupWindow(final List<PopuMenuDataStructure> popuMenuData) {
        common = new Common();

        common.PopupWindow(InventoryEntyView.this, dm, popuMenuData);
        common.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {

                if(popuMenuDatas.get(position).getName().equals("商品新增"))
                {

                    intentBack.removeExtra("product_item");
                    intentBack.putExtra("action","add");
                    startActivityForResult(intentBack,4);
                }
                else if(popuMenuDatas.get(position).getName().equals("商品复制"))

                {
                    intentBack.removeExtra("product_item");
                    intentBack.putExtra("action","add");
                    intentBack.putExtra("product_item", quantityCount);
                    startActivityForResult(intentBack,4);

                }else
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
                   InventoryEntyView.this.finish();
                }
                break;
            default:
        }
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
    private double stockInventory(String stockname,String number)
    {

        double  in=DataSupport.where("stockIn=? and number=?",stockname,number).sum(AppropriationEnty.class,"quantity",double.class);
        double  out=DataSupport.where("stockOut=? and number=?",stockname,number).sum(AppropriationEnty.class,"quantity",double.class);
        double  stocktaking=DataSupport.where("stock=? and number=?",stockname,number).sum(StockTakingEnty.class,"quantity",double.class);
        double  initis=DataSupport.where("stock=? and number=?",stockname,number).sum(StockIniti.class,"quantity",double.class);
        double   salesOut=DataSupport.where("billtype =? and stock=? and number=?","2",stockname,number).sum(SalesOutEnty.class,"quantity",double.class);
        double  supplierin=DataSupport.where("billtype =? and stock=? and number=?","1",stockname,number).sum(SalesOutEnty.class,"quantity",double.class);
        quantity=0.00;
        quantity=initis+supplierin+in+stocktaking-salesOut-out;

        return  quantity;
    }

}
