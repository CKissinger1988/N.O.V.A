package com.spartanai.nova.core

import com.spartanai.nova.data.model.ExploitCommand
import com.spartanai.nova.data.model.NovaSettings
import com.spartanai.nova.data.model.DiscoveredDevice
import com.spartanai.nova.ui.widget.NovaWidgetProvider
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import android.util.Log

/**
 * Main Orchestrator for N.O.V.A. (Network Offensive Vulnerability Application)
 * Handles state management and coordination between different security modules.
 */
class NovaOrchestrator private constructor() {
    
    private val _terminalOutput = MutableStateFlow<List<String>>(emptyList())
    val terminalOutput = _terminalOutput.asStateFlow()

    private val _isKaliActive = MutableStateFlow(false)
    val isKaliActive = _isKaliActive.asStateFlow()

    private val _systemStatus = MutableStateFlow(SystemStatus())
    val systemStatus = _systemStatus.asStateFlow()

    private val _discoveredDevices = MutableStateFlow<List<DiscoveredDevice>>(emptyList())
    val discoveredDevices = _discoveredDevices.asStateFlow()

    private val _settings = MutableStateFlow(NovaSettings())
    val settings = _settings.asStateFlow()

    private val _tacticalAdvisory = MutableStateFlow("Mission ready. Awaiting reconnaissance data.")
    val tacticalAdvisory = _tacticalAdvisory.asStateFlow()

    fun updateTacticalAdvisory(advisory: String) {
        _tacticalAdvisory.value = advisory
        appContext?.let { context ->
            NovaWidgetProvider.refreshAllWidgets(context)
        }
    }

    fun getLearningProgress() = aiEngine?.learningProgress ?: MutableStateFlow(0f)

    data class SystemStatus(


        val cpuUsage: Int = 0,
        val ramUsage: Int = 0,
        val batteryLevel: Int = 100,
        val nodeCount: Int = 0,
        val networkEncryption: String = "AES-256-GCM + ChaCha20",
        val c2LinkStatus: String = "DISCONNECTED",
        val vpnActive: Boolean = false,
        val proxyActive: Boolean = false,
        val threatLevel: String = "LOW"
    )


    private val _knowledgeBase = MutableStateFlow<List<ExploitCommand>>(emptyList())
    val knowledgeBase = _knowledgeBase.asStateFlow()

    private val knowledgeManager = KnowledgeManager()
    private var voiceManager: VoiceManager? = null
    private var fullSendExploiter: FullSendExploiter? = null
    private var credentialHarvester: CredentialHarvester? = null
    private var usbExploitManager: USBExploitManager? = null
    private var cryptoHarvester: CryptoHarvester? = null
    private var exploitScraper: ExploitScraper? = null
    private var aiEngine: NovaAIEngine? = null
    private var peripheralManager: PeripheralManager? = null
    private var mobileNetworkManager: MobileNetworkManager? = null
    private var swarmManager: SwarmManager? = null
    private var duressManager: DuressManager? = null
    private var stegoManager: StegoManager? = null
    private var networkScanner: NetworkScanner? = null
    private var portScanner: PortScanner? = null
    private var panicManager: PanicManager? = null
    private var dms: DeadMansSwitch? = null
    private var phishingServer: PhishingServer? = null
    private val securityManager = SecurityManager()
    private var appContext: android.content.Context? = null
    private var commandProcessor: CommandProcessor

    fun getAIEngine() = aiEngine

    fun getFullSendExploiter() = fullSendExploiter
    fun getPeripheralManager() = peripheralManager
    fun getCredentialHarvester() = credentialHarvester
    fun getCryptoHarvester() = cryptoHarvester
    fun getUSBExploitManager() = usbExploitManager
    fun getExploitScraper() = exploitScraper
    fun getPhishingServer() = phishingServer
    fun getSwarmManager() = swarmManager
    fun getStegoManager() = stegoManager

    val capturedCredentials: kotlinx.coroutines.flow.StateFlow<List<String>> by lazy { phishingServer!!.capturedCredentials }
    val dmsRemainingTime: kotlinx.coroutines.flow.StateFlow<Long> by lazy { dms!!.remainingTime }

    init {
        _knowledgeBase.value = knowledgeManager.fetchKnowledge()
        commandProcessor = CommandProcessor(this)
    }

    private var isInitialized = false

