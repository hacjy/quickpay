package com.ylzb.fastpay_demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ylzb.fastpay.PayConfig;
import com.ylzb.fastpay.WebviewActivity;
import com.ylzb.fastpay.pay.PayManager;

public class MainActivity extends AppCompatActivity {
    private String url = "http://118.126.107.18:82/#/cashier?mch_id=3&out_order_no=48&token=2434dd1eed6542afbc317f8400926163";

    private TextView tvInfo;
    private Button btnPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        initListener();
    }

    private void initView(){
        tvInfo = findViewById(R.id.tv_info);
        btnPay = findViewById(R.id.btn_pay);
    }

    private void initData(){
        PayManager.getInstance().initPayConfig(new PayConfig(MainActivity.this,""));

        tvInfo.setText(url);
    }

    private void initListener(){
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, WebviewActivity.class);
                intent.putExtra(WebviewActivity.EXTRA_TITLE,"支付页面");
                intent.putExtra(WebviewActivity.EXTRA_URL,url);
                startActivity(intent);
            }
        });
    }
}


