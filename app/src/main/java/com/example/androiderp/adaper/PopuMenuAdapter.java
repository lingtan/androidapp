package com.example.androiderp.adaper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.androiderp.R;
import com.example.androiderp.bean.PopBean;

import java.util.List;

//继承ArrayAdapter<DataStructure>
public class PopuMenuAdapter extends ArrayAdapter<PopBean>   {
//类成员变量
    private int resourceId;
    Context context;
    List<PopBean> data; //这个数据是会改变的，所以要有个变量来备份一下原始数据


    //构造函数，context是一个抽象类，可以理解为类的类型！
    public PopuMenuAdapter(Context context, int textViewResourceId,
                           List<PopBean> data) {
        
        super(context, textViewResourceId, data);
        this.context=context;
        this.data=data;
        resourceId = textViewResourceId;
    }



    //重写getView方法，方法视图！，position是item的位置，converView 展示在界面上的一个item，parent是converView所在的父控件！
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {// 获取当前项的Fruit实例
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.image = (ImageView) view.findViewById (R.id.popu_iten_layout_one_image);
            viewHolder.name = (TextView) view.findViewById (R.id.popu_iten_layout_name);

            view.setTag(viewHolder); // 将ViewHolder存储在View中
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag(); // 重新获取ViewHolder
        }

        viewHolder.name.setText(data.get(position).getName());
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

    class ViewHolder {

        ImageView image;
        TextView  name;


    }
//屏蔽每项的单击事件
    /*
    @Override
    public boolean areAllItemsEnabled() {

        return false;
    }
    */
}
