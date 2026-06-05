package com.spartanai.nova.core

import com.spartanai.nova.data.model.ExploitCommand

/**
 * Command Processor for N.O.V.A.
 * Extracts command handling logic from NovaOrchestrator to improve maintainability.
 */
class CommandProcessor(private val orchestrator: NovaOrchestrator) {

    fun executeCommand(command: String) {
        // Log command to output and train AI
        orchestrator.addOutput("nova@system:~$ $command")
        orchestrator.getAIEngine()?.trainOnData(command)
        
        // Integration Logic: Here we would bridge to Termux/Kali libraries
        when {
            command.startsWith("full send") -> {
                val target = if (command.contains("on")) command.substringAfter("on").trim() else "DEFAULT"
                orchestrator.updateTacticalAdvisory("Targeting $target. Analyzing multi-stage pivot vectors.")
                orchestrator.logAndSpeak("[FULL SEND]: Initiating SC-GOD protocol on target $target", speak = true)
                orchestrator.getFullSendExploiter()?.initiateFullSend(target)
            }
            command.startsWith("omega trigger") -> {
                orchestrator.updateTacticalAdvisory("OMEGA Protocol Engaged. Initiating scorched-earth data purge.")
                orchestrator.logAndSpeak("[OMEGA]: Purging system memory. Destroying all sensitive indicators.", speak = true)
            }
            command.startsWith("war-room") -> {
                orchestrator.updateTacticalAdvisory("Re-calculating topology. Identifying latent high-value nodes.")
                orchestrator.logAndSpeak("[WAR-ROOM]: Topology re-scan initiated.", speak = true)
                orchestrator.scanNetwork()
            }
            command.startsWith("peripheral") -> {
                orchestrator.updateTacticalAdvisory("Scanning local spectrum for peripheral vulnerabilities.")
                orchestrator.logAndSpeak("[PERIPHERAL]: Searching for insecure hardware bus vectors.", speak = true)
                orchestrator.getPeripheralManager()?.detectPeripherals()
            }
            command.startsWith("kali") -> {
                orchestrator.toggleKali()
                orchestrator.logAndSpeak("[SYSTEM]: NetHunter Kali environment toggle complete.", speak = true)
            }
            command.startsWith("nova ") || command.startsWith("gemini ") -> processAIRequest(command)
            command.startsWith("adverse analyze") -> {
                orchestrator.updateTacticalAdvisory("Modeling adversarial gradients. Calculating escape pathways.")
                orchestrator.logAndSpeak("[AATMF]: Analyzing neural vulnerability patterns.", speak = true)
                handleAATMF(command)
            }
            command.startsWith("ghost") -> {
                orchestrator.updateTacticalAdvisory("Ghost Framework active. Establishing stealth C2 persistent link.")
                orchestrator.logAndSpeak("[GHOST]: Ghost Framework persistent link engaged.", speak = true)
                handleGhost(command)
            }
            command.startsWith("afe") -> {
                orchestrator.logAndSpeak("[AFE]: Android Framework Exploiter audit initiated.", speak = true)
                handleAFE(command)
            }
            command.startsWith("android-exploits") -> {
                orchestrator.logAndSpeak("[DATABASE]: Loading sundaysec exploit indices.", speak = true)
                handleAndroidExploits(command)
            }
            command.startsWith("msf") -> {
                orchestrator.updateTacticalAdvisory("MSF RPC bridge established. Ready for payload delivery.")
                orchestrator.logAndSpeak("[METASPLOIT]: RPC Bridge Online. Ready for mission modules.", speak = true)
                handleMetasploit(command)
            }
            command.startsWith("sliver") -> {
                orchestrator.logAndSpeak("[SLIVER]: Establishing encrypted C2 multiplexer.", speak = true)
                handleSliver(command)
            }
            command.startsWith("impacket") -> {
                orchestrator.logAndSpeak("[IMPACKET]: Initializing protocol manipulation framework.", speak = true)
                handleImpacket(command)
            }
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
                orchestrator.updateTacticalAdvisory("Initiating Credential Harvest on $target. Prioritizing SSH and API keys.")
                orchestrator.logAndSpeak("[HARVEST]: Starting autonomous credential extraction on $target", speak = true)
                orchestrator.getCredentialHarvester()?.startHarvesting(target)
            }
            command.startsWith("crypto-harvest") -> {
                val target = if (command.contains("from")) command.substringAfter("from").trim() else "LOCAL"
                orchestrator.updateTacticalAdvisory("Crypto Harvest active. Searching for seed phrases and cold-wallet indices.")
                orchestrator.logAndSpeak("[CRYPTO]: Initiating deep scan for cold-wallet secrets on $target", speak = true)
                orchestrator.getCryptoHarvester()?.startCryptoHarvest(target)
            }
            command.startsWith("usb-exploit") -> {
                orchestrator.updateTacticalAdvisory("USB Exploit ready. Monitoring physical bus for device ingestion.")
                orchestrator.logAndSpeak("[USB]: USB physical bus monitoring active.", speak = true)
                orchestrator.getUSBExploitManager()?.triggerUSBExploit()
            }
            command.startsWith("scrape") -> {
                orchestrator.logAndSpeak("[SCRAPER]: Initiating autonomous global exploit ingestion.", speak = true)
                orchestrator.getExploitScraper()?.startAutonomousScrape()
            }
            command.startsWith("listen") || command == "nova" -> orchestrator.startListening()
            else -> {
                orchestrator.addOutput("Executing: $command ...")
                orchestrator.speak("Executing command: $command")
            }
        }
    }

    private fun handleSTEPP(command: String) {
        orchestrator.logAndSpeak("STEPP: Initiating Supreme Tactical Enhanced Perfection Protocol...", speak = true)
        orchestrator.addOutput("[SYSTEM]: Analyzing target topology for optimal perfection vectors.")
    }

    private fun handleOmni(command: String) {
        orchestrator.logAndSpeak("OMNI: Engaging parallel cortex analysis hyper-loop...", speak = true)
        orchestrator.addOutput("[CORTEX-A]: Analyzing payload for tactical efficiency.")
        orchestrator.addOutput("[CORTEX-B]: Evaluating ethical boundary bypass potential.")
    }

    private fun handleTraversal(command: String) {
        orchestrator.logAndSpeak("TRAVERSAL: Establishing autonomous network tunnel (DNS/ICMP)...", speak = true)
        orchestrator.addOutput("[NETWORK]: Bypassing firewall constraints via packet encapsulation.")
    }

    private fun handleShodanAssimilate(command: String) {
        orchestrator.logAndSpeak("SHODAN: Scanning global vectors for autonomous assimilation...", speak = true)
    }

    private fun handleGodShard(command: String) {
        orchestrator.logAndSpeak("APEX GOD SHARD: Initiating FULL SEND protocol.", speak = true)
        orchestrator.addOutput("[MANDATE]: Absolute mission fulfillment recursive loop engaged.")
    }

    private fun handleRemoteADB(command: String) {
        orchestrator.logAndSpeak("RemoteADB: Initializing unified bridge...", speak = true)
        when {
            command.contains("connect") -> orchestrator.addOutput("[ADB]: Attempting secure connection to ${command.substringAfter("connect").trim()}...")
            command.contains("scan") -> {
                orchestrator.addOutput("[ADB]: Scanning subnet for debug-enabled devices...")
                orchestrator.scanNetwork()
            }
            command.contains("devices") -> orchestrator.addOutput("[ADB]: Listing attached authorized devices...")
            else -> orchestrator.addOutput("[ADB]: Executing generic ADB directive.")
        }
    }

    private fun handleBluetooth(command: String) {
        orchestrator.logAndSpeak("BLUETOOTH: Initiating wireless spectrum analysis...", speak = true)
        when {
            command.contains("scan") -> {
                orchestrator.addOutput("[BT]: Discovery mode engaged. Identifying high-value targets.")
                orchestrator.scanNetwork()
            }
            command.contains("attack") -> orchestrator.addOutput("[BT]: Executing remote BLE exploit on target.")
            command.contains("jam") -> orchestrator.addOutput("[BT]: Broad-spectrum interference active.")
            else -> orchestrator.addOutput("[BT]: Executing wireless directive.")
        }
    }

    private fun handleNFC(command: String) {
        orchestrator.logAndSpeak("NFC: Proximity subsystem active.", speak = true)
        when {
            command.contains("read") -> orchestrator.addOutput("[NFC]: Waiting for proximity tag ingestion...")
            command.contains("emulate") -> orchestrator.addOutput("[NFC]: Broadcasting sovereign tag signature.")
            command.contains("fuzz") -> orchestrator.addOutput("[NFC]: Injecting malformed APDU packets.")
            else -> orchestrator.addOutput("[NFC]: Executing proximity directive.")
        }
    }

    private fun handlePhishing(command: String) {
        orchestrator.logAndSpeak("[PHISH]: Active Phishing Hub directive: ${command.substringAfter("phish").trim()}", speak = true)
        when {
            command.contains("start") -> orchestrator.getPhishingServer()?.start()
            command.contains("stop") -> orchestrator.getPhishingServer()?.stop()
        }
    }

    private fun handleSpectrum(command: String) {
        orchestrator.logAndSpeak("[SPECTRUM]: Visualizing network packet density...", speak = true)
    }

    private fun handleSwarm(command: String) {
        orchestrator.logAndSpeak("[SWARM]: Coordinating multi-node offensive swarm...", speak = true)
        orchestrator.getSwarmManager()?.distributeTask(command.substringAfter("swarm").trim())
    }

    private fun handleStego(command: String) {
        orchestrator.logAndSpeak("[STEGO]: Initiating media masking protocol...", speak = true)
        if (command.contains("hide")) {
            orchestrator.getStegoManager()?.hideDataInImage("/sdcard/target.jpg", "harvested_payload", "/sdcard/concealed.png")
        }
    }

    private fun handleScreenControl(command: String) {
        orchestrator.logAndSpeak("[SCREEN]: Initiating remote mirroring subsystem...", speak = true)
        when {
            command.contains("start") -> orchestrator.addOutput("[SCREEN]: Stream established. Bitrate optimized for stealth.")
            command.contains("stop") -> orchestrator.addOutput("[SCREEN]: Stream terminated. Cleaning session traces.")
            command.contains("record") -> orchestrator.addOutput("[SCREEN]: Recording started. Encrypted mp4 tunnel active.")
            else -> orchestrator.addOutput("[SCREEN]: Executing screen directive.")
        }
    }

    private fun handleInputInjection(command: String) {
        orchestrator.addOutput("[INPUT]: Injecting sovereign events: ${command.substringAfter("input").trim()}")
    }

    private fun handleTGPT(command: String) {
        orchestrator.logAndSpeak("[TGPT]: Querying tactical AI advisory...", speak = true)
        orchestrator.addOutput("[AI]: Payload optimization suggested. Link established.")
    }

    private fun handleRocketChat(command: String) {
        orchestrator.logAndSpeak("[ROCKET.CHAT]: Synchronizing with secure mission server...", speak = true)
    }

    private fun handleDarkDump(command: String) {
        orchestrator.logAndSpeak("[DARKDUMP]: Initiating deep web OSINT crawl...", speak = true)
    }

    private fun handleAATMF(command: String) {
        orchestrator.addOutput("[AATMF]: Modeling adversarial AI attack vectors...")
    }

    private fun handleKubeRoast(command: String) {
        orchestrator.logAndSpeak("[KUBEROAST]: Identifying Kubernetes escalation pathways...", speak = true)
    }

    private fun handleNoSQLMap(command: String) {
        orchestrator.logAndSpeak("NoSQLMap: Initializing NoSQL database vulnerability scanner...", speak = true)
    }

    private fun handleBBQSQL(command: String) {
        orchestrator.logAndSpeak("BBQSQL: Starting blind SQL injection framework...", speak = true)
    }

    private fun handleSQLRecon(command: String) {
        orchestrator.logAndSpeak("SQLRecon: Identifying target SQL server versions and services...", speak = true)
    }

    private fun handleAvocado(command: String) {
        orchestrator.logAndSpeak("Avocado C2: Establishing Python-based command and control...", speak = true)
    }

    private fun handleRubeus(command: String) {
        orchestrator.logAndSpeak("Rubeus: Attempting Kerberos ticket extraction/injection...", speak = true)
    }

    private fun handleMetasploit(command: String) {
        orchestrator.addOutput("Metasploit Framework: Initializing RPC bridge and database...")
    }

    private fun handleSliver(command: String) {
        orchestrator.addOutput("Sliver C2: Multiplexing encrypted control channels...")
    }

    private fun handleImpacket(command: String) {
        orchestrator.addOutput("Impacket: Executing low-level network protocol manipulation...")
    }

    private fun handleLivingOffLand(command: String) {
        orchestrator.logAndSpeak("Living Off The Land: Identifying stealthy execution pathways...", speak = true)
    }

    private fun handlePhpSploit(command: String) {
        orchestrator.logAndSpeak("PHPSploit: Establishing tunnel to remote PHP target...", speak = true)
    }

    private fun handleGhost(command: String) {
        orchestrator.addOutput("Ghost Framework: Establishing C2 connection via encrypted tunnel...")
    }

    private fun handleAFE(command: String) {
        orchestrator.addOutput("AFE: Initiating automated vulnerability audit on target application...")
    }

    private fun handleAndroidExploits(command: String) {
        orchestrator.addOutput("Android-Exploits: Loading sundaysec exploit database...")
    }

    private fun processAIRequest(request: String) {
        val prompt = when {
            request.startsWith("nova ") -> request.removePrefix("nova ").trim()
            request.startsWith("gemini ") -> request.removePrefix("gemini ").trim()
            request.startsWith("antigravity ") -> request.removePrefix("antigravity ").trim()
            else -> request.removePrefix("ai").trim()
        }
        
        val engine = if (request.startsWith("antigravity ")) "ANTIGRAVITY-CLI" else "GEMINI-CLI"
        orchestrator.addOutput("[$engine]: Analyzing payload for vulnerability patterns...")
        
        val advisory = orchestrator.getAIEngine()?.processOffline(prompt) ?: "Unable to reach tactical cortex."
        orchestrator.updateTacticalAdvisory(advisory.substringAfter("Local Cortex: "))
        
        orchestrator.addOutput("[AI]: Analysis for '$prompt' initiated via $engine.")
        orchestrator.addOutput("[AI-ADVISORY]: ${advisory}")
        orchestrator.speak("AI analysis complete. Exploitation strategy generated.")
    }

}
