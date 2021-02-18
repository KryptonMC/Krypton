package me.bristermitten.minekraft.encryption

import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.PublicKey
import javax.crypto.Cipher

class Encryption {

    private val keyPair = generateKeyPair()

    val publicKey: PublicKey = keyPair.public

    private fun generateKeyPair(): KeyPair {
        val generator = KeyPairGenerator.getInstance(PAIR_ALGORITHM)
        generator.initialize(1024)
        return generator.generateKeyPair()
    }

    fun decryptWithPrivateKey(encryptedData: ByteArray): ByteArray {
        val cipher = Cipher.getInstance(PAIR_ALGORITHM)
        cipher.init(Cipher.DECRYPT_MODE, keyPair.private)
        return cipher.doFinal(encryptedData)
    }

    companion object {
        const val SHARED_SECRET_ALGORITHM = "AES/CFB8/NoPadding"
        const val PAIR_ALGORITHM = "RSA"
    }
}