package com.spartanai.nova.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import com.spartanai.nova.core.NovaOrchestrator
import com.spartanai.nova.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun SpectrumScreen() {
    val orchestrator = NovaOrchestrator.getInstance()
    val settings by orchestrator.settings.collectAsState()
    var packets by remember { mutableStateOf(List(40) { 0f }) }
    var waterfall by remember { mutableStateOf(List(20) { List(40) { 0f } }) }

    // Generative Logic: High-entropy signal noise for deep visualization
    // In a real device with root, we'd pull from /dev/radio or similar
    LaunchedEffect(Unit) {
        while (true) {
            val nextPackets = List(40) { 
                val base = (0..100).random().toFloat()
                if (it % 8 == 0) (base + 40f).coerceAtMost(100f) else base
            }
            packets = nextPackets
            waterfall = (listOf(nextPackets) + waterfall).take(20)
            delay(200)
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(HoloBlack)) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text("TACTICAL SPECTRUM HUB", style = MaterialTheme.typography.headlineMedium, color = HoloCyan)
            Text("Wideband Signal Intelligence & Packet Density", style = MaterialTheme.typography.bodySmall, color = Color.Gray)

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(HoloBlack)
                    .border(0.5.dp, HoloCyanTrans)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val barWidth = size.width / packets.size
                    val waterfallHeight = size.height * 0.75f
                    val realTimeHeight = size.height * 0.25f

                    // Draw Waterfall (Historical Intelligence)
                    waterfall.forEachIndexed { rowIndex, row ->
                        row.forEachIndexed { colIndex, value ->
                            val color = when {
                                value > 90f -> HoloRed
                                value > 75f -> HoloCyan
                                value > 50f -> HoloBlue
                                else -> HoloBlue.copy(alpha = 0.2f)
                            }
                            val cellHeight = waterfallHeight / 20f
                            drawRect(
                                color = color.copy(alpha = 1f - (rowIndex / 20f)),
                                topLeft = Offset(colIndex * barWidth, rowIndex * cellHeight),
                                size = Size(barWidth - 1f, cellHeight - 1f)
                            )
                        }
                    }

                    // Draw Real-time Power Spectrum
                    packets.forEachIndexed { index, value ->
                        val color = if (value > 85f) HoloRed else HoloCyan
                        val barHeight = (value / 100f) * realTimeHeight
                        drawRect(
                            color = color,
                            topLeft = Offset(index * barWidth, size.height - barHeight),
                            size = Size(barWidth - 2f, barHeight)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Active Intelligence Tags
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = HoloDarkBlue.copy(alpha = 0.3f)),
                border = androidx.compose.foundation.BorderStroke(1.dp, HoloBlueTrans)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("ACTIVE SIGNAL TAGS", style = MaterialTheme.typography.labelSmall, color = HoloBlue)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("• DETECTED: Wideband pulse on 2.483GHz", color = HoloCyan, fontSize = 10.sp, fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace)
                    Text("• WARNING: Cryptographic weak IV identified", color = HoloYellow, fontSize = 10.sp, fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace)
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Button(
                onClick = { orchestrator.executeCommand("spectrum capture start") }, 
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = HoloCyan.copy(alpha = 0.2f)),
                border = androidx.compose.foundation.BorderStroke(1.dp, HoloCyan)
            ) {
                Text("ENGAGE WIDEBAND CAPTURE", color = HoloCyan, fontSize = 11.sp)
            }
        }
    }
}



