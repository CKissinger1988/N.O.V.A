package com.spartanai.nova.ui

import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.spartanai.nova.core.NovaOrchestrator
import com.spartanai.nova.ui.components.StatusHeader
import com.spartanai.nova.ui.navigation.Screen
import com.spartanai.nova.ui.screens.*
import com.spartanai.nova.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NovaApp(startScreen: String? = null) {
    val navController = rememberNavController()
    
    // Process initial navigation from widget
    LaunchedEffect(startScreen) {
        startScreen?.let { route ->
            val screenRoute = when(route) {
                "AI" -> Screen.AI.route
                "SETTINGS" -> Screen.Settings.route
                "TOOLS" -> Screen.Tools.route
                else -> null
            }
            screenRoute?.let { 
                navController.navigate(it) {
                    popUpTo(navController.graph.startDestinationId) { inclusive = false }
                }
            }
        }
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val orchestrator = NovaOrchestrator.getInstance()
    val settings by orchestrator.settings.collectAsState()
    
    val configuration = androidx.compose.ui.platform.LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val isLargeScreen = screenWidth > 840 // Support for large tablets/4K displays

    var leftPanelVisible by remember { mutableStateOf(isLargeScreen) }
    var rightPanelVisible by remember { mutableStateOf(isLargeScreen) }
    
    val displayScale = settings.uiDisplayScale

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = HoloBlack
    ) {
        Scaffold(
            topBar = {
                Column {
                    CenterAlignedTopAppBar(
                        title = { 
                            Text(
                                "N.O.V.A. SUPREME", 
                                style = MaterialTheme.typography.headlineMedium,
                                color = HoloCyan,
                                fontWeight = FontWeight.Bold,
                                fontSize = (MaterialTheme.typography.headlineMedium.fontSize.value * displayScale).sp,
                                modifier = Modifier.graphicsLayer { alpha = settings.uiHolographicGlowIntensity }
                            ) 
                        },
                        navigationIcon = {
                            IconButton(onClick = { leftPanelVisible = !leftPanelVisible }) {
                                Icon(Icons.Default.Menu, contentDescription = "Menu", tint = HoloCyan)
                            }
                        },
                        actions = {
                            IconButton(onClick = { rightPanelVisible = !rightPanelVisible }) {
                                Icon(Icons.Default.Info, contentDescription = "Intelligence", tint = HoloCyan)
                            }
                            IconButton(onClick = { navController.navigate(Screen.Settings.route) }) {
                                Icon(Icons.Default.Settings, contentDescription = "Settings", tint = HoloCyan)
                            }
                        },
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = HoloBlackTrans,
                            titleContentColor = HoloCyan
                        )
                    )
                    StatusHeader()
                }
            },
            bottomBar = {
                NavigationBar(
                    containerColor = HoloBlackTrans,
                    tonalElevation = 0.dp,
                    modifier = Modifier.height((56 * displayScale).dp)
                ) {
                    Screen.items.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = screen.title, modifier = Modifier.size((24 * displayScale).dp)) },
                            label = { Text(screen.title, fontSize = (10 * displayScale).sp) },
                            selected = currentDestination?.route == screen.route,
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = HoloCyan,
                                unselectedIconColor = HoloBlue.copy(alpha = 0.5f),
                                selectedTextColor = HoloCyan,
                                unselectedTextColor = HoloBlue.copy(alpha = 0.5f),
                                indicatorColor = HoloCyanTrans
                            ),
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
                    containerColor = HoloBlueTrans,
                    contentColor = HoloCyan,
                    modifier = Modifier.size((56 * displayScale).dp)
                ) {
                    Icon(Icons.Default.Mic, contentDescription = "NOVA Activation", modifier = Modifier.size((24 * displayScale).dp))
                }
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
                // Background Effect: Resolution-Aware Scanlines
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val lineDensity = if (settings.uiHighResRendering) 80 else 40
                    val lineHeight = size.height / lineDensity
                    for (i in 0 until lineDensity) {
                        if (i % 2 == 0) {
                            drawRect(
                                color = Color.White.copy(alpha = 0.02f),
                                topLeft = androidx.compose.ui.geometry.Offset(0f, i * lineHeight),
                                size = androidx.compose.ui.geometry.Size(size.width, lineHeight)
                            )
                        }
                    }
                }

                Row(modifier = Modifier.fillMaxSize()) {
                    // Left Panel (Navigation/Hierarchy)
                    androidx.compose.animation.AnimatedVisibility(
                        visible = leftPanelVisible,
                        enter = androidx.compose.animation.slideInHorizontally(
                            initialOffsetX = { -it },
                            animationSpec = tween(settings.uiAnimationSpeedMs)
                        ),
                        exit = androidx.compose.animation.slideOutHorizontally(
                            targetOffsetX = { -it },
                            animationSpec = tween(settings.uiAnimationSpeedMs)
                        )
                    ) {
                        val panelWidth = (settings.uiPanelWidthLeft * displayScale).coerceAtMost(screenWidth * 0.4f)
                        Surface(
                            modifier = Modifier.width(panelWidth.dp).fillMaxHeight(),
                            color = HoloDarkBlueTrans,
                            border = androidx.compose.foundation.BorderStroke(1.dp, HoloCyanTrans)
                        ) {
                            Column(modifier = Modifier.padding((16 * displayScale).dp)) {
                                Text("SYSTEM HIERARCHY", style = MaterialTheme.typography.labelLarge, color = HoloCyan, fontSize = (MaterialTheme.typography.labelLarge.fontSize.value * displayScale).sp)
                                Spacer(modifier = Modifier.height((16 * displayScale).dp))
                                Screen.items.forEach { screen ->
                                    TextButton(
                                        onClick = { 
                                            navController.navigate(screen.route)
                                            if (!isLargeScreen) leftPanelVisible = false 
                                        },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                                            Icon(screen.icon, contentDescription = null, tint = if (currentDestination?.route == screen.route) HoloCyan else HoloBlue, modifier = Modifier.size((18 * displayScale).dp))
                                            Spacer(modifier = Modifier.width((12 * displayScale).dp))
                                            Text(screen.title.uppercase(), color = if (currentDestination?.route == screen.route) HoloCyan else HoloBlue, fontSize = (12 * displayScale).sp)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Main Content
                    Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
                        NavHost(
                            navController = navController,
                            startDestination = Screen.Terminal.route,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            composable(Screen.Terminal.route) { TerminalScreen() }
                            composable(Screen.Kali.route) { KaliScreen() }
                            composable(Screen.AI.route) { AIScreen() }
                            composable(Screen.WarRoom.route) { WarRoomScreen() }
                            composable(Screen.Spectrum.route) { SpectrumScreen() }
                            composable(Screen.Phishing.route) { PhishingScreen() }
                            composable(Screen.RemoteADB.route) { RemoteADBScreen() }
                            composable(Screen.Wireless.route) { WirelessScreen() }
                            composable(Screen.RemoteScreen.route) { RemoteScreen() }
                            composable(Screen.Comms.route) { CommsScreen() }
                            composable(Screen.Wallet.route) { WalletScreen() }
                            composable(Screen.Tools.route) { ToolsScreen(navController) }
                            composable(Screen.Knowledge.route) { KnowledgeScreen() }
                            composable(Screen.Settings.route) { SettingsScreen() }
                        }
                    }

                    // Right Panel (Intelligence/Telemetry)
                    androidx.compose.animation.AnimatedVisibility(
                        visible = rightPanelVisible,
                        enter = androidx.compose.animation.slideInHorizontally(
                            initialOffsetX = { it },
                            animationSpec = tween(settings.uiAnimationSpeedMs)
                        ),
                        exit = androidx.compose.animation.slideOutHorizontally(
                            targetOffsetX = { it },
                            animationSpec = tween(settings.uiAnimationSpeedMs)
                        )
                    ) {
                        val panelWidth = (settings.uiPanelWidthRight * displayScale).coerceAtMost(screenWidth * 0.4f)
                        Surface(
                            modifier = Modifier.width(panelWidth.dp).fillMaxHeight(),
                            color = HoloDarkBlueTrans,
                            border = androidx.compose.foundation.BorderStroke(1.dp, HoloCyanTrans)
                        ) {
                            Column(modifier = Modifier.padding((16 * displayScale).dp)) {
                                Text("TACTICAL TELEMETRY", style = MaterialTheme.typography.labelLarge, color = HoloCyan, fontSize = (MaterialTheme.typography.labelLarge.fontSize.value * displayScale).sp)
                                Spacer(modifier = Modifier.height((16 * displayScale).dp))
                                val systemStatus by orchestrator.systemStatus.collectAsState()
                                TelemetryItem("CPU GOVERNOR", settings.cpuGovernorMode, HoloGreen, displayScale)
                                TelemetryItem("NET TIMEOUT", "${settings.netScanTimeoutMs}ms", HoloBlue, displayScale)
                                TelemetryItem("ENC PROTOCOL", systemStatus.networkEncryption, HoloCyan, displayScale)
                                TelemetryItem("THREAT INDEX", systemStatus.threatLevel, if (systemStatus.threatLevel == "HIGH") HoloRed else HoloGreen, displayScale)
                                
                                Spacer(modifier = Modifier.height((24 * displayScale).dp))
                                Text("ACTIVE ADVISORY", style = MaterialTheme.typography.labelLarge, color = HoloCyan, fontSize = (MaterialTheme.typography.labelLarge.fontSize.value * displayScale).sp)
                                val advisory by orchestrator.tacticalAdvisory.collectAsState()
                                Text(
                                    text = advisory,
                                    color = HoloWhite,
                                    fontSize = (11 * displayScale).sp,
                                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                                    modifier = Modifier.padding(top = (8 * displayScale).dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TelemetryItem(label: String, value: String, color: Color, displayScale: Float) {
    Column(modifier = Modifier.padding(vertical = (4 * displayScale).dp)) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = Color.Gray, fontSize = (MaterialTheme.typography.labelSmall.fontSize.value * displayScale).sp)
        Text(value, style = MaterialTheme.typography.bodySmall, color = color, fontWeight = FontWeight.Bold, fontSize = (MaterialTheme.typography.bodySmall.fontSize.value * displayScale).sp)
    }
}
