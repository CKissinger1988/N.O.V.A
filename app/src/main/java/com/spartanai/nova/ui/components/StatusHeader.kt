package com.spartanai.nova.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spartanai.nova.core.NovaOrchestrator
import com.spartanai.nova.ui.theme.*

@Composable
fun StatusHeader() {
    val orchestrator = NovaOrchestrator.getInstance()
    val systemStatus by orchestrator.systemStatus.collectAsState()
    val dmsTime by orchestrator.dmsRemainingTime.collectAsState()

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = HoloBlackTrans,
        border = androidx.compose.foundation.BorderStroke(0.5.dp, HoloCyanTrans)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            // Top Row: Performance Metrics
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatusIndicator(label = "CPU", value = "${systemStatus.cpuUsage}%", icon = Icons.Default.Memory, color = HoloCyan)
                StatusIndicator(label = "RAM", value = "${systemStatus.ramUsage}%", icon = Icons.Default.Dns, color = HoloCyan)
                StatusIndicator(label = "BATT", value = "${systemStatus.batteryLevel}%", icon = Icons.Default.BatteryChargingFull, color = if (systemStatus.batteryLevel < 20) HoloRed else HoloGreen)
                StatusIndicator(label = "NODES", value = "${systemStatus.nodeCount}", icon = Icons.Default.Hub, color = HoloBlue)
                
                if (dmsTime > 0) {
                    val minutes = dmsTime / 60000
                    val seconds = (dmsTime % 60000) / 1000
                    StatusIndicator(
                        label = "DMS", 
                        value = "%02d:%02d".format(minutes, seconds), 
                        icon = Icons.Default.Timer, 
                        color = if (dmsTime < 30000) HoloRed else HoloYellow
                    )
                }
                
                StatusIndicator(label = "THREAT", value = systemStatus.threatLevel, icon = Icons.Default.Warning, color = if (systemStatus.threatLevel == "LOW") HoloGreen else HoloRed)
                
                IconButton(onClick = { orchestrator.scanNetwork() }, modifier = Modifier.size(24.dp)) {
                    Icon(Icons.Default.Refresh, contentDescription = "Scan Network", tint = HoloCyan, modifier = Modifier.size(16.dp))
                }
            }


            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), color = HoloCyanTrans, thickness = 0.5.dp)

            // Bottom Row: Network & Security
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SecurityTag(label = "VPN", isActive = systemStatus.vpnActive, icon = Icons.Default.VpnLock)
                SecurityTag(label = "PROXY", isActive = systemStatus.proxyActive, icon = Icons.Default.Security)
                Text(
                    text = "C2: ${systemStatus.c2LinkStatus}",
                    color = if (systemStatus.c2LinkStatus.contains("CONNECTED")) HoloBlue else Color.Gray,
                    fontSize = 10.sp,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun StatusIndicator(label: String, value: String, icon: ImageVector, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = color.copy(alpha = 0.8f), modifier = Modifier.size(14.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Column {
            Text(label, color = Color.Gray, fontSize = 8.sp)
            Text(value, color = color, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun SecurityTag(label: String, isActive: Boolean, icon: ImageVector) {
    val color = if (isActive) HoloGreen else Color.DarkGray
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(12.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text(label, color = color, fontSize = 9.sp, fontWeight = FontWeight.SemiBold)
    }
}


