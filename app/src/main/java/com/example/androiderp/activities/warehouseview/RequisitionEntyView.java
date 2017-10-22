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

import com.example.androiderp.bean.Appropriation;
import com.example.androiderp.bean.AppropriationEnty;
import com.example.androiderp.bean.Employee;
import com.example.androiderp.bean.Stock;
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

public class RequisitionEntyView extends CSearchBase implements View.OnClickListener, AdapterView.OnItemClickListener,
        SlideAndDragListView.OnMenuItemClickListener, SlideAndDragListView.OnItemDeleteListener {
    private InputMethodManager manager;
    private TextView note,save,tile,back,add, documentmaker,out, in, businessdata, billnumber,totalQuantity;
    private DisplayMetrics dm;
    private Appropriation appropriation;
    private String id;
    private Drawable errorIcon;
    private Common common;
    private double quantityCount;
    private Intent  iBack;
    private List<PopBean> popList;
    private List<AppropriationEnty> appropriationEntyList=new ArrayList<AppropriationEnty>();
    private List<CommonAdapterData> commonAdapterDataList = new ArrayList<CommonAdapterData>();
    private SlideAndDragListView<CommonAdapterData> listView;
    private AppropriationListViewAdapter adapter;
    private Menu menu;
    private List<Stock> stockList;
    public void iniView() {
        setContentView(R.layout.requisition_enty);
        initMenu();
        initUiAndListener();
        dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        showStockWindow();
        iBack= new Intent(RequisitionEntyView.this, RequisitionEntyView.class);
        final Intent intent=getIntent();
        id=intent.getStringExtra("custom_item");
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        out =(TextView)findViewById(R.id.requisition_enty_out);
        in =(TextView)findViewById(R.id.requisition_enty_in);
        businessdata =(TextView)findViewById(R.id.requisition_enty_businessdata);
        billnumber =(TextView)findViewById(R.id.requisition_enty_billnumber);
        note=(TextView)findViewById(R.id.note);
        documentmaker =(TextView)findViewById(R.id.requisition_enty_documentmaker);
        save=(TextView)findViewById(R.id.customtoobar_right);
        tile=(TextView)findViewById(R.id.customtoobar_midd);
        back=(TextView)findViewById(R.id.customtoobar_left);
        add=(TextView)findViewById(R.id.customtoobar_r) ;
        totalQuantity=(TextView)findViewById(R.id.requisition_enty_totalquantity);
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
        out.setText("调出仓库");
        in.setText("调入仓库");
        formInit();


        tile.setText("调拨明细");

    }
    private void  formInit()
    {  quantityCount=0;
        DecimalFormat df = new DecimalFormat("#####0.00");
        if(id!=null) {
            Employee  employeelist = DataSupport.find(Employee.class, 1);
            if(employeelist==null)
            {

            }else {
                documentmaker.setText(employeelist.getName());
            }
            appropriation = DataSupport.find(Appropriation.class, Long.parseLong(id),true);
            appropriationEntyList=appropriation.getSalesOutEntyList();
            out.setText(appropriation.getStockOut());
            in.setText(appropriation.getStockIn());
            businessdata.setText(appropriation.getDate().toString().trim());
            billnumber.setText(appropriation.getNuber());
            note.setText(appropriation.getNote());
          for(AppropriationEnty appropriationEnty:appropriationEntyList) {
              CommonAdapterData commonData = new CommonAdapterData();
              commonData.setUnitId(appropriationEnty.getId());
              commonData.setNumber(appropriationEnty.getNumber());
              commonData.setName(appropriationEnty.getName());
              commonData.setFqty(appropriationEnty.getQuantity());
              quantityCount+=appropriationEnty.getQuantity();
              commonAdapterDataList.add(commonData);
          }

            totalQuantity.setText(df.format(quantityCount));
            adapter = new AppropriationListViewAdapter(RequisitionEntyView.this, R.layout.saleproduct_item, commonAdapterDataList);
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

                    RequisitionEntyView.this.finish();

             break;
            case R.id.customtoobar_r:
                if( common.mPopWindow==null ||!common.mPopWindow.isShowing())
                {   popList.clear();

                    PopBean popuMenuN = new PopBean(R.drawable.poppu_wrie, "商品新增");
                    popList.add(popuMenuN);
                    PopBean popuMenuC = new PopBean(R.drawable.poppu_wrie, "商品复制");
                    popList.add(popuMenuC);
                    int xPos = dm.widthPixels / 3;
                    showPopupWindow(popList);
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

        common.PopupWindow(RequisitionEntyView.this, dm, popuMenuData);
        common.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {

                if(popList.get(position).getName().equals("商品新增"))
                {

                    iBack.removeExtra("product_item");
                    iBack.putExtra("action","add");
                    startActivityForResult(iBack,4);
                }
                else if(popList.get(position).getName().equals("商品复制"))

                {
                    iBack.removeExtra("product_item");
                    iBack.putExtra("action","add");
                    iBack.putExtra("product_item", quantityCount);
                    startActivityForResult(iBack,4);

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
                   RequisitionEntyView.this.finish();
                }
                break;
            default:
        }
    }
    private void showStockWindow() {
        common = new Common();
        popList = new ArrayList<PopBean>();
        stockList= DataSupport.findAll(Stock.class);
        for(Stock stock:stockList)

        {
            PopBean popuMenua = new PopBean(R.drawable.poppu_wrie, stock.getName());
            popList.add(popuMenua);

        }
        common.PopupWindow(RequisitionEntyView.this, dm, popList);
        common.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {

                in.setText(popList.get(position).getName());
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
