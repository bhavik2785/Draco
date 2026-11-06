package com.example.dracodemo

import android.app.Service
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView

/**
 * User-invoked, movable demo overlay. It only displays local demo controls.
 */
class FloatingPanelService : Service() {
    private lateinit var windowManager: WindowManager
    private var panel: View? = null
    private lateinit var params: WindowManager.LayoutParams

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(22, 16, 22, 16)
            setBackgroundColor(Color.rgb(20, 29, 39))
        }
        val header = TextView(this).apply {
            text = "DRACO DEMO   •   drag here     ×"
            setTextColor(Color.rgb(69, 214, 197))
            textSize = 13f
            setPadding(4, 4, 4, 14)
            setOnClickListener { stopSelf() }
        }
        val info = TextView(this).apply {
            text = "TRAINING OVERLAY\\nSynthetic data only\\nESP demo: ON\\nAim automation: OFF"
            setTextColor(Color.WHITE)
            textSize = 12f
        }
        root.addView(header)
        root.addView(info)
        params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply { gravity = Gravity.TOP or Gravity.START; x = 30; y = 180 }
        header.setOnTouchListener(object : View.OnTouchListener {
            var startX = 0
            var startY = 0
            var touchX = 0f
            var touchY = 0f
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        startX = params.x; startY = params.y
                        touchX = event.rawX; touchY = event.rawY
                        return true
                    }
                    MotionEvent.ACTION_MOVE -> {
                        params.x = startX + (event.rawX - touchX).toInt()
                        params.y = startY + (event.rawY - touchY).toInt()
                        windowManager.updateViewLayout(root, params)
                        return true
                    }
                }
                return false
            }
        })
        panel = root
        windowManager.addView(root, params)
    }

    override fun onDestroy() {
        panel?.let { windowManager.removeView(it) }
        panel = null
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
