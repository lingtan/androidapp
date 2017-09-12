package com.example.androiderp.form;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.androiderp.CustomDataClass.Stock;
import com.example.androiderp.CustomDataClass.Tally;
import com.example.androiderp.R;
import com.example.androiderp.adaper.CommonAdapterData;
import com.example.androiderp.adaper.PopuMenuDataStructure;
import com.example.androiderp.basicdata.AccountsListView;
import com.example.androiderp.basicdata.BalanceAccountListView;
import com.example.androiderp.basicdata.StockIntentListview;
import com.example.androiderp.common.Common;
import com.example.androiderp.custom.CustomSearchBase;
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

public class TallyForm extends CustomSearchBase implements View.OnClickListener{
    private InputMethodManager manager;
    private EditText note,amount;
    private TextView save,toobarTile,toobarBack,toobarAdd, accounts, balanceAccount,businessdata;
    private DisplayMetrics dm;
    private LinearLayout balanceaccountLayout, accountsLayout,businessdataLayout;
    private Stock  stock;
    private Drawable errorIcon;
    private Common common;
    private List<PopuMenuDataStructure> popuMenuList;
    private List<CommonAdapterData> commonAdapterDataList = new ArrayList<CommonAdapterData>();
    private Calendar calendar;
    private int year,month,day;
    private Intent intent;
    private DecimalFormat df = new DecimalFormat("#####0.00");


    public void iniView() {
        setContentView(R.layout.tallyform);
        dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        final Intent intent=getIntent();
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        balanceAccount =(TextView)findViewById(R.id.balanceaccount);
        accounts =(TextView)findViewById(R.id.accounts);
        note=(EditText)findViewById(R.id.note);
        amount=(EditText)findViewById(R.id.amount);
        businessdata=(TextView) findViewById(R.id.businessdata);
        save=(TextView)findViewById(R.id.customtoobar_right);
        toobarTile=(TextView)findViewById(R.id.customtoobar_midd);
        toobarBack=(TextView)findViewById(R.id.customtoobar_left);
        toobarAdd=(TextView)findViewById(R.id.customtoobar_r) ;
        accountsLayout =(LinearLayout)findViewById(R.id.accounts_layout);
        balanceaccountLayout =(LinearLayout)findViewById(R.id.balanceaccount_layout);
        businessdataLayout=(LinearLayout)findViewById(R.id.businessdata_layout);
        accountsLayout.setOnClickListener(this);
        businessdataLayout.setOnClickListener(this);
        balanceaccountLayout.setOnClickListener(this);
        save.setOnClickListener(this);
        toobarBack.setOnClickListener(this);
        toobarAdd.setOnClickListener(this);
        save.setCompoundDrawables(null,null,null,null);
        toobarTile.setCompoundDrawables(null,null,null,null);
        errorIcon = getResources().getDrawable(R.drawable.icon_error);
// 设置图片大小
        errorIcon.setBounds(new Rect(0, 0, errorIcon.getIntrinsicWidth(), errorIcon.getIntrinsicHeight()));
        save.setText("保存");
        getDate();
        toobarTile.setText("记账");

    }


    //获取当前日期
    private void getDate() {
        calendar=Calendar.getInstance();
        year=calendar.get(Calendar.YEAR);       //获取年月日时分秒
        month=calendar.get(Calendar.MONTH);   //获取到的月份是从0开始计数
        day=calendar.get(Calendar.DAY_OF_MONTH);
        businessdata.setText(year+"-"+(++month)+"-"+day);
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
                if (TextUtils.isEmpty(balanceAccount.getText().toString())) {
                    Toast.makeText(TallyForm.this,"请选择结算账户",Toast.LENGTH_SHORT).show();

                }else if (TextUtils.isEmpty(accounts.getText().toString()))
                {
                    Toast.makeText(TallyForm.this,"请选择账目类型",Toast.LENGTH_SHORT).show();
                } else

                {

                        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
                        Date curData = new Date(System.currentTimeMillis());
                        String fdate = format.format(curData);

                    Tally tally=new Tally();

                    tally.setNumber("JZLS" + fdate);
                    tally.setBalanceAccount(balanceAccount.getText().toString());
                    tally.setAccounts(accounts.getText().toString());
                    tally.setDate(businessdata.getText().toString());
                    tally.setAmount(Double.parseDouble(amount.getText().toString()));
                    tally.setNote(note.getText().toString());
                    tally.save();
                        Toast.makeText(TallyForm.this, "新增成功", Toast.LENGTH_SHORT).show();
                        save.setVisibility(View.GONE);
                        toobarAdd.setVisibility(View.VISIBLE);


                }

            break;
            case R.id.accounts_layout:

                    Intent intentstock=new Intent(TallyForm.this, AccountsListView.class);
                    intentstock.putExtra("index", accounts.getText().toString());
                    startActivityForResult(intentstock,11);

                break;

            case R.id.balanceaccount_layout:


                    Intent intentn=new Intent(TallyForm.this, BalanceAccountListView.class);
                    intentn.putExtra("index", balanceAccount.getText().toString());
                    startActivityForResult(intentn,12);

                break;

            case R.id.businessdata_layout:

                DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        businessdata.setText(year+"-"+(++month)+"-"+dayOfMonth);
                    }
                };
                DatePickerDialog dialogData=new DatePickerDialog(TallyForm.this, 0,listener,year,month,day);//后边三个参数为显示dialog时默认的日期，月份从0开始，0-11对应1-12个月
                dialogData.setButton(DialogInterface.BUTTON_NEGATIVE,"取消",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialogData.show();


                break;

            case R.id.customtoobar_left:
                if(commonAdapterDataList.size()>0)
                {
                    AlertDialog.Builder dialog=new AlertDialog.Builder(TallyForm.this);
                    dialog.setTitle("提示");
                    dialog.setMessage("单据还没保存，确认要退出？");
                    dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            TallyForm.this.finish();
                        }
                    });
                    dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    dialog.show();
                }else {
                    Intent intent=new Intent();
                    setResult(RESULT_OK,intent);
                    TallyForm.this.finish();
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
                default:

        }
    }
    private void showPopupWindow(final List<PopuMenuDataStructure> popuMenuData) {
        common = new Common();

        common.PopupWindow(TallyForm.this, dm, popuMenuData);
        common.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {

                if(popuMenuList.get(position).getName().equals("销售单新增"))
                {
                    intent = new Intent(TallyForm.this, TallyForm.class);
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
                   TallyForm.this.finish();
                }
                break;

            case 11:
                if(resultCode==RESULT_OK){
                    accounts.setText(data.getStringExtra("data_return"));
                }
                break;
            case 12:
                if(resultCode==RESULT_OK){
                    balanceAccount.setText(data.getStringExtra("data_return"));
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




}
