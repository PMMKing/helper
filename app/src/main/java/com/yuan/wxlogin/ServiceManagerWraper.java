package com.yuan.wxlogin;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by jiangwei1-g on 2016/9/7.
 */
public class ServiceManagerWraper {
	
    public static void hookPMS(Context context, String signed, String appPkgName, int hashCode){
        try{
            // 获取全局的ActivityThread对象
            Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
            Method currentActivityThreadMethod =
            		activityThreadClass.getDeclaredMethod("currentActivityThread");
            Object currentActivityThread = currentActivityThreadMethod.invoke(null);
            // 获取ActivityThread里面原始的sPackageManager
            Field sPackageManagerField = activityThreadClass.getDeclaredField("sPackageManager");
            sPackageManagerField.setAccessible(true);
            Object sPackageManager = sPackageManagerField.get(currentActivityThread);
            // 准备好代理对象, 用来替换原始的对象
            Class<?> iPackageManagerInterface = Class.forName("android.content.pm.IPackageManager");
            Object proxy = Proxy.newProxyInstance(
                    iPackageManagerInterface.getClassLoader(),
                    new Class<?>[] { iPackageManagerInterface },
                    new PmsHookBinderInvocationHandler(sPackageManager, signed, appPkgName, hashCode));
            // 1. 替换掉ActivityThread里面的 sPackageManager 字段
            sPackageManagerField.set(currentActivityThread, proxy);
            // 2. 替换 ApplicationPackageManager里面的 mPM对象
            PackageManager pm = context.getPackageManager();
            Field mPmField = pm.getClass().getDeclaredField("mPM");
            mPmField.setAccessible(true);
            mPmField.set(pm, proxy);
            android.os.Process.killProcess(0);
            System.exit(0);
        }catch (Exception e){
            Log.d("jw", "hook pms error:"+ Log.getStackTraceString(e));
        }
    }
    
    public static void hookPMS(Context context){
    	String dftt = "3082021d30820186a0030201020204535bc20e300d06092a864886f70d01010505003052310b30090603550406130238363110300e060355040813074265694a696e673110300e060355040713074265694a696e67310e300c060355040a13054368696e61310f300d06035504031306436f6f4875613020170d3134303432363134323632325a180f32313134303430323134323632325a3052310b30090603550406130238363110300e060355040813074265694a696e673110300e060355040713074265694a696e67310e300c060355040a13054368696e61310f300d06035504031306436f6f48756130819f300d06092a864886f70d010101050003818d00308189028181008027e899a73dbef3cb8c26c959892b2d45f350cea9651f566146e680aa50b47f7d767ab393f687e569c0a2ed0fefde962b703ef386dfd6c00d3fb5e2ce7732929f8d59c58381e81d87bc2e2b9dd1b6e4a041013dde0b24ccbbfa91c30c66852b7fd3f6f28ea274569791b8a8e3c3dd8dd96f43b5744905f7d24f19319b9d7f9d0203010001300d06092a864886f70d010105050003818100096092338eada9868813659715848dc7f89194e34d322684ccb7e46c5cd50e7e93a7453e2da4e98fa83495b14bb4855a4c045b7ae51a97835d45bc9a6e6592bff12a319f3121a2d1b02393002ae13db198f91bdafc6feb114cba9b2605a9f93521bf829c4f4360381b1a5ae6c440dc759e7a11087b11cd63296f65018d6279e5";
    	hookPMS(context, dftt, context.getPackageName(), 0);
    }
    
}
