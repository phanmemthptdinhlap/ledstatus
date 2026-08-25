package com.led.monitor

import android.app.Activity
import android.content.Intent
import android.os.Bundle

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Khởi động dịch vụ chạy ngầm
        val serviceIntent = Intent(this, LedService::class.java)
        startForegroundService(serviceIntent)
        
        // Tự đóng giao diện ứng dụng
        finish()
    }
}