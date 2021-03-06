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

import com.example.androiderp.bean.Employee;
import com.example.androiderp.bean.Stock;
import com.example.androiderp.bean.StockTaking;
import com.example.androiderp.bean.StockTakingEnty;
import com.example.androiderp.R;
import com.example.androiderp.adaper.AppropriationListViewAdapter;
import com.example.androiderp.adaper.CommonAdapterData;
import com.example.androiderp.bean.PopBean;
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

public class StockTakingEntyView extends CSearchBase implements View.OnClickListener, AdapterView.OnItemClickListener,
        SlideAndDragListView.OnMenuItemClickListener, SlideAndDragListView.OnItemDeleteListener {
    private InputMethodManager manager;
    private TextView note,save, tile, back, add,category,number,data,consignment,totalQuantity;
    private DisplayMetrics dm;
    private StockTaking stockTaking;
    private String id;
    private Drawable errorIcon;
    private Common common;
    private double quantityCount;
    private Intent  intentBack;
    private List<PopBean> popBeanList;
    private List<StockTakingEnty> stockTakingEntyList =new ArrayList<StockTakingEnty>();
    private List<CommonAdapterData> listdatas = new ArrayList<CommonAdapterData>();
    private SlideAndDragListView<CommonAdapterData> listView;
    private AppropriationListViewAdapter adapter;
    private Menu menu;
    private List<Stock> stockList;
    public void iniView() {
        setContentView(R.layout.stocktakingentyform);
        initMenu();
        initUiAndListener();
        dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        showStockWindow();
        intentBack= new Intent(StockTakingEntyView.this, StockTakingEntyView.class);
        final Intent intent=getIntent();
        id =intent.getStringExtra("custom_item");
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        number=(TextView)findViewById(R.id.stockin);
        data=(TextView)findViewById(R.id.businessdata);
        consignment=(TextView)findViewById(R.id.billnumber);
        note=(TextView)findViewById(R.id.note);
        category=(TextView)findViewById(R.id.documentmaker);
        save=(TextView)findViewById(R.id.customtoobar_right);
        tile =(TextView)findViewById(R.id.customtoobar_midd);
        back =(TextView)findViewById(R.id.customtoobar_left);
        add =(TextView)findViewById(R.id.customtoobar_r) ;
        totalQuantity=(TextView)findViewById(R.id.totalquantity);
        save.setOnClickListener(this);
        back.setOnClickListener(this);
        add.setOnClickListener(this);
        Drawable more= getResources().getDrawable(R.drawable.toobar_more);
        more.setBounds(0, 0, more.getMinimumWidth(), more.getMinimumHeight());
        save.setCompoundDrawables(more,null,null,null);
        tile.setCompoundDrawables(null,null,null,null);
        errorIcon = getResources().getDrawable(R.drawable.icon_error);
// 设置图片大小
        errorIcon.setBounds(new Rect(0, 0, errorIcon.getIntrinsicWidth(),
                errorIcon.getIntrinsicHeight()));
        save.setText("");
        number.setText("仓库");
        formInit();


        tile.setText("盘点明细");

    }
    private void  formInit()
    {  quantityCount=0;
        DecimalFormat df = new DecimalFormat("#####0.00");
        if(id !=null) {
            Employee  employeelist = DataSupport.find(Employee.class, 1);
            if(employeelist==null)
            {

            }else {
                category.setText(employeelist.getName());
            }
            stockTaking = DataSupport.find(StockTaking.class, Long.parseLong(id),true);
            stockTakingEntyList = stockTaking.getStockTakingEntyList();
            number.setText(stockTaking.getStock());
            data.setText(stockTaking.getDate().toString().trim());
            consignment.setText(stockTaking.getNuber());
            note.setText(stockTaking.getNote());
          for(StockTakingEnty stockTakingEntyy: stockTakingEntyList) {
              CommonAdapterData commonData = new CommonAdapterData();
              commonData.setUnitId(stockTakingEntyy.getId());
              commonData.setNumber(stockTakingEntyy.getNumber());
              commonData.setName(stockTakingEntyy.getName());
              commonData.setFqty(stockTakingEntyy.getQuantity());
              quantityCount+=stockTakingEntyy.getQuantity();
              listdatas.add(commonData);
          }

            totalQuantity.setText(df.format(quantityCount));
            adapter = new AppropriationListViewAdapter(StockTakingEntyView.this, R.layout.saleproduct_item, listdatas);
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

                    StockTakingEntyView.this.finish();

             break;
            case R.id.customtoobar_r:
                if( common.mPopWindow==null ||!common.mPopWindow.isShowing())
                {   popBeanList.clear();

                    PopBean popuMenub = new PopBean(R.drawable.poppu_wrie, "商品新增");
                    popBeanList.add(popuMenub);
                    PopBean popuMenua = new PopBean(R.drawable.poppu_wrie, "商品复制");
                    popBeanList.add(popuMenua);
                    int xPos = dm.widthPixels / 3;
                    showPopupWindow(popBeanList);
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
    private void showPopupWindow(final List<PopBean> popuMenuData) {
        common = new Common();

        common.PopupWindow(StockTakingEntyView.this, dm, popuMenuData);
        common.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {

                if(popBeanList.get(position).getName().equals("商品新增"))
                {

                    intentBack.removeExtra("product_item");
                    intentBack.putExtra("action","add");
                    startActivityForResult(intentBack,4);
                }
                else if(popBeanList.get(position).getName().equals("商品复制"))

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
                   StockTakingEntyView.this.finish();
                }
                break;
            default:
        }
    }
    private void showStockWindow() {
        common = new Common();
        popBeanList = new ArrayList<PopBean>();
        stockList= DataSupport.findAll(Stock.class);
        for(Stock stock:stockList)

        {
            PopBean popuMenua = new PopBean(R.drawable.poppu_wrie, stock.getName());
            popBeanList.add(popuMenua);

        }
        common.PopupWindow(StockTakingEntyView.this, dm, popBeanList);
        common.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {

                number.setText(popBeanList.get(position).getName());
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

}
