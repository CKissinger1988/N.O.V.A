package com.spartanai.nova.core

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileOutputStream

/**
 * Stealth Steganography Manager
 * Mandate: Conceal offensive payloads within standard media files.
 */
class StegoManager(private val orchestrator: NovaOrchestrator) {

    fun hideDataInImage(sourceImagePath: String, data: String, outputImagePath: String) {
        orchestrator.addOutput("[STEGO]: Embedding payload into $sourceImagePath...")
        
        try {
            // 1. Load Bitmap
            val bitmap = BitmapFactory.decodeFile(sourceImagePath) ?: return
            val mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
            
            // 2. Real LSB Encoding
            val dataBytes = data.toByteArray(Charsets.UTF_8)
            val dataSize = dataBytes.size
            
            // Embed size first (4 bytes)
            embedByte(mutableBitmap, 0, (dataSize shr 24).toByte())
            embedByte(mutableBitmap, 8, (dataSize shr 16).toByte())
            embedByte(mutableBitmap, 16, (dataSize shr 8).toByte())
            embedByte(mutableBitmap, 24, dataSize.toByte())
            
            // Embed data
            dataBytes.forEachIndexed { index, byte ->
                embedByte(mutableBitmap, 32 + (index * 8), byte)
            }
            
            // 3. Save Output (Must be lossless PNG)
            val outFile = File(outputImagePath)
            if (!outFile.parentFile.exists()) outFile.parentFile.mkdirs()
            FileOutputStream(outFile).use { out ->
                mutableBitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
            
            orchestrator.addOutput("[SUCCESS]: Payload concealed in $outputImagePath. Forensic signature minimized.")
            orchestrator.speak("Data concealment complete.")
        } catch (e: Exception) {
            orchestrator.addOutput("[ERROR]: Stego failed: ${e.message}")
        }
    }

    private fun embedByte(bitmap: Bitmap, startBitIndex: Int, byte: Byte) {
        val width = bitmap.width
        for (i in 0 until 8) {
            val bit = (byte.toInt() shr (7 - i)) and 1
            val totalIndex = startBitIndex + i
            val x = totalIndex % width
            val y = totalIndex / width
            
            val pixel = bitmap.getPixel(x, y)
            // Manipulate the Blue channel's LSB
            val newPixel = (pixel and 0xFFFFFFFE.toInt()) or bit
            bitmap.setPixel(x, y, newPixel)
        }
    }
}
