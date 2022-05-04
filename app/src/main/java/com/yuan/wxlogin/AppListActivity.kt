package com.yuan.wxlogin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.annotation.SuppressLint
import android.content.pm.ApplicationInfo

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_app_list.*
import java.math.BigInteger
import java.security.MessageDigest


class AppListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_list)
        with(rv_list) {
            layoutManager = LinearLayoutManager(this@AppListActivity)
            adapter = AppAdapter()
            setHasFixedSize(true)
            (adapter as AppAdapter).setData(getAllAppInfo())
        }

    }


    /**
     * 获取手机已安装应用列表
     * @return
     */
    fun getAllAppInfo(): ArrayList<AppInfo> {
        val appBeanList: ArrayList<AppInfo> = ArrayList()
        val list = packageManager.getInstalledPackages(0)
        for (app in list) {
            // 判断是否是属于系统的apk
            if (app.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0) {
//                bean.setSystem(true);
            } else {
                appBeanList.add(
                    AppInfo(
                        app.applicationInfo.loadIcon(packageManager),
                        packageManager.getApplicationLabel(app.applicationInfo).toString(),
                        app.applicationInfo.packageName
                    )
                )
            }
        }
        return appBeanList
    }


    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        var ivIcon = view.findViewById<ImageView>(R.id.iv_icon)
        var tvAppName = view.findViewById<TextView>(R.id.tv_app_name)
        fun refreshView(appInfo: AppInfo) {
            ivIcon.setImageDrawable(appInfo.icon)
            tvAppName.text = appInfo.laber
            view.setOnClickListener {
                Log.e("EEEEEEE", appInfo.packageName)
                sign(appInfo.packageName)
            }
        }

        fun sign(pkg: String) {
            try {
                val packageInfo = view.context.packageManager.getPackageInfo(pkg, 64)
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

    }

    class AppAdapter : RecyclerView.Adapter<ViewHolder>() {

        private val appBeanList: ArrayList<AppInfo> = ArrayList()

        @SuppressLint("NotifyDataSetChanged")
        fun setData(apps: ArrayList<AppInfo>) {
            appBeanList.clear()
            appBeanList.addAll(apps)
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.layout_app_list_item, null)
            )
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val appInfo = appBeanList[position]
            holder.refreshView(appInfo)
        }

        override fun getItemCount(): Int {
            return appBeanList.size
        }

    }

    data class AppInfo(val icon: Drawable, val laber: String, val packageName: String)
}