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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.spartanai.nova.core.NovaOrchestrator
import com.spartanai.nova.ui.theme.*

@Composable
fun AIScreen() {
    val orchestrator = NovaOrchestrator.getInstance()
    val settings by orchestrator.settings.collectAsState()
    val systemStatus by orchestrator.systemStatus.collectAsState()
    val tacticalAdvisory by orchestrator.tacticalAdvisory.collectAsState()
    val learningProgress by orchestrator.getLearningProgress().collectAsState()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Text("AI TACTICAL HUB", style = MaterialTheme.typography.headlineMedium, color = HoloCyan)
        Text("Autonomous Intelligence & Strategic Analysis", style = MaterialTheme.typography.bodySmall, color = Color.Gray)

        Spacer(modifier = Modifier.height(24.dp))

        // Cortex Status Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = HoloDarkBlue.copy(alpha = 0.4f)),
            border = androidx.compose.foundation.BorderStroke(1.dp, HoloCyanTrans)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("LOCAL CORTEX STATUS", style = MaterialTheme.typography.labelMedium, color = HoloBlue)
                    Text("EVOLUTION: ${(learningProgress * 100).toInt()}%", style = MaterialTheme.typography.labelSmall, color = HoloGreen)
                }
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { learningProgress },
                    modifier = Modifier.fillMaxWidth().height(4.dp),
                    color = HoloCyan,
                    trackColor = HoloBlueTrans,
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "ACTIVE MODEL: ${settings.aiModel}",
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                    color = HoloWhite
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Manual Cortex Controls
        Text("MANUAL CORTEX OVERRIDE", style = MaterialTheme.typography.labelLarge, color = HoloCyan)
        Spacer(modifier = Modifier.height(8.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = HoloDarkBlueTrans),
            border = androidx.compose.foundation.BorderStroke(0.5.dp, HoloCyanTrans)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                SettingsSlider(
                    label = "Cortex Temperature",
                    value = settings.aiTemperature,
                    onValueChange = { orchestrator.updateSettings(settings.copy(aiTemperature = it)) }
                )
                SettingsSlider(
                    label = "Top-P (Nucleus)",
                    value = settings.aiTopP,
                    onValueChange = { orchestrator.updateSettings(settings.copy(aiTopP = it)) }
                )
                SettingsTextField(
                    label = "System Instruction Protocol",
                    value = settings.aiSystemInstruction,
                    onValueChange = { orchestrator.updateSettings(settings.copy(aiSystemInstruction = it)) }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Tactical Briefing
        Text("STRATEGIC ADVISORY", style = MaterialTheme.typography.labelLarge, color = HoloCyan)
        Spacer(modifier = Modifier.height(8.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = HoloBlueTrans.copy(alpha = 0.1f)),
            border = androidx.compose.foundation.BorderStroke(1.dp, HoloBlueTrans)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = tacticalAdvisory,
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                    color = HoloWhite
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = { orchestrator.executeCommand("nova analyze target") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = HoloCyan.copy(alpha = 0.2f)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, HoloCyan)
                ) {
                    Text("INITIATE DEEP RECON ANALYSIS", color = HoloCyan, fontSize = 10.sp)
                }
            }
        }
    }
}