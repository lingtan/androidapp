package com.example.androiderp.activities.basicfrom;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.androiderp.basic.BasicView;
import com.example.androiderp.bean.AcivityPostBen;
import com.example.androiderp.bean.AdapterBean;
import com.example.androiderp.bean.PostProductData;
import com.example.androiderp.bean.PostUserData;
import com.example.androiderp.bean.Product;
import com.example.androiderp.bean.ReturnUserData;
import com.example.androiderp.bean.SalesOutEnty;
import com.example.androiderp.bean.StockIniti;
import com.example.androiderp.bean.StockInitiData;
import com.example.androiderp.bean.StockInitiTem;
import com.example.androiderp.R;
import com.example.androiderp.bean.DataStructure;
import com.example.androiderp.bean.PopuMenuDataStructure;
import com.example.androiderp.activities.warehouseview.StockInitiView;
import com.example.androiderp.tools.Common;
import com.example.androiderp.tools.GlobalVariable;
import com.example.androiderp.tools.HttpUtil;
import com.example.androiderp.ui.CPopupWindow;
import com.example.androiderp.ui.CPictureFullPopupWindow;
import com.example.androiderp.scanning.CommonScanActivity;
import com.example.androiderp.scanning.utils.Constant;
import com.example.androiderp.ui.DataLoadingDialog;
import com.google.gson.Gson;

import org.litepal.crud.DataSupport;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by lingtan on 2017/5/15.
 */

