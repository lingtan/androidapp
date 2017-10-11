package com.example.androiderp.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.androiderp.R;

/**
 * Created by lingtan on 2017/5/18.
 */

public class FiveFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fourthfragment_layout,container,false);

        return view;
    }



}
