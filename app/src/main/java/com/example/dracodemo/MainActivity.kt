package com.example.dracodemo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Bg = Color(0xFF0B0E13)
private val Panel = Color(0xFF151B24)
private val Accent = Color(0xFF45D6C5)
private val Muted = Color(0xFF9AA7B5)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { DracoApp(this) }
    }
}

@Composable
private fun DracoApp(activity: ComponentActivity) {
    var loggedIn by remember { mutableStateOf(false) }
    var page by remember { mutableStateOf("Home") }
    var username by remember { mutableStateOf("demo") }
    var password by remember { mutableStateOf("demo123") }

    MaterialTheme(colorScheme = darkColorScheme(
        background = Bg, surface = Panel, primary = Accent,
        onBackground = Color.White, onSurface = Color.White
    )) {
        Surface(Modifier.fillMaxSize(), color = Bg) {
            if (!loggedIn) {
                LoginScreen(username, { username = it }, password, { password = it }) {
                    if (username == "demo" && password == "demo123") loggedIn = true
                }
            } else {
                Column(Modifier.fillMaxSize().padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text("DRACO DEMO", color = Accent, fontSize = 22.sp, fontWeight = FontWeight.Black)
                            Text("TRAINING UTILITY • OFFLINE", color = Muted, fontSize = 10.sp)
                        }
                        Text("DEMO", color = Accent, fontSize = 11.sp)
                    }
                    Spacer(Modifier.height(22.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("Home", "Simulator", "Settings").forEach { item ->
                            val selected = page == item
                            Surface(
                                color = if (selected) Accent.copy(alpha = .18f) else Panel,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1f).clickable { page = item }
                            ) {
                                Text(item, modifier = Modifier.padding(vertical = 12.dp),
                                    color = if (selected) Accent else Muted,
                                    fontSize = 12.sp, fontWeight = FontWeight.Bold,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                            }
                        }
                    }
                    Spacer(Modifier.height(18.dp))
                    when (page) {
                        "Home" -> HomeScreen(
                            onSimulator = { page = "Simulator" },
                            onFloating = { openFloatingPanel(activity) }
                        )
                        "Simulator" -> SimulatorScreen()
                        else -> SettingsScreen(onLogout = { loggedIn = false; page = "Home" })
                    }
                }
            }
        }
    }
}

private fun openFloatingPanel(activity: ComponentActivity) {
    if (Settings.canDrawOverlays(activity)) {
        activity.startService(Intent(activity, FloatingPanelService::class.java))
    } else {
        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:${activity.packageName}"))
        activity.startActivity(intent)
    }
}

@Composable
private fun LoginScreen(
    username: String, onUsername: (String) -> Unit,
    password: String, onPassword: (String) -> Unit,
    onLogin: () -> Unit
) {
    var error by remember { mutableStateOf(false) }
    Column(
        Modifier.fillMaxSize().padding(28.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("DRACO", color = Accent, fontSize = 34.sp, fontWeight = FontWeight.Black)
        Text("GAMING TRAINING SUITE", color = Muted, fontSize = 11.sp)
        Spacer(Modifier.height(30.dp))
        Text("Welcome back", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text("Sign in to your local demo workspace", color = Muted, fontSize = 13.sp)
        Spacer(Modifier.height(22.dp))
        OutlinedTextField(username, onUsername, label = { Text("Username") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(10.dp))
        OutlinedTextField(password, onPassword, label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
        if (error) Text("Incorrect demo credentials.", color = Color(0xFFFF7777), fontSize = 12.sp)
        Spacer(Modifier.height(18.dp))
        Button(onClick = { error = !(username == "demo" && password == "demo123"); onLogin() },
            modifier = Modifier.fillMaxWidth()) { Text("SIGN IN") }
        Spacer(Modifier.height(14.dp))
        Text("Demo login: demo / demo123", color = Muted, fontSize = 12.sp)
        Text("Local demo only — do not use a real password.", color = Muted, fontSize = 10.sp)
    }
}

@Composable
private fun HomeScreen(onSimulator: () -> Unit, onFloating: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Surface(color = Panel, shape = RoundedCornerShape(18.dp)) {
            Column(Modifier.fillMaxWidth().padding(18.dp)) {
                Text("TRAINING CONSOLE", color = Muted, fontSize = 11.sp)
                Spacer(Modifier.height(6.dp))
                Text("Practice. Configure. Improve.", fontSize = 21.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Text("A self-contained visual simulator using generated sample data.",
                    color = Muted, fontSize = 12.sp)
            }
        }
        FeatureCard("ESP-STYLE SIMULATOR", "Synthetic targets, labels, health and distance", onSimulator)
        FeatureCard("FLOATING CONTROL PANEL", "Movable on-screen demo controls", onFloating)
        Surface(color = Panel, shape = RoundedCornerShape(14.dp)) {
            Column(Modifier.padding(16.dp)) {
                Text("SYSTEM STATUS", color = Accent, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Text("Native demo library loaded • Offline mode", color = Muted, fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun FeatureCard(title: String, detail: String, action: () -> Unit) {
    Surface(color = Panel, shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth().clickable { action() }) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(title, color = Accent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(5.dp))
                Text(detail, color = Muted, fontSize = 12.sp)
            }
            Text("OPEN  ›", color = Color.White, fontSize = 11.sp)
        }
    }
}

@Composable
private fun SettingsScreen(onLogout: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text("SETTINGS", color = Accent, fontWeight = FontWeight.Bold)
        listOf("Rendering: Demo mode", "Target data: Synthetic", "Native helper: Enabled",
            "Game integration: Disabled", "Network: Not required").forEach {
            Surface(color = Panel, shape = RoundedCornerShape(12.dp)) {
                Text(it, Modifier.fillMaxWidth().padding(16.dp), color = Color.White, fontSize = 13.sp)
            }
        }
        Button(onClick = onLogout, modifier = Modifier.fillMaxWidth()) { Text("LOG OUT") }
    }
}
