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

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Close
import com.spartanai.nova.data.model.DiscoveredDevice

@Composable
fun WarRoomScreen() {
    val orchestrator = NovaOrchestrator.getInstance()
    val devices by orchestrator.discoveredDevices.collectAsState()
    var selectedDevice by remember { mutableStateOf<DiscoveredDevice?>(null) }
    
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("War Room: Global Topology", style = MaterialTheme.typography.headlineMedium)
        Text("Real-time Visual Attack Surface: ${devices.size} Nodes", style = MaterialTheme.typography.bodySmall)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Box(modifier = Modifier.weight(1f).fillMaxWidth().background(Color.Black)) {
            Canvas(modifier = Modifier.fillMaxSize().clickable {
                // Mock click detection - in real app would use tap offset
                if (devices.isNotEmpty()) selectedDevice = devices.random()
            }) {
                val center = Offset(size.width / 2, size.height / 2)
                
                // Draw Network Rings
                drawCircle(color = Color.DarkGray, radius = 200f, center = center, style = Stroke(1f))
                drawCircle(color = Color.DarkGray, radius = 400f, center = center, style = Stroke(1f))
                
                // Central Node (Android Device)
                drawCircle(color = NovaBlue, radius = 20f, center = center)
                
                // Map real devices to positions
                devices.forEachIndexed { index, device ->
                    val angle = (index.toFloat() / devices.size) * 2 * Math.PI
                    val distance = 300f
                    val pos = Offset(
                        (center.x + Math.cos(angle) * distance).toFloat(),
                        (center.y + Math.sin(angle) * distance).toFloat()
                    )
                    
                    val color = when (device.status) {
                        com.spartanai.nova.data.model.DeviceStatus.BREACHED -> NovaRed
                        com.spartanai.nova.data.model.DeviceStatus.SECURE -> NovaGreen
                        else -> Color.Yellow
                    }
                    
                    // Highlight selected device
                    val radius = if (selectedDevice?.ip == device.ip) 25f else 15f
                    val stroke = if (selectedDevice?.ip == device.ip) 4f else 2f
                    
                    drawLine(color = color.copy(alpha = 0.5f), start = center, end = pos, strokeWidth = stroke)
                    drawCircle(color = color, radius = radius, center = pos)
                }
            }
            
            // Node Inspection Overlay
            selectedDevice?.let { device ->
                NodeInspectionPanel(device, onDismiss = { selectedDevice = null })
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

@Composable
fun NodeInspectionPanel(device: DiscoveredDevice, onDismiss: () -> Unit) {
    Card(
        modifier = Modifier.padding(16.dp).fillMaxWidth().wrapContentHeight(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Node Inspection", style = MaterialTheme.typography.titleMedium, color = NovaBlue)
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.Gray)
                }
            }
            
            Divider(color = Color.DarkGray, thickness = 1.dp)
            Spacer(modifier = Modifier.height(8.dp))
            
            InspectionRow("IP ADDR", device.ip)
            InspectionRow("MAC ADDR", device.mac)
            InspectionRow("STATUS", device.status.name)
            InspectionRow("VENDOR", device.vendor)
            InspectionRow("GUESSED OS", device.guessedOS)
            InspectionRow("OPEN PORTS", if (device.openPorts.isEmpty()) "None detected" else device.openPorts.joinToString(", "))
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                val orchestrator = NovaOrchestrator.getInstance()
                TextButton(onClick = { orchestrator.inspectNode(device.ip) }) {
                    Text("PORT SCAN", color = Color.Cyan)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { /* Launch Exploit */ }, colors = ButtonDefaults.buttonColors(containerColor = NovaRed)) {
                    Text("EXPLOIT")
                }
            }
        }
    }
}

@Composable
fun InspectionRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        Text(value, style = MaterialTheme.typography.bodySmall, color = Color.White)
    }
}
