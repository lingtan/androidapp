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
import com.example.androiderp.basicdata.CustomTowListView;
import com.example.androiderp.basicdata.InventoryListView;
import com.example.androiderp.basicdata.SaleOutListView;
import com.example.androiderp.form.AppropriationForm;
import com.example.androiderp.form.ReturnSaleProductForm;
import com.example.androiderp.form.SaleProductForm;

/**
 * Created by lingtan on 2017/5/18.
 */

public class ThressFragment extends Fragment implements View.OnClickListener {
    private   Intent intent;
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.thressfragment_layout,container,false);
        RelativeLayout xsck=(RelativeLayout)view.findViewById(R.id.thressfragmnet_xscklayout) ;
        RelativeLayout xsth=(RelativeLayout)view.findViewById(R.id.thressfragmnet_xsthlayout) ;
        RelativeLayout db=(RelativeLayout)view.findViewById(R.id.thressfragmnet_dblayout) ;
        RelativeLayout dbls=(RelativeLayout)view.findViewById(R.id.thressfragmnet_dblslayout) ;
        LinearLayout xsls=(LinearLayout)view.findViewById(R.id.thressfragmnet_xslslayout) ;
        RelativeLayout ckcx=(RelativeLayout)view.findViewById(R.id.thressfragmnet_kccxlayout) ;
        RelativeLayout kh=(RelativeLayout)view.findViewById(R.id.thressfragmnet_khlayout) ;
        xsck.setOnClickListener(this);
        xsth.setOnClickListener(this);
        db.setOnClickListener(this);
        dbls.setOnClickListener(this);
        xsls.setOnClickListener(this);
        ckcx.setOnClickListener(this);
        kh.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        Context context = v.getContext();
        switch (v.getId())

        {   case R.id.thressfragmnet_xscklayout:
            intent = new Intent(context,SaleProductForm.class);
            startActivity(intent);
            break;
            case R.id.thressfragmnet_xsthlayout:
                intent = new Intent(context, ReturnSaleProductForm.class);
                startActivity(intent);
                break;
            case R.id.thressfragmnet_dblayout:
                intent = new Intent(context, AppropriationForm.class);
                startActivity(intent);
                break;
            case R.id.thressfragmnet_dblslayout:
                intent = new Intent(context, AppropriationListView.class);
                startActivity(intent);
                break;
            case R.id.thressfragmnet_xslslayout:
                intent = new Intent(context, SaleOutListView.class);
                startActivity(intent);
                break;
            case R.id.thressfragmnet_kccxlayout:
                intent = new Intent(context, InventoryListView.class);
                startActivity(intent);
                break;
            case R.id.thressfragmnet_khlayout:
                intent = new Intent(context,   CustomTowListView.class);
                startActivity(intent);
                break;
            default:
                break;

        }
    }


}
