package com.example.androiderp.adaper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.androiderp.CustomDataClass.GridView;
import com.example.androiderp.R;

import java.util.List;

/**
 * Created by lingtan on 2017/5/10.
 */
public class CusteomGridAdapter extends BaseAdapter {
    private Context mContext;
    private List<GridView> data;


    public CusteomGridAdapter(Context mContext, List<GridView> data) {
        super();
        this.mContext = mContext;
        this.data=data;

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemViewTag viewTag;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.customgridview_item, parent, false);
            viewTag = new ItemViewTag((ImageView) convertView.findViewById(R.id.customgridview_item_layout_one_image), (TextView) convertView.findViewById(R.id.customgridview_item_layout_one_item));
            convertView.setTag(viewTag);
        }else
        {
            viewTag = (ItemViewTag) convertView.getTag();
        }
        viewTag.mName.setText(data.get(position).getName());;
        viewTag.mIcon.setBackgroundResource(data.get(position).getImage());

        return convertView;
    }

    class ItemViewTag
    {
        protected ImageView mIcon;
        protected TextView mName;

        public ItemViewTag(ImageView icon, TextView name)
        {
            this.mName = name;
            this.mIcon = icon;
        }
    }

}