package com.example.androiderp.form;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.androiderp.CustomDataClass.Product;
import com.example.androiderp.CustomDataClass.ProductShopping;
import com.example.androiderp.R;

import org.litepal.crud.DataSupport;

import java.text.DecimalFormat;

/**
 * Created by lingtan on 2017/5/15.
 */

public class ScrennProductShoppingForm extends AppCompatActivity implements View.OnClickListener {
    private InputMethodManager manager;
    private EditText name,number,salesfqty,salesprice,category;
    private TextView save,toobar_tile,toobar_back,toobar_add;
    private ProductShopping shopping;
    private DisplayMetrics dm;
    private Product customlist;
    private String customid,edit;
    private Drawable errorIcon;
    private String  amounttext;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.productshoppingform);
        dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        final Intent intent=getIntent();
        customid=intent.getStringExtra("product_item");
        edit=intent.getStringExtra("action");
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        name=(EditText)findViewById(R.id.product_name);
        name.setKeyListener(null);
        number=(EditText)findViewById(R.id.product_number);
        number.setKeyListener(null);
        category=(EditText)findViewById(R.id.product_category);
        category.setKeyListener(null);
        salesfqty=(EditText)findViewById(R.id.product_fqty);
        salesprice=(EditText)findViewById(R.id.product_salesprice);
        save=(TextView)findViewById(R.id.customtoobar_right);
        toobar_tile=(TextView)findViewById(R.id.customtoobar_midd);
        toobar_back=(TextView)findViewById(R.id.customtoobar_left);
        toobar_add=(TextView)findViewById(R.id.customtoobar_r) ;
        save.setOnClickListener(this);
        toobar_back.setOnClickListener(this);
        toobar_add.setCompoundDrawables(null,null,null,null);
        save.setCompoundDrawables(null,null,null,null);
        toobar_tile.setCompoundDrawables(null,null,null,null);
        toobar_back.setCompoundDrawables(null,null,null,null);
        errorIcon = getResources().getDrawable(R.drawable.icon_error);
// 设置图片大小
        errorIcon.setBounds(new Rect(0, 0, errorIcon.getIntrinsicWidth(),
                errorIcon.getIntrinsicHeight()));
        save.setText("保存");
        toobar_tile.setText("商品销售");
        Drawable del= getResources().getDrawable(R.drawable.suppliercategory_delete);
        del.setBounds(0, 0, del.getMinimumWidth(), del.getMinimumHeight());
        toobar_back.setCompoundDrawables(del,null,null,null);
        toobar_back.setText("");
        formInit();
        salesprice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(s.toString().trim().length()==0|salesfqty.getText().toString().trim().length()==0)
                {
                    toobar_tile.setText("商品销售");
                }else {

                    DecimalFormat df = new DecimalFormat("#####0.00");
                    amounttext=df.format(Double.valueOf(salesprice.getText().toString().trim()) * Integer.parseInt(salesfqty.getText().toString().trim()));

                    toobar_tile.setText("金额：¥"+amounttext);
                }


            }
        });
        salesfqty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(s.toString().trim().length()==0|salesprice.getText().toString().trim().length()==0)
                {
                    toobar_tile.setText("商品销售");
                }else {

                    DecimalFormat df = new DecimalFormat("#####0.00");
                    amounttext=df.format(Double.valueOf(salesprice.getText().toString().trim()) * Integer.parseInt(salesfqty.getText().toString().trim()));

                    toobar_tile.setText("金额：¥"+amounttext);
                }
                }

        });

    }


    private void  formInit()
    {

        if(customid!=null) {

            customlist = DataSupport.find(Product.class, Long.parseLong(customid));
            name.setText(customlist.getName());
            number.setText(customlist.getNumber());
            salesprice.setText(customlist.getSalesprice());
            category.setText(customlist.getCategory());
            if(edit.equals("edit")) {
                toobar_add.setVisibility(View.VISIBLE);
            }else {
                toobar_add.setVisibility(View.INVISIBLE);
            }
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId())

        {
            case R.id.customtoobar_right:
                if (TextUtils.isEmpty(salesprice.getText().toString())) {
                    salesprice.setError("销售价格不能为0",errorIcon);
                }else if (TextUtils.isEmpty(salesfqty.getText().toString()))
                {
                    salesfqty.setError("销售数量不能为0",errorIcon);
                }else {
                    shopping = new ProductShopping();
                    shopping.setSalename(name.getText().toString());
                    shopping.setSalenumber(number.getText().toString());
                    shopping.setCategory(category.getText().toString());
                    shopping.setSalesprice(Double.valueOf(salesprice.getText().toString().trim()));
                    shopping.setSalefqty(Integer.parseInt(salesfqty.getText().toString().trim()));
                    shopping.setSaleamount(Double.valueOf(salesprice.getText().toString().trim())*Integer.parseInt(salesfqty.getText().toString().trim()));
                    toobar_add.setVisibility(View.VISIBLE);
                    Intent intent = new Intent();
                    intent.putExtra("shop_data",shopping);
                    setResult(RESULT_OK,intent);
                    finish();
                }


            break;
            case R.id.customtoobar_left:
                Intent intent = new Intent();
                setResult(RESULT_FIRST_USER,intent);
                finish();
             break;



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
