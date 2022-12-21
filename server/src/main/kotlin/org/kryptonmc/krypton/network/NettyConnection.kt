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
import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.timeout.TimeoutException
import net.kyori.adventure.text.Component
import org.apache.logging.log4j.LogManager
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.locale.Messages
import org.kryptonmc.krypton.network.handlers.HandshakeHandler
import org.kryptonmc.krypton.network.handlers.PacketHandler
import org.kryptonmc.krypton.network.handlers.PlayHandler
import org.kryptonmc.krypton.network.handlers.TickablePacketHandler
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
import org.kryptonmc.krypton.packet.InboundPacket
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.packet.PacketState
import org.kryptonmc.krypton.packet.out.login.PacketOutLoginDisconnect
import org.kryptonmc.krypton.packet.out.login.PacketOutSetCompression
import org.kryptonmc.krypton.packet.out.play.PacketOutDisconnect
import org.kryptonmc.krypton.util.PacketFraming
import java.net.InetSocketAddress
import java.net.SocketAddress
import java.util.concurrent.Executor
import java.util.concurrent.RejectedExecutionException
import javax.crypto.SecretKey

/**
 * A combination of the old channel handler and session classes. This handles
 * both in one class, and offers minor improvements over the previous two, such
 * as the absence of `lateinit` values.
 */
class NettyConnection(private val server: KryptonServer) : SimpleChannelInboundHandler<Packet>(), NetworkConnection {

    private var channel: Channel? = null
    private var latency = 0
    @Volatile
    private var currentState = PacketState.HANDSHAKE
    @Volatile
    private var handler: PacketHandler? = null
    @Volatile
    private var compressionEnabled = false
    private var handlingFault = false

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

    fun playHandler(): PlayHandler =
        checkNotNull(handler as? PlayHandler) { "Attempted to use handler as play handler before the handler was changed! This is a bug!" }

    fun tick() {
        val handler = handler
        if (handler is TickablePacketHandler) handler.tick()
        flush()
    }

    fun changeState(newState: PacketState, newHandler: PacketHandler) {
        currentState = newState
        handler = newHandler
    }

    fun updateLatency(lastKeepAlive: Long) {
        latency = (latency * 3 + (System.currentTimeMillis() - lastKeepAlive).toInt()) / 3
    }

    fun setReadOnly() {
        channel().config().isAutoRead = false
    }

    override fun send(packet: Packet) {
        write(packet)
    }

    override fun send(packet: Packet, listener: PacketSendListener) {
        writeAndFlush(packet, listener)
    }

    override fun write(packet: GenericPacket) {
        when (packet) {
            is Packet -> {
                synchronized(tickBufferLock) {
                    if (tickBuffer.refCnt() <= 0) return
                    PacketFraming.writeFramedPacket(tickBuffer, packet)
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
        writeAndFlush(packet, null)
    }

    private fun writeAndFlush(packet: Packet, listener: PacketSendListener?) {
        writeWaitingPackets()
        val channel = channel()
        val future = channel.writeAndFlush(packet)
        if (listener != null) {
            future.addListener {
                if (it.isSuccess) {
                    listener.onSuccess()
                } else {
                    val failPacket = listener.onFailure()
                    if (failPacket != null) channel.writeAndFlush(failPacket).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE)
                }
            }
        }
        future.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE)
    }

    private fun flush() {
        val channel = channel ?: return
        val bufferSize = tickBuffer.writerIndex()
        if (bufferSize <= 0 || !channel.isActive) return
        writeWaitingPackets()
        channel.flush()
    }

    fun disconnect(message: Component) {
        val channel = channel()
        if (channel.isOpen) {
            channel.close().awaitUninterruptibly()
            handler?.onDisconnect(message)
        }
    }

    override fun channelActive(ctx: ChannelHandlerContext) {
        super.channelActive(ctx)
        channel = ctx.channel()
        changeState(PacketState.HANDSHAKE, HandshakeHandler(server, this))
        ctx.channel().config().isAutoRead = true
    }

    override fun channelInactive(ctx: ChannelHandlerContext) {
        disconnect(END_OF_STREAM)
        synchronized(tickBufferLock) { tickBuffer.release() }
    }

    override fun channelRead0(ctx: ChannelHandlerContext, msg: Packet) {
        if (msg !is InboundPacket<*>) error("Received outbound packet $msg! This is a bug!")
        if (!channel().isOpen) return
        try {
            handleCap(msg, handler!!)
        } catch (_: RejectedExecutionException) {
            disconnect(Component.translatable("multiplayer.disconnect.server_shutdown"))
        } catch (_: ClassCastException) {
            // We could possibly throw and catch a different exception, however it's cleaner in the client code if we just try to do a generic
            // cast to the handler type and catch that if it fails.
            LOGGER.error("Received invalid packet from ${connectAddress()}!")
            LOGGER.debug("Invalid packet $msg from ${connectAddress()} in state $currentState with handler $handler")
            disconnect(Component.translatable("multiplayer.disconnect.invalid_packet"))
        }
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        val channel = channel()
        val noExistingFault = !handlingFault
        handlingFault = true
        if (!channel.isOpen) return

        if (cause is TimeoutException) {
            val address = channel.remoteAddress() as InetSocketAddress
            LOGGER.debug("Connection from ${address.address}:${address.port} timed out!", cause)
            disconnect(TIMEOUT)
            return
        }

        val reason = Component.translatable("disconnect.genericReason", Component.text("Internal Exception: $cause"))
        if (noExistingFault) {
            LOGGER.debug("Failed to send packet or received invalid packet!", cause)
            val packet = if (currentState == PacketState.LOGIN) PacketOutLoginDisconnect(reason) else PacketOutDisconnect(reason)
            send(packet, PacketSendListener.thenRun { disconnect(reason) })
            setReadOnly()
        } else {
            LOGGER.debug("Double fault!", cause)
            disconnect(reason)
        }
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
        private val LOGGER = LogManager.getLogger()
        private val END_OF_STREAM = Messages.Disconnect.END_OF_STREAM.build()
        private val TIMEOUT = Messages.Disconnect.TIMEOUT.build()

        @JvmStatic
        private fun <H : PacketHandler> handleCap(packet: InboundPacket<H>, handler: PacketHandler) {
            @Suppress("UNCHECKED_CAST")
            packet.handle(handler as H)
        }
    }
}
