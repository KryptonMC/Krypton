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
