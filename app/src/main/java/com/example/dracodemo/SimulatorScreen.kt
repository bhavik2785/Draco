package com.example.dracodemo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

/**
 * The main demo screen: a short intro, a button that toggles the floating
 * overlay panel, and a list of sample [DemoTarget]s whose radius is clamped by
 * the native library.
 */
@Composable
fun SimulatorScreen(
    targets: List<DemoTarget>,
    panelRunning: Boolean,
    onTogglePanel: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Draco Demo",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = "Jetpack Compose UI + a draggable overlay panel + a tiny JNI sample.",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(Modifier.height(16.dp))

        Button(onClick = onTogglePanel, modifier = Modifier.fillMaxWidth()) {
            Text(if (panelRunning) "Stop floating panel" else "Start floating panel")
        }

        Spacer(Modifier.height(16.dp))

        Text(
            text = "Targets",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(Modifier.height(8.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(targets) { target ->
                TargetRow(target)
            }
        }
    }
}

@Composable
private fun TargetRow(target: DemoTarget) {
    // Clamp the radius in native code so each row exercises the JNI bridge.
    val clamped = NativeDemo.clampSafe(target.radius, 0f, 100f)
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(target.name, fontWeight = FontWeight.Medium)
                Text(
                    "distance ${target.distance} • radius ${target.radius}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Text(
                "clamped ${clamped.roundToInt()}",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SimulatorPreview() {
    DracoDemoTheme {
        SimulatorScreen(
            targets = DemoTarget.samples(),
            panelRunning = false,
            onTogglePanel = {}
        )
    }
}
