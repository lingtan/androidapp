package com.example.androiderp.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.androiderp.CustomDataClass.GridView;
import com.example.androiderp.R;
import com.example.androiderp.adaper.CusteomGridAdapter;
import com.example.androiderp.basicdata.AppropriationListView;
import com.example.androiderp.basicdata.CustomTowListView;
import com.example.androiderp.basicdata.EmployeeListview;
import com.example.androiderp.basicdata.HomeMoreListView;
import com.example.androiderp.basicdata.InventoryListView;
import com.example.androiderp.basicdata.ProductTowListView;
import com.example.androiderp.basicdata.SaleOutListView;
import com.example.androiderp.basicdata.StockListView;
import com.example.androiderp.basicdata.PurchaseOutListView;
import com.example.androiderp.basicdata.StockTakingListView;
import com.example.androiderp.basicdata.SupplierTowListView;
import com.example.androiderp.custom.CustomGridView;
import com.example.androiderp.form.AppropriationForm;
import com.example.androiderp.form.PurchaseProductForm;
import com.example.androiderp.form.SaleProductForm;
import com.example.androiderp.form.StockTakingForm;
import com.example.androiderp.home.ErpHome;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lingtan on 2017/5/18.
 */

public class FirstFragment extends Fragment {
    private List<GridView> gridViewList=new ArrayList<GridView>();
    private List<GridView> gridViewAddList=new ArrayList<GridView>();
    private String[] img_text = {
            "商品管理", "采购新增","销售新增",
            "库存查询","采购流水", "销售流水",
            ""};

    private int[] imgs = {
            R.drawable.home_firstfragment_sp, R.drawable.hoem_firstfragmnet_cg,R.drawable.firstfragment_xs,
            R.drawable.hoem_fistfragment_search,R.drawable.home_fisrtfragment_cgcx, R.drawable.home_fisrtfragment_xscx,
            R.drawable.home_firstfragmnet_more};
    private   Intent intent;
    CustomGridView gridView;
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
        gridView=(CustomGridView)view.findViewById(R.id.customgridview_gridview);
        gridView.setAdapter(new CusteomGridAdapter(getActivity(),gridViewList));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Context context = view.getContext();

                switch (gridViewList.get(position).getName().toString()) {
                    case "商品管理":
                        intent = new Intent(context, ProductTowListView.class);
                        startActivity(intent);
                        break;
                    case "采购新增":
                        intent = new Intent(context, PurchaseProductForm.class);
                        startActivity(intent);
                        break;
                    case "销售新增":
                        intent = new Intent(context, SaleProductForm.class);
                        startActivity(intent);

                        break;
                    case "库存查询":
                        intent = new Intent(context, InventoryListView.class);
                        startActivity(intent);
                        break;
                    case "采购流水":
                        intent = new Intent(context, PurchaseOutListView.class);
                        startActivity(intent);
                        break;
                    case "销售流水":
                        intent = new Intent(context, SaleOutListView.class);
                        startActivity(intent);
                        break;
                    case "供应商管理":
                        intent = new Intent(context, SupplierTowListView.class);
                        startActivity(intent);
                        break;
                    case "客户管理":
                        intent = new Intent(context, CustomTowListView.class);
                        startActivity(intent);
                        break;
                    case "仓库管理":
                        intent = new Intent(context, StockListView.class);
                        startActivity(intent);
                        break;
                    case "库存调拨":
                        intent = new Intent(context, AppropriationForm.class);
                        startActivity(intent);
                        break;
                    case "调拨流水":
                        intent = new Intent(context, AppropriationListView.class);
                        startActivity(intent);
                        break;

                    case "盘点作业":
                        intent = new Intent(context, StockTakingForm.class);
                        startActivity(intent);
                        break;

                    case "职员管理":
                        intent = new Intent(context, EmployeeListview.class);
                        startActivity(intent);
                        break;
                    case "盘点流水":
                        intent = new Intent(context, StockTakingListView.class);
                        startActivity(intent);
                        break;
                    case "":
                        intent = new Intent(context, HomeMoreListView.class);
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
