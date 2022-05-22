package com.yuan.wxlogin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.math.BigInteger
import java.security.MessageDigest


class AppListActivity : AppCompatActivity() {

    var apps = mutableStateListOf<AppInfo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            contentView()
        }
        MainScope().launch(Dispatchers.IO) {
            getAllAppInfo()
        }
    }

    @Composable
    @Preview
    private fun contentView() {
        LazyColumn(
            Modifier
                .fillMaxHeight()
                .fillMaxWidth()
        ) {
            items(apps.size) { index ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clickable {
                            sign(apps[index].packageName)
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        bitmap = apps[index].icon,
                        contentDescription = "",
                        Modifier
                            .width(80.dp)
                            .height(80.dp)
                            .padding(10.dp)
                            .clip(shape = RoundedCornerShape(5.dp))
                    )
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                    ) {
                        Text(
                            text = apps[index].laber,
                            Modifier.fillMaxWidth(),
                            fontSize = 20.sp
                        )
                        Text(text = apps[index].packageName)
                    }
                }
            }
        }
    }

    fun sign(pkg: String) {
        try {
            val packageInfo = packageManager.getPackageInfo(pkg, PackageManager.GET_SIGNATURES)
            val sign = packageInfo.signatures[0]
            Log.e("EEEEEEE", sign.toCharsString())
            Log.e(
                "EEEEEEE",
                BigInteger(
                    1,
                    MessageDigest.getInstance("MD5").digest(sign.toByteArray())
                ).toString(
                    16
                )
            )
            Log.e(
                "EEEEEEE", BigInteger(
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

    /**
     * 获取手机已安装应用列表
     * @return
     */
    fun getAllAppInfo() {
        apps.clear()
        val list = packageManager.getInstalledPackages(0)
        for (app in list) {
            // 判断是否是属于系统的apk
            if (app.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0) {
//                bean.setSystem(true);
            } else {
                val drawable = app.applicationInfo.loadIcon(packageManager);
                val bitmap = Bitmap.createBitmap(
                    drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(),
                    Bitmap.Config.ARGB_8888
                )
                val canvas = Canvas(bitmap);
                drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight())
                drawable.draw(canvas)
                apps.add(
                    AppInfo(
                        bitmap.asImageBitmap(),
                        packageManager.getApplicationLabel(app.applicationInfo).toString(),
                        app.applicationInfo.packageName
                    )
                )
            }
        }
    }

    data class AppInfo(val icon: ImageBitmap, val laber: String, val packageName: String)
}