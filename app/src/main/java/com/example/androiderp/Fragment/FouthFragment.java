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
import com.example.androiderp.basicdata.CustomTowListView;
import com.example.androiderp.basicdata.ProductAppropriationListView;
import com.example.androiderp.basicdata.SaleOutListView;
import com.example.androiderp.basicdata.StockTakingListView;
import com.example.androiderp.form.AppropriationForm;
import com.example.androiderp.form.ReturnSaleProductForm;
import com.example.androiderp.form.SaleProductForm;
import com.example.androiderp.form.StockTakingForm;

/**
 * Created by lingtan on 2017/5/18.
 */

public class FouthFragment extends Fragment implements View.OnClickListener {
    private   Intent intent;
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fourthfragment_layout,container,false);
        RelativeLayout pdzy=(RelativeLayout)view.findViewById(R.id.fourthfragment_pdzylayout) ;
        RelativeLayout pdls=(RelativeLayout)view.findViewById(R.id.fourthfragment_pdlslayout) ;
        RelativeLayout db=(RelativeLayout)view.findViewById(R.id.fourthfragment_dblayout) ;
        RelativeLayout dbls=(RelativeLayout)view.findViewById(R.id.fourthfragment_dblslayout) ;
        LinearLayout ckcx=(LinearLayout)view.findViewById(R.id.fourthfragment_kccxlayout) ;

        RelativeLayout kh=(RelativeLayout)view.findViewById(R.id.thressfragmnet_khlayout) ;
        pdzy.setOnClickListener(this);
        pdls.setOnClickListener(this);
        db.setOnClickListener(this);
        dbls.setOnClickListener(this);
        ckcx.setOnClickListener(this);
        kh.setOnClickListener(this);


        return view;

    }

    @Override
    public void onClick(View v) {
        Context context = v.getContext();
        switch (v.getId())

        {
            case R.id.fourthfragment_kccxlayout:
                intent = new Intent(context, ProductAppropriationListView.class);
                startActivity(intent);
                break;

            case R.id.fourthfragment_pdzylayout:
            intent = new Intent(context,StockTakingForm.class);
            startActivity(intent);
            break;
            case R.id.fourthfragment_pdlslayout:
                intent = new Intent(context, StockTakingListView.class);
                startActivity(intent);
                break;
            case R.id.fourthfragment_dblayout:
                intent = new Intent(context, AppropriationForm.class);
                startActivity(intent);
                break;
            case R.id.fourthfragment_dblslayout:
                intent = new Intent(context, AppropriationListView.class);
                startActivity(intent);
                break;
            case R.id.thressfragmnet_xslslayout:
                intent = new Intent(context, SaleOutListView.class);
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
