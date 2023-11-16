package com.yuan.wxlogin

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothGattServerCallback
import android.bluetooth.BluetoothGattService
import android.bluetooth.BluetoothManager
import android.bluetooth.le.AdvertiseData
import android.bluetooth.le.AdvertiseSettings
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.Bundle
import android.os.ParcelUuid
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
import java.util.UUID


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
                    suportWX()
                },
                Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                content = { Text(text = "写运动", color = Color.Green) }
            )
            Button(
                {
                    readWX()
                },
                Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                content = { Text(text = "读蓝牙", color = Color.Green) }
            )
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

    @SuppressLint("MissingPermission")
    private fun suportWX() {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager?
        val serviceUUID = UUID.fromString("0000fee7-0000-1000-8000-00805f9b34fb")
        val charactUUID = UUID.fromString("0000fec9-0000-1000-8000-00805f9b34fb")
        val charactUUID1 = UUID.fromString("0000fea1-0000-1000-8000-00805f9b34fb")
        val charactUUID2 = UUID.fromString("0000fea2-0000-1000-8000-00805f9b34fb")


        // 初始化BLE适配器
        val bluetoothAdapter = bluetoothManager?.adapter

// 初始化GATT服务器
        val bluetoothGattServer = bluetoothManager?.openGattServer(this, object : BluetoothGattServerCallback() {
            // GATT服务器回调方法

            override fun onConnectionStateChange(device: BluetoothDevice?, status: Int, newState: Int) {
                Log.e("EEEEEEEEEE", "onConnectionStateChange $device $status $newState")
            }

            override fun onServiceAdded(status: Int, service: BluetoothGattService?) {
                Log.e("EEEEEEEEEE", "onServiceAdded $service $status")
            }

            override fun onCharacteristicReadRequest(
                device: BluetoothDevice?,
                requestId: Int,
                offset: Int,
                characteristic: BluetoothGattCharacteristic?
            ) {
                // 处理读请求
                Log.e("EEEEEEEEEE", "onCharacteristicReadRequest  $device $requestId $offset $characteristic ")
            }

            override fun onCharacteristicWriteRequest(
                device: BluetoothDevice?,
                requestId: Int,
                characteristic: BluetoothGattCharacteristic?,
                preparedWrite: Boolean,
                responseNeeded: Boolean,
                offset: Int,
                value: ByteArray?
            ) {
                // 处理写请求
                Log.e(
                    "EEEEEEEEEE",
                    "onCharacteristicWriteRequest $device $requestId $characteristic $preparedWrite $responseNeeded $offset $value"
                )

            }

            override fun onDescriptorReadRequest(
                device: BluetoothDevice?,
                requestId: Int,
                offset: Int,
                descriptor: BluetoothGattDescriptor?
            ) {
                Log.e("EEEEEEEEEE", "onDescriptorReadRequest  $device $requestId $offset $descriptor ")
            }

            override fun onDescriptorWriteRequest(
                device: BluetoothDevice?,
                requestId: Int,
                descriptor: BluetoothGattDescriptor?,
                preparedWrite: Boolean,
                responseNeeded: Boolean,
                offset: Int,
                value: ByteArray?
            ) {
                Log.e(
                    "EEEEEEEEEE",
                    "onDescriptorWriteRequest $device $requestId $descriptor $preparedWrite $responseNeeded $offset $value"
                )
            }

            override fun onExecuteWrite(device: BluetoothDevice?, requestId: Int, execute: Boolean) {
                Log.e("EEEEEEEEEE", "onExecuteWrite $device $requestId $execute")
            }

            override fun onNotificationSent(device: BluetoothDevice?, status: Int) {
                Log.e("EEEEEEEEEE", "onNotificationSent $device $status")
            }

            override fun onMtuChanged(device: BluetoothDevice?, mtu: Int) {
                Log.e("EEEEEEEEEE", "onMtuChanged $mtu")
            }

            override fun onPhyUpdate(device: BluetoothDevice?, txPhy: Int, rxPhy: Int, status: Int) {
                Log.e("EEEEEEEEEE", "onPhyUpdate $device $txPhy $rxPhy $status")
            }

            override fun onPhyRead(device: BluetoothDevice?, txPhy: Int, rxPhy: Int, status: Int) {
                Log.e("EEEEEEEEEE", "onPhyRead $device $txPhy $rxPhy $status")
            }

        })
        val gattService = BluetoothGattService(serviceUUID, BluetoothGattService.SERVICE_TYPE_PRIMARY);
// 创建特征
        val gattCharacteristic1 = BluetoothGattCharacteristic(
            charactUUID1,
            BluetoothGattCharacteristic.PROPERTY_READ and BluetoothGattCharacteristic.PROPERTY_WRITE,
            BluetoothGattCharacteristic.PERMISSION_READ and BluetoothGattCharacteristic.PERMISSION_WRITE
        )
// 创建特征
        val gattCharacteristic2 = BluetoothGattCharacteristic(
            charactUUID2,
            BluetoothGattCharacteristic.PROPERTY_READ and BluetoothGattCharacteristic.PROPERTY_WRITE,
            BluetoothGattCharacteristic.PERMISSION_READ and BluetoothGattCharacteristic.PERMISSION_WRITE
        )
// 将特征添加到服务中
        gattService.addCharacteristic(gattCharacteristic1)
        gattService.addCharacteristic(gattCharacteristic2)
// 将服务添加到GATT服务器中
        bluetoothGattServer?.addService(gattService)

        //初始化广播设置
        //初始化广播设置
        val advertiseSettings = AdvertiseSettings.Builder() //设置广播模式，以控制广播的功率和延迟。
            .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_POWER) //发射功率级别
            .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH) //不得超过180000毫秒。值为0将禁用时间限制。
            .setTimeout(3000) //设置是否可以连接
            .setConnectable(false)
            .build()
        //初始化广播包
        val advertiseData =  AdvertiseData.Builder()
            //设置广播设备名称
            .setIncludeDeviceName(true)
            //设置发射功率级别
            .setIncludeDeviceName(true)
            .build();

