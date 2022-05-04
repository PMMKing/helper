package com.yuan.wxlogin

import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.tencent.mm.opensdk.modelmsg.SendAuth
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*
import java.math.BigInteger
import java.security.MessageDigest


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        sign()
        (getApplicationContext().getSystemService(Context.WIFI_SERVICE) as WifiManager?)?.apply {
            connectionInfo?.ipAddress?.let {
                showTv(intToIp(it))
            }
        }

        tv_test.setOnClickListener {
        }

        tv_apps.setOnClickListener {
            startActivity(Intent(this@MainActivity, AppListActivity::class.java))
        }

        tv_login.setOnClickListener {
            val req = SendAuth.Req()
            req.scope = "snsapi_userinfo"
            req.state = "wechat_sdk_demo_test"
            MainApp.createWXAPI?.sendReq(req)
        }

        tv_adb.setOnClickListener {
            try {
                exec("setprop service.adb.tcp.port 5555 \nstop adbd \nstart adbd\nexit\n")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                Log.d("WWWWWWWWWW", source.toString() + event)
            }
        })
    }

    private fun sign() {
        try {
            val packageInfo = packageManager.getPackageInfo("com.brush.fun", 64)
            val sign = packageInfo.signatures[0]
            showTv(sign.toCharsString())
            showTv(
                BigInteger(1, MessageDigest.getInstance("MD5").digest(sign.toByteArray())).toString(
                    16
                )
            )
            showTv(
                BigInteger(
                    1,
                    MessageDigest.getInstance("SHA1").digest(sign.toByteArray())
                ).toString(
                    16
                )
            )
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private fun showTv(o: Any?) {
        o.let {
            tv_show.text = "${o.toString()}\n${tv_show.text}"
            Log.d("WWWWWWWWWW", o.toString() ?: "")
        }
    }

    fun Any.logd(any: Any?) {
        any?.let {
            Log.d("HHHHHH", toString())
        }
    }


    private fun exec(s: String) {
        var process: Process? = null
        var dos: DataOutputStream? = null
        try {
            process = Runtime.getRuntime().exec("su")
            dos = DataOutputStream(process.outputStream)
            dos.writeBytes(s)
            dos.flush()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            dos?.close()
        }
    }

    fun intToIp(ipInt: Int): String? {
        val sb = StringBuilder()
        sb.append(ipInt and 0xFF).append(".")
        sb.append(ipInt shr 8 and 0xFF).append(".")
        sb.append(ipInt shr 16 and 0xFF).append(".")
        sb.append(ipInt shr 24 and 0xFF)
        return sb.toString()
    }

}