    fun initialize(context: android.content.Context) {
        if (isInitialized) return
        isInitialized = true
        appContext = context.applicationContext
        voiceManager = VoiceManager(context) { command ->
            executeCommand(command)
        }
        fullSendExploiter = FullSendExploiter(this, _knowledgeBase.value)
        credentialHarvester = CredentialHarvester(this)
        usbExploitManager = USBExploitManager(context, this)
        cryptoHarvester = CryptoHarvester(this)
        exploitScraper = ExploitScraper(this)
        aiEngine = NovaAIEngine(context, this)
        peripheralManager = PeripheralManager(context, this)
        mobileNetworkManager = MobileNetworkManager(context, this)
        swarmManager = SwarmManager(this)
        duressManager = DuressManager(context, this)
        stegoManager = StegoManager(this)
        networkScanner = NetworkScanner(this)
        portScanner = PortScanner(this)
        panicManager = PanicManager(context, this)
        panicManager?.startListening()
        dms = DeadMansSwitch(context, this)
        phishingServer = PhishingServer(this)
        
        addOutput("Nova System Initialized. Voice Control: ACTIVE.")
        speak("Nova System Online. Ready for mission directives.")
        startSystemMonitoring()
        startAutoScanners()
    }


    fun armDMS(minutes: Int) {
        dms?.arm(minutes)
    }

    fun resetDMS() {
        dms?.reset()
    }

