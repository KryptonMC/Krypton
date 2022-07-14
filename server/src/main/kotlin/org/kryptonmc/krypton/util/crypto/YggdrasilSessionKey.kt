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

import java.security.PublicKey
import java.security.Signature

@JvmRecord
data class YggdrasilSessionKey(val publicKey: PublicKey) {

    fun createSignature(): Signature = Signature.getInstance("SHA1withRSA").apply { initVerify(publicKey) }

    companion object {

        private val INSTANCE = load()

        @JvmStatic
        fun get(): YggdrasilSessionKey = INSTANCE

        @JvmStatic
        private fun load(): YggdrasilSessionKey {
            try {
                val keyBytes = YggdrasilSessionKey::class.java.getResourceAsStream("/yggdrasil_session_pubkey.der").readAllBytes()
                return YggdrasilSessionKey(keyBytes.decodeToPublicKey())
            } catch (exception: Exception) {
                throw IllegalStateException("Cannot load Yggdrasil session public key!", exception)
            }
        }
    }
}
