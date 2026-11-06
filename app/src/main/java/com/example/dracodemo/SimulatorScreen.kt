package com.example.dracodemo

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.min

private data class DemoTarget(val x: Float, val y: Float, val name: String, val health: Float, val meters: Int)

@Composable
fun SimulatorScreen() {
    var showNames by remember { mutableStateOf(true) }
    var showHealth by remember { mutableStateOf(true) }
    var showVehicles by remember { mutableStateOf(true) }
    var radius by remember { mutableFloatStateOf(120f) }
    val safeRadius = NativeDemo.clampDisplayRadius(radius.toInt()).toFloat()
    val targets = remember {
        listOf(
            DemoTarget(.24f, .28f, "BOT-ALPHA", .85f, 42),
            DemoTarget(.70f, .38f, "BOT-BRAVO", .55f, 68),
            DemoTarget(.43f, .70f, "BOT-CHARLIE", .32f, 91)
        )
    }
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("TRAINING SIMULATOR", color = Color(0xFF45D6C5), fontSize = 13.sp)
        Text("All entities below are fictional sample data.", color = Color(0xFF9AA7B5), fontSize = 11.sp)
        Canvas(Modifier.fillMaxWidth().weight(1f).background(Color(0xFF101820), RoundedCornerShape(16.dp))) {
            val w = size.width
            val h = size.height
            // Subtle training grid.
            for (i in 1..5) {
                drawLine(Color(0x223F9E99), Offset(w * i / 6f, 0f), Offset(w * i / 6f, h), 1f)
                drawLine(Color(0x223F9E99), Offset(0f, h * i / 6f), Offset(w, h * i / 6f), 1f)
            }
            val ring = min(w, h) * (safeRadius / 400f)
            drawCircle(Color(0xFF45D6C5), ring, center = Offset(w * .5f, h * .5f), style = Stroke(2f))
            targets.forEach { target ->
                val cx = w * target.x
                val cy = h * target.y
                val boxW = 56f
                val boxH = 88f
                drawRect(Color(0xFF45D6C5), Offset(cx - boxW / 2, cy - boxH / 2), Size(boxW, boxH), style = Stroke(2f))
                drawCircle(Color(0xFFFFD166), 5f, Offset(cx, cy - boxH / 2 - 8f))
                if (showHealth) {
                    drawRect(Color.DarkGray, Offset(cx - 28f, cy + boxH / 2 + 5f), Size(56f, 5f))
                    drawRect(Color(0xFF45D6C5), Offset(cx - 28f, cy + boxH / 2 + 5f), Size(56f * target.health, 5f))
                }
            }
            if (showVehicles) {
                drawRoundRect(Color(0xFFFFD166), Offset(w * .72f, h * .78f), Size(42f, 20f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(5f), style = Stroke(2f))
            }
        }
        targets.forEach {
            Text(
                "${if (showNames) it.name else "TRAINING TARGET"}  •  ${it.meters}m  •  ${if (showHealth) (it.health * 100).toInt().toString() + "%" else "Health hidden"}",
                color = Color(0xFFCAD4DE), fontSize = 10.sp
            )
        }
        Text("Display radius: ${safeRadius.toInt()}", color = Color.White, fontSize = 12.sp)
        Slider(value = radius, onValueChange = { radius = it }, valueRange = 40f..240f)
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            FilterChip(selected = showNames, onClick = { showNames = !showNames }, label = { Text("Names") })
            FilterChip(selected = showHealth, onClick = { showHealth = !showHealth }, label = { Text("Health") })
            FilterChip(selected = showVehicles, onClick = { showVehicles = !showVehicles }, label = { Text("Vehicles") })
        }
    }
}
