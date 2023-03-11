/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.krypton.util.crypto

import java.security.KeyPair
import java.security.KeyPairGenerator
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
