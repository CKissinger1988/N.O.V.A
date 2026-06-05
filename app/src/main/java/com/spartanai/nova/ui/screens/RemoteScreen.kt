package com.spartanai.nova.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spartanai.nova.core.NovaOrchestrator

@Composable
fun RemoteScreen() {
    val orchestrator = NovaOrchestrator.getInstance()
    var isStreaming by remember { mutableStateOf(false) }
    var targetInfo by remember { mutableStateOf("NOT CONNECTED") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Remote Screen & Control", style = MaterialTheme.typography.headlineMedium)
        Text("Stealth Mirroring & Input Injection", style = MaterialTheme.typography.bodySmall)

        Spacer(modifier = Modifier.height(16.dp))

        // Remote Viewport Overlay
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .background(Color.Black)
                .border(2.dp, if (isStreaming) Color.Green else Color.DarkGray),
            contentAlignment = Alignment.Center
        ) {
            if (isStreaming) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Android, contentDescription = null, tint = Color.Green, modifier = Modifier.size(64.dp))
                    Text("LIVE STREAM: $targetInfo", color = Color.Green, fontWeight = FontWeight.Bold)
                }
            } else {
                Text("Waiting for connection...", color = Color.Gray)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Control Panel
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Input Injection Controls", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    IconButton(onClick = { orchestrator.executeCommand("input keyevent 4") }) { // BACK
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                    IconButton(onClick = { orchestrator.executeCommand("input keyevent 3") }) { // HOME
                        Icon(Icons.Default.Home, contentDescription = "Home")
                    }
                    IconButton(onClick = { orchestrator.executeCommand("input keyevent 187") }) { // RECENT
                        Icon(Icons.Default.Apps, contentDescription = "Recent")
                    }
                    IconButton(onClick = { orchestrator.executeCommand("input tap 500 500") }) { // CLICK
                        Icon(Icons.Default.TouchApp, contentDescription = "Tap")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(
                onClick = { 
                    isStreaming = !isStreaming
                    if (isStreaming) {
                        targetInfo = "192.168.1.100"
                        orchestrator.executeCommand("screen mirror start")
                        orchestrator.speak("Remote screen mirroring engaged.")
                    } else {
                        orchestrator.executeCommand("screen mirror stop")
                        orchestrator.speak("Remote screen mirroring terminated.")
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isStreaming) Color.Red else MaterialTheme.colorScheme.primary
                )
            ) {
                Text(if (isStreaming) "TERMINATE" else "ESTABLISH LINK")
            }

            Button(onClick = { orchestrator.executeCommand("screen record") }) {
                Icon(Icons.Default.FiberManualRecord, contentDescription = null, tint = Color.Red)
                Spacer(modifier = Modifier.width(8.dp))
                Text("RECORD")
            }
        }
    }
}
