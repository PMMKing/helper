package com.yuan.wxlogin

import android.app.Application
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory

/**
 * @author shucheng.qu
 * @date 12/26/20 2:32 PM
 */

public class MainApp : Application() {

    companion object {
        var app: MainApp? = null
        var appid = BuildConfig.WX_APPID
        var createWXAPI: IWXAPI? = null
    }

    override fun onCreate() {
        super.onCreate()
        app = this
        createWXAPI = WXAPIFactory.createWXAPI(app, appid, true)
        createWXAPI?.registerApp(appid)
        ServiceManagerWraper.hookPMS(this)
    }


}


