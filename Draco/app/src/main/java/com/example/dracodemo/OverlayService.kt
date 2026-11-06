
package com.example.bgmcompanion

import android.app.*
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import android.view.Gravity
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class OverlayService : Service() {

    private var windowManager: WindowManager? = null
    private var panel: LinearLayout? = null
    private val channelId = "bgmi_companion"

    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "BGMI Companion",
                NotificationManager.IMPORTANCE_LOW
            )

            getSystemService(NotificationManager::class.java)
                .createNotificationChannel(channel)
        }

        val notification = Notification.Builder(this, channelId)
            .setContentTitle("BGMI Companion")
            .setContentText("Floating panel is active")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .build()

        startForeground(1001, notification)

        if (Settings.canDrawOverlays(this)) {
            showPanel()
        } else {
            stopSelf()
        }
    }

    private fun showPanel() {
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(20, 16, 20, 16)
            setBackgroundColor(0xEE202A36.toInt())
        }

        val title = TextView(this).apply {
            text = "BGMI Companion"
            textSize = 17f
            setTextColor(0xFFFFFFFF.toInt())
        }

        val info = TextView(this).apply {
            text = "Quick gaming tools\\nNo game data access"
            textSize = 13f
            setTextColor(0xFFFFFFFF.toInt())
        }

        val close = Button(this).apply {
            text = "Close Panel"
            setOnClickListener { stopSelf() }
        }

        layout.addView(title)
        layout.addView(info)
        layout.addView(close)

        val type =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else
                WindowManager.LayoutParams.TYPE_PHONE

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            type,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.END
            x = 24
            y = 180
        }

        panel = layout
        windowManager?.addView(layout, params)
    }

    override fun onDestroy() {
        panel?.let {
            try {
                windowManager?.removeView(it)
            } catch (_: Exception) {
            }
        }

        panel = null
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
