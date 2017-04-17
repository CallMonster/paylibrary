package com.liuchao.paylibrary.wxpay;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;


import com.liuchao.paylibrary.nozzle.WxPayResultCallBack;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.HashMap;

/**
 * Created by liuchao on 17/4/10.
 */
public class WxPay {

    private static IWXAPI mIwxapi;
    private HashMap<String, String> mWxDataMap;
    private Context mContext;
    private String mAppId;
    private static WxPay mWxPay;

    private final static int WXPAY_SUCCESS=0;
    private final static int WXPAY_FAIL=-1;
    private final static int WXPAY_CANCEL=-2;

    private WxPayResultCallBack mWxPayResultCallBack;

    public WxPay(Context context, String wx_appid) {

        mContext=context;
        mAppId=wx_appid;

        mIwxapi = WXAPIFactory.createWXAPI(context, wx_appid);
        mIwxapi.registerApp(wx_appid);
    }

    public static WxPay init(Context context, String wx_appid) {
        if(mWxPay == null) {
            mWxPay = new WxPay(context, wx_appid);
        }
        return mWxPay;
    }

    public static WxPay getInstance(){
        return mWxPay;
    }

    public IWXAPI getWXApi() {
        return mIwxapi;
    }

    public void pay(HashMap<String,String> data,WxPayResultCallBack wxPayResultCallBack) {

        mWxPayResultCallBack=wxPayResultCallBack;

        if (regToWx(mAppId)) {
            if (isWxPayable()) {
                PayReq payReq = new PayReq();
                payReq.appId = data.get("appid");
                payReq.nonceStr = data.get("nonce_str");
                payReq.sign = data.get("sign");
                payReq.partnerId = data.get("partnerid");
                payReq.prepayId = data.get("prepay_id");
                payReq.timeStamp = data.get("timestamp");
                payReq.packageValue = data.get("package");
                mIwxapi.sendReq(payReq);
            } else {
                Toast.makeText(mContext, "微信未开启或未安装微信", Toast.LENGTH_SHORT).show();
            }
        }else {
            new AlertDialog.Builder(mContext).setTitle("警告").setMessage("appId验证失败")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {

                        }
                    }).show();
        }
    }

    /**
     * 微信参数验证
     *
     * @param appId
     * @return
     */
    private boolean regToWx(String appId) {
        mIwxapi = WXAPIFactory.createWXAPI(mContext, appId, false);
        return mIwxapi.registerApp(appId);
    }

    /**
     * 微信是否可支付
     *
     * @return
     */
    private boolean isWxPayable() {
        return (mIwxapi.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT);
    }


    protected void onResp(int errorCode){
        if(mWxPayResultCallBack == null) {
            return;
        }

        switch (errorCode){
            case WXPAY_SUCCESS:
                mWxPayResultCallBack.paySuccess();
                break;
            case WXPAY_FAIL:
                mWxPayResultCallBack.payFail();
                break;
            case WXPAY_CANCEL:
                mWxPayResultCallBack.payCancel();
                break;
        }

        mWxPayResultCallBack = null;
    }

}
