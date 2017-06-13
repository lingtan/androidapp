package com.example.androiderp.adaper;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.androiderp.CustomDataClass.Product;
import com.example.androiderp.R;
import com.example.androiderp.custom.CustomBadgeView;

import java.util.HashSet;
import java.util.List;

//继承ArrayAdapter<DataStructure>
public class ProductBadgeAdapter extends ArrayAdapter<Product>   {
//类成员变量
    private int resourceId;
    private Context context;
    private int clickTemp=-1;
    private int ai;
    public HashSet<Long> selectedItems = new HashSet<Long>();
    private List<Product> data;


    //这个数据是会改变的，所以要有个变量来备份一下原始数据


    //构造函数，context是一个抽象类，可以理解为类的类型！
    public ProductBadgeAdapter(Context context, int textViewResourceId,
                               List<Product> data) {
        
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
    public Product getItem(int position) {
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
            viewHolder.image=(ImageView) view.findViewById(R.id.custom_item_layout_one_image);
            viewHolder.number = (TextView) view.findViewById (R.id.custom_item_layout_number);
            viewHolder.model = (TextView) view.findViewById (R.id.custom_item_layout_model);
            viewHolder.salesprice = (TextView) view.findViewById (R.id.custom_item_layout_salesprice);

            view.setTag(viewHolder); // 将ViewHolder存储在View中
        } else {

            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
            //这句非常重要，不然出现重复
            viewHolder.image.setVisibility(View.INVISIBLE);

            // 重新获取ViewHolder

        }

        RelativeLayout itemLayout=(RelativeLayout)view.findViewById(R.id.badge_item_layout);

         if(selectedItems.contains((long)position)){

            viewHolder.image.setVisibility(View.VISIBLE);
            itemLayout.setBackgroundColor(Color.parseColor("#ffffff"));

        }else {
            itemLayout.setBackgroundColor(Color.TRANSPARENT);
             viewHolder.image.setVisibility(View.INVISIBLE);

            //itemLayout.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        // 重新获取ViewHolder
        viewHolder.name.setText(data.get(position).getName().toString());
        viewHolder.number.setText(data.get(position).getNumber());
        viewHolder.model.setText(data.get(position).getModel());
        viewHolder.salesprice.setText("¥"+data.get(position).getSalesprice().toString());
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


public void setSeclection(int position,int i)
{
    clickTemp=position;
    ai=i;
}
    class ViewHolder {

        ImageView image;
        TextView  name;
        TextView  number;
        TextView  model;
        TextView  salesprice;



    }
//屏蔽每项的单击事件
    /*
    @Override
    public boolean areAllItemsEnabled() {

        return false;
    }
    */
}