    fun inspectNode(ip: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val openPorts = portScanner?.scanPorts(ip) ?: emptyList()
            val guessedOS = portScanner?.guessOS(ip, openPorts) ?: "Unknown"
            
            val current = _discoveredDevices.value.toMutableList()
            val index = current.indexOfFirst { it.ip == ip }
            if (index != -1) {
                current[index] = current[index].copy(
                    openPorts = openPorts,
                    guessedOS = guessedOS,
                    status = if (openPorts.isNotEmpty()) com.spartanai.nova.data.model.DeviceStatus.VULNERABLE else com.spartanai.nova.data.model.DeviceStatus.SECURE
                )
                _discoveredDevices.value = current
                speak("Node inspection for $ip complete. Found ${openPorts.size} open ports. Predicted OS: $guessedOS.")
            }
        }
    }

    private fun startSystemMonitoring() {
        GlobalScope.launch {
            while (true) {
                appContext?.let { context ->
                    val batteryManager = context.getSystemService(android.content.Context.BATTERY_SERVICE) as android.os.BatteryManager
                    val activityManager = context.getSystemService(android.content.Context.ACTIVITY_SERVICE) as android.app.ActivityManager
                    val memoryInfo = android.app.ActivityManager.MemoryInfo()
                    activityManager.getMemoryInfo(memoryInfo)

                    val batteryLevel = batteryManager.getIntProperty(android.os.BatteryManager.BATTERY_PROPERTY_CAPACITY)
                    val ramUsagePercent = if (memoryInfo.totalMem > 0) {
                        ((memoryInfo.totalMem - memoryInfo.availMem) * 100 / memoryInfo.totalMem).toInt()
                    } else 0

                    _systemStatus.value = _systemStatus.value.copy(
                        cpuUsage = (5..15).random(), // CPU usage is harder to get accurately on modern Android, using realistic low-idle base
                        ramUsage = ramUsagePercent,
                        batteryLevel = batteryLevel,
                        nodeCount = _discoveredDevices.value.size,
                        c2LinkStatus = if (_isKaliActive.value) "CONNECTED (ENCRYPTED)" else "DISCONNECTED",
                        vpnActive = isVpnActive(context),
                        threatLevel = calculateThreatLevel(ramUsagePercent)
                    )
                    
                    // Update Widgets
                    NovaWidgetProvider.refreshAllWidgets(context)
                }
                delay(3000)
            }
        }
    }

    private fun isVpnActive(context: android.content.Context): Boolean {
        val cm = context.getSystemService(android.content.Context.CONNECTIVITY_SERVICE) as android.net.ConnectivityManager
        val networks = cm.allNetworks
        return networks.any { network ->
            val caps = cm.getNetworkCapabilities(network)
            caps?.hasTransport(android.net.NetworkCapabilities.TRANSPORT_VPN) == true
        }
    }

    private fun calculateThreatLevel(ramUsage: Int): String {
        return when {
            ramUsage > 85 -> "CRITICAL"
            ramUsage > 70 -> "HIGH"
            ramUsage > 50 -> "MEDIUM"
            else -> "LOW"
        }
    }



    private fun startAutoScanners() {
        GlobalScope.launch {
            while (true) {
                if (_settings.value.nfcAutoClone) {
                    executeCommand("nfc read")
                }
                // Mobile network scan
                mobileNetworkManager?.scanMobileNetworks()
                
                // Periodic network recon
                scanNetwork()
                
                delay(30000) // 30 second interval for background scanning
            }
        }
    }

    companion object {
        @Volatile
        private var instance: NovaOrchestrator? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: NovaOrchestrator().also { instance = it }
        }
    }

    fun executeCommand(command: String) {
        commandProcessor.executeCommand(command)
    }

    fun toggleKali() {
        _isKaliActive.value = !_isKaliActive.value
        addOutput("NetHunter Environment: ${if (_isKaliActive.value) "ACTIVE" else "INACTIVE"}")
    }

    fun addOutput(line: String) {
        val current = _terminalOutput.value.toMutableList()
        current.add(line)
        
        // Memory Optimization: Cap terminal history to settings limit
        val limit = _settings.value.terminalHistoryLimit
        if (current.size > limit) {
            val overflow = current.size - limit
            repeat(overflow) { current.removeAt(0) }
        }

        _terminalOutput.value = current
        
        // Update C2 status if reverse shell established
        if (line.contains("[REVERSE SHELL]")) {
            _systemStatus.value = _systemStatus.value.copy(c2LinkStatus = "ACTIVE (REVERSE SHELL)")
        }

        // Sync harvested tokens with Kali bridge
        if (line.contains("[PHISH]: TOKEN HARVESTED")) {
            exportCapturedData()
        }
    }

    /**
     * Unified Telemetry: Updates terminal and optionally announces status via AI voice.
     */
    fun logAndSpeak(line: String, speak: Boolean = false) {
        addOutput(line)
        if (speak) {
            // Strip tags like [SYSTEM] or [SCAN] for cleaner speech
            val cleanText = line.replace(Regex("\\[.*?\\]:?\\s*"), "").trim()
            speak(cleanText)
        }
    }

    fun clearOutput() {

        _terminalOutput.value = emptyList()
        addOutput("[SYSTEM]: Terminal history purged.")
    }

    private fun exportCapturedData() {
        try {
            val file = java.io.File("/sdcard/Download/SpartanCore/harvested.json")
            file.parentFile?.let { if (!it.exists()) it.mkdirs() }
            
            val credentials = phishingServer?.capturedCredentials?.value ?: emptyList()

            val json = StringBuilder("[\n")
            credentials.forEachIndexed { index, cred ->
                json.append("  \"$cred\"${if (index < credentials.size - 1) "," else ""}\n")
            }
            json.append("]")
            
            file.writeText(json.toString())
            addOutput("[KALI-BRIDGE]: Captured data synced to ${file.absolutePath}")
        } catch (e: Exception) {
            Log.e("NovaOrchestrator", "Failed to export captured data: ${e.message}")
        }
    }

    fun speak(text: String) {
        voiceManager?.speak(text)
    }

    fun startListening() {
        voiceManager?.startListening()
    }

    fun updateSettings(newSettings: NovaSettings) {
        _settings.value = newSettings
        addOutput("[SYSTEM]: Settings updated successfully.")
    }

    fun scanNetwork() {
        GlobalScope.launch(Dispatchers.IO) {
            val results = networkScanner?.scanLocalSubnet() ?: emptyList()
            _discoveredDevices.value = results
            if (results.isNotEmpty()) {
                speak("Network scan complete. ${results.size} targets identified.")
                exportReconData(results)
            }
        }
    }

    private fun exportReconData(devices: List<DiscoveredDevice>) {
        try {
            val file = java.io.File("/sdcard/Download/SpartanCore/recon.json")
            file.parentFile?.let { if (!it.exists()) it.mkdirs() }
            
            val json = StringBuilder("[\n")

            devices.forEachIndexed { index, device ->
                json.append("  {\n")
                json.append("    \"ip\": \"${device.ip}\",\n")
                json.append("    \"mac\": \"${device.mac}\",\n")
                json.append("    \"status\": \"${device.status.name}\"\n")
                json.append("  }${if (index < devices.size - 1) "," else ""}\n")
            }
            json.append("]")
            
            file.writeText(json.toString())
            addOutput("[KALI-BRIDGE]: Recon data synced to ${file.absolutePath}")
        } catch (e: Exception) {
            Log.e("NovaOrchestrator", "Failed to export recon data: ${e.message}")
        }
    }

    fun importKnowledge(commands: List<ExploitCommand>) {

        _knowledgeBase.value = commands
        addOutput("Imported ${commands.size} exploit protocols from SpartanAI_Core.")
    }

    fun injectNewExploit(exploit: ExploitCommand) {
        val current = _knowledgeBase.value.toMutableList()
        if (current.none { it.id == exploit.id }) {
            current.add(exploit)
            _knowledgeBase.value = current
        }
    }

    fun getDeviceByIp(ip: String): DiscoveredDevice? {
        return _discoveredDevices.value.find { it.ip == ip }
    }
}

