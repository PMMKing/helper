package com.yuan.wxlogin

import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tencent.mm.opensdk.modelmsg.SendAuth
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*
import java.math.BigInteger
import java.security.MessageDigest


class MainActivity : AppCompatActivity() {

    var showTvText = mutableStateOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (getApplicationContext().getSystemService(Context.WIFI_SERVICE) as WifiManager?)?.apply {
            connectionInfo?.ipAddress?.let {
                showTv(intToIp(it))
            }
        }
        setContent {
            GuidePage()
        }
    }

    @Preview("guide")
//    @Preview("guide - big", fontScale = 1f)
    @Composable
    private fun GuidePage() {
        Column(
            modifier = Modifier
                .background(Color.Black)
                .padding(20.dp)
                .fillMaxHeight()
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Bottom
        ) {
            Column(
                Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(showTvText.value, color = Color.Green)
            }
            Button(
                {
                    sign()
                },
                Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                content = { Text(text = "WX TEST", color = Color.Green) }
            )
            Button(
                {
                    startActivity(Intent(this@MainActivity, AppListActivity::class.java))
                },
                Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                content = { Text(text = "安装列表", color = Color.Green) })
            Button(
                {
                    val req = SendAuth.Req()
                    req.scope = "snsapi_userinfo"
                    req.state = "wechat_sdk_demo_test"
                    MainApp.createWXAPI?.sendReq(req)
                },
                Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                content = { Text(text = "WX Login", color = Color.Green) })
            Button(
                {
                    try {
                        exec("setprop service.adb.tcp.port 5555 \nstop adbd \nstart adbd\nexit\n")
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                },
                Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                content = { Text(text = "Start Adb", color = Color.Green) })
        }
    }

    private fun sign() {
        try {
            val packageInfo = packageManager.getPackageInfo("com.tencent.mm", 64)
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
            showTvText.value = "${o.toString()}\n${showTvText.value}"
            Log.d("WWWWWWWWWW", o.toString() ?: "")
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