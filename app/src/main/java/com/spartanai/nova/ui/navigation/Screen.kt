package com.spartanai.nova.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Terminal : Screen("terminal", "Terminal", Icons.Default.Terminal)
    object Kali : Screen("kali", "NetHunter", Icons.Default.Security)
    object AI : Screen("ai", "AI Hub", Icons.Default.Psychology)
    object RemoteADB : Screen("remote_adb", "RemoteADB", Icons.Default.PhoneAndroid)
    object WarRoom : Screen("war_room", "War Room", Icons.Default.Map)
    object Wireless : Screen("wireless", "Wireless", Icons.Default.SettingsBluetooth)
    object RemoteScreen : Screen("remote_screen", "RemoteScreen", Icons.Default.CastConnected)
    object Comms : Screen("comms", "Comms", Icons.Default.Chat)
    object Wallet : Screen("wallet", "Wallet", Icons.Default.AccountBalanceWallet)
    object Tools : Screen("tools", "Tools", Icons.Default.Build)
    object Knowledge : Screen("knowledge", "Knowledge", Icons.Default.MenuBook)
    object Settings : Screen("settings", "Settings", Icons.Default.Settings)

    companion object {
        val items = listOf(Terminal, Kali, AI, WarRoom, RemoteADB, Wireless, RemoteScreen, Comms, Wallet, Tools, Knowledge)
    }
}
