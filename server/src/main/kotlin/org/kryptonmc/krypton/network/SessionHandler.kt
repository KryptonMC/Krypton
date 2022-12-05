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
package org.kryptonmc.krypton.network

import com.velocitypowered.natives.util.Natives
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.Channel
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.timeout.TimeoutException
import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.locale.Messages
import org.kryptonmc.krypton.network.handlers.HandshakeHandler
import org.kryptonmc.krypton.network.handlers.PacketHandler
import org.kryptonmc.krypton.network.handlers.PlayHandler
import org.kryptonmc.krypton.network.netty.GroupedPacketHandler
import org.kryptonmc.krypton.network.netty.PacketCompressor
import org.kryptonmc.krypton.network.netty.PacketDecoder
import org.kryptonmc.krypton.network.netty.PacketDecompressor
import org.kryptonmc.krypton.network.netty.PacketDecrypter
import org.kryptonmc.krypton.network.netty.PacketEncoder
import org.kryptonmc.krypton.network.netty.PacketEncrypter
import org.kryptonmc.krypton.packet.CachedPacket
import org.kryptonmc.krypton.packet.FramedPacket
import org.kryptonmc.krypton.packet.GenericPacket
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.packet.PacketState
import org.kryptonmc.krypton.packet.out.login.PacketOutLoginDisconnect
import org.kryptonmc.krypton.packet.out.login.PacketOutSetCompression
import org.kryptonmc.krypton.packet.out.play.PacketOutDisconnect
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.util.writeFramedPacket
import java.net.InetSocketAddress
import java.net.SocketAddress
import java.util.concurrent.Executor
import javax.crypto.SecretKey

/**
 * A combination of the old channel handler and session classes. This handles
 * both in one class, and offers minor improvements over the previous two, such
 * as the absence of `lateinit` values.
 */
class SessionHandler(private val server: KryptonServer) : SimpleChannelInboundHandler<Packet>(), NetworkSession {

    private var channel: Channel? = null
    private var latency = 0
    @Volatile
    private var currentState = PacketState.HANDSHAKE
    @Volatile
    private var handler: PacketHandler? = null
    @Volatile
    private var compressionEnabled = false

    @Volatile
    private var tickBuffer = Unpooled.directBuffer()
    private val tickBufferLock = Any()

    private fun channel(): Channel = checkNotNull(channel) { "Cannot call channel before it is initialized!" }

    fun executor(): Executor = channel().eventLoop()

    override fun connectAddress(): SocketAddress = channel().remoteAddress()

    fun currentState(): PacketState = currentState

    override fun latency(): Int = latency

    fun enableCompression() {
        val channel = channel()
        val pipeline = channel.pipeline()
        val threshold = server.config.server.compressionThreshold

        // Check for existing encoders and decoders
        var encoder = pipeline.get(PacketCompressor.NETTY_NAME) as? PacketCompressor
        var decoder = pipeline.get(PacketDecompressor.NETTY_NAME) as? PacketDecompressor
        if (encoder != null && decoder != null) {
            encoder.threshold = threshold
            decoder.threshold = threshold
            return
        }

        // Tell the client to update its compression threshold and create our compressor
        writeAndFlush(PacketOutSetCompression(threshold))
        compressionEnabled = true
        val compressor = Natives.compress.get().create(4)
        encoder = PacketCompressor(compressor, threshold)
        decoder = PacketDecompressor(compressor, threshold)

        // Add the compressor and decompressor to our pipeline
        pipeline.addBefore(PacketDecoder.NETTY_NAME, PacketDecompressor.NETTY_NAME, decoder)
        pipeline.addBefore(PacketEncoder.NETTY_NAME, PacketCompressor.NETTY_NAME, encoder)
    }

    /**
     * Creates the ciphers for encryption and decryption with the given [key].
     * The key should be using an AES stream cipher with the key given from the
     * encryption response packet.
     */
    fun enableEncryption(key: SecretKey) {
        val cipher = Natives.cipher.get()
        val encrypter = PacketEncrypter(cipher.forEncryption(key))
        val decrypter = PacketDecrypter(cipher.forDecryption(key))
        val pipeline = channel().pipeline()
        pipeline.addBefore(GroupedPacketHandler.NETTY_NAME, PacketDecrypter.NETTY_NAME, decrypter)
        pipeline.addBefore(GroupedPacketHandler.NETTY_NAME, PacketEncrypter.NETTY_NAME, encrypter)
    }

