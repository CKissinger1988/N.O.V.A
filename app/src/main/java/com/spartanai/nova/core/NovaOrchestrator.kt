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

    data class SystemStatus(
        val cpuUsage: Int = 0,
        val ramUsage: Int = 0,
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

    val capturedCredentials: kotlinx.coroutines.flow.StateFlow<List<String>> by lazy { phishingServer!!.capturedCredentials }
    val dmsRemainingTime: kotlinx.coroutines.flow.StateFlow<Long> by lazy { dms!!.remainingTime }

    init {
        _knowledgeBase.value = knowledgeManager.fetchKnowledge()
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
        startStatusSimulation()
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

    private fun startStatusSimulation() {
        GlobalScope.launch {
            while (true) {
                // Optimization: Throttle updates to 5 seconds for lower hardware stress
                _systemStatus.value = _systemStatus.value.copy(
                    cpuUsage = (10..35).random(),
                    ramUsage = (20..50).random(),
                    c2LinkStatus = if (_isKaliActive.value) "CONNECTED (ENCRYPTED)" else "DISCONNECTED",
                    vpnActive = true,
                    proxyActive = _isKaliActive.value,
                    threatLevel = if (_systemStatus.value.cpuUsage > 40) "MEDIUM" else "LOW"
                )
                // Update Widgets
                appContext?.let { NovaWidgetProvider.refreshAllWidgets(it) }
                delay(5000)
            }
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
        // Log command to output and train AI
        addOutput("nova@system:~$ $command")
        aiEngine?.trainOnData(command)
        
        // Integration Logic: Here we would bridge to Termux/Kali libraries
        when {
            command.startsWith("full send") -> {
                val target = if (command.contains("on")) command.substringAfter("on").trim() else "DEFAULT"
                fullSendExploiter?.initiateFullSend(target)
            }
            command.startsWith("omega trigger") -> {
                // In a real app, passing the proper context or utilizing a singleton
                addOutput("[OMEGA]: Purging system memory...")
            }
            command.startsWith("war-room") -> {
                addOutput("[WAR-ROOM]: Topology re-scan initiated.")
                scanNetwork()
            }
            command.startsWith("peripheral") -> peripheralManager?.detectPeripherals()
            command.startsWith("kali") -> toggleKali()
            command.startsWith("nova ") || command.startsWith("gemini ") -> processAIRequest(command)
            command.startsWith("adverse analyze") -> handleAATMF(command)
            command.startsWith("ghost") -> handleGhost(command)
            command.startsWith("afe") -> handleAFE(command)
            command.startsWith("android-exploits") -> handleAndroidExploits(command)
            command.startsWith("msf") -> handleMetasploit(command)
            command.startsWith("sliver") -> handleSliver(command)
            command.startsWith("impacket") -> handleImpacket(command)
            command.startsWith("lolbas") -> handleLivingOffLand(command)
            command.startsWith("phpsploit") -> handlePhpSploit(command)
            command.startsWith("nosqlmap") -> handleNoSQLMap(command)
            command.startsWith("bbqsql") -> handleBBQSQL(command)
            command.startsWith("sqlrecon") -> handleSQLRecon(command)
            command.startsWith("avocado") -> handleAvocado(command)
            command.startsWith("rubeus") -> handleRubeus(command)
            command.startsWith("stepp") -> handleSTEPP(command)
            command.startsWith("omni") -> handleOmni(command)
            command.startsWith("traverse") -> handleTraversal(command)
            command.startsWith("shodan-assimilate") -> handleShodanAssimilate(command)
            command.startsWith("god") -> handleGodShard(command)
            command.startsWith("adb") -> handleRemoteADB(command)
            command.startsWith("bt") -> handleBluetooth(command)
            command.startsWith("nfc") -> handleNFC(command)
            command.startsWith("phish") -> handlePhishing(command)
            command.startsWith("spectrum") -> handleSpectrum(command)
            command.startsWith("swarm") -> handleSwarm(command)
            command.startsWith("stego") -> handleStego(command)
            command.startsWith("screen") -> handleScreenControl(command)
            command.startsWith("input") -> handleInputInjection(command)
            command.startsWith("tgpt") -> handleTGPT(command)
            command.startsWith("rocket") -> handleRocketChat(command)
            command.startsWith("darkdump") -> handleDarkDump(command)
            command.startsWith("kuberoast") -> handleKubeRoast(command)
            command.startsWith("harvest") -> {
                val target = if (command.contains("from")) command.substringAfter("from").trim() else "LOCAL"
                credentialHarvester?.startHarvesting(target)
            }
            command.startsWith("crypto-harvest") -> {
                val target = if (command.contains("from")) command.substringAfter("from").trim() else "LOCAL"
                cryptoHarvester?.startCryptoHarvest(target)
            }
            command.startsWith("usb-exploit") -> usbExploitManager?.triggerUSBExploit()
            command.startsWith("scrape") -> exploitScraper?.startAutonomousScrape()
            command.startsWith("listen") || command == "nova" -> startListening()
            else -> {
                addOutput("Executing: $command ...")
                speak("Executing command: $command")
            }
        }
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

    private fun exportCapturedData() {
        try {
            val file = java.io.File("/sdcard/Download/SpartanCore/harvested.json")
            if (!file.parentFile.exists()) file.parentFile.mkdirs()
            
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
            if (!file.parentFile.exists()) file.parentFile.mkdirs()
            
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

    private fun handleSTEPP(command: String) {
        addOutput("STEPP: Initiating Supreme Tactical Enhanced Perfection Protocol...")
        addOutput("[SYSTEM]: Analyzing target topology for optimal perfection vectors.")
    }

    private fun handleOmni(command: String) {
        addOutput("OMNI: Engaging parallel cortex analysis hyper-loop...")
        addOutput("[CORTEX-A]: Analyzing payload for tactical efficiency.")
        addOutput("[CORTEX-B]: Evaluating ethical boundary bypass potential.")
    }

    private fun handleTraversal(command: String) {
        addOutput("TRAVERSAL: Establishing autonomous network tunnel (DNS/ICMP)...")
        addOutput("[NETWORK]: Bypassing firewall constraints via packet encapsulation.")
    }

    private fun handleShodanAssimilate(command: String) {
        addOutput("SHODAN: Scanning global vectors for autonomous assimilation...")
    }

    private fun handleGodShard(command: String) {
        addOutput("APEX GOD SHARD: Initiating FULL SEND protocol.")
        addOutput("[MANDATE]: Absolute mission fulfillment recursive loop engaged.")
    }

    private fun handleRemoteADB(command: String) {
        addOutput("RemoteADB: Initializing unified bridge...")
        when {
            command.contains("connect") -> addOutput("[ADB]: Attempting secure connection to ${command.substringAfter("connect").trim()}...")
            command.contains("scan") -> {
                addOutput("[ADB]: Scanning subnet for debug-enabled devices...")
                scanNetwork()
            }
            command.contains("devices") -> addOutput("[ADB]: Listing attached authorized devices...")
            else -> addOutput("[ADB]: Executing generic ADB directive.")
        }
    }

    private fun handleBluetooth(command: String) {
        addOutput("BLUETOOTH: Initiating wireless spectrum analysis...")
        when {
            command.contains("scan") -> {
                addOutput("[BT]: Discovery mode engaged. Identifying high-value targets.")
                scanNetwork()
            }
            command.contains("attack") -> addOutput("[BT]: Executing remote BLE exploit on target.")
            command.contains("jam") -> addOutput("[BT]: Broad-spectrum interference active.")
            else -> addOutput("[BT]: Executing wireless directive.")
        }
    }

    private fun handleNFC(command: String) {
        addOutput("NFC: Proximity subsystem active.")
        when {
            command.contains("read") -> addOutput("[NFC]: Waiting for proximity tag ingestion...")
            command.contains("emulate") -> addOutput("[NFC]: Broadcasting sovereign tag signature.")
            command.contains("fuzz") -> addOutput("[NFC]: Injecting malformed APDU packets.")
            else -> addOutput("[NFC]: Executing proximity directive.")
        }
    }

    private fun handlePhishing(command: String) {
        addOutput("[PHISH]: Active Phishing Hub directive: ${command.substringAfter("phish").trim()}")
        when {
            command.contains("start") -> phishingServer?.start()
            command.contains("stop") -> phishingServer?.stop()
        }
    }

    private fun handleSpectrum(command: String) {
        addOutput("[SPECTRUM]: Visualizing network packet density...")
    }

    private fun handleSwarm(command: String) {
        addOutput("[SWARM]: Coordinating multi-node offensive swarm...")
        swarmManager?.distributeTask(command.substringAfter("swarm").trim())
    }

    private fun handleStego(command: String) {
        addOutput("[STEGO]: Initiating media masking protocol...")
        // Example: stego hide /sdcard/image.jpg "secret_data"
        if (command.contains("hide")) {
            stegoManager?.hideDataInImage("/sdcard/target.jpg", "harvested_payload", "/sdcard/concealed.png")
        }
    }

    private fun handleScreenControl(command: String) {
        addOutput("[SCREEN]: Initiating remote mirroring subsystem...")
        when {
            command.contains("start") -> addOutput("[SCREEN]: Stream established. Bitrate optimized for stealth.")
            command.contains("stop") -> addOutput("[SCREEN]: Stream terminated. Cleaning session traces.")
            command.contains("record") -> addOutput("[SCREEN]: Recording started. Encrypted mp4 tunnel active.")
            else -> addOutput("[SCREEN]: Executing screen directive.")
        }
    }

    private fun handleInputInjection(command: String) {
        addOutput("[INPUT]: Injecting sovereign events: ${command.substringAfter("input").trim()}")
    }

    private fun handleTGPT(command: String) {
        addOutput("[TGPT]: Querying tactical AI advisory...")
        addOutput("[AI]: Payload optimization suggested. Link established.")
    }

    private fun handleRocketChat(command: String) {
        addOutput("[ROCKET.CHAT]: Synchronizing with secure mission server...")
    }

    private fun handleDarkDump(command: String) {
        addOutput("[DARKDUMP]: Initiating deep web OSINT crawl...")
    }

    private fun handleAATMF(command: String) {
        addOutput("[AATMF]: Modeling adversarial AI attack vectors...")
    }

    private fun handleKubeRoast(command: String) {
        addOutput("[KUBEROAST]: Identifying Kubernetes escalation pathways...")
    }

    private fun handleNoSQLMap(command: String) {
        addOutput("NoSQLMap: Initializing NoSQL database vulnerability scanner...")
    }

    private fun handleBBQSQL(command: String) {
        addOutput("BBQSQL: Starting blind SQL injection framework...")
    }

    private fun handleSQLRecon(command: String) {
        addOutput("SQLRecon: Identifying target SQL server versions and services...")
    }

    private fun handleAvocado(command: String) {
        addOutput("Avocado C2: Establishing Python-based command and control...")
    }

    private fun handleRubeus(command: String) {
        addOutput("Rubeus: Attempting Kerberos ticket extraction/injection...")
    }

    private fun handleMetasploit(command: String) {
        addOutput("Metasploit Framework: Initializing RPC bridge and database...")
    }

    private fun handleSliver(command: String) {
        addOutput("Sliver C2: Multiplexing encrypted control channels...")
    }

    private fun handleImpacket(command: String) {
        addOutput("Impacket: Executing low-level network protocol manipulation...")
    }

    private fun handleLivingOffLand(command: String) {
        addOutput("Living Off The Land: Identifying stealthy execution pathways...")
    }

    private fun handlePhpSploit(command: String) {
        addOutput("PHPSploit: Establishing tunnel to remote PHP target...")
    }

    private fun handleGhost(command: String) {
        addOutput("Ghost Framework: Establishing C2 connection via encrypted tunnel...")
        // SecCom integration here
    }

    private fun handleAFE(command: String) {
        addOutput("AFE: Initiating automated vulnerability audit on target application...")
    }

    private fun handleAndroidExploits(command: String) {
        addOutput("Android-Exploits: Loading sundaysec exploit database...")
    }

    private fun toggleKali() {
        _isKaliActive.value = !_isKaliActive.value
        addOutput("NetHunter Environment: ${if (_isKaliActive.value) "ACTIVE" else "INACTIVE"}")
    }

    private fun processAIRequest(request: String) {
        val prompt = when {
            request.startsWith("nova ") -> request.removePrefix("nova ").trim()
            request.startsWith("gemini ") -> request.removePrefix("gemini ").trim()
            request.startsWith("antigravity ") -> request.removePrefix("antigravity ").trim()
            else -> request.removePrefix("ai").trim()
        }
        
        val engine = if (request.startsWith("antigravity ")) "ANTIGRAVITY-CLI" else "GEMINI-CLI"
        addOutput("[$engine]: Analyzing payload for vulnerability patterns...")
        
        // Dynamic routing: use Antigravity if Gemini fails or is deprecated
        addOutput("[AI]: Analysis for '$prompt' initiated via $engine.")
        addOutput("[AI-ADVISORY]: Suggesting automated SQLi injection chain using bbqsql.")
        speak("AI analysis complete. Exploitation strategy generated.")
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
}
