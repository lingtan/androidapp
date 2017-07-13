package com.example.androiderp.basicdata;

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

import com.example.androiderp.CustomDataClass.Appropriation;
import com.example.androiderp.CustomDataClass.AppropriationEnty;
import com.example.androiderp.CustomDataClass.Employee;
import com.example.androiderp.CustomDataClass.Stock;
import com.example.androiderp.R;
import com.example.androiderp.adaper.AppropriationListViewAdapter;
import com.example.androiderp.adaper.CommonDataStructure;
import com.example.androiderp.adaper.PopuMenuDataStructure;
import com.example.androiderp.common.Common;
import com.example.androiderp.custom.CustomSearchBase;
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

public class AppropriationEntyList extends CustomSearchBase implements View.OnClickListener, AdapterView.OnItemClickListener,
        SlideAndDragListView.OnMenuItemClickListener, SlideAndDragListView.OnItemDeleteListener {
    private InputMethodManager manager;
    private TextView note,save,toobarTile,toobarBack,toobarAdd,category,name,number,data,consignment,totalQuantity;
    private DisplayMetrics dm;
    private Appropriation appropriation;
    private String appropriatinId;
    private Drawable errorIcon;
    private Common common;
    private double quantityCount;
    private Intent  intentBack;
    private List<PopuMenuDataStructure> popuMenuDatas;
    private List<AppropriationEnty> appropriationEntyList=new ArrayList<AppropriationEnty>();
    private List<CommonDataStructure> listdatas = new ArrayList<CommonDataStructure>();
    private SlideAndDragListView<CommonDataStructure> listView;
    private AppropriationListViewAdapter adapter;
    private Menu menu;
    private List<Stock> stockList;
    public void iniView() {
        setContentView(R.layout.appropriationentyform);
        initMenu();
        initUiAndListener();
        dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        showStockWindow();
        intentBack= new Intent(AppropriationEntyList.this, AppropriationEntyList.class);
        final Intent intent=getIntent();
        appropriatinId=intent.getStringExtra("custom_item");
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        name=(TextView)findViewById(R.id.product_custom);
        number=(TextView)findViewById(R.id.product_stock);
        data=(TextView)findViewById(R.id.product_data);
        consignment=(TextView)findViewById(R.id.product_consignment);
        note=(TextView)findViewById(R.id.product_note);
        category=(TextView)findViewById(R.id.product_category);
        save=(TextView)findViewById(R.id.customtoobar_right);
        toobarTile=(TextView)findViewById(R.id.customtoobar_midd);
        toobarBack=(TextView)findViewById(R.id.customtoobar_left);
        toobarAdd=(TextView)findViewById(R.id.customtoobar_r) ;
        totalQuantity=(TextView)findViewById(R.id.saleoutenty_fqty);
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
        name.setText("调出仓库");
        number.setText("调入仓库");
        formInit();


        toobarTile.setText("调拨明细");

    }
    private void  formInit()
    {  quantityCount=0;
        DecimalFormat df = new DecimalFormat("#####0.00");
        if(appropriatinId!=null) {
            Employee  employeelist = DataSupport.find(Employee.class, 1);
            if(employeelist==null)
            {

            }else {
                category.setText(employeelist.getName());
            }
            appropriation = DataSupport.find(Appropriation.class, Long.parseLong(appropriatinId),true);
            appropriationEntyList=appropriation.getSalesOutEntyList();
            name.setText(appropriation.getStockOut());
            number.setText(appropriation.getStockIn());
            data.setText(appropriation.getDate().toString().trim());
            consignment.setText(appropriation.getNuber());
            note.setText(appropriation.getNote());
          for(AppropriationEnty appropriationEnty:appropriationEntyList) {
              CommonDataStructure commonData = new CommonDataStructure();
              commonData.setId(appropriationEnty.getId());
              commonData.setNumber(appropriationEnty.getNumber());
              commonData.setName(appropriationEnty.getName());
              commonData.setFqty(appropriationEnty.getQuantity());
              quantityCount+=appropriationEnty.getQuantity();
              listdatas.add(commonData);
          }

            totalQuantity.setText(df.format(quantityCount));
            adapter = new AppropriationListViewAdapter(AppropriationEntyList.this, R.layout.saleproduct_item, listdatas);
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

                    AppropriationEntyList.this.finish();

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

        common.PopupWindow(AppropriationEntyList.this, dm, popuMenuData);
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
                   AppropriationEntyList.this.finish();
                }
                break;
            default:
        }
    }
    private void showStockWindow() {
        common = new Common();
        popuMenuDatas = new ArrayList<PopuMenuDataStructure>();
        stockList= DataSupport.findAll(Stock.class);
        for(Stock stock:stockList)

        {
            PopuMenuDataStructure popuMenua = new PopuMenuDataStructure(R.drawable.poppu_wrie, stock.getName());
            popuMenuDatas.add(popuMenua);

        }
        common.PopupWindow(AppropriationEntyList.this, dm, popuMenuDatas);
        common.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {

                number.setText(popuMenuDatas.get(position).getName());
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