//初始化扫描响应包
        val scanResponseData = AdvertiseData.Builder()
            //隐藏广播设备名称
            .setIncludeDeviceName(false)
            //隐藏发射功率级别
            .setIncludeDeviceName(false)
            //设置广播的服务`UUID`
            .addServiceUuid(ParcelUuid(serviceUUID))
            .addServiceData(ParcelUuid(charactUUID1),)
            .addServiceData(ParcelUuid(charactUUID2),)
            //设置厂商数据
            .addManufacturerData(0x11,hexStrToByte(mData))
            .build();

    }

    @SuppressLint("MissingPermission")
    private fun readWX() {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager?
        val serviceUUID = UUID.fromString("0000fee7-0000-1000-8000-00805f9b34fb")
        val charactUUID1 = UUID.fromString("0000fea1-0000-1000-8000-00805f9b34fb")
        val charactUUID2 = UUID.fromString("0000fea2-0000-1000-8000-00805f9b34fb")

        bluetoothManager?.adapter?.bluetoothLeScanner?.startScan(object : ScanCallback() {
            @SuppressLint("MissingPermission")
            override fun onScanResult(callbackType: Int, result: ScanResult?) {
                super.onScanResult(callbackType, result)
                Log.e("EEEEEEEEEEEE", "$callbackType $result ${result?.device?.name}")

//                result?.device?.connectGatt(this@MainActivity, false, object : BluetoothGattCallback() {
//
//                })
            }
        })
    }

    private fun sign() {
        try {
            val packageInfo = packageManager.getPackageInfo("com.tencent.mm", PackageManager.GET_SIGNATURES)
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

    fun intToIp(ipInt: Int): String {
        val sb = StringBuilder()
        sb.append(ipInt and 0xFF).append(".")
        sb.append(ipInt shr 8 and 0xFF).append(".")
        sb.append(ipInt shr 16 and 0xFF).append(".")
        sb.append(ipInt shr 24 and 0xFF)
        return sb.toString()
    }

}