package com.ylzb.fastpay.util;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;

public class Base64Util {
    /**
     * 加密
     * oldWord：需要加密的文字/比如密码
     */
    public static String setEncryption(String oldWord){
        String encodeWord = oldWord;
        try {
            if(!TextUtils.isEmpty(oldWord)) {
                encodeWord = Base64.encodeToString(oldWord.getBytes("utf-8"), Base64.NO_WRAP);
                Log.i("Tag", " encode wrods = " + encodeWord);
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodeWord;
    }

    /**
     * 解密
     * encodeWord：加密后的文字/比如密码
     */
    public static String setDecrypt(String encodeWord){
        String decodeWord = encodeWord;
        try {
            decodeWord = new String(Base64.decode(encodeWord, Base64.NO_WRAP), "utf-8");
            Log.i("Tag", "decode wrods = " + decodeWord);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return decodeWord;
    }
}
