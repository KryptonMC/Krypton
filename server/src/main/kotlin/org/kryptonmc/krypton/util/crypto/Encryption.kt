/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton.util.crypto

import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.PublicKey
import javax.crypto.Cipher

/**
 * A utility for generating the server's key pair and decrypting data with the
 * public key.
 *
 * See [here](https://wiki.vg/Protocol_Encryption#Key_Exchange)
 */
object Encryption {

    const val SYMMETRIC_ALGORITHM: String = "AES"
    private const val ASYMMETRIC_ALGORITHM: String = "RSA"
    private const val ASYMMETRIC_BITS: Int = 1024
    const val SIGNATURE_ALGORITHM: String = "SHA256withRSA"

    private val keyPair = generateKeyPair()
    val publicKey: PublicKey = keyPair.public

    @JvmStatic
    fun decrypt(encryptedData: ByteArray): ByteArray {
        val cipher = Cipher.getInstance(ASYMMETRIC_ALGORITHM)
        cipher.init(Cipher.DECRYPT_MODE, keyPair.private)
        return cipher.doFinal(encryptedData)
    }

    @JvmStatic
    private fun generateKeyPair(): KeyPair {
        val generator = KeyPairGenerator.getInstance(ASYMMETRIC_ALGORITHM)
        generator.initialize(ASYMMETRIC_BITS)
        return generator.generateKeyPair()
    }
}
