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

import org.apache.logging.log4j.Logger
import org.kryptonmc.krypton.util.logger
import java.security.PublicKey
import java.security.Signature
import java.security.SignatureException

fun interface SignatureValidator {

    fun validate(payload: ByteArray, updater: SignatureUpdater): Boolean

    fun validate(payload: ByteArray, signature: ByteArray): Boolean = validate(signature) { it.update(signature) }

    companion object {

        @JvmField
        val LOGGER: Logger = logger<SignatureValidator>()
        @JvmField
        val YGGDRASIL: SignatureValidator = from(YggdrasilSessionKey.get().createSignature())

        @JvmStatic
        fun from(publicKey: PublicKey, algorithm: String): SignatureValidator =
            from(Signature.getInstance(algorithm).apply { initVerify(publicKey) })

        private fun from(signature: Signature): SignatureValidator = SignatureValidator { payload, updater ->
            try {
                updater.update(signature::update)
                signature.verify(payload)
            } catch (exception: SignatureException) {
                LOGGER.error("Failed to verify signature!", exception)
                false
            }
        }
    }
}
