package com.yuan.wxlogin;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by jiangwei1-g on 2016/9/7.
 */
public class PmsHookBinderInvocationHandler implements InvocationHandler {

    private Object base;

    //应用正确的签名信息
    private String SIGN;
    private String appPkgName = "";
    private int hashCode;

    public PmsHookBinderInvocationHandler(Object base, String sign, String appPkgName, int hashCode) {
        try {
            this.base = base;
            this.SIGN = sign;
            this.appPkgName = appPkgName;
            this.hashCode = hashCode;
            System.getProperty("http.proxyPort");
        } catch (Exception e) {
            Log.d("jw", "error:" + Log.getStackTraceString(e));
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Log.i("FFFFFFFFFFFFFFF", method.getName());
        if ("getPackageInfo".equals(method.getName())) {
            String pkgName = (String) args[0];
            Object param2 = args[1];
            if (param2 != null) {
                Long flag = Long.getLong(param2.toString(), 0L);
                Log.d("FFFFFFFFFFFFF", pkgName + " ::::: " + flag);
                if (flag.intValue() == PackageManager.GET_SIGNATURES && appPkgName.equals(pkgName)) {
                    Signature sign = new Signature(SIGN);
                    PackageInfo info = (PackageInfo) method.invoke(base, args);
                    info.signatures[0] = sign;
                    Log.d("FFFFFFFFFFFFF", sign.toCharsString());
                    return info;
                }
            }
        }
        return method.invoke(base, args);
    }

}
