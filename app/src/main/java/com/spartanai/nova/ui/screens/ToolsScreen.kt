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

@Composable
fun ToolsScreen() {
    val orchestrator = NovaOrchestrator.getInstance()
    
    Column(modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState())) {
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