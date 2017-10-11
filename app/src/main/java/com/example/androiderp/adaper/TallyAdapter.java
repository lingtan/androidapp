package com.example.androiderp.adaper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.androiderp.bean.Tally;
import com.example.androiderp.R;

import java.text.DecimalFormat;
import java.util.List;

//继承ArrayAdapter<DataStructure>
public class TallyAdapter extends ArrayAdapter<Tally>   {
//类成员变量
    private int resourceId;
    Context context;
    private int clickTemp=-1;
    List<Tally> data; //这个数据是会改变的，所以要有个变量来备份一下原始数据


    //构造函数，context是一个抽象类，可以理解为类的类型！
    public TallyAdapter(Context context, int textViewResourceId,
                        List<Tally> data) {
        
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
    public Tally getItem(int position) {
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
            viewHolder.number =(TextView) view.findViewById(R.id.tally_number);
            viewHolder.amount = (TextView) view.findViewById (R.id.tally_amount);
            viewHolder.Fdate = (TextView) view.findViewById (R.id.tally_fdate);
            viewHolder.note = (TextView) view.findViewById (R.id.tally_note);
            viewHolder.accounts = (TextView) view.findViewById (R.id.tally_accounts);
            viewHolder.image=(ImageView) view.findViewById(R.id.appropriation_arrow);

            view.setTag(viewHolder); // 将ViewHolder存储在View中
        } else {

            view = convertView;
            viewHolder = (ViewHolder) view.getTag();

        }
        DecimalFormat df = new DecimalFormat("#####0.00");
        // 重新获取ViewHolder
        viewHolder.number.setText(data.get(position).getNumber());
        if(data.get(position).getDealings()!=null)
        {
            viewHolder.accounts.setText(data.get(position).getDealings());
        }else {
            viewHolder.accounts.setText(data.get(position).getAccounts());
        }

        viewHolder.amount.setText("￥"+df.format(data.get(position).getAmount()));
        viewHolder.Fdate.setText(data.get(position).getDate().toString());
        viewHolder.note.setText(data.get(position).getNote());



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
        TextView  Fdate;
        TextView  accounts;
        TextView number;
        TextView amount;
        TextView note;
        ImageView image;



    }
//屏蔽每项的单击事件
    /*
    @Override
    public boolean areAllItemsEnabled() {

        return false;
    }
    */
}
