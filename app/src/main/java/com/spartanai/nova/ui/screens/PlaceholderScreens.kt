package com.spartanai.nova.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.spartanai.nova.core.NovaOrchestrator

@Composable
fun KaliScreen() {
    val orchestrator = NovaOrchestrator.getInstance()
    val isKaliActive by orchestrator.isKaliActive.collectAsState()

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Kali NetHunter Environment", style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = { orchestrator.executeCommand("kali toggle") }) {
                Text(if (isKaliActive) "DEACTIVATE" else "ACTIVATE")
            }
        }
    }
}