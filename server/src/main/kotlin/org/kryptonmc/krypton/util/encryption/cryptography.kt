package org.kryptonmc.krypton.util.encryption

import java.math.BigInteger
import java.security.Key
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec

/**
 * Convert a [Key] to a [Cipher] that is set for decryption
 */
fun Key.toDecryptingCipher(algorithm: String): Cipher = toCipher(algorithm, Cipher.DECRYPT_MODE)

/**
 * Convert a [Key] to a [Cipher] that is set for encryption
 */
fun Key.toEncryptingCipher(algorithm: String): Cipher = toCipher(algorithm, Cipher.ENCRYPT_MODE)

/**
 * Digest a [MessageDigest] into a hex string. This is Yggdrasil's strange way of digesting to hex
 */
fun MessageDigest.hexDigest(): String = BigInteger(digest()).toString(16)

private fun Key.toCipher(algorithm: String, operation: Int): Cipher {
    val cipher = Cipher.getInstance(algorithm)
    cipher.init(operation, this, IvParameterSpec(encoded))
    return cipher
}
