package com.example.androiderp.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.androiderp.R;
import com.example.androiderp.basicdata.AppropriationListView;
import com.example.androiderp.basicdata.CustomListView;
import com.example.androiderp.basicdata.InventoryListView;
import com.example.androiderp.basicdata.ProductAppropriationListView;
import com.example.androiderp.basicdata.SupplierListView;
import com.example.androiderp.basicdata.SupplierOutListView;
import com.example.androiderp.basicdata.SupplierTowListView;
import com.example.androiderp.form.AppropriationForm;
import com.example.androiderp.form.PurchaseProductForm;
import com.example.androiderp.form.ReturnPurchaseProductForm;

/**
 * Created by lingtan on 2017/5/18.
 */

public class SecondFragment extends Fragment implements View.OnClickListener {
    private   Intent intent;
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.secondfragment_layout,container,false);
        RelativeLayout gys=(RelativeLayout)view.findViewById(R.id.secondfragment_layout_gyslayout) ;
        RelativeLayout cgth=(RelativeLayout)view.findViewById(R.id.secondfragmnet_cgthlayout) ;
        LinearLayout cgls=(LinearLayout) view.findViewById(R.id.secondfragmnet_cglslayout) ;
        RelativeLayout kcxc=(RelativeLayout)view.findViewById(R.id.secondfragment_ckxclayout) ;
        RelativeLayout db=(RelativeLayout)view.findViewById(R.id.secondfragment_dblayout) ;
        RelativeLayout dbls=(RelativeLayout)view.findViewById(R.id.secondfragment_dblslayout) ;
        RelativeLayout cg=(RelativeLayout)view.findViewById(R.id.secondfragment_layout_cglayout) ;
        gys.setOnClickListener(this);
        cgth.setOnClickListener(this);
        cgls.setOnClickListener(this);
        kcxc.setOnClickListener(this);
        db.setOnClickListener(this);
        dbls.setOnClickListener(this);
        cg.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {
        Context context = v.getContext();
        switch (v.getId())

        {   case R.id.secondfragment_layout_cglayout:
            intent = new Intent(context, PurchaseProductForm.class);
            startActivity(intent);
            break;
            case R.id.secondfragment_layout_gyslayout:
                intent = new Intent(context, SupplierTowListView.class);
                startActivity(intent);
                break;
            case R.id.secondfragmnet_cgthlayout:
                intent = new Intent(context, ReturnPurchaseProductForm.class);
                startActivity(intent);
                break;
            case R.id.secondfragmnet_cglslayout:
                intent = new Intent(context, SupplierOutListView.class);
                startActivity(intent);
                break;
            case R.id.secondfragment_ckxclayout:
                intent = new Intent(context, InventoryListView.class);
                startActivity(intent);
                break;
            case R.id.secondfragment_dblayout:
                intent = new Intent(context, AppropriationForm.class);
                startActivity(intent);
                break;
            case R.id.secondfragment_dblslayout:
                intent = new Intent(context,  AppropriationListView.class);
                startActivity(intent);
                break;
            default:
                break;

        }
    }
}
