package com.spartanai.nova.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.spartanai.nova.core.NovaOrchestrator
import com.spartanai.nova.ui.theme.NovaBlue
import com.spartanai.nova.ui.theme.NovaGreen
import kotlinx.coroutines.delay

@Composable
fun SpectrumScreen() {
    val orchestrator = NovaOrchestrator.getInstance()
    var packets by remember { mutableStateOf(listOf<Float>()) }

    LaunchedEffect(Unit) {
        while (true) {
            packets = List(20) { (0..100).random().toFloat() }
            delay(500)
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Packet Spectrum Analysis", style = MaterialTheme.typography.headlineMedium)
        Text("Visual Sniffer & Deep-Packet Intelligence", style = MaterialTheme.typography.bodySmall)

        Spacer(modifier = Modifier.height(16.dp))

        Box(modifier = Modifier.weight(1f).fillMaxWidth().background(Color.Black)) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val barWidth = size.width / packets.size
                packets.forEachIndexed { index, value ->
                    val color = if (value > 80f) NovaBlue else NovaGreen
                    drawRect(
                        color = color,
                        topLeft = androidx.compose.ui.geometry.Offset(index * barWidth, size.height - (value * 5)),
                        size = androidx.compose.ui.geometry.Size(barWidth - 4f, value * 5)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("AI Traffic Tags", style = MaterialTheme.typography.titleMedium)
                Text("• AUTHORIZATION: BEARER detected in flow #12", color = NovaBlue)
                Text("• UNENCRYPTED SQL detected on 10.0.0.5", color = Color.Yellow)
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(onClick = { orchestrator.executeCommand("spectrum capture start") }, modifier = Modifier.fillMaxWidth()) {
            Text("START PCAP CAPTURE")
        }
    }
}
