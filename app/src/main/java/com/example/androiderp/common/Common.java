package com.example.androiderp.common;

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

import com.example.androiderp.CustomDataClass.Product;
import com.example.androiderp.R;
import com.example.androiderp.adaper.PopuMenuAdapter;
import com.example.androiderp.adaper.PopuMenuDataStructure;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lingtan on 2017/5/14.
 */

public class Common extends AppCompatActivity {
    public   PopupWindow mPopWindow;
    private PopuMenuAdapter menuAdapter;
    public ListView listView;
    private Context context;


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


}
