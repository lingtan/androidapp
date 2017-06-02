package com.example.androiderp.adaper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.androiderp.R;

/**
 * Created by lingtan on 2017/5/10.
 */
public class CusteomGridAdapter extends BaseAdapter {
    private Context mContext;
    private String[] itemTitles;
    private int[] itemImages;


    public CusteomGridAdapter(Context mContext, String[] itemTitles, int[] itemImages) {
        super();
        this.mContext = mContext;
        this.itemTitles=itemTitles;
        this.itemImages=itemImages;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return itemTitles.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return itemTitles[position];
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
        viewTag.mName.setText(itemTitles[position]);;
        viewTag.mIcon.setBackgroundResource(itemImages[position]);

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