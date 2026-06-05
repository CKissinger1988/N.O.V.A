package com.spartanai.nova.ui.screens

import androidx.compose.foundation.layout.*
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
fun CommsScreen() {
    val orchestrator = NovaOrchestrator.getInstance()
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Rocket.Chat", "tgpt (AI Chat)")

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Secure Communications", style = MaterialTheme.typography.headlineMedium)
        
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
            0 -> RocketChatSection(orchestrator)
            1 -> TGPTSection(orchestrator)
        }
    }
}

@Composable
fun RocketChatSection(orchestrator: NovaOrchestrator) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
        Icon(Icons.Default.Forum, contentDescription = null, modifier = Modifier.size(80.dp), tint = Color.Red)
        Text("Mission Critical Messaging", style = MaterialTheme.typography.titleLarge)
        Text("Self-hosted Rocket.Chat Bridge", style = MaterialTheme.typography.bodySmall)
        
        Spacer(modifier = Modifier.height(32.dp))
        
        OutlinedTextField(
            value = "https://chat.spartan.apex",
            onValueChange = {},
            label = { Text("Server URL") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(onClick = { orchestrator.executeCommand("comms rocket-connect") }, modifier = Modifier.fillMaxWidth()) {
            Text("ENTER SECURE CHAT")
        }
    }
}

@Composable
fun TGPTSection(orchestrator: NovaOrchestrator) {
    var prompt by remember { mutableStateOf("") }
    
    Column {
        Text("Tactical Terminal GPT", style = MaterialTheme.typography.titleLarge)
        Text("Encrypted AI Assistance via tgpt", style = MaterialTheme.typography.bodySmall)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Card(modifier = Modifier.fillMaxWidth().weight(1f)) {
            Box(modifier = Modifier.fillMaxSize().padding(8.dp)) {
                Text("AI: Waiting for input...", color = Color.Gray)
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        OutlinedTextField(
            value = prompt,
            onValueChange = { prompt = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Ask TGPT...") },
            trailingIcon = {
                IconButton(onClick = { 
                    if (prompt.isNotBlank()) {
                        orchestrator.executeCommand("tgpt $prompt")
                        prompt = ""
                    }
                }) {
                    Icon(Icons.Default.Send, contentDescription = "Ask")
                }
            }
        )
    }
}
