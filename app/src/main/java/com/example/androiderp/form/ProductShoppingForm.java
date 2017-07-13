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
import android.widget.Button;
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

public class ProductShoppingForm extends AppCompatActivity implements View.OnClickListener {
    private InputMethodManager manager;
    private EditText name,number, quantity, price,category;
    private TextView toobarSave, toobarTile, toobarBack, toobarAdd;
    private ProductShopping productShopping;
    private DisplayMetrics dm;
    private Product product;
    private String customid,edit;
    private Drawable errorIcon;
    private String amountString;
    private Button saveButton;
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
        quantity =(EditText)findViewById(R.id.product_fqty);
        price =(EditText)findViewById(R.id.product_salesprice);
        toobarSave =(TextView)findViewById(R.id.customtoobar_right);
        toobarTile =(TextView)findViewById(R.id.customtoobar_midd);
        toobarBack =(TextView)findViewById(R.id.customtoobar_left);
        toobarAdd =(TextView)findViewById(R.id.customtoobar_r) ;
        saveButton =(Button)findViewById(R.id.shopping_button);
        saveButton.setOnClickListener(this);
        toobarSave.setOnClickListener(this);
        toobarBack.setOnClickListener(this);
        toobarAdd.setCompoundDrawables(null,null,null,null);
        toobarTile.setCompoundDrawables(null,null,null,null);
        errorIcon = getResources().getDrawable(R.drawable.icon_error);
// 设置图片大小
        errorIcon.setBounds(new Rect(0, 0, errorIcon.getIntrinsicWidth(),
                errorIcon.getIntrinsicHeight()));
        toobarTile.setText("商品销售");
        Drawable del= getResources().getDrawable(R.drawable.suppliercategory_delete);
        del.setBounds(0, 0, del.getMinimumWidth(), del.getMinimumHeight());
        Drawable more= getResources().getDrawable(R.drawable.toobar_more);
        more.setBounds(0, 0, more.getMinimumWidth(), more.getMinimumHeight());
        toobarBack.setCompoundDrawables(del,null,null,null);
        toobarSave.setCompoundDrawables(more,null,null,null);
        toobarBack.setText("");
        toobarSave.setText("");
        formInit();
        price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(s.toString().trim().length()==0| quantity.getText().toString().trim().length()==0)
                {
                    toobarTile.setText("商品销售");
                }else {

                    DecimalFormat df = new DecimalFormat("#####0.00");
                    amountString =df.format(Double.valueOf(price.getText().toString().trim()) * Integer.parseInt(quantity.getText().toString().trim()));

                    toobarTile.setText("金额：¥"+ amountString);
                }


            }
        });
        quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(s.toString().trim().length()==0| price.getText().toString().trim().length()==0)
                {
                    toobarTile.setText("商品销售");
                }else {

                    DecimalFormat df = new DecimalFormat("#####0.00");
                    amountString =df.format(Double.valueOf(price.getText().toString().trim()) * Double.valueOf(quantity.getText().toString().trim()));

                    toobarTile.setText("金额：¥"+ amountString);
                }
                }

        });

    }


    private void  formInit()
    {

        if(customid!=null) {

            product = DataSupport.find(Product.class, Long.parseLong(customid));
            name.setText(product.getName());
            number.setText(product.getNumber());
            price.setText(product.getSalesPrice());
            category.setText(product.getCategory());
            if(edit.equals("edit")) {
                toobarAdd.setVisibility(View.VISIBLE);
            }else {
                toobarAdd.setVisibility(View.INVISIBLE);
            }
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId())

        {
            case R.id.customtoobar_left:
                Intent intent = new Intent();
                setResult(RESULT_FIRST_USER,intent);
                finish();
             break;

            case R.id.shopping_button:
                if (TextUtils.isEmpty(price.getText().toString())) {
                    price.setError("销售价格不能为0",errorIcon);
                }else if (TextUtils.isEmpty(quantity.getText().toString()))
                {
                    quantity.setError("销售数量不能为0",errorIcon);
                }else {
                    productShopping = new ProductShopping();
                    productShopping.setId(product.getId());
                    productShopping.setName(name.getText().toString());
                    productShopping.setNumber(number.getText().toString());
                    productShopping.setCategory(category.getText().toString());
                    productShopping.setPrice(Double.valueOf(price.getText().toString().trim()));
                    productShopping.setQuantity(Double.valueOf(quantity.getText().toString().trim()));
                    productShopping.setAmount(Double.valueOf(price.getText().toString().trim())*Double.valueOf(quantity.getText().toString().trim()));
                    toobarAdd.setVisibility(View.VISIBLE);
                    Intent intentsave = new Intent();
                    intentsave.putExtra("shop_data", productShopping);
                    setResult(RESULT_OK,intentsave);
                    finish();
                }


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
