# paylibrary
支付宝微信支付封装，调用简单，测试无已知bug欢迎各位同学下载star。

paylibrary 使用了最新版本的支付宝与微信支付的jar进行的支付方法的封装，方法对于大家一看就懂，具体封装方法在项目中paylibrary中，内容不多大家可以拷贝到自己的项目中进行使用，也可以直接依赖。

Activity or Fragment 调用支付方法如下：

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

对应的manifests文件的配置方法，将下面的配置加入到你自己项目manifests文件当中：

        <!-- 支付宝 -->
        <activity
            android:name="com.alipay.sdk.app.H5PayActivity"
            android:configChanges="orientation|keyboardHidden|navigation"
            android:exported="false"
            android:screenOrientation="behind"
            android:windowSoftInputMode="adjustResize|stateHidden" />
        <activity
            android:name="com.alipay.sdk.auth.AuthActivity"
            android:configChanges="orientation|keyboardHidden|navigation"
            android:exported="false"
            android:screenOrientation="behind"
            android:windowSoftInputMode="adjustResize|stateHidden" />

        <!-- 微信支付 -->
        <activity
            android:name="com.liuchao.paylibrary.wxpay.WxPayResultActivity"
            android:configChanges="orientation|keyboardHidden|navigation|screenSize"
            android:launchMode="singleTop"
            android:theme="@android:style/Theme.Translucent.NoTitleBar" />

        <activity-alias
            android:name=".wxapi.WXPayEntryActivity"
            android:exported="true"
            android:targetActivity="com.liuchao.paylibrary.wxpay.WxPayResultActivity" />

这中封装方式很好的解决了，微信支付必须要求要在wxapi文件目录下的WXPayEntryActivity中回调支付结果的问题，可以使大家支付activity变得更加整洁。

