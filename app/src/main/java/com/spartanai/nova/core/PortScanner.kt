package com.spartanai.nova.core

import android.util.Log
import kotlinx.coroutines.*
import java.net.InetSocketAddress
import java.net.Socket

/**
 * N.O.V.A. Port Scanner Subsystem
 * Mandate: Identify open services and potential entry points on target nodes.
 */
class PortScanner(private val orchestrator: NovaOrchestrator) {

    private val commonPorts = listOf(21, 22, 23, 80, 443, 445, 3389, 5555, 8080)

    suspend fun scanPorts(ip: String): List<Int> = withContext(Dispatchers.IO) {
        val openPorts = mutableListOf<Int>()
        orchestrator.addOutput("[PORT-SCAN]: Probing common ports on $ip...")
        
        val jobs = commonPorts.map { port ->
            async {
                if (isPortOpen(ip, port)) {
                    synchronized(openPorts) { openPorts.add(port) }
                    orchestrator.addOutput("[PORT]: Discovered OPEN port $port on $ip")
                }
            }
        }
        jobs.awaitAll()
        
        openPorts.sorted()
    }

    private fun isPortOpen(ip: String, port: Int): Boolean {
        return try {
            val socket = Socket()
            socket.connect(InetSocketAddress(ip, port), 800)
            socket.close()
            true
        } catch (e: Exception) {
            false
        }
    }

    fun guessOS(ip: String, openPorts: List<Int>): String {
        // Advanced TTL-based fingerprinting would require raw sockets (libpcap)
        // For now, we use port-based heuristics
        return when {
            openPorts.contains(5555) -> "Android (ADB)"
            openPorts.contains(445) || openPorts.contains(3389) -> "Windows"
            openPorts.contains(22) && openPorts.contains(80) -> "Linux/Server"
            openPorts.contains(62078) -> "iOS/Apple"
            else -> "Unknown OS"
        }
    }
}