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
package org.kryptonmc.krypton.network.socket

import net.kyori.adventure.text.Component
import org.apache.logging.log4j.LogManager
import org.jctools.queues.MessagePassingQueue
import org.jctools.queues.MpscUnboundedXaddArrayQueue
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.network.NioConnection
import org.kryptonmc.krypton.network.buffer.BinaryBuffer
import org.kryptonmc.krypton.ticking.TickSchedulerThread
import org.kryptonmc.krypton.util.ObjectPool
import java.io.IOException
import java.net.InetSocketAddress
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.SocketChannel
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

class NetworkWorker(
    private val server: KryptonServer,
    private val networkServer: NetworkServer
) : Thread("Krypton Network Worker ${COUNTER.getAndIncrement()}") {

    val selector: Selector = Selector.open()
    private val connections = ConcurrentHashMap<SocketChannel, NioConnection>()
    private val queue = MpscUnboundedXaddArrayQueue<Runnable>(1024)

    override fun run() {
        while (networkServer.isOpen()) {
            try {
                try {
                    queue.drain { it.run() }
                } catch (exception: IOException) {
                    LOGGER.error("Error while writing packets!", exception)
                }
                for (connection in connections.values) {
                    try {
                        connection.tickHandler()
                        connection.flush()
                    } catch (_: IOException) {
                        connection.disconnect(null)
                    } catch (exception: Exception) {
                        LOGGER.error("Error while reading packets!", exception)
                        disconnectOnError(connection, exception)
                    }
                }
                selector.select({ key ->
                    val channel = key.channel() as SocketChannel
                    if (!channel.isOpen) return@select
                    if (!key.isReadable) return@select

                    val connection = connections.get(channel)
                    if (connection == null) {
                        try {
                            channel.close()
                        } catch (_: IOException) {
                            // Channel failed to close. This won't make a difference to anything, so we don't care.
                        }
                        return@select
                    }

                    try {
                        ObjectPool.PACKET_POOL.hold().use { holder ->
                            val readBuffer = BinaryBuffer.wrap(holder.get())
                            connection.consumeCache(readBuffer)
                            readBuffer.readChannel(channel)
                            connection.processPackets(readBuffer)
                        }
                    } catch (_: IOException) {
                        connection.disconnect(null)
                    } catch (exception: Throwable) {
                        LOGGER.error("Error while processing packets!", exception)
                        connection.disconnect(Component.translatable("disconnect.genericReason", Component.text("Internal Exception: $exception")))
                    }
                }, TickSchedulerThread.MILLIS_PER_TICK)
            } catch (exception: Exception) {
                LOGGER.error("Error while processing worker!", exception)
            }
        }
    }

    private fun disconnectOnError(connection: NioConnection, exception: Throwable) {
        connection.disconnect(Component.translatable("disconnect.genericReason", Component.text("Internal Exception: $exception")))
    }

    fun disconnect(connection: NioConnection, channel: SocketChannel) {
        connections.remove(channel)
        if (channel.isOpen) {
            try {
                connection.flush()
                channel.close()
            } catch (_: IOException) {
                // Socket operation may fail if the socket is already closed
            }
        }
    }

    fun handleConnection(channel: SocketChannel) {
        connections.put(channel, NioConnection(server, this, channel, channel.remoteAddress))
        channel.configureBlocking(false)
        channel.register(selector, SelectionKey.OP_READ)
        if (channel.localAddress is InetSocketAddress) {
            val socket = channel.socket()
            socket.sendBufferSize = NetworkServer.SOCKET_SEND_BUFFER_SIZE
            socket.receiveBufferSize = NetworkServer.SOCKET_RECEIVE_BUFFER_SIZE
            socket.tcpNoDelay = true
            socket.soTimeout = 30 * 1000 // 30 seconds
        }
        selector.wakeup()
    }

    fun queue(): MessagePassingQueue<Runnable> = queue

    companion object {

        private val LOGGER = LogManager.getLogger()
        private val COUNTER = AtomicInteger()
    }
}
