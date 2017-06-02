package com.example.androiderp.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.androiderp.R;
import com.example.androiderp.basicdata.CustomList;
import com.example.androiderp.basicdata.SupplierList;

/**
 * Created by lingtan on 2017/5/18.
 */

public class SecondFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.secondfragment_layout,container,false);
        RelativeLayout layoutInflater=(RelativeLayout) view.findViewById(R.id.secondfragment_layout_gyslayout);
        RelativeLayout gys=(RelativeLayout)view.findViewById(R.id.secondfragment_layout_gyslayout) ;
        layoutInflater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, CustomList.class);
                startActivity(intent);
            }
        });
        gys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, SupplierList.class);
                startActivity(intent);
            }
        });
        return view;
    }



}
