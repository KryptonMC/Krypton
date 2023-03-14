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

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.security.PublicKey
import java.security.Signature
import java.security.SignatureException

fun interface SignatureValidator {

    fun validate(payload: ByteArray, updater: SignatureUpdater): Boolean

    fun validate(payload: ByteArray, signature: ByteArray): Boolean = validate(signature) { it.update(payload) }

    companion object {

        @JvmField
        val LOGGER: Logger = LogManager.getLogger()
        @JvmField
        val YGGDRASIL: SignatureValidator = from(YggdrasilSessionKey.get().createSignature())

        @JvmStatic
        fun from(publicKey: PublicKey, algorithm: String): SignatureValidator =
            from(Signature.getInstance(algorithm).apply { initVerify(publicKey) })

        private fun from(signature: Signature): SignatureValidator = SignatureValidator { payload, updater ->
            try {
                updater.update { signature.update(it) }
                signature.verify(payload)
            } catch (exception: SignatureException) {
                LOGGER.error("Failed to verify signature!", exception)
                false
            }
        }
    }
}
