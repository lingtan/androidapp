package com.example.androiderp.bean;

import android.os.Parcel;

import org.litepal.crud.DataSupport;

/**
 * Created by lingtan on 2017/5/15.
 */

public class Custom extends ContactBean {

    private AdapterBean customCategory;

    public AdapterBean getCustomCategory() {
        return customCategory;
    }

    public void setCustomCategory(AdapterBean customCategory) {
        this.customCategory = customCategory;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(this.customCategory, flags);
    }

    public Custom() {
    }

    protected Custom(Parcel in) {
        super(in);
        this.customCategory = in.readParcelable(CustomCategory.class.getClassLoader());
    }

    public static final Creator<Custom> CREATOR = new Creator<Custom>() {
        @Override
        public Custom createFromParcel(Parcel source) {
            return new Custom(source);
        }

        @Override
        public Custom[] newArray(int size) {
            return new Custom[size];
        }
    };
}
