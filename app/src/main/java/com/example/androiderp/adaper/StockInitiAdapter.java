package com.example.androiderp.adaper;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.androiderp.CustomDataClass.Stock;
import com.example.androiderp.CustomDataClass.StockInitiTem;
import com.example.androiderp.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

//继承ArrayAdapter<DataStructure>
public class StockInitiAdapter extends ArrayAdapter<StockInitiTem>   {
//类成员变量
    private int resourceId;
    Context context;
    private Integer index=-1;
    private int clickTemp=-1;
    List<StockInitiTem> mdata;//这个数据是会改变的，所以要有个变量来备份一下原始数据


    //构造函数，context是一个抽象类，可以理解为类的类型！
    public StockInitiAdapter(Context context, int textViewResourceId,
                             List<StockInitiTem> data) {
        
        super(context, textViewResourceId, data);
        this.context=context;
        this.mdata=data;
        resourceId = textViewResourceId;
    }

    //返回数据集的长度
    @Override
    public int getCount() {
        return mdata.size();
    }

    @Override
    public StockInitiTem getItem(int position) {
        return mdata.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //重写getView方法，方法视图！，position是item的位置，converView 展示在界面上的一个item，parent是converView所在的父控件！
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {// 获取当前项的Fruit实例
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.badge = (TextView) convertView.findViewById (R.id.custom_badge_item_show);
            viewHolder.name = (TextView) convertView.findViewById (R.id.custom_item_layout_one_name);
            viewHolder.stockiniti = (EditText) convertView.findViewById (R.id.stockiniti_edit);
            viewHolder.stockiniti.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction()==MotionEvent.ACTION_UP)
                    {
                        index=(Integer)v.getTag();
                    }

                    return false;
                }
            });
            convertView.setTag(viewHolder); // 将ViewHolder存储在View中
        } else {

            viewHolder = (ViewHolder) convertView.getTag();

            //这句非常重要，不然出现重复
            // 重新获取ViewHolder

        }
        viewHolder.stockiniti.setTag(position);


        viewHolder.stockiniti.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                Integer tag = (Integer) viewHolder.stockiniti.getTag();
                Log.d("lingu",String.valueOf(tag));
                if(s!=null&&!"".equals(s.toString())) {


                    mdata.get(tag).setSalefqty(Double.valueOf(s.toString()));
                    Log.d("lingu",s.toString());

                }
                if(s.length()==0)
                {
                       mdata.get(tag).setSalefqty(0);
                }

            }
        });
        DecimalFormat df = new DecimalFormat("#####0.00");

        if(mdata.get(position).getSalefqty()!=0 && !"".equals(mdata.get(position).getSalefqty()))
        {
            viewHolder.stockiniti.setText(df.format(mdata.get(position).getSalefqty()));
        }else {
        }


        if(index!=-1 && index==position)
        {
            viewHolder.stockiniti.requestFocus();
            viewHolder.stockiniti.setSelection(viewHolder.stockiniti.getText().length());

        }else {
            viewHolder.stockiniti.clearFocus();
        }


        // 重新获取ViewHolder
        viewHolder.name.setText(mdata.get(position).getSalename()+":");
        /*首先响应子控件的事件
        viewHolder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("lingtan","成立");
            }
        });
        */


        return convertView;
    }


public void setSeclection(int position)
{
    clickTemp=position;
}


  static final   class ViewHolder {
        EditText stockiniti;
        TextView  name;
        TextView  badge;


    }




//屏蔽每项的单击事件
    /*
    @Override
    public boolean areAllItemsEnabled() {

        return false;
    }
    */
}
