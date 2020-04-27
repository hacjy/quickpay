package com.ylzb.fastpay.pay;

public interface IPayListener {
    void onSuccess();
    void onCancel();
    void onFail();
    void payResult(String param);
}