public class ProductForm extends AppCompatActivity implements View.OnClickListener {
    private InputMethodManager manager;
    private EditText name,number, purchasePrice, salesPrice,barcode,model,note;
    private ImageView numberScreen;
    private ImageButton camera;
    private TextView toobarSave, toobarTile, toobarBack, toobarAdd,category,brand,unit, stockIniti;
    private DisplayMetrics dm;
    private LinearLayout categoryLayout,brandLayout,unitLayout,hideLayoutOne,hideLayoutTow,hideLayoutthree;
    private RelativeLayout pictureThressLayout,pictureSecondLayout,pictureFisrtLayout;
    private RelativeLayout moreLayout;
    private String categoryid,customid;
    private Button deleteButton;
    private Drawable errorIcon;
    private Common common;
    private Intent intentBack;
    private List<PopuMenuDataStructure> popuMenuDatas;
    private List<SalesOutEnty> salesOutEntyList =new ArrayList<SalesOutEnty>();
    List<StockInitiTem> stockInitiTemList = new ArrayList<StockInitiTem>();
    private CPopupWindow cPopupWindow;
    private CPictureFullPopupWindow pictureFullPopupWindow;
    public static final int TAKE_PHOTO = 11;
    public static final int CHOOSE_PHOTO = 12;
    private ImageView pictureFisrt,pictureSecond,pictureThress,pictureFisrtDelete,pictureSecondDelete,pictureThressDelete;
    private Uri imageUri;
    private String photoUri,pictureFisrtSrc,pictureSecondSrc,pictureThressSrc,pictureMainSrc;
    private List<String> imageUirData=new ArrayList<String>();
    private List<String> imagePathData=new ArrayList<String>();
    private List<ImageView> imageViewData=new ArrayList<ImageView>();
    private String categoryReturnVale;
    private AcivityPostBen acivityPost=new AcivityPostBen();
    DecimalFormat df = new DecimalFormat("#####0.00");
    private AcivityPostBen getAcivityPostBen=new AcivityPostBen();
    private AcivityPostBen postAcivityPostBen=new AcivityPostBen();
    private PostProductData postUserData = new PostProductData();
    private Product getPostData;
    private Dialog dialog;
    private String getPostType;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.productform);
        dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        intentBack = new Intent(ProductForm.this, ProductForm.class);
        final Intent intent=getIntent();
        getPostData =intent.getParcelableExtra("postdata");
        getPostType = intent.getStringExtra("type");
        getAcivityPostBen=intent.getParcelableExtra("acivityPostBen");
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        name=(EditText)findViewById(R.id.name);
        number=(EditText)findViewById(R.id.number);
        purchasePrice =(EditText)findViewById(R.id.product_purchaseprice);
        salesPrice =(EditText)findViewById(R.id.product_salesprice);
        barcode=(EditText)findViewById(R.id.product_barcode);
        model=(EditText)findViewById(R.id.product_model);
        note=(EditText)findViewById(R.id.note);
        numberScreen =(ImageView)findViewById(R.id.number_screen);
        category=(TextView)findViewById(R.id.documentmaker);
        brand=(TextView)findViewById(R.id.product_brand);
        unit=(TextView)findViewById(R.id.product_unit);
        stockIniti =(TextView)findViewById(R.id.product_stock_initi);
        toobarSave =(TextView)findViewById(R.id.customtoobar_right);
        toobarTile =(TextView)findViewById(R.id.customtoobar_midd);
        toobarBack =(TextView)findViewById(R.id.customtoobar_left);
        categoryLayout=(LinearLayout)findViewById(R.id.documentmaker_layout);
        brandLayout=(LinearLayout)findViewById(R.id.product_brand_layout);
        unitLayout=(LinearLayout)findViewById(R.id.product_unit_layout);
        toobarAdd =(TextView)findViewById(R.id.customtoobar_r) ;
        deleteButton =(Button)findViewById(R.id.loginbutton);
        moreLayout=(RelativeLayout)findViewById(R.id.productform_layout_more);
        hideLayoutOne=(LinearLayout)findViewById(R.id.productform_layout_hide_one);
        hideLayoutTow=(LinearLayout)findViewById(R.id.productform_layout_hide_tow);
        hideLayoutthree=(LinearLayout)findViewById(R.id.productform_layout_hide_three);
        camera=(ImageButton)findViewById(R.id.camera);
        pictureFisrt = (ImageView) findViewById(R.id.picture_fisrt);
        pictureSecond = (ImageView) findViewById(R.id.picture_second);
        pictureThress = (ImageView) findViewById(R.id.picture_thress);
        pictureFisrtDelete = (ImageView) findViewById(R.id.picture_fisrt_delete);
        pictureSecondDelete = (ImageView) findViewById(R.id.picture_second_delete);
        pictureThressDelete = (ImageView) findViewById(R.id.picture_thress_delete);
        pictureFisrtLayout=(RelativeLayout)findViewById(R.id.picture_fisrt_layout);
        pictureSecondLayout=(RelativeLayout)findViewById(R.id.picture_second_layout);
        pictureThressLayout=(RelativeLayout)findViewById(R.id.picture_thress_layout);
        imageViewData.add(pictureFisrt);
        imageViewData.add(pictureSecond);
        imageViewData.add(pictureThress);
        toobarSave.setOnClickListener(this);
        toobarBack.setOnClickListener(this);
        categoryLayout.setOnClickListener(this);
        brandLayout.setOnClickListener(this);
        unitLayout.setOnClickListener(this);
        toobarAdd.setOnClickListener(this);
        deleteButton.setOnClickListener(this);
        moreLayout.setOnClickListener(this);
        stockIniti.setOnClickListener(this);
        camera.setOnClickListener(this);
        toobarSave.setCompoundDrawables(null,null,null,null);
        toobarTile.setCompoundDrawables(null,null,null,null);
        errorIcon = getResources().getDrawable(R.drawable.icon_error);
