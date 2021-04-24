package org.kryptonmc.krypton.util.encryption

import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.PublicKey
import javax.crypto.Cipher

/**
 * Object used for generating the server's key pair and decrypting data with the public key
 */
object Encryption {

    /**
     * The algorithm used in the stream cipher
     */
    const val SHARED_SECRET_ALGORITHM = "AES/CFB8/NoPadding"

    /**
     * The algorithm used to generate the key pair for the server
     */
    private const val PAIR_ALGORITHM = "RSA"

    private val keyPair = generateKeyPair()

    val publicKey: PublicKey = keyPair.public

    private fun generateKeyPair(): KeyPair {
        val generator = KeyPairGenerator.getInstance(PAIR_ALGORITHM)
        generator.initialize(1024)
        return generator.generateKeyPair()
    }

    fun decrypt(encryptedData: ByteArray): ByteArray {
        val cipher = Cipher.getInstance(PAIR_ALGORITHM)
        cipher.init(Cipher.DECRYPT_MODE, keyPair.private)
        return cipher.doFinal(encryptedData)
    }
}
