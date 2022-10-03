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
package org.kryptonmc.krypton.network.data

import com.google.common.net.InetAddresses
import io.netty.buffer.ByteBuf
import kotlinx.collections.immutable.PersistentList
import org.kryptonmc.api.auth.ProfileProperty
import org.kryptonmc.krypton.auth.KryptonProfileProperty
import org.kryptonmc.krypton.entity.player.PlayerPublicKey
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
        return VelocityForwardedData(address, UUID(buf.readLong(), buf.readLong()), buf.readString(16), readProperties(buf), readKey(buf))
    }

    @JvmStatic
    private fun readProperties(buf: ByteBuf): PersistentList<ProfileProperty> =
        buf.readPersistentList { KryptonProfileProperty(buf.readString(), buf.readString(), buf.readNullable(ByteBuf::readString)) }

    @JvmStatic
    private fun readKey(buf: ByteBuf): PlayerPublicKey.Data = PlayerPublicKey.Data(buf)
}
