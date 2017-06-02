package com.example.androiderp.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.androiderp.R;
import com.example.androiderp.basicdata.CustomListView;

/**
 * Created by lingtan on 2017/5/18.
 */

public class FiveFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fourthfragment_layout,container,false);
        LinearLayout layoutInflater=(LinearLayout) view.findViewById(R.id.fourthfragmnet_custom);
        layoutInflater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, CustomListView.class);
                startActivity(intent);
            }
        });
        return view;
    }



}
