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
package org.kryptonmc.krypton.network.netty

import com.velocitypowered.natives.encryption.VelocityCipher
import com.velocitypowered.natives.util.MoreByteBufUtils
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToMessageEncoder

/**
 * Encrypts packets using a stream cipher provided by the specified [cipher].
 *
 * Thanks Velocity for the native stuff! :)
 */
class PacketEncrypter(private val cipher: VelocityCipher) : MessageToMessageEncoder<ByteBuf>() {

    override fun encode(ctx: ChannelHandlerContext, message: ByteBuf, out: MutableList<Any>) {
        val compatible = MoreByteBufUtils.ensureCompatible(ctx.alloc(), cipher, message)
        try {
            cipher.process(compatible)
            out.add(compatible)
        } catch (exception: Exception) {
            compatible.release()
            throw exception
        }
    }

    companion object {

        const val NETTY_NAME: String = "encrypter"
    }
}
