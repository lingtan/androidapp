package com.example.androiderp.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.bumptech.glide.Glide;
import com.example.androiderp.R;

public class CPictureFullPopupWindow extends PopupWindow {


	private ImageView imageView;
	private View mMenuView;

	public CPictureFullPopupWindow(final Activity context, String uir) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.picturefull_popupwindow_layout, null);
		imageView = (ImageView) mMenuView.findViewById(R.id.picture_full);
		Glide.with(context).load(uir).into(imageView);

		this.setContentView(mMenuView);
		this.setWidth(LayoutParams.MATCH_PARENT);
		this.setHeight(LayoutParams.MATCH_PARENT);
		this.setFocusable(true);
		this.setAnimationStyle(R.style.AnimBottom);
		ColorDrawable dw = new ColorDrawable(0xF0000000);
		this.setBackgroundDrawable(dw);
		imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Glide.get(context).clearMemory();
				dismiss();


			}
		});
		mMenuView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Glide.get(context).clearMemory();
				dismiss();
			}
		});

	}

}
