package com.example.androiderp.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class AcivityPostBen implements Parcelable{

    private int setClassType;
    private String name;
    private String requestServlet;
    private  String acivityName;
    private String isSelect="";
    public int getSetClassType() {
        return setClassType;
    }

    public void setSetClassType(int setClassType) {
        this.setClassType = setClassType;
    }

    public String getRequestServlet() {
        return requestServlet;
    }

    public void setRequestServlet(String requestServlet) {
        this.requestServlet = requestServlet;
    }

    public String getAcivityName() {
        return acivityName;
    }

    public void setAcivityName(String acivityName) {
        this.acivityName = acivityName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsSelect() {
        return isSelect;
    }

    public void setIsSelect(String isSelect) {
        this.isSelect = isSelect;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(setClassType);
        dest.writeString(requestServlet);
        dest.writeString(acivityName);
        dest.writeString(name);
        dest.writeString(isSelect);





    }
    public static final Creator<AcivityPostBen> CREATOR=new Creator<AcivityPostBen>(){
        @Override
        public AcivityPostBen createFromParcel(Parcel source) {
            AcivityPostBen acivityPostBen=new AcivityPostBen();
            acivityPostBen.setClassType =source.readInt();
            acivityPostBen.requestServlet=source.readString();
           acivityPostBen.acivityName=source.readString();
            acivityPostBen.name=source.readString();
            acivityPostBen.isSelect=source.readString();

            return acivityPostBen;
        }

        @Override
        public AcivityPostBen[] newArray(int size) {
            return new AcivityPostBen[size];
        }
    };
}
