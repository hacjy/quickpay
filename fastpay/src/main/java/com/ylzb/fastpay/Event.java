package com.ylzb.fastpay;

public class Event {
    /**
     * 支付结果
     */
    public static class PayResultEvent{
        //1 成功 2 失败 3 取消
        public static final int PAY_RESULT_SUCCESS = 1;
        public static final int PAY_RESULT_FAILED = 2;
        public static final int PAY_RESULT_CANCEL = 3;
        public static int result;

        public PayResultEvent(int result) {
            this.result = result;
        }

        public static String resultText(){
            if (result == PAY_RESULT_SUCCESS){
                return "success";
            }else if (result == PAY_RESULT_FAILED){
                return "failed";
            }else if (result == PAY_RESULT_CANCEL){
                return "cancel";
            }
            return "";
        }
    }
}
