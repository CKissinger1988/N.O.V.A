package com.spartanai.nova.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
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
    val terminalOutput by orchestrator.terminalOutput.collectAsState()
    
    val tgptLogs = terminalOutput.filter { line ->
        line.contains("[TGPT]") || line.contains("[AI]") || line.contains("[AI-ADVISORY]")
    }
    val listState = rememberLazyListState()
    
    LaunchedEffect(tgptLogs.size) {
        if (tgptLogs.isNotEmpty()) {
            listState.animateScrollToItem(tgptLogs.size - 1)
        }
    }
    
    Column {
        Text("Tactical Terminal GPT", style = MaterialTheme.typography.titleLarge)
        Text("Encrypted AI Assistance via tgpt", style = MaterialTheme.typography.bodySmall)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Card(modifier = Modifier.fillMaxWidth().weight(1f)) {
            Box(modifier = Modifier.fillMaxSize().padding(8.dp)) {
                if (tgptLogs.isEmpty()) {
                    Text("AI: Waiting for input...", color = Color.Gray)
                } else {
                    LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
                        items(tgptLogs) { log ->
                            Text(
                                text = log,
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        OutlinedTextField(
            value = prompt,
            onValueChange = { prompt = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Ask TGPT...") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
            keyboardActions = KeyboardActions(
                onSend = {
                    if (prompt.isNotBlank()) {
                        orchestrator.executeCommand("tgpt $prompt")
                        prompt = ""
                    }
                }
            ),
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
