package com.example.androiderp.tools;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androiderp.adaper.BasicAdapter;
import com.example.androiderp.bean.AdapterBean;
import com.example.androiderp.bean.ReturnUserData;
import com.example.androiderp.R;
import com.example.androiderp.adaper.PopuMenuAdapter;
import com.example.androiderp.bean.PopBean;
import com.example.androiderp.listview.SlideAndDragListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lingtan on 2017/5/14.
 */

public class Common extends AppCompatActivity {
    public   PopupWindow mPopWindow;
    private PopuMenuAdapter menuAdapter;
    public List<AdapterBean> HttpResponseList = new ArrayList<AdapterBean>();
    private List<PopBean> cPopBeanList = new ArrayList<PopBean>();
    public int indexPositon=-1;
    public ListView listView;
    public String popwinVaule;
    private Context context;
    public int returnResult;
    //public static final String ip="http://eedd.v228.10000net.cn/javaweb/servlet/";
    public static final String ip="wwww";
    public void PopupWindow(Context context, DisplayMetrics dm,List<PopBean>  popuMenuDatas)
    {   this.context=context;
        if(mPopWindow==null) {
            View contentView = LayoutInflater.from(context).inflate(R.layout.popu_main, null);
            mPopWindow = new PopupWindow(contentView);
            listView = (ListView) contentView.findViewById(R.id.popu_main_list);
            LinearLayout.LayoutParams lp;
            lp= (LinearLayout.LayoutParams) listView.getLayoutParams();
            lp.width = dm.widthPixels /3;  //两边有点点空隙
            //设置整个ListView的给定宽高
            listView.setLayoutParams(lp);
            menuAdapter = new PopuMenuAdapter(context, R.layout.popu_item, popuMenuDatas);
            listView.setAdapter(menuAdapter);
//popupwindow点击按钮时弹出来，再点击消失，但设置了点击外围消失，但是实际出来额效果，poupwindow会消失然后再次弹出。
            mPopWindow.setFocusable(true);//必须写
            contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            mPopWindow.setTouchInterceptor(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v,MotionEvent event) {
                    if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
                        mPopWindow.dismiss();
                        return true;
                    }
                    return false;
                }
            });
            mPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {

                }
            });
        }
        mPopWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopWindow.setAnimationStyle(R.style.AnimList);//动画
        mPopWindow.setTouchable(true);
        mPopWindow.setOutsideTouchable(true);
        mPopWindow.setBackgroundDrawable(new BitmapDrawable(context.getResources(), (Bitmap) null));

    }
    /**
     * 得到自定义的progressDialog
     * @param context
     * @param msg     * @return
     */
    public static Dialog createLoadingDialog(Context context, String msg) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.loading_dialog_layout);// 加载布局
        // main.xml中的ImageView
        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.loading_dialog_img);
        TextView tipTextView = (TextView) v.findViewById(R.id.loading_dialog_tip);// 提示文字
        // 加载动画
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                context, R.anim.load_animation);
        // 使用ImageView显示动画
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);
        tipTextView.setText(msg);// 设置加载信息

        Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog

        loadingDialog.setCancelable(true);// 不可以用“返回键”取消
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
        return loadingDialog;

    }

    public static Dialog dataLoadingDialog(Context context) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.data_loading, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.loading_dialog_layout);// 加载布局

        Dialog loadingDialog = new Dialog(context, R.style.loadingdialog);// 创建自定义样式dialog

        loadingDialog.setCancelable(true);// 不可以用“返回键”取消
        return loadingDialog;

    }

    public  void  JsonUpdateUi(String returnData, String indexName,List<AdapterBean> post, int type, Context context, BasicAdapter adapter, int textViewResourceId, SlideAndDragListView<AdapterBean> listView)
    { Gson gson = new Gson();
        Log.d("lingtanaa",returnData);
        switch (type) {

            case 1:

                indexPositon=-1;
                post.clear();
                HttpResponseList = gson.fromJson(returnData, new TypeToken<List<AdapterBean>>() {
                }.getType());
                post.addAll(HttpResponseList);
                if (HttpResponseList.size() != 0) {
                    for(AdapterBean adapterBean: post)
                    {
                        if (adapterBean.getName().equals(indexName)) {
                            indexPositon = post.indexOf(adapterBean);
                        }



                        adapterBean.setSelectImage(R.drawable.seclec_arrow);
                    }



                    adapter.setSeclection(indexPositon);
                    adapter.notifyDataSetChanged();


                } else {
                    adapter.setSeclection(indexPositon);
                    adapter.notifyDataSetChanged();

                    Toast.makeText(context, "没有数据", Toast.LENGTH_SHORT).show();

                }

                break;
            case 2:

                ReturnUserData returnUserData = gson.fromJson(returnData, ReturnUserData.class);
                returnResult=Integer.valueOf(returnUserData.getResult());
                if (Integer.valueOf(returnUserData.getResult()) > 0) {

                    adapter.setSeclection(indexPositon);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(context, "操作成功", Toast.LENGTH_SHORT).show();


                }  else  if(Integer.valueOf(returnUserData.getResult())==-2)
                {
                    adapter.setSeclection(indexPositon);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(context, "已有业务发生不能删除", Toast.LENGTH_SHORT).show();

                }else{
                    adapter.setSeclection(indexPositon);
                    adapter.notifyDataSetChanged();

                    Toast.makeText(context, "操作失败", Toast.LENGTH_SHORT).show();

                }

                break;



        }


    }

    public  void  JsonUpdatemPopWindow(String returnData, final TextView popwin, DisplayMetrics dm, Context context)
    {
        Gson gson = new Gson();
        HttpResponseList = gson.fromJson(returnData, new TypeToken<List<AdapterBean>>() {
        }.getType());
        cPopBeanList.clear();
        if (HttpResponseList.size() >0) {



            for (AdapterBean adapterBean : HttpResponseList)

            {
                PopBean popuMenua = new PopBean(R.drawable.poppu_wrie, adapterBean.getName());
                cPopBeanList.add(popuMenua);

            }
            PopupWindow(context, dm, cPopBeanList);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> adapterView, View view,
                                        int position, long id) {

                    popwin.setText(cPopBeanList.get(position).getName());
                    mPopWindow.dismiss();
                }
            });

            if (mPopWindow == null || !mPopWindow.isShowing()) {
                int xPos = dm.widthPixels / 3;
                mPopWindow.showAsDropDown(popwin, xPos, 5);
                //mPopWindow.showAtLocation(findViewById(R.id.main), Gravity.BOTTOM, 0, 0);//从底部弹出
            } else {
                mPopWindow.dismiss();
            }




        }else {
            Toast.makeText(context, "没有数据", Toast.LENGTH_SHORT).show();
        }

    }


}
