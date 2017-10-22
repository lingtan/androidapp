package com.example.androiderp.ui;

import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by lingtan on 2017/5/10.
 */

public class CGridView extends GridView {
    public CGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CGridView(Context context) {
        super(context);
    }

    public CGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }


}
