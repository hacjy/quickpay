package com.ylzb.fastpay.pay;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * 支付model
 */

public class PayModel implements Serializable {
    /**
     * ReturnCode : 1
     * ReturnMessage :
     * ReturnData : 服务器返回的josn数据
     */

    private String ReturnCode;
    private String ReturnMessage;
    private List<ReturnDataBean> ReturnData;

    public String getReturnCode() {
        return ReturnCode;
    }

    public void setReturnCode(String ReturnCode) {
        this.ReturnCode = ReturnCode;
    }

    public String getReturnMessage() {
        return ReturnMessage;
    }

    public void setReturnMessage(String ReturnMessage) {
        this.ReturnMessage = ReturnMessage;
    }

    public List<ReturnDataBean> getReturnData() {
        return ReturnData;
    }

    public void setReturnData(List<ReturnDataBean> ReturnData) {
        this.ReturnData = ReturnData;
    }

    public static class ReturnDataBean implements Parcelable {
        /**
         * appid : //对应的参数后台返回
         * partnerid :
         * prepayid :
         * package : Sign=WXPay
         * noncestr :
         * timestamp : 1521789303
         * sign :
         */

        private String appid;
        private String partnerid;
        private String prepayid;
        @SerializedName("package")
        private String packageX = "Sign=WXPay";
        private String noncestr;
        private String timestamp;
        private String sign;

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getPartnerid() {
            return partnerid;
        }

        public void setPartnerid(String partnerid) {
            this.partnerid = partnerid;
        }

        public String getPrepayid() {
            return prepayid;
        }

        public void setPrepayid(String prepayid) {
            this.prepayid = prepayid;
        }

        public String getPackageX() {
            return packageX;
        }

        public void setPackageX(String packageX) {
            this.packageX = packageX;
        }

        public String getNoncestr() {
            return noncestr;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.appid);
            dest.writeString(this.partnerid);
            dest.writeString(this.prepayid);
            dest.writeString(this.packageX);
            dest.writeString(this.noncestr);
            dest.writeString(this.timestamp);
            dest.writeString(this.sign);
        }

        public ReturnDataBean() {
        }

        protected ReturnDataBean(Parcel in) {
            this.appid = in.readString();
            this.partnerid = in.readString();
            this.prepayid = in.readString();
            this.packageX = in.readString();
            this.noncestr = in.readString();
            this.timestamp = in.readString();
            this.sign = in.readString();
        }

        public static final Parcelable.Creator<ReturnDataBean> CREATOR = new Parcelable.Creator<ReturnDataBean>() {
            @Override
            public ReturnDataBean createFromParcel(Parcel source) {
                return new ReturnDataBean(source);
            }

            @Override
            public ReturnDataBean[] newArray(int size) {
                return new ReturnDataBean[size];
            }
        };
    }
}
