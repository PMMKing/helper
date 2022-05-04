package com.yuan.wxlogin

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import com.alibaba.fastjson.JSON
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.yuan.wxlogin.MainApp

/**
 * @author shucheng.qu
 * @date 12/26/20 2:55 PM
 */
class WXEntryActivity : ComponentActivity(), IWXAPIEventHandler {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainApp.createWXAPI?.handleIntent(intent, this)
    }

    override fun onReq(baseReq: BaseReq) {
        Log.e("WWWWWWWWWW onReq", baseReq.toString())
        finish()
    }

    override fun onResp(baseResp: BaseResp) {
        Log.e("WWWWWWWWWW onResp", JSON.toJSONString(baseResp))
        when (baseResp.errCode) {
            BaseResp.ErrCode.ERR_OK -> {
                if (baseResp is SendAuth.Resp) {
                    var code = baseResp.code
                    Log.e("WWWWWWWWWW onResp", code)
                    code
                }
            }
        }

        finish()

    }
}