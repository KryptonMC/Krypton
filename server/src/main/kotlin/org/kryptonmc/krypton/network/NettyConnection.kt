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
import io.netty.util.AttributeKey
import net.kyori.adventure.text.Component
import org.apache.logging.log4j.LogManager
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.locale.DisconnectMessages
import org.kryptonmc.krypton.network.handlers.PacketHandler
import org.kryptonmc.krypton.network.handlers.PlayPacketHandler
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

    // Lateinit is perfect here, since most of the time this will be non-null, we want to throw an exception if it isn't initialized when we need
    // it to be, and we want the clean usage of lateinit and not the old way of doing things
    @Suppress("LateinitUsage")
    private lateinit var channel: Channel
    private var latency = 0
    @Volatile
    private var handler: PacketHandler? = null
    @Volatile
    private var compressionEnabled = false
    private var handlingFault = false

    @Volatile
    private var tickBuffer = Unpooled.directBuffer()
    private val tickBufferLock = Any()

    fun executor(): Executor = channel.eventLoop()

    override fun connectAddress(): SocketAddress = channel.remoteAddress()

    private fun currentState(): PacketState = channel.attr(PACKET_STATE_ATTRIBUTE).get()

    fun setState(newState: PacketState) {
        channel.attr(PACKET_STATE_ATTRIBUTE).set(newState)
        channel.config().isAutoRead = true
    }

    override fun latency(): Int = latency

    fun enableCompression() {
        val threshold = server.config.server.compressionThreshold

        // Check for existing encoders and decoders
        var encoder = channel.pipeline().get(PacketCompressor.NETTY_NAME) as? PacketCompressor
        var decoder = channel.pipeline().get(PacketDecompressor.NETTY_NAME) as? PacketDecompressor
        if (encoder != null && decoder != null) {
            encoder.threshold = threshold
            decoder.threshold = threshold
            return
        }

        // Tell the client to update its compression threshold and create our compressor
        writeNow(PacketOutSetCompression(threshold))
        compressionEnabled = true
        val compressor = Natives.compress.get().create(4)
        encoder = PacketCompressor(compressor, threshold)
        decoder = PacketDecompressor(compressor, threshold)

        // Add the compressor and decompressor to our pipeline
        channel.pipeline().addBefore(PacketDecoder.NETTY_NAME, PacketDecompressor.NETTY_NAME, decoder)
        channel.pipeline().addBefore(PacketEncoder.NETTY_NAME, PacketCompressor.NETTY_NAME, encoder)
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
        channel.pipeline().addBefore(GroupedPacketHandler.NETTY_NAME, PacketDecrypter.NETTY_NAME, decrypter)
        channel.pipeline().addBefore(GroupedPacketHandler.NETTY_NAME, PacketEncrypter.NETTY_NAME, encrypter)
    }

    fun inPlayState(): Boolean = handler is PlayPacketHandler

    fun playHandler(): PlayPacketHandler =
        checkNotNull(handler as? PlayPacketHandler) { "Attempted to use handler as play handler before the handler was changed! This is a bug!" }

    fun tick() {
        val handler = handler
        if (handler is TickablePacketHandler) handler.tick()
        flush()
    }

    fun setHandler(handler: PacketHandler) {
        this.handler = handler
    }

    fun updateLatency(lastKeepAlive: Long) {
        latency = (latency * 3 + (System.currentTimeMillis() - lastKeepAlive).toInt()) / 3
    }

    fun setReadOnly() {
        channel.config().isAutoRead = false
    }

    override fun send(packet: Packet) {
        writePacket(packet)
    }

    override fun write(packet: GenericPacket) {
        when (packet) {
            is Packet -> writePacket(packet)
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
                    if (body != null) {
                        tickBuffer.writeBytes(body, body.readerIndex(), body.readableBytes())
                    } else {
                        writePacket(packet.packet())
                    }
                }
            }
            else -> throw UnsupportedOperationException("Unsupported message type $packet!")
        }
    }

    private fun writePacket(packet: Packet) {
        synchronized(tickBufferLock) {
            if (tickBuffer.refCnt() > 0) PacketFraming.writeFramedPacket(tickBuffer, packet)
        }
    }

    fun writeAndDisconnect(packet: Packet, disconnectReason: Component) {
        writeAndFlush(packet) { disconnect(disconnectReason) }
    }

    /**
     * This exists for certain packets that need to be written out immediately, usually because
     * they need to happen before a change of state, e.g. the login success packet, which needs to
     * be sent before the state is changed to PLAY.
     */
    fun writeNow(packet: Packet) {
        writeAndFlush(packet, null)
    }

    private fun writeAndFlush(packet: Packet, listener: ChannelFutureListener?) {
        writeWaitingPackets()
        val future = channel.writeAndFlush(packet)
        if (listener != null) future.addListener(listener)
        future.addListener(FIRE_EXCEPTION_ON_FAILURE_UNLESS_CLOSED)
    }

    private fun flush() {
        val bufferSize = tickBuffer.writerIndex()
        if (bufferSize <= 0 || !channel.isActive) return
        writeWaitingPackets()
        channel.flush()
    }

    fun disconnect(message: Component) {
        if (channel.isOpen) channel.close().awaitUninterruptibly()
        handler?.onDisconnect(message)
    }

    override fun channelActive(ctx: ChannelHandlerContext) {
        super.channelActive(ctx)
        channel = ctx.channel()
        server.connectionManager.register(this)
        try {
            setState(PacketState.HANDSHAKE)
        } catch (exception: Throwable) {
            LOGGER.error("Failed to change packet state to handshake!", exception)
        }
    }

    override fun channelInactive(ctx: ChannelHandlerContext) {
        disconnect(DisconnectMessages.END_OF_STREAM)
        synchronized(tickBufferLock) { tickBuffer.release() }
        server.connectionManager.unregister(this)
    }

    override fun channelRead0(ctx: ChannelHandlerContext, msg: Packet) {
        if (msg !is InboundPacket<*>) error("Received outbound packet $msg! This is a bug!")
        if (!channel.isOpen) return
        try {
            handleCap(msg, handler!!)
        } catch (_: RejectedExecutionException) {
            disconnect(Component.translatable("multiplayer.disconnect.server_shutdown"))
        } catch (_: ClassCastException) {
            // We could possibly throw and catch a different exception, however it's cleaner in the client code if we just try to do a generic
            // cast to the handler type and catch that if it fails.
            LOGGER.error("Received invalid packet from ${connectAddress()}!")
            disconnect(Component.translatable("multiplayer.disconnect.invalid_packet"))
        }
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        val noExistingFault = !handlingFault
        handlingFault = true
        if (!channel.isOpen) return

        if (cause is TimeoutException) {
            LOGGER.debug("Connection from ${channel.remoteAddress()} timed out!", cause)
            disconnect(DisconnectMessages.TIMEOUT)
            return
        }

        val reason = Component.translatable("disconnect.genericReason", Component.text("Internal Exception: $cause"))
        if (noExistingFault) {
            LOGGER.debug("Failed to send packet or received invalid packet!", cause)
            val packet = if (currentState() == PacketState.LOGIN) PacketOutLoginDisconnect(reason) else PacketOutDisconnect(reason)
            writeAndDisconnect(packet, reason)
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
        channel.write(FramedPacket(copy)).addListener { copy.release() }
    }

    companion object {

        const val NETTY_NAME: String = "handler"
        @JvmField
        val PACKET_STATE_ATTRIBUTE: AttributeKey<PacketState> = AttributeKey.valueOf("state")
        // Took this from Netty's ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE
        // The reason why we don't use that is because if it tries to fire an exception when the channel has been closed, we will get console spam
        // because there won't be a handler in the pipeline to handle the exception.
        private val FIRE_EXCEPTION_ON_FAILURE_UNLESS_CLOSED = ChannelFutureListener {
            // If the channel is closed, we will get an endless stream of exceptions.
            if (!it.channel().isOpen) return@ChannelFutureListener
            if (!it.isSuccess) it.channel().pipeline().fireExceptionCaught(it.cause())
        }

        private val LOGGER = LogManager.getLogger()

        @JvmStatic
        private fun <H : PacketHandler> handleCap(packet: InboundPacket<H>, handler: PacketHandler) {
            @Suppress("UNCHECKED_CAST")
            packet.handle(handler as H)
        }
    }
}
