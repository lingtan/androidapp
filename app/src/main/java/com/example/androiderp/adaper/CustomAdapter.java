package com.example.androiderp.adaper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.androiderp.R;

import java.util.List;
//继承ArrayAdapter<DataStructure>
public class CustomAdapter extends ArrayAdapter<DataStructure>   {
//类成员变量
    private int resourceId;
    Context context;
    List<DataStructure> data; //这个数据是会改变的，所以要有个变量来备份一下原始数据



    //构造函数，context是一个抽象类，可以理解为类的类型！
    public CustomAdapter(Context context, int textViewResourceId,
                         List<DataStructure> data) {
        
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
            viewHolder.fruitImage = (ImageView) view.findViewById (R.id.thumb_url);
            viewHolder.fruitName = (TextView) view.findViewById (R.id.fruit_name);
            viewHolder.artist = (TextView) view.findViewById (R.id.artist);
            viewHolder.duration = (TextView) view.findViewById (R.id.duration);
            view.setTag(viewHolder); // 将ViewHolder存储在View中
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag(); // 重新获取ViewHolder
        }
        Glide.with(getContext()).load(data.get(position).getThumb_url()).into(viewHolder.fruitImage);
        viewHolder.fruitName.setText(data.get(position).getName());
        viewHolder.artist.setText(data.get(position).getArtist());
        viewHolder.duration.setText(data.get(position).getDuration());
        return view;
    }

    class ViewHolder {

        ImageView fruitImage;
        TextView  artist;
        TextView  duration;
        TextView fruitName;

    }


}
