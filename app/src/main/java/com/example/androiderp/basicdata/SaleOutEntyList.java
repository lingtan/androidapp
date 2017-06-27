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
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;
import com.example.androiderp.CustomDataClass.SalesOut;
import com.example.androiderp.CustomDataClass.SalesOutEnty;
import com.example.androiderp.CustomDataClass.Stock;
import com.example.androiderp.R;
import com.example.androiderp.adaper.CommonDataStructure;
import com.example.androiderp.adaper.PopuMenuDataStructure;
import com.example.androiderp.adaper.SaleProductListViewAdapter;
import com.example.androiderp.common.Common;
import com.example.androiderp.custom.CustomSearchBase;
import com.example.androiderp.listview.Menu;
import com.example.androiderp.listview.MenuItem;
import com.example.androiderp.listview.SlideAndDragListView;
import org.litepal.crud.DataSupport;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by lingtan on 2017/5/15.
 */

public class SaleOutEntyList extends CustomSearchBase implements View.OnClickListener, AdapterView.OnItemClickListener,
        SlideAndDragListView.OnMenuItemClickListener, SlideAndDragListView.OnItemDeleteListener {
    private InputMethodManager manager;
    private TextView note,save,toobar_tile,toobar_back,toobar_add,category,name,number,data,consignment,totalamout,totalfqty;
    private DisplayMetrics dm;
    private SalesOut customlist;
    private String customid;
    private Drawable errorIcon;
    private Common common;
    private Intent  intentback;
    private List<PopuMenuDataStructure> popuMenuDatas;
    private List<SalesOutEnty> salesOutEntyList=new ArrayList<SalesOutEnty>();
    private List<CommonDataStructure> listdatas = new ArrayList<CommonDataStructure>();
    private SlideAndDragListView<CommonDataStructure> plistView;
    private SaleProductListViewAdapter adapter;
    private Menu mMenu;
    private List<Stock> stocks;
    public void iniView() {
        setContentView(R.layout.saleoutentyform);
        initMenu();
        initUiAndListener();
        dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        showStockWindow();
        intentback= new Intent(SaleOutEntyList.this, SaleOutEntyList.class);
        final Intent intent=getIntent();
        customid=intent.getStringExtra("custom_item");
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        name=(TextView)findViewById(R.id.product_custom);
        number=(TextView)findViewById(R.id.product_stock);
        data=(TextView)findViewById(R.id.product_data);
        consignment=(TextView)findViewById(R.id.product_consignment);
        note=(TextView)findViewById(R.id.product_note);
        category=(TextView)findViewById(R.id.product_category);
        save=(TextView)findViewById(R.id.customtoobar_right);
        toobar_tile=(TextView)findViewById(R.id.customtoobar_midd);
        toobar_back=(TextView)findViewById(R.id.customtoobar_left);
        toobar_add=(TextView)findViewById(R.id.customtoobar_r) ;
        totalfqty=(TextView)findViewById(R.id.saleoutenty_fqty);
        totalamout=(TextView)findViewById(R.id.saleoutenty_amount);
        save.setOnClickListener(this);
        toobar_back.setOnClickListener(this);
        toobar_add.setOnClickListener(this);
        Drawable more= getResources().getDrawable(R.drawable.toobar_more);
        more.setBounds(0, 0, more.getMinimumWidth(), more.getMinimumHeight());
        save.setCompoundDrawables(more,null,null,null);
        toobar_tile.setCompoundDrawables(null,null,null,null);
        errorIcon = getResources().getDrawable(R.drawable.icon_error);
// 设置图片大小
        errorIcon.setBounds(new Rect(0, 0, errorIcon.getIntrinsicWidth(),
                errorIcon.getIntrinsicHeight()));
        save.setText("");
        formInit();


        toobar_tile.setText("销售明细");

    }
    private void  formInit()
    {
        DecimalFormat df = new DecimalFormat("#####0.00");
        if(customid!=null) {

            customlist = DataSupport.find(SalesOut.class, Long.parseLong(customid),true);
            salesOutEntyList=customlist.getSalesOutEntyList();
            name.setText(customlist.getCustomer());
            number.setText(customlist.getStock());
            data.setText(customlist.getFdate().toString().trim());
            category.setText(customlist.getSalesman());
            consignment.setText(customlist.getConsignment());
            totalamout.setText("¥"+df.format(customlist.getSaleamount()));
            totalfqty.setText(String.valueOf(customlist.getSalefqty()));
            note.setText(customlist.getNote());
          for(SalesOutEnty salesOutEnty:salesOutEntyList) {
              CommonDataStructure commonData = new CommonDataStructure();
              commonData.setId(salesOutEnty.getId());
              commonData.setNumber(salesOutEnty.getItemnumber());
              commonData.setName(salesOutEnty.getItemname());
              commonData.setFqty(salesOutEnty.getItemfqty());
              commonData.setSaleamount(salesOutEnty.getItemamount());
              commonData.setSalesprice(salesOutEnty.getItemprice());

              listdatas.add(commonData);
          }
            adapter = new SaleProductListViewAdapter(SaleOutEntyList.this, R.layout.saleproduct_item, listdatas);
            plistView.setAdapter(adapter);
            setListViewHeightBasedOnChildren(plistView);

        }
    }
    public void initMenu() {
        mMenu = new Menu(true);

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

                    SaleOutEntyList.this.finish();

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

        common.PopupWindow(SaleOutEntyList.this, dm, popuMenuData);
        common.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {

                if(popuMenuDatas.get(position).getName().equals("商品新增"))
                {

                    intentback.removeExtra("product_item");
                    intentback.putExtra("action","add");
                    startActivityForResult(intentback,4);
                }
                else if(popuMenuDatas.get(position).getName().equals("商品复制"))

                {
                    intentback.removeExtra("product_item");
                    intentback.putExtra("action","add");
                    intentback.putExtra("product_item", customid);
                    startActivityForResult(intentback,4);

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
                   SaleOutEntyList.this.finish();
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
        common.PopupWindow(SaleOutEntyList.this, dm, popuMenuDatas);
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
