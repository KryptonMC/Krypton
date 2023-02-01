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
