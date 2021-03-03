package org.kryptonmc.krypton.encryption

import java.math.BigInteger
import java.security.Key
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec

fun Key.toDecryptingCipher(algorithm: String): Cipher = toCipher(algorithm, Cipher.DECRYPT_MODE)

fun Key.toEncryptingCipher(algorithm: String): Cipher = toCipher(algorithm, Cipher.ENCRYPT_MODE)

fun MessageDigest.hexDigest(): String = BigInteger(this.digest()).toString(16)

private fun Key.toCipher(algorithm: String, operation: Int): Cipher {
    val cipher = Cipher.getInstance(algorithm)
    cipher.init(operation, this, IvParameterSpec(encoded))
    return cipher
}