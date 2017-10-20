package com.example.androiderp.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.androiderp.R;
import com.example.androiderp.adaper.CusteomGridAdapter;
import com.example.androiderp.activities.warehouseview.AppropriationView;
import com.example.androiderp.activities.basicview.CustomMoreView;
import com.example.androiderp.activities.homeview.HomeMoreView;
import com.example.androiderp.activities.warehouseview.InventoryView;
import com.example.androiderp.activities.basicview.ProductMoreView;
import com.example.androiderp.activities.salesview.SaleView;
import com.example.androiderp.activities.purchaseview.PurchaseView;
import com.example.androiderp.activities.warehouseview.StockTakingView;
import com.example.androiderp.activities.basicview.SupplierMoreView;
import com.example.androiderp.activities.accountsview.TallyExpandableView;
import com.example.androiderp.activities.warehouseform.AppropriationForm;
import com.example.androiderp.activities.purchaseform.PurchaseForm;
import com.example.androiderp.activities.salesfrom.SaleForm;
import com.example.androiderp.activities.warehouseform.StockTakingForm;
import com.example.androiderp.basic.BasicView;
import com.example.androiderp.bean.AcivityPostBen;
import com.example.androiderp.bean.GridView;
import com.example.androiderp.tools.GlobalVariable;
import com.example.androiderp.ui.CGridView;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lingtan on 2017/5/18.
 */

public class FirstFragment extends Fragment {
    private List<GridView> gridViewList=new ArrayList<GridView>();
    private List<GridView> gridViewAddList=new ArrayList<GridView>();
    private AcivityPostBen acivityPostBen=new AcivityPostBen();
    private String[] img_text = {
            "商品管理", "采购新增","销售新增",
            "库存查询","采购流水", "销售流水",
            ""};

    private int[] imgs = {
            R.drawable.home_firstfragment_sp, R.drawable.hoem_firstfragmnet_cg,R.drawable.firstfragment_xs,
            R.drawable.hoem_fistfragment_search,R.drawable.home_fisrtfragment_cgcx, R.drawable.home_fisrtfragment_xscx,
            R.drawable.home_firstfragmnet_more};
    private   Intent intent;
    CGridView gridView;
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        gridViewAddList = DataSupport.findAll(GridView.class);

        for(int i=0;i<6;i++)
        {
            GridView gridView=new GridView();
            gridView.setName(img_text[i]);
            gridView.setImage(imgs[i]);
            gridViewList.add(gridView);
        }

            gridViewAddList = DataSupport.findAll(GridView.class);
            for (GridView gridView : gridViewAddList) {

                gridViewList.add(gridView);
            }

        GridView gridView2=new GridView();
        gridView2.setName(img_text[6]);
        gridView2.setImage(imgs[6]);
        gridViewList.add(gridView2);
     View view=inflater.inflate(R.layout.customgridview_layout,container,false);
        gridView=(CGridView)view.findViewById(R.id.customgridview_gridview);
        gridView.setAdapter(new CusteomGridAdapter(getActivity(),gridViewList));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Context context = view.getContext();

                switch (gridViewList.get(position).getName().toString()) {
                    case "商品管理":

                        acivityPostBen.setAcivityName("产品");
                        acivityPostBen.setRequestServlet("ProductOperate");
                        acivityPostBen.setName("");
                        acivityPostBen.setSetClassType(GlobalVariable.customMoreViewType);
                        Intent intentProduct = new Intent(context, ProductMoreView.class);
                        intentProduct.putExtra("acivityPostBen",acivityPostBen);
                        startActivity(intentProduct);
                        break;
                    case "采购新增":
                        intent = new Intent(context, PurchaseForm.class);
                        startActivity(intent);
                        break;
                    case "销售新增":
                        intent = new Intent(context, SaleForm.class);
                        startActivity(intent);

                        break;
                    case "库存查询":
                        intent = new Intent(context, InventoryView.class);
                        startActivity(intent);
                        break;
                    case "采购流水":
                        intent = new Intent(context, PurchaseView.class);
                        startActivity(intent);
                        break;
                    case "销售流水":
                        intent = new Intent(context, SaleView.class);
                        startActivity(intent);
                        break;
                    case "供应商管理":
                        acivityPostBen.setAcivityName("供应商");
                        acivityPostBen.setRequestServlet("ContactOperate");
                        acivityPostBen.setName("");
                        acivityPostBen.setSetClassType(GlobalVariable.supplierMoreViewType);
                        Intent intentSupplier = new Intent(context, CustomMoreView.class);
                        intentSupplier.putExtra("acivityPostBen",acivityPostBen);
                        startActivity(intentSupplier);
                        break;
                    case "客户管理":
                        acivityPostBen.setAcivityName("客户");
                        acivityPostBen.setRequestServlet("ContactOperate");
                        acivityPostBen.setName("");
                        acivityPostBen.setSetClassType(GlobalVariable.customMoreViewType);
                        Intent intentCustom = new Intent(context, CustomMoreView.class);
                        intentCustom.putExtra("acivityPostBen",acivityPostBen);
                        startActivity(intentCustom);
                        break;
                    case "仓库管理":
                        acivityPostBen.setAcivityName("仓库");
                        acivityPostBen.setRequestServlet("BrandOperate");
                        acivityPostBen.setName("");
                        acivityPostBen.setSetClassType(4);
                        Intent intentStock = new Intent(context, BasicView.class);
                        intentStock.putExtra("acivityPostBen",acivityPostBen);
                        startActivity(intentStock);
                        break;
                    case "库存调拨":
                        intent = new Intent(context, AppropriationForm.class);
                        startActivity(intent);
                        break;
                    case "调拨流水":
                        intent = new Intent(context, AppropriationView.class);
                        startActivity(intent);
                        break;

                    case "盘点作业":
                        intent = new Intent(context, StockTakingForm.class);
                        startActivity(intent);
                        break;

                    case "职员管理":
                        acivityPostBen.setAcivityName("职员");
                        acivityPostBen.setRequestServlet("BrandOperate");
                        acivityPostBen.setName("");
                        acivityPostBen.setSetClassType(5);
                        Intent intentEmp = new Intent(context, BasicView.class);
                        intentEmp.putExtra("acivityPostBen",acivityPostBen);
                        startActivity(intentEmp);
                        break;
                    case "盘点流水":
                        intent = new Intent(context, StockTakingView.class);
                        startActivity(intent);
                        break;
                    case "账户管理":
                        intent = new Intent(context, TallyExpandableView.class);
                        startActivity(intent);
                        break;
                    case "仓库预警":
                        intent = new Intent(context, StockTakingView.class);
                        startActivity(intent);
                        break;
                    case "":
                        intent = new Intent(context, HomeMoreView.class);
                        startActivityForResult(intent,1);
                        break;



                    default:
                        break;
                }
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case 1:
                gridViewList.clear();
                for(int i=0;i<6;i++)
                {
                    GridView gridView=new GridView();
                    gridView.setName(img_text[i]);
                    gridView.setImage(imgs[i]);
                    gridViewList.add(gridView);
                }
                gridViewAddList = DataSupport.findAll(GridView.class);
                for (GridView gridView : gridViewAddList) {

                    gridViewList.add(gridView);
                }

                GridView gridView2=new GridView();
                gridView2.setName(img_text[6]);
                gridView2.setImage(imgs[6]);
                gridViewList.add(gridView2);
                gridView.setAdapter(new CusteomGridAdapter(getActivity(),gridViewList));

        }
    }
}
