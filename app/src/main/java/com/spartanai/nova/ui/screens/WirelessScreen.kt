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
fun WirelessScreen() {
    val orchestrator = NovaOrchestrator.getInstance()
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Bluetooth", "NFC")

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Wireless Offensive Hub", style = MaterialTheme.typography.headlineMedium)
        
        TabRow(selectedTabIndex = selectedTab, containerColor = Color.Transparent) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        when (selectedTab) {
            0 -> BluetoothSection(orchestrator)
            1 -> NFCSection(orchestrator)
        }
    }
}

@Composable
fun BluetoothSection(orchestrator: NovaOrchestrator) {
    val devices by orchestrator.discoveredDevices.collectAsState()
    
    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Network Discovery", style = MaterialTheme.typography.titleLarge)
            IconButton(onClick = { orchestrator.executeCommand("bt scan") }) {
                Icon(Icons.Default.Refresh, contentDescription = "Scan")
            }
        }
        
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(devices) { device ->
                WirelessTargetItem("${device.ip} (${device.mac})", Icons.Default.Devices, onAttack = {
                    orchestrator.executeCommand("bt attack ${device.ip}")
                })
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(onClick = { orchestrator.executeCommand("bt pair-flood") }) {
                Text("Pair Flood")
            }
            Button(onClick = { orchestrator.executeCommand("bt jam") }, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)) {
                Text("Jam Signal")
            }
        }
    }
}

@Composable
fun NFCSection(orchestrator: NovaOrchestrator) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
        Text("NFC / RFID Proximity Tools", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(32.dp))
        
        Icon(Icons.Default.Contactless, contentDescription = null, modifier = Modifier.size(100.dp), tint = Color.Cyan)
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(onClick = { orchestrator.executeCommand("nfc read") }, modifier = Modifier.fillMaxWidth()) {
            Text("Read / Clone Tag")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { orchestrator.executeCommand("nfc emulate") }, modifier = Modifier.fillMaxWidth()) {
            Text("Emulate Saved Tag")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { orchestrator.executeCommand("nfc fuzz") }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)) {
            Text("Fuzz Target Reader")
        }
    }
}

@Composable
fun WirelessTargetItem(name: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onAttack: () -> Unit) {
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
                Icon(icon, contentDescription = null, tint = Color.Cyan)
                Spacer(modifier = Modifier.width(12.dp))
                Text(name, style = MaterialTheme.typography.bodyLarge)
            }
            Button(onClick = onAttack, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)) {
                Text("EXPLOIT")
            }
        }
    }
}
