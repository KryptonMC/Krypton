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
package org.kryptonmc.krypton.packet.transformers

import com.velocitypowered.natives.compression.VelocityCompressor
import com.velocitypowered.natives.encryption.JavaVelocityCipher
import com.velocitypowered.natives.util.MoreByteBufUtils
import com.velocitypowered.natives.util.Natives
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder
import org.kryptonmc.krypton.util.varIntBytes
import org.kryptonmc.krypton.util.write21BitVarInt
import org.kryptonmc.krypton.util.writeVarInt

/**
 * Compresses packets that meet or exceed the specified [threshold] in length.
 *
 * Thanks Velocity for the fast var ints and native stuff!
 */
class PacketCompressor(private val compressor: VelocityCompressor, var threshold: Int) : MessageToByteEncoder<ByteBuf>() {

    override fun encode(ctx: ChannelHandlerContext, msg: ByteBuf, out: ByteBuf) {
        val uncompressedSize = msg.readableBytes()
        if (uncompressedSize < threshold) {
            out.writeVarInt(uncompressedSize + 1)
            out.writeVarInt(0)
            out.writeBytes(msg)
            return
        }
        if (MUST_USE_SAFE_AND_SLOW_COMPRESSION_HANDLING) compressSafely(ctx, msg, out) else compressFast(ctx, msg, out)
    }

    private fun compressFast(ctx: ChannelHandlerContext, msg: ByteBuf, out: ByteBuf) {
        val uncompressed = msg.readableBytes()
        out.write21BitVarInt(0)
        out.writeVarInt(uncompressed)
        val compatibleIn = MoreByteBufUtils.ensureCompatible(ctx.alloc(), compressor, msg)

        try {
            compressor.deflate(compatibleIn, out)
        } finally {
            compatibleIn.release()
        }

        val writerIndex = out.writerIndex()
        val packetLength = out.readableBytes() - 3
        out.writerIndex(0)
        out.write21BitVarInt(packetLength)
        out.writerIndex(writerIndex)
    }

    private fun compressSafely(ctx: ChannelHandlerContext, msg: ByteBuf, out: ByteBuf) {
        val uncompressed = msg.readableBytes()
        val tempBuffer = MoreByteBufUtils.preferredBuffer(ctx.alloc(), compressor, uncompressed - 1)
        try {
            tempBuffer.writeVarInt(uncompressed)
            val compatibleIn = MoreByteBufUtils.ensureCompatible(ctx.alloc(), compressor, msg)
            try {
                compressor.deflate(compatibleIn, tempBuffer)
            } finally {
                compatibleIn.release()
            }

            out.writeVarInt(tempBuffer.readableBytes())
            out.writeBytes(tempBuffer)
        } finally {
            tempBuffer.release()
        }
    }

    override fun allocateBuffer(ctx: ChannelHandlerContext, msg: ByteBuf, preferDirect: Boolean): ByteBuf {
        val uncompressed = msg.readableBytes()
        if (uncompressed < threshold) {
            val finalBufferSize = (uncompressed + 1) + (uncompressed + 1).varIntBytes
            return if (IS_JAVA_CIPHER) ctx.alloc().heapBuffer(finalBufferSize) else ctx.alloc().directBuffer(finalBufferSize)
        }

        // (maximum data length after compression) + packet length varint + uncompressed data varint
        val initialBufferSize = (uncompressed - 1) + 3 + uncompressed.varIntBytes
        return MoreByteBufUtils.preferredBuffer(ctx.alloc(), compressor, initialBufferSize)
    }

    override fun handlerRemoved(ctx: ChannelHandlerContext?) = compressor.close()

    companion object {

        const val NETTY_NAME = "compressor"
        // From Velocity
        private val MUST_USE_SAFE_AND_SLOW_COMPRESSION_HANDLING = java.lang.Boolean.getBoolean("krypton.increased-compression-cap")
        val IS_JAVA_CIPHER = Natives.cipher.get() == JavaVelocityCipher.FACTORY
    }
}