    fun tickHandler() {
        val handler = handler ?: return
        if (handler is PlayHandler) handler.tick()
    }

    fun changeState(newState: PacketState, newHandler: PacketHandler) {
        currentState = newState
        handler = newHandler
    }

    fun updateLatency(lastKeepAlive: Long) {
        latency = (latency * 3 + (System.currentTimeMillis() - lastKeepAlive).toInt()) / 3
    }

    private fun compressionThreshold(): Int = if (compressionEnabled) server.config.server.compressionThreshold else -1

    override fun send(packet: Packet) {
        write(packet)
    }

    override fun write(packet: GenericPacket) {
        when (packet) {
            is Packet -> {
                synchronized(tickBufferLock) {
                    if (tickBuffer.refCnt() <= 0) return
                    tickBuffer.writeFramedPacket(packet, compressionThreshold())
                }
            }
            is FramedPacket -> {
                synchronized(tickBufferLock) {
                    if (tickBuffer.refCnt() <= 0) return
                    val body = packet.body
                    tickBuffer.writeBytes(body, body.readerIndex(), body.readableBytes())
                }
            }
            is CachedPacket -> {
                synchronized(tickBufferLock) {
                    if (tickBuffer.refCnt() <= 0) return
                    val body = packet.body()
                    tickBuffer.writeBytes(body, body.readerIndex(), body.readableBytes())
                    body.release()
                }
            }
            else -> throw UnsupportedOperationException("Unsupported message type $packet!")
        }
    }

    fun writeAndFlush(packet: Packet) {
        writeWaitingPackets()
        channel().writeAndFlush(packet)
    }

    fun flush() {
        val bufferSize = tickBuffer.writerIndex()
        if (bufferSize <= 0 || !channel().isActive) return
        writeWaitingPackets()
        channel().flush()
    }

    fun disconnect(reason: Component) {
        when (currentState) {
            PacketState.PLAY -> playDisconnect(reason)
            PacketState.LOGIN -> loginDisconnect(reason)
            else -> LOGGER.debug("Attempted to disconnect from state $currentState")
        }
    }

    fun loginDisconnect(reason: Component) {
        writeAndFlush(PacketOutLoginDisconnect(reason))
        doDisconnect()
    }

    fun playDisconnect(reason: Component) {
        writeAndFlush(PacketOutDisconnect(reason))
        doDisconnect()
    }

    private fun doDisconnect() {
        handler?.onDisconnect()
        val channel = channel()
        if (channel.isOpen) channel.close().awaitUninterruptibly()
    }

    override fun channelActive(ctx: ChannelHandlerContext) {
        if (channel != null) error("Channel already initialized!")
        channel = ctx.channel()
        handler = HandshakeHandler(server, this)
        ctx.channel().config().isAutoRead = true
    }

    override fun channelInactive(ctx: ChannelHandlerContext) {
        disconnect(END_OF_STREAM)
        synchronized(tickBufferLock) { tickBuffer.release() }
    }

    override fun channelRead0(ctx: ChannelHandlerContext, msg: Packet) {
        if (!ctx.channel().isOpen) return
        handler?.handle(msg)
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        if (!ctx.channel().isOpen) return
        if (cause is TimeoutException) {
            val address = ctx.channel().remoteAddress() as InetSocketAddress
            LOGGER.debug("Connection from ${address.address}:${address.port} timed out!", cause)
            disconnect(TIMEOUT)
            return
        }

        LOGGER.debug("Failed to send or received invalid packet!", cause)
        disconnect(Messages.Disconnect.GENERIC_REASON.build("Internal Exception: $cause"))
        ctx.channel().config().isAutoRead = false
    }

    private fun writeWaitingPackets() {
        if (tickBuffer.writerIndex() == 0) return
        val copy: ByteBuf
        synchronized(tickBufferLock) {
            if (tickBuffer.refCnt() <= 0) return
            copy = tickBuffer
            tickBuffer = tickBuffer.alloc().buffer(tickBuffer.writerIndex())
        }
        channel().write(FramedPacket(copy)).addListener { copy.release() }
    }

    companion object {

        const val NETTY_NAME: String = "handler"
        private val LOGGER = logger<SessionHandler>()
        private val END_OF_STREAM = Messages.Disconnect.END_OF_STREAM.build()
        private val TIMEOUT = Messages.Disconnect.TIMEOUT.build()
    }
}
