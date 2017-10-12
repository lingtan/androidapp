package com.example.androiderp.tools;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.example.androiderp.adaper.CommonAdapterData;
import com.example.androiderp.adaper.PopuMenuAdapter;
import com.example.androiderp.bean.PopuMenuDataStructure;
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
    public int indexPositon=-1;
    public ListView listView;
    private Context context;
    //public static final String ip="http://eedd.v228.10000net.cn/webdemo/servlet/";
    public static final String ip="http://192.168.0.103:8080/webdemo/servlet/";


    public void PopupWindow(Context context, DisplayMetrics dm,List<PopuMenuDataStructure>  popuMenuDatas)
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

    public  void  JsonUpdateUi(String returnData, String indexName, String type, Context context, BasicAdapter adapter, int textViewResourceId, SlideAndDragListView<AdapterBean> listView) {


        if (type.equals("select")) {
            Gson gson = new Gson();
            HttpResponseList = gson.fromJson(returnData, new TypeToken<List<AdapterBean>>() {
            }.getType());

            if (HttpResponseList.size() != 0) {
                for(AdapterBean adapterBean: HttpResponseList)
                {
                    if (adapterBean.getName().equals(indexName)) {
                        indexPositon = HttpResponseList.indexOf(adapterBean);
                    }



                    adapterBean.setSelectImage(R.drawable.seclec_arrow);
                }


                adapter = new BasicAdapter(context, textViewResourceId, HttpResponseList);
                adapter.setSeclection(indexPositon);
                listView.setAdapter(adapter);


            } else {
                adapter = new BasicAdapter(context, textViewResourceId, HttpResponseList);
                listView.setAdapter(adapter);

                Toast.makeText(context, "没有数据", Toast.LENGTH_SHORT).show();

            }

        } else {
            Gson gson = new Gson();
            ReturnUserData returnUserData = (ReturnUserData) gson.fromJson(returnData, ReturnUserData.class);
            if (returnUserData.getResult() > 0) {


                Toast.makeText(context, "操作成功", Toast.LENGTH_SHORT).show();


            } else {

                Toast.makeText(context, "操作失败", Toast.LENGTH_SHORT).show();

            }


        }

    }


}
