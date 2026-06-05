package com.spartanai.nova.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.spartanai.nova.core.NovaOrchestrator
import com.spartanai.nova.ui.theme.NovaGreen
import com.spartanai.nova.ui.theme.NovaRed
import com.spartanai.nova.ui.theme.NovaBlue

@Composable
fun WarRoomScreen() {
    val orchestrator = NovaOrchestrator.getInstance()
    
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("War Room: Global Topology", style = MaterialTheme.typography.headlineMedium)
        Text("Real-time Visual Attack Surface", style = MaterialTheme.typography.bodySmall)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Box(modifier = Modifier.weight(1f).fillMaxWidth().background(Color.Black)) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val center = Offset(size.width / 2, size.height / 2)
                
                // Draw Network Rings
                drawCircle(color = Color.DarkGray, radius = 200f, center = center, style = Stroke(1f))
                drawCircle(color = Color.DarkGray, radius = 400f, center = center, style = Stroke(1f))
                
                // Central Node (Android Device)
                drawCircle(color = NovaBlue, radius = 20f, center = center)
                
                // Target Nodes (Mock)
                val targets = listOf(
                    Offset(center.x + 150f, center.y - 100f) to NovaRed, // Breached
                    Offset(center.x - 200f, center.y + 150f) to NovaGreen, // Secure
                    Offset(center.x + 50f, center.y + 300f) to Color.Yellow // Scanning
                )
                
                targets.forEach { (pos, color) ->
                    drawLine(color = color.copy(alpha = 0.5f), start = center, end = pos, strokeWidth = 2f)
                    drawCircle(color = color, radius = 15f, center = pos)
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(onClick = { orchestrator.executeCommand("war-room scan") }) {
                Text("RESCAN")
            }
            Button(onClick = { orchestrator.executeCommand("omega trigger") }, colors = ButtonDefaults.buttonColors(containerColor = NovaRed)) {
                Text("OMEGA PROTOCOL")
            }
        }
    }
}
