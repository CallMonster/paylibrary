package com.liuchao.paylibrary.alipay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.alipay.sdk.app.PayTask;
import com.liuchao.paylibrary.nozzle.AlipayResultCallBack;

import java.util.Map;

/**
 * Created by liuchao on 17/4/10.
 */
public class Alipay {
    /**
     * 支付宝支付业务：入参app_id
     */
    public static final String APPID = "";

    /**
     * 支付宝账户登录授权业务：入参pid值
     */
    public static final String PID = "";
    /**
     * 支付宝账户登录授权业务：入参target_id值
     */
    public static final String TARGET_ID = "";

    /** 商户私钥，pkcs8格式 */
    /** 如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个 */
    /** 如果商户两个都设置了，优先使用 RSA2_PRIVATE */
    /** RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议使用 RSA2_PRIVATE */
    /** 获取 RSA2_PRIVATE，建议使用支付宝提供的公私钥生成工具生成， */
    /**
     * 工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097&docType=1
     */
    public static final String RSA2_PRIVATE = "";
    public static final String RSA_PRIVATE = "";

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;

    public static final int PAY_ALIPAY = 3;
    public static final int PAY_WECHAT = 4;


    private Activity mContext;
    private String mPayInfo;
    private AlipayResultCallBack mAlipayResultCallBack;
    private static Alipay mAlipay;

    public Alipay(Activity context) {
        mContext = context;
    }

    public static Alipay init(Activity context){
        if (mAlipay==null){
            mAlipay=new Alipay(context);
        }
        return mAlipay;
    }

    /**
     * 支付宝支付业务
     */
    public void pay(String payInfo, AlipayResultCallBack alipayResultCallBack) {

        mPayInfo = payInfo;
        mAlipayResultCallBack = alipayResultCallBack;

        if (null==alipayResultCallBack){
            new AlertDialog.Builder(mContext).setTitle("警告").setMessage("AlipayResultCallBack回调不能为空")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {

                        }
                    }).show();
            return;
        }

        if (null==payInfo){
            new AlertDialog.Builder(mContext).setTitle("警告").setMessage("支付信息不能为空")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {

                        }
                    }).show();
            return;
        }


        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(mContext);
                Map<String, String> result = alipay.payV2(mPayInfo, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();

    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    AliPayResult payResult = new AliPayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        //支付成功
                        mAlipayResultCallBack.paySuccess();
                    } else if (TextUtils.equals(resultStatus, "8000")) {
                        //支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        mAlipayResultCallBack.payFail();
                    } else if (TextUtils.equals(resultStatus, "6001")) {
                        //支付取消
                        mAlipayResultCallBack.payCancel();
                    } else if (TextUtils.equals(resultStatus, "6002")) {
                        //网络连接出错
                        mAlipayResultCallBack.payFail();
                    } else if (TextUtils.equals(resultStatus, "4000")) {
                        mAlipayResultCallBack.payFail();
                        //支付错误
                    }else {
                        mAlipayResultCallBack.payFail();
                    }

                    break;
                }

                default:
                    break;
            }
        }

        ;
    };
}
