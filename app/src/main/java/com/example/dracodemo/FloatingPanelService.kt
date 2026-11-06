package com.example.dracodemo

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import kotlin.math.roundToInt

/**
 * A foreground service that shows a small draggable panel on top of other apps
 * using the SYSTEM_ALERT_WINDOW overlay window type.
 *
 * The panel is a self-contained demo: it shows a counter and a value that is
 * run through the native [NativeDemo.clampDisplayRadius] function. It does not
 * read, touch, or interact with any other app.
 */
class FloatingPanelService : Service() {

    private lateinit var windowManager: WindowManager
    private var panelView: View? = null
    private var counter = 0

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        startAsForeground()
        addPanel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    private fun startAsForeground() {
        val channelId = "draco_panel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nm = getSystemService(NotificationManager::class.java)
            val channel = NotificationChannel(
                channelId,
                "Floating panel",
                NotificationManager.IMPORTANCE_LOW
            )
            nm.createNotificationChannel(channel)
        }
        val notification: Notification =
            Notification.Builder(this, channelId)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.panel_running))
                .setSmallIcon(android.R.drawable.ic_menu_view)
                .build()
        startForeground(1, notification)
    }

    private fun addPanel() {
        val padding = (12 * resources.displayMetrics.density).roundToInt()

        val container = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.parseColor("#E6202124"))
            setPadding(padding, padding, padding, padding)
        }

        val title = TextView(this).apply {
            text = getString(R.string.panel_title)
            setTextColor(Color.WHITE)
            textSize = 16f
        }

        val value = TextView(this).apply {
            setTextColor(Color.parseColor("#9FD3C7"))
            textSize = 14f
            text = renderValue()
        }

        val bump = Button(this).apply {
            text = getString(R.string.panel_bump)
            setOnClickListener {
                counter++
                value.text = renderValue()
            }
        }

        val close = Button(this).apply {
            text = getString(R.string.panel_close)
            setOnClickListener { stopSelf() }
        }

        container.addView(title)
        container.addView(value)
        container.addView(bump)
        container.addView(close)

        val type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            @Suppress("DEPRECATION")
            WindowManager.LayoutParams.TYPE_PHONE
        }

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            type,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = padding
            y = padding * 8
        }

        makeDraggable(container, params)

        panelView = container
        windowManager.addView(container, params)
    }

    /** Lets the user drag the panel around the screen. */
    private fun makeDraggable(view: View, params: WindowManager.LayoutParams) {
        var startX = 0
        var startY = 0
        var touchX = 0f
        var touchY = 0f

        view.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    startX = params.x
                    startY = params.y
                    touchX = event.rawX
                    touchY = event.rawY
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    params.x = startX + (event.rawX - touchX).roundToInt()
                    params.y = startY + (event.rawY - touchY).roundToInt()
                    windowManager.updateViewLayout(view, params)
                    true
                }
                else -> false
            }
        }
    }

    private fun renderValue(): String {
        // Run the raw counter through the native clamp as a JNI demonstration.
        val raw = counter * 15f
        val clamped = NativeDemo.clampSafe(raw, min = 0f, max = 120f)
        return getString(R.string.panel_value, counter, clamped.roundToInt())
    }

    override fun onDestroy() {
        super.onDestroy()
        panelView?.let { windowManager.removeView(it) }
        panelView = null
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, FloatingPanelService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

        fun stop(context: Context) {
            context.stopService(Intent(context, FloatingPanelService::class.java))
        }
    }
}
