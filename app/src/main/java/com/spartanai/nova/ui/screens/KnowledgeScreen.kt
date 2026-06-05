package com.spartanai.nova.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.spartanai.nova.core.NovaOrchestrator
import com.spartanai.nova.data.model.ExploitCommand
import com.spartanai.nova.data.model.RiskLevel

@Composable
fun KnowledgeScreen() {
    val orchestrator = NovaOrchestrator.getInstance()
    val knowledgeBase by orchestrator.knowledgeBase.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Exploit Knowledge Base", style = MaterialTheme.typography.headlineMedium)
            IconButton(onClick = { orchestrator.executeCommand("scrape") }) {
                Icon(Icons.Default.CloudDownload, contentDescription = "Sync Global")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        
        LazyColumn {
            items(knowledgeBase) { exploit ->
                ExploitItem(exploit)
            }
        }
    }
}

@Composable
fun ExploitItem(exploit: ExploitCommand) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(exploit.name, style = MaterialTheme.typography.titleMedium)
            Text(exploit.description, style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Command: ${exploit.command}", style = MaterialTheme.typography.bodyMedium)
            
            val color = when(exploit.riskLevel) {
                RiskLevel.LOW -> androidx.compose.ui.graphics.Color.Green
                RiskLevel.MEDIUM -> androidx.compose.ui.graphics.Color.Yellow
                RiskLevel.HIGH -> androidx.compose.ui.graphics.Color.Red
                RiskLevel.CRITICAL -> androidx.compose.ui.graphics.Color.Magenta
            }
            
            AssistChip(
                onClick = {},
                label = { Text(exploit.riskLevel.name) },
                colors = AssistChipDefaults.assistChipColors(labelColor = color)
            )
        }
    }
}
