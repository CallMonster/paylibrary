package com.liuchao.paylibrary.nozzle;

/**
 * Created by liuchao on 17/4/10.
 */
public interface AlipayResultCallBack {
    void paySuccess();
    void payCancel();
    void payFail();
}
