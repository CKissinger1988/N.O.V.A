package com.spartanai.nova.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.background
import com.spartanai.nova.core.NovaOrchestrator
import androidx.navigation.NavController
import androidx.compose.foundation.clickable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Alignment
import androidx.compose.foundation.BorderStroke
import com.spartanai.nova.ui.navigation.Screen
import com.spartanai.nova.ui.theme.*

private data class SubScreenItem(
    val screen: Screen,
    val title: String,
    val description: String,
    val color: Color
)

@Composable
fun ToolsScreen(navController: NavController) {
    val orchestrator = NovaOrchestrator.getInstance()
    val settings by orchestrator.settings.collectAsState()
    
    val subScreens = listOf(
        SubScreenItem(Screen.AI, "AI Hub", "Cortex parameters & advisories", HoloGreen),
        SubScreenItem(Screen.WarRoom, "War Room", "Sovereign topology map", HoloRed),
        SubScreenItem(Screen.Wireless, "Wireless", "Signal & proximity hub", HoloCyan),
        SubScreenItem(Screen.Phishing, "Phish Portal", "Sovereign token harvester", HoloYellow),
        SubScreenItem(Screen.RemoteADB, "Remote ADB", "Android debug controller", HoloBlue),
        SubScreenItem(Screen.Spectrum, "Spectrum", "Signal intelligence waterfall", HoloBlue)
    )
    
    Column(modifier = Modifier.fillMaxSize().background(HoloBlack).padding(16.dp).verticalScroll(rememberScrollState())) {
        Text("OFFENSIVE DASHBOARD", style = MaterialTheme.typography.headlineMedium, color = HoloCyan)
        Text("Deploy autonomous mission modules and exploit chains.", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        
        Spacer(modifier = Modifier.height(24.dp))

        // Sub-Navigation Grid
        Text("MISSION CRITICAL SYSTEMS", style = MaterialTheme.typography.labelLarge, color = HoloBlue)
        Spacer(modifier = Modifier.height(12.dp))
        
        subScreens.chunked(2).forEach { rowItems ->
            Row(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                rowItems.forEachIndexed { index, item ->
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = if (index == 0) 4.dp else 0.dp, start = if (index == 1) 4.dp else 0.dp)
                            .clickable { navController.navigate(item.screen.route) },
                        border = BorderStroke(1.dp, item.color.copy(alpha = 0.4f)),
                        colors = CardDefaults.cardColors(
                            containerColor = HoloDarkBlue.copy(alpha = 0.4f)
                        )
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(item.screen.icon, contentDescription = null, tint = item.color, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(item.title.uppercase(), style = MaterialTheme.typography.labelMedium, color = item.color, maxLines = 1)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(item.description, style = MaterialTheme.typography.labelSmall, color = Color.Gray, maxLines = 2)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text("OFFENSIVE TOOLKIT", style = MaterialTheme.typography.labelLarge, color = HoloCyan)
        Spacer(modifier = Modifier.height(16.dp))
        
        ToolCategory("Global Exploitation", Icons.Default.Public, listOf("Metasploit", "Impacket", "Sliver", "phpsploit", "Avocado"))
        ToolCategory("Autonomous Core", Icons.Default.Bolt, listOf("STEPP", "Omni", "GodShard", "Assimilator"))
        ToolCategory("Adversarial AI", Icons.Default.PrecisionManufacturing, listOf("AATMF", "Claude-Red", "AATMF-Toolkit"))
        ToolCategory("Credential Harvesting", Icons.Default.Fingerprint, listOf("Harvest Local", "Harvest Remote", "Crypto Harvest", "Shadow Loot"))
        ToolCategory("Wireless Dominance", Icons.Default.Wifi, listOf("Wifite2", "Bettercap", "Rogue AP", "Karma"))
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(
            onClick = { orchestrator.executeCommand("tools update") },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = HoloCyan.copy(alpha = 0.2f)),
            border = androidx.compose.foundation.BorderStroke(1.dp, HoloCyan)
        ) {
            Icon(Icons.Default.Refresh, contentDescription = null, tint = HoloCyan)
            Spacer(modifier = Modifier.width(8.dp))
            Text("SYNC MISSION PACKAGES", color = HoloCyan, fontSize = 11.sp)
        }
    }
}

@Composable
fun ToolCategory(name: String, icon: ImageVector, tools: List<String>) {
    Text(name, style = MaterialTheme.typography.labelMedium, color = HoloBlue)
    Spacer(modifier = Modifier.height(8.dp))
    androidx.compose.foundation.layout.FlowRow(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
        tools.forEach { tool ->
            AssistChip(
                onClick = { NovaOrchestrator.getInstance().executeCommand("tool $tool") },
                label = { Text(tool.uppercase(), fontSize = 9.sp, color = HoloWhite) },
                modifier = Modifier.padding(end = 4.dp),
                border = null, // Letting theme handle or use direct BorderStroke if needed elsewhere
                colors = AssistChipDefaults.assistChipColors(containerColor = HoloDarkBlue.copy(alpha = 0.4f))
            )

        }
    }
}