package com.spartanai.nova.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spartanai.nova.core.NovaOrchestrator
import com.spartanai.nova.data.model.NovaSettings
import com.spartanai.nova.ui.theme.*

@Composable
fun SettingsScreen() {
    val orchestrator = NovaOrchestrator.getInstance()
    val settings by orchestrator.settings.collectAsState()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Text("SOVEREIGN CONFIGURATION", style = MaterialTheme.typography.headlineMedium, color = HoloCyan)
        Text("Master control for all N.O.V.A sub-systems", style = MaterialTheme.typography.bodySmall, color = androidx.compose.ui.graphics.Color.Gray)
        
        Spacer(modifier = Modifier.height(24.dp))

        // UI & Aesthetic Section
        SettingsSection("HOLOGRAPHIC INTERFACE") {
            SettingsSlider(
                label = "Glow Intensity",
                value = settings.uiHolographicGlowIntensity,
                onValueChange = { orchestrator.updateSettings(settings.copy(uiHolographicGlowIntensity = it)) }
            )
            SettingsSlider(
                label = "Tactical View Scaling",
                value = settings.uiDisplayScale,
                range = 0.5f..2.0f,
                onValueChange = { orchestrator.updateSettings(settings.copy(uiDisplayScale = it)) }
            )
            SettingsSwitch(
                label = "High-Res Rendering (4K Mode)",
                checked = settings.uiHighResRendering,
                onCheckedChange = { orchestrator.updateSettings(settings.copy(uiHighResRendering = it)) }
            )
            SettingsSlider(
                label = "Animation Speed (ms)",
                value = settings.uiAnimationSpeedMs.toFloat(),
                range = 100f..1000f,
                onValueChange = { orchestrator.updateSettings(settings.copy(uiAnimationSpeedMs = it.toInt())) }
            )
        }


        // AI & Intelligence Section
        SettingsSection("AI TACTICAL ENGINE") {
            SettingsTextField(
                label = "AI Provider",
                value = settings.aiProvider,
                onValueChange = { orchestrator.updateSettings(settings.copy(aiProvider = it)) }
            )
            SettingsTextField(
                label = "Strategic Model",
                value = settings.aiModel,
                onValueChange = { orchestrator.updateSettings(settings.copy(aiModel = it)) }
            )
            SettingsSlider(
                label = "Temperature (Cortex Chaos)",
                value = settings.aiTemperature,
                onValueChange = { orchestrator.updateSettings(settings.copy(aiTemperature = it)) }
            )
            SettingsSwitch(
                label = "Autonomous Execution",
                checked = settings.aiAutoExecute,
                onCheckedChange = { orchestrator.updateSettings(settings.copy(aiAutoExecute = it)) }
            )
        }

        // Network & Recon Section
        SettingsSection("RECONNAISSANCE PARAMETERS") {
            SettingsSlider(
                label = "Network Scan Timeout (ms)",
                value = settings.netScanTimeoutMs.toFloat(),
                range = 500f..5000f,
                onValueChange = { orchestrator.updateSettings(settings.copy(netScanTimeoutMs = it.toInt())) }
            )
            SettingsSlider(
                label = "Scanner Threads",
                value = settings.netScanThreads.toFloat(),
                range = 10f..200f,
                onValueChange = { orchestrator.updateSettings(settings.copy(netScanThreads = it.toInt())) }
            )
            SettingsSwitch(
                label = "Stealth Mode (Anti-Forensics)",
                checked = settings.stealthModeActive,
                onCheckedChange = { orchestrator.updateSettings(settings.copy(stealthModeActive = it)) }
            )
        }

        // Hardware Section
        SettingsSection("HARDWARE & OPTIMIZATION") {
            SettingsSwitch(
                label = "Low Power Mode",
                checked = settings.lowPowerMode,
                onCheckedChange = { orchestrator.updateSettings(settings.copy(lowPowerMode = it)) }
            )
            SettingsTextField(
                label = "CPU Governor",
                value = settings.cpuGovernorMode,
                onValueChange = { orchestrator.updateSettings(settings.copy(cpuGovernorMode = it)) }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = { /* In production, this would save to SharedPreferences/DataStore */ },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = HoloCyan.copy(alpha = 0.2f)),
            border = androidx.compose.foundation.BorderStroke(1.dp, HoloCyan)
        ) {
            Text("COMMIT SOVEREIGN CHANGES", color = HoloCyan)
        }
    }
}

@Composable
fun SettingsSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(modifier = Modifier.padding(vertical = 12.dp)) {
        Text(title, style = MaterialTheme.typography.labelLarge, color = HoloBlue)
        Spacer(modifier = Modifier.height(8.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = HoloDarkBlueTrans),
            border = androidx.compose.foundation.BorderStroke(0.5.dp, HoloCyanTrans)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                content()
            }
        }
    }
}

@Composable
fun SettingsSlider(label: String, value: Float, range: ClosedFloatingPointRange<Float> = 0f..1f, onValueChange: (Float) -> Unit) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label, style = MaterialTheme.typography.bodySmall, color = HoloWhite)
            Text("%.2f".format(value), style = MaterialTheme.typography.labelSmall, color = HoloCyan)
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = range,
            colors = SliderDefaults.colors(thumbColor = HoloCyan, activeTrackColor = HoloCyan, inactiveTrackColor = HoloBlueTrans)
        )
    }
}

@Composable
fun SettingsSwitch(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, style = MaterialTheme.typography.bodySmall, color = HoloWhite)
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(checkedThumbColor = HoloCyan, checkedTrackColor = HoloBlue)
        )
    }
}

@Composable
fun SettingsTextField(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, fontSize = 10.sp) },
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        textStyle = MaterialTheme.typography.bodySmall.copy(color = HoloWhite),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = HoloCyan,
            unfocusedBorderColor = HoloBlueTrans,
            focusedLabelColor = HoloCyan,
            unfocusedLabelColor = androidx.compose.ui.graphics.Color.Gray
        )
    )
}

