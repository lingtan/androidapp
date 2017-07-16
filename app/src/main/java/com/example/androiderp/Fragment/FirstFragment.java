package com.example.androiderp.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.androiderp.CustomDataClass.Employee;
import com.example.androiderp.R;
import com.example.androiderp.adaper.CusteomGridAdapter;
import com.example.androiderp.basicdata.AppropriationListView;
import com.example.androiderp.basicdata.BrandBadgeListView;
import com.example.androiderp.basicdata.CustomCategoryListview;
import com.example.androiderp.basicdata.CustomListView;
import com.example.androiderp.basicdata.CustomTowListView;
import com.example.androiderp.basicdata.EmployeeListview;
import com.example.androiderp.basicdata.InventoryListView;
import com.example.androiderp.basicdata.ProductAppropriationListView;
import com.example.androiderp.basicdata.ProductBadgeListView;
import com.example.androiderp.basicdata.ProductTowListView;
import com.example.androiderp.basicdata.SaleOutListView;
import com.example.androiderp.basicdata.StockListView;
import com.example.androiderp.basicdata.SupplierOutListView;
import com.example.androiderp.basicdata.SupplierTowListView;
import com.example.androiderp.basicdata.UnitListView;
import com.example.androiderp.custom.CustomGridView;
import com.example.androiderp.form.AppropriationForm;
import com.example.androiderp.form.PurchaseProductForm;
import com.example.androiderp.form.SaleProductForm;
import com.example.androiderp.form.StockTakingForm;

/**
 * Created by lingtan on 2017/5/18.
 */

public class FirstFragment extends Fragment {
    private String[] img_text = { "商品管理", "采购新增", "销售新增", "库存查询", "客户管理", "盘点作业",
            "经营分析", "仓库管理", "库存调拨","供应商管理","销售流水","采购流水","职员管理","账户管理",""};
    private int[] imgs = { R.drawable.home_firstfragment_sp, R.drawable.hoem_firstfragmnet_cg,
            R.drawable.firstfragment_xs, R.drawable.hoem_fistfragment_search,
            R.drawable.home_firstfragment_kh, R.drawable.home_fisrfragment_pd,
            R.drawable.hoem_fisrtfragment_jy, R.drawable.home_fisrtfragment_kc, R.drawable.home_fistfragment_db ,
            R.drawable.home_fisrtfragment_gy,R.drawable.home_fisrtfragment_xscx,R.drawable.home_fisrtfragment_cgcx,
            R.drawable.home_firstfragment_zy,R.drawable.home_fisrtfragment_zh,R.drawable.home_firstfragmnet_more};
    private   Intent intent;
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {

     View view=inflater.inflate(R.layout.customgridview_layout,container,false);
        CustomGridView gridView=(CustomGridView)view.findViewById(R.id.customgridview_gridview);
        gridView.setAdapter(new CusteomGridAdapter(getActivity(),img_text,imgs));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Context context = view.getContext();
                switch (position) {
                    case 0:
                        intent = new Intent(context, ProductTowListView.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(context, PurchaseProductForm.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(context, SaleProductForm.class);
                        startActivity(intent);

                        break;
                    case 3:
                        intent = new Intent(context, InventoryListView.class);
                        startActivity(intent);

                        break;
                    case 4:
                        intent = new Intent(context, CustomTowListView.class);
                        startActivity(intent);
                        break;
                    case 5:
                        intent = new Intent(context, StockTakingForm.class);
                        startActivity(intent);
                        break;
                    case 6:
                        intent = new Intent(context, CustomListView.class);
                        startActivity(intent);
                        break;
                    case 7:
                        intent = new Intent(context, StockListView.class);
                        startActivity(intent);
                        break;
                    case 8:
                        intent = new Intent(context, AppropriationForm.class);
                        startActivity(intent);
                        break;
                    case 9:
                        intent = new Intent(context, SupplierTowListView.class);
                        startActivity(intent);
                        break;

                    case 10:
                        intent = new Intent(context, SaleOutListView.class);
                        startActivity(intent);
                        break;
                    case 11:
                        intent = new Intent(context, SupplierOutListView.class);
                        startActivity(intent);
                        break;
                    case 12:
                        intent = new Intent(context, EmployeeListview.class);
                        startActivity(intent);
                        break;


                    default:
                        break;
                }
            }
        });
        return view;
    }



}
