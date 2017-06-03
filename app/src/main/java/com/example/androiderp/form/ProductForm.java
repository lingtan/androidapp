package com.example.androiderp.form;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androiderp.CustomDataClass.Product;
import com.example.androiderp.CustomDataClass.ProductCategory;
import com.example.androiderp.CustomDataClass.Supplier;
import com.example.androiderp.R;
import com.example.androiderp.adaper.DataStructure;
import com.example.androiderp.basicdata.BrandListView;
import com.example.androiderp.basicdata.ProductCategoryListView;
import com.example.androiderp.basicdata.SupplierCategoryListView;
import com.example.androiderp.basicdata.UnitListView;
import com.example.androiderp.common.Common;

import org.litepal.crud.DataSupport;

/**
 * Created by lingtan on 2017/5/15.
 */

public class ProductForm extends AppCompatActivity implements View.OnClickListener {
    private InputMethodManager manager;
    private EditText name,number,purchaseprice,salesprice,barcode,model,note;
    private TextView save,toobar_tile,toobar_back,toobar_add,category,brand,unit;
    private Product supplier;
    private DisplayMetrics dm;
    private LinearLayout categoryLayout,brandLayout,unitLayout;
    private Product customlist;
    private String categoryid,customid,edit;
    private Button buttondelete;
    private Drawable errorIcon;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.productform);
        dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        final Intent intent=getIntent();
        customid=intent.getStringExtra("product_item");
        edit=intent.getStringExtra("action");
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        name=(EditText)findViewById(R.id.product_name);
        number=(EditText)findViewById(R.id.product_number);
        purchaseprice=(EditText)findViewById(R.id.product_purchaseprice);
        salesprice=(EditText)findViewById(R.id.product_salesprice);
        barcode=(EditText)findViewById(R.id.product_barcode);
        model=(EditText)findViewById(R.id.product_model);
        note=(EditText)findViewById(R.id.product_note);

        category=(TextView)findViewById(R.id.product_category);
        brand=(TextView)findViewById(R.id.product_brand);
        unit=(TextView)findViewById(R.id.product_unit);
        save=(TextView)findViewById(R.id.customtoobar_right);
        toobar_tile=(TextView)findViewById(R.id.customtoobar_midd);
        toobar_back=(TextView)findViewById(R.id.customtoobar_left);
        categoryLayout=(LinearLayout)findViewById(R.id.product_category_layout);
        brandLayout=(LinearLayout)findViewById(R.id.product_brand_layout);
        unitLayout=(LinearLayout)findViewById(R.id.product_unit_layout);
        toobar_add=(TextView)findViewById(R.id.customtoobar_r) ;
        buttondelete=(Button)findViewById(R.id.loginbutton);
        save.setOnClickListener(this);
        toobar_back.setOnClickListener(this);
        categoryLayout.setOnClickListener(this);
        brandLayout.setOnClickListener(this);
        unitLayout.setOnClickListener(this);
        toobar_add.setOnClickListener(this);
        buttondelete.setOnClickListener(this);
        save.setCompoundDrawables(null,null,null,null);
        toobar_tile.setCompoundDrawables(null,null,null,null);
        errorIcon = getResources().getDrawable(R.drawable.icon_error);
