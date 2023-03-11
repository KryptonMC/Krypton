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

import java.security.KeyFactory
import java.security.PublicKey
import java.security.SecureRandom
import java.security.spec.X509EncodedKeySpec
import java.util.Base64

object Crypto {

    private const val RSA_PUBLIC_KEY_HEADER = "-----BEGIN RSA PUBLIC KEY-----"
    private const val RSA_PUBLIC_KEY_FOOTER = "-----END RSA PUBLIC KEY-----"
    private val MIME_ENCODER = Base64.getMimeEncoder(76, "\n".toByteArray())
    private val RSA_KEY_FACTORY = KeyFactory.getInstance("RSA")

    @JvmStatic
    fun publicKeyToRsaString(key: PublicKey): String {
        require(key.algorithm == "RSA") { "Public key must be an RSA key to be converted to an RSA string!" }
        return RSA_PUBLIC_KEY_HEADER + "\n" + MIME_ENCODER.encodeToString(key.encoded) + "\n" + RSA_PUBLIC_KEY_FOOTER + "\n"
    }

    @JvmStatic
    fun bytesToRsaPublicKey(bytes: ByteArray): PublicKey {
        try {
            return RSA_KEY_FACTORY.generatePublic(X509EncodedKeySpec(bytes))
        } catch (exception: Exception) {
            throw CryptException(exception)
        }
    }

    object SaltSupplier {

        private val secureRandom = SecureRandom()

        @JvmStatic
        fun getLong(): Long = secureRandom.nextLong()
    }
}
