package com.spartanai.nova

import com.spartanai.nova.core.SecurityManager
import com.spartanai.nova.core.KnowledgeManager
import org.junit.Test
import org.junit.Assert.*

class NovaCoreTest {

    @Test
    fun testEncryptionDecryption() {
        val securityManager = SecurityManager()
        val originalData = "SPARTAN-SECRET-PAYLOAD"
        val key = securityManager.generateSecureKey()
        
        val encrypted = securityManager.encryptCascaded(originalData, key)
        val decrypted = securityManager.decryptCascaded(encrypted, key)
        
        assertEquals(originalData, decrypted)
    }

    @Test
    fun testKnowledgeIngestion() {
        val knowledgeManager = KnowledgeManager()
        val knowledge = knowledgeManager.fetchKnowledge()
        
        assertTrue("Knowledge base should not be empty", knowledge.isNotEmpty())
        assertTrue("Should contain Spartan God Mode", knowledge.any { it.name == "Spartan God Mode" })
    }
}
