package com.example.androiderp.adaper;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.androiderp.R;

import java.text.DecimalFormat;
import java.util.List;

//继承ArrayAdapter<DataStructure>
public class SaleProductListViewAdapter extends ArrayAdapter<CommonAdapterData>   {
//类成员变量
    private int resourceId;
    Context context;
    private int clickTemp=-1;
    List<CommonAdapterData> data; //这个数据是会改变的，所以要有个变量来备份一下原始数据


    //构造函数，context是一个抽象类，可以理解为类的类型！
    public SaleProductListViewAdapter(Context context, int textViewResourceId,
                                      List<CommonAdapterData> data) {
        
        super(context, textViewResourceId, data);
        this.context=context;
        this.data=data;
        resourceId = textViewResourceId;
    }

    //返回数据集的长度
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public CommonAdapterData getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //重写getView方法，方法视图！，position是item的位置，converView 展示在界面上的一个item，parent是converView所在的父控件！
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {// 获取当前项的Fruit实例
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) view.findViewById (R.id.custom_item_layout_one_name);
            viewHolder.quantity = (TextView) view.findViewById (R.id.custom_item_layout_one_fqty);
            viewHolder.saleAmount = (TextView) view.findViewById (R.id.custom_item_layout_one_saleamount);
            viewHolder.salesPrice = (TextView) view.findViewById (R.id.custom_item_layout_one_salesprice);
            viewHolder.image=(ImageView) view.findViewById(R.id.custom_item_layout_one_image);

            view.setTag(viewHolder); // 将ViewHolder存储在View中
        } else {

            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
            //这句非常重要，不然出现重复
            viewHolder.image.setVisibility(View.INVISIBLE);

            // 重新获取ViewHolder

        }
        RelativeLayout itemLayout=(RelativeLayout)view.findViewById(R.id.custom_item_layout);
        if(clickTemp==position)
        {
            viewHolder.image.setVisibility(View.VISIBLE);
            itemLayout.setBackgroundColor(Color.parseColor("#ffffff"));

        }else {
            itemLayout.setBackgroundColor(Color.TRANSPARENT);

            itemLayout.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        // 重新获取ViewHolder
        DecimalFormat df = new DecimalFormat("#####0.00");
        DecimalFormat quantityDf = new DecimalFormat("#####0.##");
        viewHolder.name.setText(data.get(position).getName());
        viewHolder.quantity.setText(String.valueOf(quantityDf.format(data.get(position).getFqty())));
        viewHolder.saleAmount.setText("¥"+df.format(data.get(position).getSaleamount()));
        viewHolder.salesPrice.setText("¥"+df.format(data.get(position).getSalesprice()));
        viewHolder.image.setImageResource(data.get(position).getImage());


        /*首先响应子控件的事件
        viewHolder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("lingtan","成立");
            }
        });
        */
        return view;
    }


public void setSeclection(int position)
{
    clickTemp=position;
}
    class ViewHolder {

        ImageView image;
        TextView  name;
        TextView  quantity;
        TextView  saleAmount;
        TextView  salesPrice;


    }
//屏蔽每项的单击事件
    /*
    @Override
    public boolean areAllItemsEnabled() {

        return false;
    }
    */
}