// 设置图片大小
        errorIcon.setBounds(new Rect(0, 0, errorIcon.getIntrinsicWidth(),
                errorIcon.getIntrinsicHeight()));
        save.setText("保存");
        formInit();

    }
    private void  formInit()
    {

        if(customid!=null) {

            customlist = DataSupport.find(Product.class, Long.parseLong(customid));
            name.setText(customlist.getName());
            number.setText(customlist.getNumber());
            purchaseprice.setText(customlist.getPurchaseprice());
            salesprice.setText(customlist.getSalesprice());
            barcode.setText(customlist.getBarcode());
            model.setText(customlist.getModel());
            note.setText(customlist.getNote());
            category.setText(customlist.getCategory());
            brand.setText(customlist.getBrand());
            unit.setText(customlist.getUnit());
            toobar_add.setVisibility(View.VISIBLE);
            buttondelete.setVisibility(View.VISIBLE);
        }
        if(edit!=null) {
            if (edit.equals("edit")) {
                toobar_tile.setText("商品修改");
            } else {
                toobar_tile.setText("商品新增");
            }
        }
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
                    name.setError("需要输入商品名称",errorIcon);
                }else if (TextUtils.isEmpty(category.getText().toString()))
                {
                    category.setError("请选择分类",errorIcon);
                }
                else if (edit.equals("edit")) {
                    supplier = new Product();
                    supplier.setName(name.getText().toString());
                    supplier.setNumber(number.getText().toString());
                    supplier.setPurchaseprice(purchaseprice.getText().toString());
                    supplier.setSalesprice(salesprice.getText().toString());
                    supplier.setBarcode(barcode.getText().toString());
                    supplier.setModel(model.getText().toString());
                    supplier.setNote(note.getText().toString());
                    supplier.setCategory(category.getText().toString());
                    supplier.setBrand(brand.getText().toString());
                    supplier.setUnit(unit.getText().toString());
                    supplier.update(Long.parseLong(customid));
                    Toast.makeText(ProductForm.this,"修改成功",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    setResult(RESULT_OK,intent);
                    hintKbTwo();

                } else {
                    supplier = new Product();
                    supplier.setName(name.getText().toString());
                    supplier.setNumber(number.getText().toString());
                    supplier.setPurchaseprice(purchaseprice.getText().toString());
                    supplier.setSalesprice(salesprice.getText().toString());
                    supplier.setBarcode(barcode.getText().toString());
                    supplier.setModel(model.getText().toString());
                    supplier.setNote(note.getText().toString());
                    supplier.setCategory(category.getText().toString());
                    supplier.setBrand(brand.getText().toString());
                    supplier.setUnit(unit.getText().toString());
                    supplier.save();
                    Toast.makeText(ProductForm.this,"新增成功",Toast.LENGTH_SHORT).show();
                    edit="edit";
                    toobar_add.setVisibility(View.VISIBLE);
                    buttondelete.setVisibility(View.VISIBLE);
                    Intent intent = new Intent();
                    setResult(RESULT_OK,intent);
                    hintKbTwo();

                }

            break;
            case R.id.customtoobar_left:
                if(edit.equals("edit"))
                {Intent intent = new Intent();
                    intent.putExtra("category",categoryid);
                    setResult(RESULT_OK,intent);
                    finish();
                }else {
                    finish();
                }
             break;
            case R.id.product_category_layout:

                Intent intentcategory=new Intent(ProductForm.this, ProductCategoryListView.class);
                intentcategory.putExtra("index",category.getText().toString());
                startActivityForResult(intentcategory,1);
                break;

            case R.id.product_brand_layout:

                Intent intentbrand=new Intent(ProductForm.this, BrandListView.class);
                intentbrand.putExtra("index",brand.getText().toString());
                startActivityForResult(intentbrand,2);
                break;

            case R.id.product_unit_layout:

                Intent intentunit=new Intent(ProductForm.this, UnitListView.class);
                intentunit.putExtra("index",unit.getText().toString());
                startActivityForResult(intentunit,3);
                break;
            case R.id.loginbutton:
                AlertDialog.Builder dialog=new AlertDialog.Builder(ProductForm.this);
                dialog.setTitle("提示");
                dialog.setMessage("您确认要删除该商品？");
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DataStructure.deleteAll(Product.class,"name = ?",name.getText().toString());

                        AlertDialog.Builder dialogOK=new AlertDialog.Builder(ProductForm.this);
                        dialogOK.setMessage("该商品已经删除");
                        dialogOK.setNegativeButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();
                                setResult(RESULT_OK,intent);
                                finish();
                            }
                        });
                        dialogOK.show();




                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();
                break;

            case R.id.customtoobar_r:
                name.setText("");
                number.setText("");
                purchaseprice.setText("");
                salesprice.setText("");
                barcode.setText("");
                model.setText("");
                note.setText("");
                category.setText("");
                brand.setText("");
                unit.setText("");
                toobar_add.setVisibility(View.INVISIBLE);
                toobar_tile.setText("商品新增");
                buttondelete.setVisibility(View.INVISIBLE);
                edit="";
                hintKbTwo();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if(resultCode==RESULT_OK){
                    category.setText(data.getStringExtra("data_return"));
                }
                break;
            case 2:
                if(resultCode==RESULT_OK){
                    brand.setText(data.getStringExtra("data_return"));
                }
                break;
            case 3:
                if(resultCode==RESULT_OK){
                    unit.setText(data.getStringExtra("data_return"));
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
