package com.spartanai.nova.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.spartanai.nova.core.NovaOrchestrator

@Composable
fun RemoteADBScreen() {
    val orchestrator = NovaOrchestrator.getInstance()
    var targetIp by remember { mutableStateOf("") }
    val adbTargets = listOf("192.168.1.50", "10.0.0.12", "172.16.0.5") // Mock discovered targets

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("RemoteADB Command Center", style = MaterialTheme.typography.headlineMedium)
        Text("Unified Android Debug Bridge Controller", style = MaterialTheme.typography.bodySmall)
        
        Spacer(modifier = Modifier.height(24.dp))
        
        OutlinedTextField(
            value = targetIp,
            onValueChange = { targetIp = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Target IP Address") },
            placeholder = { Text("e.g. 192.168.1.100") },
            trailingIcon = {
                IconButton(onClick = { 
                    if (targetIp.isNotBlank()) {
                        orchestrator.executeCommand("adb connect $targetIp")
                    }
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Connect")
                }
            }
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text("Discovered Targets", style = MaterialTheme.typography.titleMedium)
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(adbTargets) { ip ->
                ADBTargetItem(ip, onConnect = { orchestrator.executeCommand("adb connect $ip") })
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            ActionButton(text = "SCAN", icon = Icons.Default.Radar) {
                orchestrator.executeCommand("adb scan")
            }
            ActionButton(text = "DEVICES", icon = Icons.Default.List) {
                orchestrator.executeCommand("adb devices")
            }
            ActionButton(text = "KILL", icon = Icons.Default.Close, color = MaterialTheme.colorScheme.error) {
                orchestrator.executeCommand("adb kill-server")
            }
        }
    }
}

@Composable
fun ADBTargetItem(ip: String, onConnect: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Smartphone, contentDescription = null, tint = Color.Green)
                Spacer(modifier = Modifier.width(12.dp))
                Text(ip, style = MaterialTheme.typography.bodyLarge)
            }
            Button(onClick = onConnect) {
                Text("CONNECT")
            }
        }
    }
}

@Composable
fun ActionButton(text: String, icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color = MaterialTheme.colorScheme.primary, onClick: () -> Unit) {
    Button(onClick = onClick, colors = ButtonDefaults.buttonColors(containerColor = color)) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text)
    }
}
