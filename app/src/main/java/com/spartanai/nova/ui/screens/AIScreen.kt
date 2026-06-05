package com.spartanai.nova.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.spartanai.nova.core.NovaOrchestrator

@Composable
fun AIScreen() {
    val orchestrator = NovaOrchestrator.getInstance()
    var input by remember { mutableStateOf("") }
    val terminalOutput by orchestrator.terminalOutput.collectAsState()
    
    // Filter output for AI related logs
    val aiLogs = terminalOutput.filter { line ->
        val lower = line.lowercase()
        lower.contains("ai agent") || lower.contains("gemini") || lower.contains("claude") || lower.contains("antigravity") || lower.contains("[ai") || lower.contains("tgpt")
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("AI Offensive Command Center", style = MaterialTheme.typography.headlineMedium)
        Text("Powered by Gemini-CLI, Claude-Red & HexStrike", style = MaterialTheme.typography.bodySmall)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(aiLogs) { log ->
                Text(log, color = MaterialTheme.colorScheme.primary)
            }
            if (aiLogs.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Initiate an AI offensive request to see analysis.", color = androidx.compose.ui.graphics.Color.Gray)
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        OutlinedTextField(
            value = input,
            onValueChange = { input = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Ask AI for exploit strategy...") },
            trailingIcon = {
                IconButton(onClick = {
                    if (input.isNotBlank()) {
                        orchestrator.executeCommand("ai $input")
                        input = ""
                    }
                }) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = "Send")
                }
            }
        )
    }
}