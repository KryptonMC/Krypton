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
package org.kryptonmc.krypton.network.forwarding

import com.google.common.net.InetAddresses
import org.kryptonmc.krypton.network.buffer.BinaryReader
import java.security.MessageDigest
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object VelocityProxy {

    private const val SIGNATURE_ALGORITHM = "HmacSHA256"
    private const val SIGNATURE_BYTES = 32
    private const val MODERN_FORWARDING_WITH_KEY = 2
    const val MAX_SUPPORTED_FORWARDING_VERSION: Int = MODERN_FORWARDING_WITH_KEY

    @JvmStatic
    fun verifyIntegrity(reader: BinaryReader, secret: ByteArray): Boolean {
        val signature = reader.readBytes(SIGNATURE_BYTES)
        val data = reader.readAllBytes()

        val mac = Mac.getInstance(SIGNATURE_ALGORITHM)
        mac.init(SecretKeySpec(secret, SIGNATURE_ALGORITHM))
        val mySignature = mac.doFinal(data)
        return MessageDigest.isEqual(signature, mySignature)
    }

    @JvmStatic
    fun readData(reader: BinaryReader): VelocityForwardedData {
        val address = InetAddresses.forString(reader.readString())
        val uuid = reader.readUUID()
        val name = reader.readString()
        require(name.length <= 16) { "Name too long! Max: 16" }
        return VelocityForwardedData(address, uuid, name, reader.readProfileProperties())
    }
}
