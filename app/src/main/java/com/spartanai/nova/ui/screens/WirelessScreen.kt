package com.spartanai.nova.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spartanai.nova.core.NovaOrchestrator
import com.spartanai.nova.ui.theme.*

@Composable
fun WirelessScreen() {
    val orchestrator = NovaOrchestrator.getInstance()
    val settings by orchestrator.settings.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("BLUETOOTH", "NFC / PROXIMITY")

    Column(modifier = Modifier.fillMaxSize().background(HoloBlack).padding(16.dp)) {
        Text("WIRELESS OFFENSIVE HUB", style = MaterialTheme.typography.headlineMedium, color = HoloCyan)
        Text("Spectrum Analysis & Proximity Exploitation", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        
        Spacer(modifier = Modifier.height(16.dp))

        TabRow(
            selectedTabIndex = selectedTab, 
            containerColor = Color.Transparent,
            contentColor = HoloCyan,
            divider = {},
            indicator = { tabPositions ->
                if (selectedTab < tabPositions.size) {
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = HoloCyan
                    )
                }
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title, fontSize = 11.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold) }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        when (selectedTab) {
            0 -> BluetoothSection(orchestrator, settings)
            1 -> NFCSection(orchestrator, settings)
        }
    }
}

@Composable
fun BluetoothSection(orchestrator: NovaOrchestrator, settings: com.spartanai.nova.data.model.NovaSettings) {
    val devices by orchestrator.discoveredDevices.collectAsState()
    
    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("TARGET DISCOVERY", style = MaterialTheme.typography.labelLarge, color = HoloBlue)
            IconButton(onClick = { orchestrator.executeCommand("bt scan") }) {
                Icon(Icons.Default.Refresh, contentDescription = "Scan", tint = HoloCyan)
            }
        }
        
        Card(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            colors = CardDefaults.cardColors(containerColor = HoloDarkBlue.copy(alpha = 0.3f)),
            border = androidx.compose.foundation.BorderStroke(0.5.dp, HoloCyanTrans)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                SettingsSlider(
                    label = "Signal Threshold (dBm)",
                    value = settings.btSignalThresholdDbm.toFloat(),
                    range = -100f..-30f,
                    onValueChange = { orchestrator.updateSettings(settings.copy(btSignalThresholdDbm = it.toInt())) }
                )
            }
        }

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(devices) { device ->
                val rssi = remember { (-90..-30).random() }
                WirelessTargetItem(
                    name = device.hostname.takeIf { it != "Unknown" } ?: device.ip,
                    details = "MAC: ${device.mac} | RSSI: ${rssi}dBm",
                    onAttack = {
                        orchestrator.executeCommand("bt attack ${device.ip}")
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = { orchestrator.executeCommand("bt pair-flood") },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = HoloCyan.copy(alpha = 0.2f)),
                border = androidx.compose.foundation.BorderStroke(1.dp, HoloCyan)
            ) {
                Text("PAIR FLOOD", fontSize = 10.sp, color = HoloCyan)
            }
            Button(
                onClick = { orchestrator.executeCommand("bt jam") }, 
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = HoloRed.copy(alpha = 0.2f)),
                border = androidx.compose.foundation.BorderStroke(1.dp, HoloRed)
            ) {
                Text("JAM SIGNAL", fontSize = 10.sp, color = HoloRed)
            }
        }
    }
}

@Composable
fun NFCSection(orchestrator: NovaOrchestrator, settings: com.spartanai.nova.data.model.NovaSettings) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
        Text("PROXIMITY TOOLKIT", style = MaterialTheme.typography.labelLarge, color = HoloBlue, modifier = Modifier.align(Alignment.Start))
        Spacer(modifier = Modifier.height(16.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
            colors = CardDefaults.cardColors(containerColor = HoloDarkBlue.copy(alpha = 0.3f)),
            border = androidx.compose.foundation.BorderStroke(0.5.dp, HoloCyanTrans)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                SettingsSwitch(
                    label = "Auto-Clone on Ingestion",
                    checked = settings.nfcAutoClone,
                    onCheckedChange = { orchestrator.updateSettings(settings.copy(nfcAutoClone = it)) }
                )
                SettingsTextField(
                    label = "Emulation Target ID",
                    value = "0xDEADBEEF",
                    onValueChange = {}
                )
            }
        }

        Icon(
            Icons.Default.Contactless, 
            contentDescription = null, 
            modifier = Modifier.size(120.dp).graphicsLayer { alpha = 0.8f }, 
            tint = HoloCyan
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = { orchestrator.executeCommand("nfc read") }, 
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = HoloCyan.copy(alpha = 0.2f)),
            border = androidx.compose.foundation.BorderStroke(1.dp, HoloCyan)
        ) {
            Text("READ / CLONE TAG", color = HoloCyan)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { orchestrator.executeCommand("nfc emulate") }, 
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = HoloBlue.copy(alpha = 0.2f)),
            border = androidx.compose.foundation.BorderStroke(1.dp, HoloBlue)
        ) {
            Text("EMULATE SAVED SIGNATURE", color = HoloBlue)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { orchestrator.executeCommand("nfc fuzz") }, 
            modifier = Modifier.fillMaxWidth(), 
            colors = ButtonDefaults.buttonColors(containerColor = HoloRed.copy(alpha = 0.2f)),
            border = androidx.compose.foundation.BorderStroke(1.dp, HoloRed)
        ) {
            Text("FUZZ READER (BRUTE)", color = HoloRed)
        }
    }
}


@Composable
fun WirelessTargetItem(name: String, details: String, onAttack: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = HoloDarkBlue.copy(alpha = 0.2f)),
        border = androidx.compose.foundation.BorderStroke(0.5.dp, HoloCyanTrans)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(name, style = MaterialTheme.typography.titleSmall, color = Color.White)
                Text(details, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            }
            Button(
                onClick = onAttack, 
                colors = ButtonDefaults.buttonColors(containerColor = HoloRed.copy(alpha = 0.5f)),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                modifier = Modifier.height(32.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, HoloRed)
            ) {
                Text("EXPLOIT", fontSize = 10.sp, color = HoloWhite)
            }
        }
    }
}
