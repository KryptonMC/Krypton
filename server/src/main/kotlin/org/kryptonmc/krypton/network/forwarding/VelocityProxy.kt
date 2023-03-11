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
import io.netty.buffer.ByteBuf
import kotlinx.collections.immutable.PersistentList
import org.kryptonmc.api.auth.ProfileProperty
import org.kryptonmc.krypton.auth.KryptonProfileProperty
import org.kryptonmc.krypton.util.readAvailableBytes
import org.kryptonmc.krypton.util.readNullable
import org.kryptonmc.krypton.util.readPersistentList
import org.kryptonmc.krypton.util.readString
import java.security.MessageDigest
import java.util.UUID
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object VelocityProxy {

    private const val SIGNATURE_ALGORITHM = "HmacSHA256"
    private const val SIGNATURE_BYTES = 32
    const val MODERN_FORWARDING_WITH_KEY: Int = 2
    const val MAX_SUPPORTED_FORWARDING_VERSION: Int = MODERN_FORWARDING_WITH_KEY

    @JvmStatic
    fun verifyIntegrity(buf: ByteBuf, secret: ByteArray): Boolean {
        val signature = buf.readAvailableBytes(SIGNATURE_BYTES)

        val data = ByteArray(buf.readableBytes())
        buf.getBytes(buf.readerIndex(), data, 0, buf.readableBytes())

        val mac = Mac.getInstance(SIGNATURE_ALGORITHM)
        mac.init(SecretKeySpec(secret, SIGNATURE_ALGORITHM))
        val mySignature = mac.doFinal(data)
        return MessageDigest.isEqual(signature, mySignature)
    }

    @JvmStatic
    fun readData(buf: ByteBuf): VelocityForwardedData {
        val address = InetAddresses.forString(buf.readString())
        return VelocityForwardedData(address, UUID(buf.readLong(), buf.readLong()), buf.readString(16), readProperties(buf))
    }

    @JvmStatic
    private fun readProperties(buf: ByteBuf): PersistentList<ProfileProperty> {
        return buf.readPersistentList { KryptonProfileProperty(buf.readString(), buf.readString(), buf.readNullable(ByteBuf::readString)) }
    }
}
