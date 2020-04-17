package com.ylzb.fastpay;

import android.os.Parcel;
import android.os.Parcelable;

public class JsParamInfo implements Parcelable {
    public static final String PAY_TYPE_WX = "wx";
    public static final String PAY_TYPE_ZFB = "alipay";

    public String type;//类型 支付宝 微信
    public String pay_info; //支付参数


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.type);
        dest.writeString(this.pay_info);
    }

    public JsParamInfo() {
    }

    protected JsParamInfo(Parcel in) {
        this.type = in.readString();
        this.pay_info = in.readString();
    }

    public static final Parcelable.Creator<JsParamInfo> CREATOR = new Parcelable.Creator<JsParamInfo>() {
        @Override
        public JsParamInfo createFromParcel(Parcel source) {
            return new JsParamInfo(source);
        }

        @Override
        public JsParamInfo[] newArray(int size) {
            return new JsParamInfo[size];
        }
    };
}
