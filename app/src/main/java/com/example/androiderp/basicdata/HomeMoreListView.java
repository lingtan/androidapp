package com.example.androiderp.basicdata;

import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.androiderp.CustomDataClass.GridView;
import com.example.androiderp.R;
import com.example.androiderp.adaper.HomeMoreAdapter;
import com.example.androiderp.custom.CustomSearchBase;
import com.example.androiderp.home.ErpHome;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class HomeMoreListView extends CustomSearchBase implements View.OnClickListener {
    private HomeMoreAdapter adapter;
    private ListView listView;
    private DisplayMetrics dm;
    private List<GridView> gridViewList=new ArrayList<GridView>();
    private List<GridView> gridViewaddList=new ArrayList<GridView>();
    private TextView toobarBack, toobarAdd, toobarTile;
    @Override
    public void iniView(){
        setContentView(R.layout.home_morelayout);
        toobarBack =(TextView)findViewById(R.id.custom_toobar_left) ;
        toobarTile =(TextView)findViewById(R.id.custom_toobar_midd);
        toobarAdd =(TextView)findViewById(R.id.custom_toobar_right);
        toobarBack.setOnClickListener(this);
        toobarAdd.setOnClickListener(this);
        toobarTile.setText("快捷菜单");
        GridView gridView1=new GridView();
        gridView1.setName("盘点流水");
        gridView1.setChoiceImage(R.drawable.nochoice);
        gridView1.setImage(R.drawable.fourthfragmnet_pdls);
        gridView1.setChoice(false);
        gridViewList.add(gridView1);
        GridView gridView2=new GridView();
        gridView2.setName("仓库预警");
        gridView2.setChoiceImage(R.drawable.nochoice);
        gridView2.setImage(R.drawable.fourthfragment_kcyj);
        gridView2.setChoice(false);
        gridViewList.add(gridView2);
        gridViewaddList=DataSupport.findAll(GridView.class);
        for(GridView gridView:gridViewList)
        {
            for(GridView gridView3:gridViewaddList)
            {
                if(gridView.getName().equals(gridView3.getName()))
                {   gridView.setChoiceImage(R.drawable.yeschoice);
                    gridView.setChoice(true);
                }
            }
        }
        //构造函数第一参数是类的对象，第二个是布局文件，第三个是数据源
        listView = (ListView) findViewById(R.id.list);
        listView.setTextFilterEnabled(true);
        dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {

             if (gridViewList.get(position).isChoice())
             {
                 gridViewList.get(position).setChoiceImage(R.drawable.nochoice);
                 gridViewList.get(position).setChoice(false);
             }else {
                 gridViewList.get(position).setChoiceImage(R.drawable.yeschoice);
                 gridViewList.get(position).setChoice(true);
             }
                adapter = new HomeMoreAdapter(HomeMoreListView.this, R.layout.home_moreitem, gridViewList);
                listView.setAdapter(adapter);
            }
        });


                 adapter = new HomeMoreAdapter(HomeMoreListView.this, R.layout.home_moreitem, gridViewList);
                 listView.setAdapter(adapter);
            





    }


    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.custom_toobar_left:
                DataSupport.deleteAll(GridView.class);
                for(GridView gridView:gridViewList)
                {
                    if(gridView.isChoice())
                    {
                        gridView.save();
                    }
                }
                Intent intent =new Intent(HomeMoreListView.this, ErpHome.class);
                intent.putExtra("fragment","fisrt");
                startActivity(intent);
                HomeMoreListView.this.finish();
                break;

            case R.id.custom_toobar_midd:

                break;

            case R.id.custom_toobar_right:

                break;


        }

    }


}