// 设置图片大小
        errorIcon.setBounds(new Rect(0, 0, errorIcon.getIntrinsicWidth(),
                errorIcon.getIntrinsicHeight()));
        toobarSave.setText("保存");
        formInit();

        popuMenuDatas = new ArrayList<PopuMenuDataStructure>();
        PopuMenuDataStructure popuMenua = new PopuMenuDataStructure(android.R.drawable.ic_menu_edit, "美的");
        popuMenuDatas.add(popuMenua);
        PopuMenuDataStructure popuMenub = new PopuMenuDataStructure(android.R.drawable.ic_menu_edit, "松下");
        popuMenuDatas.add(popuMenub);
        showPopupWindow(popuMenuDatas);
        numberScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent openCameraIntent = new Intent(ProductForm.this,CommonScanActivity.class);
                openCameraIntent.putExtra(Constant.REQUEST_SCAN_MODE,Constant.REQUEST_SCAN_MODE_ALL_MODE);
                startActivityForResult(openCameraIntent, 5);
            }
        });


    }
    private void  formInit()
    {

        if(getPostData!=null) {
            name.setText(getPostData.getName());
            number.setText(getPostData.getNumber());
            purchasePrice.setText(df.format(getPostData.getPurchasePrice()));
            salesPrice.setText(df.format(getPostData.getSalesPrice()));
            barcode.setText(getPostData.getBarcode());
            model.setText(getPostData.getModel());
            note.setText(getPostData.getNote());
            category.setText(getPostData.getCategory_name());
            categoryReturnVale= getPostData.getCategory_name();
            brand.setText(getPostData.getBrand_name());
            unit.setText(getPostData.getUnit_name());
            if(getPostType.equals("edit")) {
                number.setKeyListener(null);
                toobarAdd.setVisibility(View.VISIBLE);
                deleteButton.setVisibility(View.VISIBLE);
            }else {
                toobarAdd.setVisibility(View.GONE);
                deleteButton.setVisibility(View.GONE);
            }
            if(getPostData.getPhotoFirstPath()!=null&&!getPostData.getPhotoFirstPath().isEmpty())
            {   pictureFisrtLayout.setVisibility(View.VISIBLE);
                Glide.with(this).load(getPostData.getPhotoFirstPath()).override(100,100).into(pictureFisrt);
                imagePathData.add(getPostData.getPhotoFirstPath());
                pictureFisrt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        pictureFullPopupWindow = new CPictureFullPopupWindow(ProductForm.this, getPostData.getPhotoFirstPath());

                        pictureFullPopupWindow.showAtLocation(ProductForm.this.findViewById(R.id.main), Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);

                    }


                });
                pictureFisrtDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pictureFisrt.setImageDrawable(null);
                        Product      product = new Product();
                        product.setPhotoFirstPath("");
                        if(getPostData.getPhotoMainPath().equals(getPostData.getPhotoFirstPath())) {
                            product.setPhotoMainPath("");
                        }

                        File file=new File(getPostData.getPhotoFirstPath());
                        file.delete();
                        pictureFisrtLayout.setVisibility(View.GONE);





                    }
                });

               pictureFisrt.setOnLongClickListener(new View.OnLongClickListener() {
                   @Override
                   public boolean onLongClick(View v) {
                       Product      product = new Product();
                       product.setPhotoMainPath(getPostData.getPhotoFirstPath());

                       Toast.makeText(ProductForm.this,"次图片已经设置为封面",Toast.LENGTH_SHORT).show();
                       return true;
                   }
               });
            }

            if(getPostData.getPhotoSecondPath()!=null&&!getPostData.getPhotoSecondPath().isEmpty())
            {   pictureSecondLayout.setVisibility(View.VISIBLE);
                Glide.with(this).load(getPostData.getPhotoSecondPath()).override(100,100).into(pictureSecond);
                imagePathData.add(getPostData.getPhotoSecondPath());
                pictureSecond.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        pictureFullPopupWindow = new CPictureFullPopupWindow(ProductForm.this, getPostData.getPhotoSecondPath());

                        pictureFullPopupWindow.showAtLocation(ProductForm.this.findViewById(R.id.main), Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);

                    }
                });
                pictureSecondDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pictureSecond.setImageDrawable(null);
                        Product      product = new Product();
                        product.setPhotoSecondPath("");
                        if(getPostData.getPhotoMainPath().equals(getPostData.getPhotoSecondPath())) {
                            product.setPhotoMainPath("");
                        }

                        File file=new File(getPostData.getPhotoSecondPath());
                        file.delete();
                        pictureSecondLayout.setVisibility(View.GONE);


                    }
                });

                pictureSecond.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        Product      product = new Product();
                        product.setPhotoMainPath(getPostData.getPhotoSecondPath());


                        Toast.makeText(ProductForm.this,"次图片已经设置为封面",Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });
            }

            if(getPostData.getPhotoThressPath()!=null&&!getPostData.getPhotoThressPath().isEmpty())
            {   pictureThressLayout.setVisibility(View.VISIBLE);
                Glide.with(this).load(getPostData.getPhotoThressPath()).override(100,100).into(pictureThress);
                imagePathData.add(getPostData.getPhotoThressPath());
                pictureThress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        pictureFullPopupWindow = new CPictureFullPopupWindow(ProductForm.this, getPostData.getPhotoThressPath());

                        pictureFullPopupWindow.showAtLocation(ProductForm.this.findViewById(R.id.main), Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);

                    }
                });

                pictureThressDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pictureThress.setImageDrawable(null);
                        Product      product = new Product();
                        product.setPhotoThressPath("");
                        if(getPostData.getPhotoMainPath().equals(getPostData.getPhotoThressPath())) {
                            product.setPhotoMainPath("");
                        }

                        File file=new File(getPostData.getPhotoThressPath());
                        file.delete();
                        pictureThressLayout.setVisibility(View.GONE);


                    }
                });

                pictureThress.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        Product      product = new Product();
                        product.setPhotoMainPath(getPostData.getPhotoThressPath());

                        Toast.makeText(ProductForm.this,"次图片已经设置为封面",Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });

            }


        }
        if(getPostType!=null) {
            if (getPostType.equals("edit")) {
                toobarTile.setText("商品修改");
            } else {
                toobarTile.setText("商品新增");
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
                else if (getPostType.equals("edit")) {
                    postUserData.setProduct_id(getPostData.getProduct_id());
                    postUserData.setName(name.getText().toString());
                    postUserData.setNumber(number.getText().toString());
                    postUserData.setPurchasePrice( Double.parseDouble(purchasePrice.getText().toString()));
                    postUserData.setSalesPrice(Double.parseDouble(salesPrice.getText().toString()));
                    postUserData.setBarcode(barcode.getText().toString());
                    postUserData.setModel(model.getText().toString());
                    postUserData.setNote(note.getText().toString());
                    postUserData.setCategory_name(category.getText().toString());
                    postUserData.setBrand_name(brand.getText().toString());
                    postUserData.setUnit_name(unit.getText().toString());
                    categoryReturnVale=category.getText().toString();
                    postUserData.setRequestType(GlobalVariable.cfUpdate);
                    postUserData.setServerIp(Common.ip);
                    postUserData.setServlet(getAcivityPostBen.getRequestServlet());
                    postUserData.setClassType(getAcivityPostBen.getSetClassType());
                    postUserData.setPhotoFirstPath(getPostData.getPhotoFirstPath());
                    postUserData.setPhotoSecondPath(getPostData.getPhotoSecondPath());
                    postUserData.setPhotoThressPath(getPostData.getPhotoThressPath());
                    postUserData.setPhotoMainPath(getPostData.getPhotoMainPath());

                    showDialog();
                    getHttpData(postUserData);
                    hintKbTwo();

                } else

                {

                    postUserData.setName(name.getText().toString());
                    postUserData.setNumber(number.getText().toString());
                    postUserData.setPurchasePrice( Double.parseDouble(purchasePrice.getText().toString()));
                    postUserData.setSalesPrice( Double.parseDouble(salesPrice.getText().toString()));
                    postUserData.setBarcode(barcode.getText().toString());
                    postUserData.setModel(model.getText().toString());
                    postUserData.setNote(note.getText().toString());
                    postUserData.setCategory_name(category.getText().toString());
                    postUserData.setBrand_name(brand.getText().toString());
                    postUserData.setUnit_name(unit.getText().toString());
                    postUserData.setImage(R.drawable.listvist_item_delete);
                    postUserData.setBadgeShow("");
                    if(pictureFisrt.getDrawable()!=null)
                    {
                        postUserData.setPhotoFirstPath(pictureFisrtSrc);
                    }
                    if(pictureSecond.getDrawable()!=null)
                    {
                        postUserData.setPhotoSecondPath(pictureSecondSrc);
                    }
                    if(pictureThress.getDrawable()!=null) {
                        postUserData.setPhotoThressPath(pictureThressSrc);
                    }
                    if(pictureMainSrc==null)
                    {
                        postUserData.setPhotoMainPath(pictureFisrtSrc);
                    }else {
                        postUserData.setPhotoMainPath(pictureMainSrc);
                    }

                    postUserData.setRequestType(GlobalVariable.cfInsert);
                    postUserData.setServerIp(Common.ip);
                    postUserData.setServlet("ProductOperate");
                    postUserData.setClassType(1);
                    showDialog();
                    getHttpData(postUserData);

                    for(int i = 0; i < stockInitiTemList.size(); i++) {

                        if(stockInitiTemList.get(i).getQuantity()>0) {
                            StockIniti stockIniti=new StockIniti();
                            stockIniti.setName(name.getText().toString());
                            stockIniti.setNumber(number.getText().toString());
                            stockIniti.setStock(stockInitiTemList.get(i).getName());
                            stockIniti.setQuantity(stockInitiTemList.get(i).getQuantity());
                            stockIniti.save();

                        }


                    }

                    getPostType="edit";
                    hideLayoutthree.setVisibility(View.GONE);
                    toobarAdd.setVisibility(View.VISIBLE);
                    deleteButton.setVisibility(View.VISIBLE);
                    categoryReturnVale=category.getText().toString();
                    Intent intent = new Intent();
                    setResult(RESULT_OK,intent);
                    hintKbTwo();

                }

            break;
            case R.id.customtoobar_left:
                if(getPostType.equals("edit"))
                {Intent intent = new Intent();
                    intent.putExtra("category",categoryid);
                    intent.putExtra("returncategory",categoryReturnVale);
                    setResult(RESULT_OK,intent);
                    ProductForm.this.finish();
                    Glide.get(this).clearMemory();
                }else {
                    //不需要直接返回第一页
                    Intent intent = new Intent();
                    intent.putExtra("returncategory",categoryReturnVale);
                    setResult(RESULT_OK,intent);
                    ProductForm.this.finish();
                    Glide.get(this).clearMemory();
                }
             break;
            case R.id.documentmaker_layout:
                postAcivityPostBen.setAcivityName("产品类别");
                postAcivityPostBen.setRequestServlet("BrandOperate");
                postAcivityPostBen.setName(category.getText().toString());
                postAcivityPostBen.setSetClassType(2);
                postAcivityPostBen.setIsSelect("YES");
                Intent intentcategory=new Intent(ProductForm.this, BasicView.class);
                intentcategory.putExtra("acivityPostBen",postAcivityPostBen);
                startActivityForResult(intentcategory,1);
                break;

            case R.id.product_brand_layout:
                postAcivityPostBen.setAcivityName("品牌");
                postAcivityPostBen.setRequestServlet("BrandOperate");
                postAcivityPostBen.setName(brand.getText().toString());
                postAcivityPostBen.setSetClassType(1);
                postAcivityPostBen.setIsSelect("YES");
                Intent intentbrand=new Intent(ProductForm.this, BasicView.class);
                intentbrand.putExtra("acivityPostBen",postAcivityPostBen);
                startActivityForResult(intentbrand,2);
                break;

            case R.id.product_unit_layout:
                postAcivityPostBen.setAcivityName("单位");
                postAcivityPostBen.setRequestServlet("BrandOperate");
                postAcivityPostBen.setName(unit.getText().toString());
                postAcivityPostBen.setSetClassType(3);
                postAcivityPostBen.setIsSelect("YES");
                Intent intentunit=new Intent(ProductForm.this, BasicView.class);
                intentunit.putExtra("acivityPostBen",postAcivityPostBen);
                startActivityForResult(intentunit,3);
                break;
            case R.id.loginbutton:
                AlertDialog.Builder dialog=new AlertDialog.Builder(ProductForm.this);
                dialog.setTitle("提示");
                dialog.setMessage("您确认要删除该商品？");
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        postUserData.setProduct_id(getPostData.getProduct_id());
                        postUserData.setServerIp(Common.ip);
                        postUserData.setRequestType(GlobalVariable.cfDelete);
                        postUserData.setServlet(getAcivityPostBen.getRequestServlet());
                        postUserData.setClassType(getAcivityPostBen.getSetClassType());
                        postUserData.setRequestType(GlobalVariable.cfDelete);
                        showDialog();
                        getHttpData(postUserData);
                        Intent intent = new Intent();
                        setResult(RESULT_OK,intent);


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
            case  R.id.camera:


                cPopupWindow = new CPopupWindow(ProductForm.this, itemsOnClick);

                cPopupWindow.showAtLocation(ProductForm.this.findViewById(R.id.main), Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0); //����layout��PopupWindow����ʾ��λ��

                break;

            case R.id.productform_layout_more:
                moreLayout.setVisibility(View.GONE);
                hideLayoutOne.setVisibility(View.VISIBLE);
                hideLayoutTow.setVisibility(View.VISIBLE);
                if (getPostType.equals("edit")) {
                   hideLayoutthree.setVisibility(View.GONE);
                }else {
                    hideLayoutthree.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.product_stock_initi:

                Intent intentstock=new Intent(ProductForm.this, StockInitiView.class);
                StockInitiData stockInitiData=new StockInitiData();
                stockInitiData.setShoppingdata(stockInitiTemList);
                intentstock.putExtra("stockinitidata",stockInitiData);
                startActivityForResult(intentstock,10);

        }
    }
    private void showPopupWindow(final List<PopuMenuDataStructure> popuMenuData) {
        common = new Common();

        common.PopupWindow(ProductForm.this, dm, popuMenuData);
        common.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {

                if(popuMenuDatas.get(position).getName().equals("商品新增"))
                {

                    intentBack.removeExtra("product_item");
                    intentBack.putExtra("action","add");
                    startActivityForResult(intentBack,4);
                    finish();
                }
                else if(popuMenuDatas.get(position).getName().equals("商品复制"))

                {
                    intentBack.removeExtra("product_item");
                    intentBack.putExtra("action","add");
                    intentBack.putExtra("product_item", customid);
                    startActivityForResult(intentBack,4);
                    finish();

                }else
                {

                }
                common.mPopWindow.dismiss();
            }
        });
    }
    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO); // 打开相册
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
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

            case 4:
                if(resultCode==RESULT_OK){
                    Intent intent = new Intent();
                    setResult(RESULT_OK,intent);
                   ProductForm.this.finish();
                }
                break;

            case 5:
                if(resultCode==RESULT_OK) {

                    Intent intent=getIntent();
                    number.setText(data.getStringExtra("scanResult"));

                }

                break;
            case 10:
                if(resultCode==RESULT_OK) {

                    StockInitiData stockInitiData=data.getParcelableExtra("stockinitidata");
                    stockInitiTemList =stockInitiData.getShoppingdata();

                    for(StockInitiTem stock: stockInitiTemList)

                    {
                        DecimalFormat df = new DecimalFormat("#####0.00");
                        Log.d("linga",stock.getName());
                        Log.d("linga",df.format(stock.getQuantity()));




                    }

                }

                break;

            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {

                        if(pictureFisrt.getDrawable()==null)
                        {
                            pictureFisrtLayout.setVisibility(View.VISIBLE);
                            Glide.with(this).load(imageUri).override(100,100).into(pictureFisrt);
                            if(customid!=null) {
                                Product product = new Product();
                                product.setPhotoFirstPath(imageUri.toString());
                                if(pictureMainSrc==null&&(getPostData.getPhotoMainPath()==null|| getPostData.getPhotoMainPath().isEmpty()))
                                {
                                    product.setPhotoMainPath(imageUri.toString());
                                }

                            }
                            pictureFisrtSrc=imageUri.toString();


                            pictureFisrt.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View v) {
                                    if(customid!=null) {
                                        Product product = new Product();
                                        product.setPhotoMainPath(imageUri.toString());

                                    }else {
                                        pictureMainSrc=imageUri.toString();
                                    }
                                    Toast.makeText(ProductForm.this,"次图片已经设置为封面",Toast.LENGTH_SHORT).show();
                                    return true;
                                }
                            });

                            pictureFisrtDelete.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    pictureFisrt.setImageDrawable(null);
                                    pictureMainSrc="";
                                    File file=new File(imageUri.toString());
                                    file.delete();
                                    pictureFisrtLayout.setVisibility(View.GONE);


                                }
                            });
                        }else if(pictureSecond.getDrawable()==null)
                        {
                            pictureSecondLayout.setVisibility(View.VISIBLE);
                            Glide.with(this).load(imageUri).override(100,100).into(pictureSecond);
                            pictureSecondSrc=imageUri.toString();
                            if(customid!=null) {
                                Product product = new Product();
                                product.setPhotoSecondPath(imageUri.toString());

                            }
                            pictureSecond.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View v) {
                                    if(customid!=null) {
                                        Product product = new Product();
                                        product.setPhotoMainPath(imageUri.toString());

                                    }else  {
                                        pictureMainSrc=imageUri.toString();
                                    }
                                    Toast.makeText(ProductForm.this,"次图片已经设置为封面",Toast.LENGTH_SHORT).show();
                                    return true;
                                }
                            });
                            pictureSecondDelete.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    pictureSecond.setImageDrawable(null);
                                    pictureMainSrc="";
                                    File file=new File(imageUri.toString());
                                    file.delete();
                                    pictureSecondLayout.setVisibility(View.GONE);


                                }
                            });
                        }else if(pictureThress.getDrawable()==null)
                        {
                            pictureThressLayout.setVisibility(View.VISIBLE);
                            Glide.with(this).load(imageUri).override(100,100).into(pictureThress);
                            pictureThressSrc=imageUri.toString();
                            if(customid!=null) {
                                Product product = new Product();
                                product.setPhotoThressPath(imageUri.toString());

                            }
                            pictureThress.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View v) {
                                    if(customid!=null) {
                                        Product product = new Product();
                                        product.setPhotoMainPath(imageUri.toString());

                                    }else  {
                                        pictureMainSrc=imageUri.toString();
                                    }
                                    Toast.makeText(ProductForm.this,"次图片已经设置为封面",Toast.LENGTH_SHORT).show();
                                    return true;
                                }
                            });

                            pictureThressDelete.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    pictureThress.setImageDrawable(null);
                                    pictureMainSrc="";
                                    File file=new File(imageUri.toString());
                                    file.delete();
                                    pictureThressLayout.setVisibility(View.GONE);


                                }
                            });
                        }
                        // 将拍摄的照片显示出来

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    // 判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        // 4.4及以上系统使用这个方法处理图片
                        handleImageOnKitKat(data);
                    } else {
                        // 4.4以下系统使用这个方法处理图片
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
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
    public boolean isCustom(String number)
    {

        salesOutEntyList =DataSupport.where("number =?",number).find(SalesOutEnty.class);

        if (salesOutEntyList.size()>0)
        {
            return true;
        }else {
            return false;
        }

    }
    private View.OnClickListener itemsOnClick = new View.OnClickListener(){

        public void onClick(View v) {
            cPopupWindow.dismiss();
            switch (v.getId()) {
                case R.id.takephoto_layout:
                    // 创建File对象，用于存储拍照后的图片
                    if(pictureThress.getDrawable()==null||pictureSecond.getDrawable()==null||pictureFisrt.getDrawable()==null) {
                        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
                        Date date = new Date(System.currentTimeMillis());
                        String filename = format.format(date);
                        File outputImage = new File(getExternalCacheDir(), filename + ".jpg");
                        try {
                            if (outputImage.exists()) {
                                outputImage.delete();
                            }
                            outputImage.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (Build.VERSION.SDK_INT < 24) {
                            imageUri = Uri.fromFile(outputImage);
                            photoUri = imageUri.toString();
                            imageUirData.add(photoUri);
                            imagePathData.add("");
                        } else {
                            imageUri = FileProvider.getUriForFile(ProductForm.this, "com.example.cameraalbumtest.fileprovider", outputImage);
                            photoUri = imageUri.toString();
                            imageUirData.add(photoUri);
                            imagePathData.add("");
                        }
                        // 启动相机程序
                        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(intent, TAKE_PHOTO);
                    }else {
                        Toast.makeText(ProductForm.this, "只能上传三张", Toast.LENGTH_SHORT).show();
                    }
                case R.id.pickphoto_layout:
                    if(pictureThress.getDrawable()==null||pictureSecond.getDrawable()==null||pictureFisrt.getDrawable()==null)
                    {
                    if (ContextCompat.checkSelfPermission(ProductForm.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(ProductForm.this, new String[]{ Manifest.permission. WRITE_EXTERNAL_STORAGE }, 1);
                    } else {
                        openAlbum();
                    }}else {
                        Toast.makeText(ProductForm.this, "只能上传三张", Toast.LENGTH_SHORT).show();
                    }
                default:
                    break;
            }


        }

    };


    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        Log.d("TAG", "handleImageOnKitKat: uri is " + uri);
        if (DocumentsContract.isDocumentUri(this, uri)) {
            // 如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1]; // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        displayImage(imagePath); // 根据图片路径显示图片
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(final String imagePath) {
        if (imagePath != null) {
            if(pictureFisrt.getDrawable()==null)
            {
                pictureFisrtLayout.setVisibility(View.VISIBLE);
                Glide.with(this).load(imagePath).override(100,100).into(pictureFisrt);
                pictureFisrtSrc=imagePath;
                if(customid!=null) {
                    Product product = new Product();
                    product.setPhotoFirstPath(imagePath);
                    if(pictureMainSrc==null&&(getPostData.getPhotoMainPath()==null|| getPostData.getPhotoMainPath().isEmpty()))
                    {
                        product.setPhotoMainPath(imagePath);
                    }

                }

                pictureFisrt.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if(customid!=null) {
                            Product product = new Product();
                            product.setPhotoMainPath(imagePath);

                        }else {

                            pictureMainSrc=imagePath;
                        }
                        Toast.makeText(ProductForm.this,"次图片已经设置为封面",Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });
                pictureFisrtDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pictureFisrt.setImageDrawable(null);
                        pictureMainSrc="";
                        File file=new File(imagePath);
                        file.delete();
                        pictureFisrtLayout.setVisibility(View.GONE);


                    }
                });

            }else if(pictureSecond.getDrawable()==null)
            {
                pictureSecondLayout.setVisibility(View.VISIBLE);
                Glide.with(this).load(imagePath).override(100,100).into(pictureSecond);
                pictureSecondSrc=imagePath;
                if(customid!=null) {
                    Product product = new Product();
                    product.setPhotoSecondPath(imagePath);

                }
                pictureSecond.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if(customid!=null) {
                            Product product = new Product();
                            product.setPhotoMainPath(imagePath);

                        }else {

                            pictureMainSrc=imagePath;
                        }
                        Toast.makeText(ProductForm.this,"次图片已经设置为封面",Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });
                pictureSecondDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pictureSecond.setImageDrawable(null);
                        pictureMainSrc="";
                        File file=new File(imagePath);
                        file.delete();
                        pictureSecondLayout.setVisibility(View.GONE);


                    }
                });
            }else if(pictureThress.getDrawable()==null)
            {
                pictureThressLayout.setVisibility(View.VISIBLE);
                Glide.with(this).load(imagePath).override(100,100).into(pictureThress);
                pictureThressSrc=imagePath;
                if(customid!=null) {
                    Product product = new Product();
                    product.setPhotoThressPath(imagePath);

                }
                pictureThress.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if(customid!=null) {
                            Product product = new Product();
                            product.setPhotoMainPath(imagePath);

                        }else {

                            pictureMainSrc=imagePath;
                        }
                        Toast.makeText(ProductForm.this,"次图片已经设置为封面",Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });
                pictureThressDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pictureThress.setImageDrawable(null);
                        pictureMainSrc="";
                        File file=new File(imagePath);
                        file.delete();
                        pictureThressLayout.setVisibility(View.GONE);


                    }
                });
            }
        } else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }


    private void getHttpData( final PostProductData postPostUserData) {


        HttpUtil.sendProductRequst(postPostUserData, new okhttp3.Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        Toast.makeText(getApplicationContext(), "网络连接失败", Toast.LENGTH_SHORT).show();

                    }
                });

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {


                            Gson gson = new Gson();
                            ReturnUserData returnUserData = (ReturnUserData) gson.fromJson(response.body().string(), ReturnUserData.class);
                            Log.d("lingtana",returnUserData.getError());

                            if (returnUserData.getResult() > 0) {
                                Intent intent = new Intent();

                                if(getPostData !=null) {
                                    setResult(RESULT_OK, intent);

                                }else
                                {
                                    setResult(RESULT_OK, intent);

                                }
                                finish();
                                Toast.makeText(getApplicationContext(), "操作成功", Toast.LENGTH_SHORT).show();
                            } else {
                                closeDialog();
                                Toast.makeText(getApplicationContext(), "操作失败", Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e)
                        {
                            closeDialog();
                            Toast.makeText(getApplicationContext(), "网络连接失败", Toast.LENGTH_SHORT).show();
                        }



                    }
                });


            }
        });


    }

    /**
     * 显示进度对话框
     */
    private void showDialog() {

        dialog = new DataLoadingDialog(this);
        dialog.show();//显示

    }

    /**
     * 关闭进度对话框
     */
    private void closeDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
