package com.spartanai.nova.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.spartanai.nova.core.NovaOrchestrator
import com.spartanai.nova.ui.components.StatusHeader
import com.spartanai.nova.ui.navigation.Screen
import com.spartanai.nova.ui.screens.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NovaApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val orchestrator = NovaOrchestrator.getInstance()

    Scaffold(
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    title = { Text("N.O.V.A.", style = MaterialTheme.typography.headlineMedium) },
                    actions = {
                        IconButton(onClick = { navController.navigate(Screen.Settings.route) }) {
                            Icon(Icons.Default.Settings, contentDescription = "Settings", tint = MaterialTheme.colorScheme.primary)
                        }
                        TextButton(onClick = { orchestrator.startListening() }) {
                            Text("ACTIVATE NOVA", color = MaterialTheme.colorScheme.primary)
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        titleContentColor = MaterialTheme.colorScheme.primary
                    )
                )
                StatusHeader()
            }
        },
        bottomBar = {
            NavigationBar {
                Screen.items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = currentDestination?.route == screen.route,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { orchestrator.startListening() },
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.onTertiary
            ) {
                Icon(Icons.Default.Mic, contentDescription = "NOVA Activation")
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Terminal.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Terminal.route) { TerminalScreen() }
            composable(Screen.Kali.route) { KaliScreen() }
            composable(Screen.AI.route) { AIScreen() }
            composable(Screen.RemoteADB.route) { RemoteADBScreen() }
            composable(Screen.Wireless.route) { WirelessScreen() }
            composable(Screen.RemoteScreen.route) { RemoteScreen() }
            composable(Screen.Comms.route) { CommsScreen() }
            composable(Screen.Wallet.route) { WalletScreen() }
            composable(Screen.Tools.route) { ToolsScreen() }
            composable(Screen.Knowledge.route) { KnowledgeScreen() }
            composable(Screen.Settings.route) { SettingsScreen() }
        }
    }
}
