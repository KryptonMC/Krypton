/*
 * This file is part of the Krypton project, licensed under the GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
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
