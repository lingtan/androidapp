package com.example.androiderp.activities.homeview;

import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.androiderp.bean.GridView;
import com.example.androiderp.R;
import com.example.androiderp.adaper.HomeMoreAdapter;
import com.example.androiderp.ui.CSearchBase;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class HomeMoreView extends CSearchBase implements View.OnClickListener {
    private HomeMoreAdapter adapter;
    private ListView listView;
    private DisplayMetrics dm;
    private List<GridView> gridViewList=new ArrayList<GridView>();
    private List<GridView> gridViewaddList=new ArrayList<GridView>();
    private TextView back, add, tile;
    private String[] home_name = {
            "供应商管理","客户管理","仓库管理",
            "库存调拨","调拨流水", "盘点作业","盘点流水","仓库预警",
            "职员管理","账户管理","经营分析",};
    private int[] home_image = {
            R.drawable.home_fisrtfragment_gy, R.drawable.home_firstfragment_kh,R.drawable.home_fisrtfragment_kc,
            R.drawable.home_fistfragment_db, R.drawable.home_dbls,R.drawable.home_fisrfragment_pd,R.drawable.fourthfragmnet_pdls,R.drawable.fourthfragment_kcyj,
            R.drawable.home_firstfragment_zy,R.drawable.home_fisrtfragment_zh,R.drawable.hoem_fisrtfragment_jy,
           };
    @Override
    public void iniView(){
        setContentView(R.layout.home_morelayout);
        back =(TextView)findViewById(R.id.custom_toobar_left) ;
        tile =(TextView)findViewById(R.id.custom_toobar_midd);
        add =(TextView)findViewById(R.id.custom_toobar_right);
        Drawable more= getResources().getDrawable(R.drawable.toobar_more);
        more.setBounds(0, 0, more.getMinimumWidth(), more.getMinimumHeight());
        add.setCompoundDrawables(more,null,null,null);
        back.setOnClickListener(this);
        add.setOnClickListener(this);

        tile.setText("快捷菜单");

        for(int i=0;i<11;i++)
        {
            GridView gridView=new GridView();
            gridView.setName(home_name[i]);
            gridView.setChoiceImage(R.drawable.nochoice);
            gridView.setImage(home_image[i]);
            gridView.setChoice(false);
            gridViewList.add(gridView);
        }

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
               adapter.notifyDataSetChanged();
            }
        });


                 adapter = new HomeMoreAdapter(HomeMoreView.this, R.layout.home_moreitem, gridViewList);
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
                HomeMoreView.this.finish();
                break;

            case R.id.custom_toobar_midd:

                break;

            case R.id.custom_toobar_right:

                break;


        }

    }


}
