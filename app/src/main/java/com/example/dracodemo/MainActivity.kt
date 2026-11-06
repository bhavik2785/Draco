
package com.example.bgmcompanion

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Column(
                    Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        "BGMI Companion",
                        style = MaterialTheme.typography.headlineMedium
                    )

                    Text("Your gaming companion and quick settings panel.")

                    Button(
                        onClick = { enableOverlay() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Enable Floating Panel")
                    }

                    Button(
                        onClick = {
                            stopService(
                                Intent(this@MainActivity, OverlayService::class.java)
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Stop Floating Panel")
                    }

                    Button(
                        onClick = { openBgmi() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Open BGMI")
                    }

                    Text("Sensitivity notes")

                    var camera by remember { mutableFloatStateOf(90f) }
                    var ads by remember { mutableFloatStateOf(85f) }
                    var gyro by remember { mutableFloatStateOf(250f) }

                    Text("Camera: ${camera.toInt()}")
                    Slider(
                        value = camera,
                        onValueChange = { camera = it },
                        valueRange = 0f..100f
                    )

                    Text("ADS: ${ads.toInt()}")
                    Slider(
                        value = ads,
                        onValueChange = { ads = it },
                        valueRange = 0f..100f
                    )

                    Text("Gyroscope: ${gyro.toInt()}")
                    Slider(
                        value = gyro,
                        onValueChange = { gyro = it },
                        valueRange = 0f..400f
                    )

                    Text(
                        "These sliders are personal notes only. " +
                        "They do not change BGMI settings or inspect game data."
                    )
                }
            }
        }
    }

    private fun enableOverlay() {
        if (!Settings.canDrawOverlays(this)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            startActivity(intent)
        } else {
            ContextCompat.startForegroundService(
                this,
                Intent(this, OverlayService::class.java)
            )
        }
    }

    private fun openBgmi() {
        val intent =
            packageManager.getLaunchIntentForPackage("com.pubg.imobile")

        if (intent != null) {
            startActivity(intent)
        }
    }
}
