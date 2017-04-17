package com.liuchao.paylibrary.wxpay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.liuchao.paylibrary.R;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

import java.util.HashMap;

public class WxPayResultActivity extends Activity implements IWXAPIEventHandler {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wx_pay_result);

        if(WxPay.getInstance() != null) {
            WxPay.getInstance().getWXApi().handleIntent(getIntent(), this);
        } else {
            finish();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if(WxPay.getInstance() != null) {
            WxPay.getInstance().getWXApi().handleIntent(intent, this);
        }
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        if(baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if(WxPay.getInstance() != null) {
                if(baseResp.errStr != null) {
                    Log.e("wxpay", "errstr=" + baseResp.errStr);
                }
                WxPay.getInstance().onResp(baseResp.errCode);
                finish();
            }
        }
    }
}
