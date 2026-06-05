package com.spartanai.nova.ui.screens

import androidx.compose.animation.core.*
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
import androidx.compose.ui.unit.sp
import com.spartanai.nova.core.NovaOrchestrator
import com.spartanai.nova.ui.theme.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Close
import com.spartanai.nova.data.model.DiscoveredDevice
import androidx.compose.foundation.border
import androidx.compose.ui.Alignment

@Composable
fun WarRoomScreen() {
    val orchestrator = NovaOrchestrator.getInstance()
    val devices by orchestrator.discoveredDevices.collectAsState()
    val settings by orchestrator.settings.collectAsState()
    var selectedDevice by remember { mutableStateOf<DiscoveredDevice?>(null) }
    
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    val displayScale = settings.uiDisplayScale
    val density = androidx.compose.ui.platform.LocalDensity.current
    val touchSlopPx = with(density) { 40.dp.toPx() * displayScale }

    Box(modifier = Modifier.fillMaxSize().background(HoloBlack)) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text("SOVEREIGN TOPOLOGY", style = MaterialTheme.typography.headlineMedium, color = HoloCyan, fontSize = (MaterialTheme.typography.headlineMedium.fontSize.value * displayScale).sp)
            Text("Visualizing ${devices.size} Active Nodes in Local Subnet", style = MaterialTheme.typography.bodySmall, color = Color.Gray, fontSize = (MaterialTheme.typography.bodySmall.fontSize.value * displayScale).sp)
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(HoloBlack)
                    .border(0.5.dp, HoloCyanTrans)
            ) {
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(devices, displayScale) {
                            detectTapGestures { tapOffset ->
                                val center = Offset(size.width / 2f, size.height / 2f)
                                var clickedDevice: DiscoveredDevice? = null
                                devices.forEachIndexed { index, device ->
                                    val angle = (index.toFloat() / devices.size) * 2 * Math.PI
                                    val distance = size.width.coerceAtMost(size.height) * 0.35f
                                    val pos = Offset(
                                        (center.x + Math.cos(angle) * distance).toFloat(),
                                        (center.y + Math.sin(angle) * distance).toFloat()
                                    )
                                    val dx = tapOffset.x - pos.x
                                    val dy = tapOffset.y - pos.y
                                    val dist = Math.sqrt((dx * dx + dy * dy).toDouble()).toFloat()
                                    if (dist <= touchSlopPx) {
                                        clickedDevice = device
                                    }
                                }
                                selectedDevice = clickedDevice
                            }
                        }
                ) {
                    val center = Offset(size.width / 2, size.height / 2)
                    val radius = size.width.coerceAtMost(size.height) * 0.35f
                    
                    // Draw Tactical Grid
                    for (i in 0 until 4) {
                        drawCircle(color = HoloBlue.copy(alpha = 0.1f), radius = radius * (i + 1) / 4f, center = center, style = Stroke(1f))
                    }
                    
                    // Central Node Glow
                    drawCircle(color = HoloCyan.copy(alpha = 0.15f), radius = (50f * pulseScale) * displayScale, center = center)
                    drawCircle(color = HoloCyan, radius = 25f * displayScale, center = center)
                    
                    // Map real devices
                    devices.forEachIndexed { index, device ->
                        val angle = (index.toFloat() / devices.size) * 2 * Math.PI
                        val pos = Offset(
                            (center.x + Math.cos(angle) * radius).toFloat(),
                            (center.y + Math.sin(angle) * radius).toFloat()
                        )
                        
                        val color = when (device.status) {
                            com.spartanai.nova.data.model.DeviceStatus.BREACHED -> HoloRed
                            com.spartanai.nova.data.model.DeviceStatus.SECURE -> HoloGreen
                            else -> HoloYellow
                        }
                        
                        val nodeBaseRadius = 20f * displayScale
                        val nodeRadius = if (selectedDevice?.ip == device.ip) nodeBaseRadius * 1.5f else nodeBaseRadius
                        val finalRadius = if (device.status == com.spartanai.nova.data.model.DeviceStatus.BREACHED) nodeRadius * pulseScale else nodeRadius
                        
                        // Connection Line
                        drawLine(color = color.copy(alpha = 0.3f), start = center, end = pos, strokeWidth = 1f)
                        
                        // Node
                        drawCircle(color = color, radius = finalRadius, center = pos)
                        if (selectedDevice?.ip == device.ip) {
                            drawCircle(color = color.copy(alpha = 0.4f), radius = finalRadius + (10f * displayScale), center = pos, style = Stroke(2f))
                        }
                    }
                }

                
                // Node Inspection Overlay
                selectedDevice?.let { device ->
                    NodeInspectionPanel(device, onDismiss = { selectedDevice = null })
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Button(
                    onClick = { orchestrator.executeCommand("war-room scan") },
                    colors = ButtonDefaults.buttonColors(containerColor = HoloCyan.copy(alpha = 0.2f)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, HoloCyan)
                ) {
                    Text("REFRESH TOPOLOGY", color = HoloCyan, fontSize = 10.sp)
                }
                Button(
                    onClick = { orchestrator.executeCommand("omega trigger") }, 
                    colors = ButtonDefaults.buttonColors(containerColor = HoloRed.copy(alpha = 0.2f)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, HoloRed)
                ) {
                    Text("OMEGA PROTOCOL", color = HoloRed, fontSize = 10.sp)
                }
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
                Text("Node Inspection", style = MaterialTheme.typography.titleMedium, color = HoloBlue)
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.Gray)
                }
            }
            
            HorizontalDivider(color = Color.DarkGray, thickness = 1.dp)
            Spacer(modifier = Modifier.height(8.dp))
            
            InspectionRow("HOSTNAME", device.hostname)
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
                Button(onClick = { /* Launch Exploit */ }, colors = ButtonDefaults.buttonColors(containerColor = HoloRed)) {
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
