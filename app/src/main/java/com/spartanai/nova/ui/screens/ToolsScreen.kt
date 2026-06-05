package com.spartanai.nova.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.spartanai.nova.core.NovaOrchestrator

import androidx.navigation.NavController
import androidx.compose.foundation.clickable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Alignment
import androidx.compose.foundation.BorderStroke
import com.spartanai.nova.ui.navigation.Screen

private data class SubScreenItem(
    val screen: Screen,
    val title: String,
    val description: String,
    val color: Color
)

@Composable
fun ToolsScreen(navController: NavController) {
    val orchestrator = NovaOrchestrator.getInstance()
    
    val subScreens = listOf(
        SubScreenItem(Screen.AI, "AI Hub", "Autonomous AI agents and chat", Color(0xFF4CAF50)),
        SubScreenItem(Screen.WarRoom, "War Room", "Real-time topology & target map", Color(0xFFFF5722)),
        SubScreenItem(Screen.Kali, "Kali NetHunter", "Offensive pentesting & shell", Color(0xFFE53935)),
        SubScreenItem(Screen.RemoteADB, "Remote ADB", "Android device controller", Color(0xFF00ACC1)),
        SubScreenItem(Screen.Wireless, "Wireless Tools", "BLE, NFC & Wifi analysis", Color(0xFF8E24AA)),
        SubScreenItem(Screen.RemoteScreen, "Remote Mirror", "Stealth mirroring & feed", Color(0xFFFB8C00)),
        SubScreenItem(Screen.Comms, "Secure Comms", "Encrypted Rocket.Chat & TGPT", Color(0xFF00897B)),
        SubScreenItem(Screen.Wallet, "Crypto Wallet", "Autonomous transaction hub", Color(0xFFFDD835)),
        SubScreenItem(Screen.Knowledge, "Knowledge Base", "Exploit payload repository", Color(0xFF757575)),
        SubScreenItem(Screen.Spectrum, "Spectrum", "SDR frequency visualization", Color(0xFF3949AB)),
        SubScreenItem(Screen.Phishing, "Phish Portal", "Token harvester and landing", Color(0xFFD81B60))
    )
    
    Column(modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState())) {
        Text("Offensive Hub Dashboard", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.primary)
        Text("Navigate to sub-systems and deploy exploit modules.", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        Spacer(modifier = Modifier.height(16.dp))

        // Sub-Navigation Grid
        Text("Sub-Systems", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(8.dp))
        
        subScreens.chunked(2).forEach { rowItems ->
            Row(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                rowItems.forEachIndexed { index, item ->
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = if (index == 0) 4.dp else 0.dp, start = if (index == 1) 4.dp else 0.dp)
                            .clickable { navController.navigate(item.screen.route) },
                        border = BorderStroke(1.dp, item.color.copy(alpha = 0.5f)),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
                        )
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(item.screen.icon, contentDescription = null, tint = item.color, modifier = Modifier.size(24.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(item.title, style = MaterialTheme.typography.titleSmall, maxLines = 1)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(item.description, style = MaterialTheme.typography.bodySmall, color = Color.Gray, maxLines = 2)
                        }
                    }
                }
                if (rowItems.size < 2) {
                    Spacer(modifier = Modifier.weight(1f).padding(start = 4.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Offensive Toolkit", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        
        ToolCategory("Global Exploitation", Icons.Default.Public, listOf("Metasploit", "Impacket", "Sliver", "phpsploit", "Avocado"))
        ToolCategory("Autonomous Core", Icons.Default.Bolt, listOf("STEPP", "Omni", "GodShard", "Assimilator"))
        ToolCategory("Adversarial AI", Icons.Default.PrecisionManufacturing, listOf("AATMF", "Claude-Red", "AATMF-Toolkit"))
        ToolCategory("Cloud & K8s Security", Icons.Default.CloudQueue, listOf("KubeRoast", "Burp-MCP", "JSB-Miner", "Pacu"))
        ToolCategory("Credential Harvesting", Icons.Default.Fingerprint, listOf("Harvest Local", "Harvest Remote", "Crypto Harvest", "Shadow Loot"))
        ToolCategory("Wireless Dominance", Icons.Default.Wifi, listOf("Wifite2", "Bettercap", "Rogue AP", "Karma"))
        ToolCategory("Vulnerability Scanning", Icons.Default.Radar, listOf("Nuclei", "Nikto", "ZAP"))
        ToolCategory("USB & Hardware", Icons.Default.Usb, listOf("USB Exploit", "Flipper Bridge", "Pineapple", "SDR-IMSI"))
        ToolCategory("Off-Grid Comms", Icons.Default.Radio, listOf("LoRa Mesh", "Meshtastic", "Tor Proxy"))
        ToolCategory("Defensive Mesh", Icons.Default.Shield, listOf("Shield", "FlashGuard", "Honeypot", "Integrity"))
        ToolCategory("OSINT & Recon", Icons.Default.Search, listOf("OSINT-Framework", "darkdump", "OnionSearch"))
        ToolCategory("Database Hacking", Icons.Default.Storage, listOf("NoSQLMap", "bbqsql", "SQLRecon", "sqli-exploiter"))
        ToolCategory("GhostPack (AD/Win)", Icons.Default.VerifiedUser, listOf("Rubeus", "Seatbelt", "Certify", "SharpRoast", "SafetyKatz"))
        ToolCategory("Stealth & LOLBAS", Icons.Default.VisibilityOff, listOf("LOLBAS", "LLOLBAS", "PowerHub", "mat"))
        ToolCategory("Windows Offensive", Icons.Default.Window, listOf("PowerSploit", "Mimikatz", "SharpWMI", "SharpDPAPI"))
        ToolCategory("IoT & Keys", Icons.Default.Memory, listOf("IoTSeeker", "ssh-badkeys", "sslxray"))
        ToolCategory("OSINT & Recon", Icons.Default.Search, listOf("OSINT-Framework", "DnsEnum", "Whois"))
        ToolCategory("Android Exploitation", Icons.Default.BugReport, listOf("Ghost", "AFE", " sundaysec-Exploits"))
        ToolCategory("Remote Access", Icons.Default.Devices, listOf("RemoteADB", "SSH Bridge", "VNC"))
        ToolCategory("Decentralized Net", Icons.Default.Hub, listOf("ZeroNet", "Tor Proxy", "IPFS Node"))
        ToolCategory("Virtualization", Icons.Default.Dns, listOf("WebVM", "Docker Engine", "RootFS"))
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(
            onClick = { orchestrator.executeCommand("tools update") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Refresh, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Update Packages (Termux)")
        }
    }
}

@Composable
fun ToolCategory(name: String, icon: ImageVector, tools: List<String>) {
    Text(name, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.secondary)
    Spacer(modifier = Modifier.height(8.dp))
    Row(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
        tools.forEach { tool ->
            FilterChip(
                selected = false,
                onClick = { NovaOrchestrator.getInstance().executeCommand("tool $tool") },
                label = { Text(tool) },
                modifier = Modifier.padding(end = 4.dp)
            )
        }
    }
}