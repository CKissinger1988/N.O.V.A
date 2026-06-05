package com.spartanai.nova.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.Alignment
import com.spartanai.nova.core.NovaOrchestrator
import com.spartanai.nova.ui.theme.*

@Composable
fun TerminalScreen() {
    val orchestrator = NovaOrchestrator.getInstance()
    val terminalOutput by orchestrator.terminalOutput.collectAsState()
    val systemStatus by orchestrator.systemStatus.collectAsState()
    val listState = rememberLazyListState()
    var currentCommand by remember { mutableStateOf("") }

    LaunchedEffect(terminalOutput.size) {
        if (terminalOutput.isNotEmpty()) {
            listState.animateScrollToItem(terminalOutput.size - 1)
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(HoloBlack)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(), 
                horizontalArrangement = Arrangement.SpaceBetween, 
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                TextButton(onClick = { orchestrator.clearOutput() }) {
                    Text("PURGE LOGS", color = HoloBlue, fontSize = 10.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                }
                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.VpnLock,
                        contentDescription = "SecCom Active",
                        tint = HoloGreen,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        "SECCOM: ${systemStatus.networkEncryption}",
                        color = HoloGreen,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(start = 4.dp),
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                state = listState
            ) {
                items(terminalOutput) { line ->
                    val color = when {
                        line.contains("[ERROR]") -> HoloRed
                        line.contains("[SUCCESS]") || line.contains("[SCAN]") -> HoloCyan
                        line.contains("[SYSTEM]") || line.contains("[OMEGA]") -> HoloYellow
                        line.contains("[AI]") -> Color(0xFFBB86FC)
                        line.startsWith("nova@system") -> HoloWhite
                        else -> HoloGreen
                    }
                    Text(
                        text = line,
                        color = color,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(vertical = 1.dp)
                    )
                }
            }


            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = currentCommand,
                onValueChange = { currentCommand = it },
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.bodySmall.copy(
                    color = HoloCyan,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                ),
                placeholder = { Text("Enter directive...", color = Color.Gray, fontSize = 12.sp) },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
                keyboardActions = KeyboardActions(
                    onGo = {
                        if (currentCommand.isNotBlank()) {
                            orchestrator.executeCommand(currentCommand)
                            currentCommand = ""
                        }
                    }
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = HoloCyan,
                    unfocusedBorderColor = HoloBlue.copy(alpha = 0.3f),
                    cursorColor = HoloCyan
                ),
                singleLine = true,
                trailingIcon = {
                    IconButton(onClick = {
                        if (currentCommand.isNotBlank()) {
                            orchestrator.executeCommand(currentCommand)
                            currentCommand = ""
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Run",
                            tint = HoloCyan
                        )
                    }

                }
            )
        }

        // Floating Tactical HUD
        Card(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .width(140.dp)
                .graphicsLayer { alpha = 0.7f },
            colors = CardDefaults.cardColors(containerColor = HoloDarkBlue.copy(alpha = 0.6f)),
            border = androidx.compose.foundation.BorderStroke(1.dp, HoloCyan.copy(alpha = 0.4f))
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text("TACTICAL HUD", style = MaterialTheme.typography.labelSmall, color = HoloBlue)
                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), color = HoloCyanTrans)
                HudItem("C2", systemStatus.c2LinkStatus, if (systemStatus.c2LinkStatus.contains("CONNECTED")) HoloGreen else HoloRed)
                HudItem("THREAT", systemStatus.threatLevel, if (systemStatus.threatLevel == "HIGH") HoloRed else HoloGreen)
                HudItem("VPN", if (systemStatus.vpnActive) "ACTIVE" else "OFF", if (systemStatus.vpnActive) HoloGreen else HoloRed)
            }
        }
    }
}


@Composable
fun HudItem(label: String, value: String, color: Color) {
    Column(modifier = Modifier.padding(vertical = 2.dp)) {
        Text(label, fontSize = 8.sp, color = Color.Gray)
        Text(value, fontSize = 9.sp, color = color, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, maxLines = 1)
    }
}

