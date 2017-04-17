package com.welink.walk;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.liuchao.paylibrary.alipay.Alipay;
import com.liuchao.paylibrary.nozzle.AlipayResultCallBack;
import com.liuchao.paylibrary.nozzle.WxPayResultCallBack;
import com.liuchao.paylibrary.wxpay.WxPay;

import java.util.HashMap;

public class MainActivity extends Activity {

    private Button mBtnWxPay;
    private Button mBtnAliPay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnAliPay= (Button) findViewById(R.id.btn_ali_pay);
        mBtnWxPay= (Button) findViewById(R.id.btn_wx_pay);

        //发起微信支付
        mBtnAliPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //模拟支付宝参数
                String payInfo="\"payment_type=\\\"1\\\"&out_trade_no=\\\"ORDTR17041716210000001749\\\"&partner=\\\"2088221885967853\\\"&service=\\\"mobile.securitypay.pay\\\"&_input_charset=\\\"utf-8\\\"&subject=\\\"廊坊亲子\\\"&total_fee=\\\"1.00\\\"&notify_url=\\\"http://123.57.210.36:8015/alipay/notifyUrlFetcher\\\"&seller_id=\\\"2088221885967853\\\"&sign=\\\"pn1I7Qi4upyg3JGlvWJr0G0uUumV4gZUu8XM9BPrNdyMswS5wfF4GiWe1RZkcTGadeSNc7exPGOOOWoJM%2BBhYWONcWb4JFGBIQD2rZRd2tSK7hSeRqdQUabryTAebvsnnAYHUgoljQXoIFKZElkn3DRJj5MGz%2FwDl6HJbZCLwGg%3D\\\"&sign_type=\\\"RSA\\\"\"";

                Alipay.init(MainActivity.this).pay(payInfo, new AlipayResultCallBack() {
                    @Override
                    public void paySuccess() {

                    }

                    @Override
                    public void payCancel() {

                    }

                    @Override
                    public void payFail() {

                    }
                });
            }
        });


        mBtnWxPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //获取数据并拼参数
                HashMap payInfo=new HashMap();

                payInfo.put("appId","");
                payInfo.put("nonceStr","");
                payInfo.put("sign","");
                payInfo.put("partnerId","");
                payInfo.put("prepayId","");
                payInfo.put("timeStamp","");
                payInfo.put("packageValue","");

                WxPay.init(MainActivity.this,"应用的appId").pay(payInfo, new WxPayResultCallBack() {
                    @Override
                    public void paySuccess() {

                    }

                    @Override
                    public void payCancel() {

                    }

                    @Override
                    public void payFail() {

                    }
                });
            }
        });


    }

}
