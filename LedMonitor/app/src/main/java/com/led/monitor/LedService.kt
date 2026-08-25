package com.led.monitor

import android.app.*
import android.content.*
import android.graphics.Color
import android.os.*

class LedService : Service() {

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
            val isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL

            updateLed(level, isCharging)
        }
    }

    override fun onCreate() {
        super.onCreate()
        createChannels()
        // Đăng ký lắng nghe sự kiện pin thay đổi
        registerReceiver(receiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        
        // Bắt buộc chạy ngầm
        val notification = Notification.Builder(this, "channel_green")
            .setContentTitle("LED Monitor")
            .setContentText("Đang theo dõi pin...")
            .setSmallIcon(android.R.drawable.ic_lock_idle_charging)
            .build()
        startForeground(1, notification)
    }

    private fun updateLed(level: Int, isCharging: Boolean) {
        val manager = getSystemService(NotificationManager::class.java)
        val builder = Notification.Builder(this, "channel_green") // Mặc định
            .setSmallIcon(android.R.drawable.ic_lock_idle_charging)
            .setContentTitle("Trạng thái Pin")

        if (isCharging && level < 100) {
            // Đang sạc: Đỏ
            builder.setChannelId("channel_red")
            builder.setContentText("Đang sạc ($level%)")
        } else if (isCharging && level == 100) {
            // Sạc đầy: Xanh
            builder.setChannelId("channel_green")
            builder.setContentText("Đã sạc đầy")
        } else if (!isCharging && level < 20) {
            // Pin yếu: Đỏ nháy
            builder.setChannelId("channel_red")
            builder.setContentText("Pin yếu ($level%)")
        } else {
            // Bình thường: Xanh nháy
            builder.setChannelId("channel_green")
            builder.setContentText("Bình thường ($level%)")
        }

        // Ghi đè thông báo ID 1 để đổi màu đèn
        manager.notify(1, builder.build())
    }

    private fun createChannels() {
        val manager = getSystemService(NotificationManager::class.java)
        // Kênh Xanh lá
        val greenChannel = NotificationChannel("channel_green", "Đèn Xanh", NotificationManager.IMPORTANCE_HIGH).apply {
            enableLights(true)
            lightColor = Color.GREEN
        }
        // Kênh Đỏ
        val redChannel = NotificationChannel("channel_red", "Đèn Đỏ", NotificationManager.IMPORTANCE_HIGH).apply {
            enableLights(true)
            lightColor = Color.RED
        }
        manager.createNotificationChannels(listOf(greenChannel, redChannel))
    }

    override fun onBind(intent: Intent): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }
}