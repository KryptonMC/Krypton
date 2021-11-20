/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
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
import org.kryptonmc.krypton.auth.KryptonProfileProperty
import org.kryptonmc.krypton.util.readAvailableBytes
import org.kryptonmc.krypton.util.readString
import org.kryptonmc.krypton.util.readVarInt
import java.security.MessageDigest
import java.util.UUID
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

private const val SUPPORTED_FORWARDING_VERSION = 1

fun ByteBuf.verifyVelocityIntegrity(secret: ByteArray): Boolean {
    val signature = readAvailableBytes(32)

    val data = ByteArray(readableBytes())
    getBytes(readerIndex(), data, 0, readableBytes())

    val mac = Mac.getInstance("HmacSHA256")
    mac.init(SecretKeySpec(secret, "HmacSHA256"))
    val mySignature = mac.doFinal(data)
    if (!MessageDigest.isEqual(signature, mySignature)) return false

    val version = readVarInt()
    return version == SUPPORTED_FORWARDING_VERSION
}

fun ByteBuf.readVelocityProperties(): List<KryptonProfileProperty> {
    val properties = mutableListOf<KryptonProfileProperty>()
    repeat(readVarInt()) {
        val name = readString()
        val value = readString()
        val signature = if (readBoolean()) readString() else ""
        properties += KryptonProfileProperty(name, value, signature)
    }
    return properties
}

fun ByteBuf.readVelocityData(): VelocityForwardedData = VelocityForwardedData(
    InetAddresses.forString(readString()),
    UUID(readLong(), readLong()),
    readString(16),
    readVelocityProperties()
)
