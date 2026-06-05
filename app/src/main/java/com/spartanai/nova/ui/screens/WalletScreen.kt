package com.spartanai.nova.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spartanai.nova.core.NovaOrchestrator

@Composable
fun WalletScreen() {
    val orchestrator = NovaOrchestrator.getInstance()
    var walletAddress by remember { mutableStateOf("1Esi1EKp7UqagemAcwySn8m5yJkjyVucHU") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Sovereign Wallet Hub", style = MaterialTheme.typography.headlineMedium)
        Text("Integrated Spark-Wallet Bridge for c-lightning", style = MaterialTheme.typography.bodySmall)

        Spacer(modifier = Modifier.height(24.dp))

        // Balance Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("TOTAL BALANCE", style = MaterialTheme.typography.labelMedium)
                Text("0.4285 BTC", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
                Text("≈ $28,450.12 USD", color = Color.Gray, fontSize = 14.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Harvested Asset Vault", style = MaterialTheme.typography.titleMedium)
        LazyColumn(modifier = Modifier.weight(1f)) {
            item { AssetRow("BTC", "0.3500", "Main Wallet") }
            item { AssetRow("XMR", "12.50", "Stealth Shard") }
            item { AssetRow("LND", "0.0785", "Lightning Node") }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = walletAddress,
            onValueChange = { walletAddress = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Exodus Destination Address") },
            readOnly = true,
            trailingIcon = { Icon(Icons.Default.Lock, contentDescription = null) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(onClick = { orchestrator.executeCommand("wallet sweep") }) {
                Icon(Icons.Default.CleaningServices, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("SWEEP ALL")
            }
            Button(
                onClick = { orchestrator.executeCommand("wallet lightning-connect") },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Yellow, contentColor = Color.Black)
            ) {
                Icon(Icons.Default.Bolt, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("SPARK LINK")
            }
        }
    }
}

@Composable
fun AssetRow(symbol: String, amount: String, source: String) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(symbol, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(source, fontSize = 12.sp, color = Color.Gray)
            }
            Text(amount, color = Color.Green, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
        }
    }
}
