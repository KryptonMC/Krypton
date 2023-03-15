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

import org.apache.logging.log4j.LogManager
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.util.ImmutableLists
import java.io.IOException
import java.net.InetSocketAddress
import java.net.SocketAddress
import java.net.StandardProtocolFamily
import java.net.UnixDomainSocketAddress
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel
import java.nio.file.Files

class NetworkServer(private val server: KryptonServer) {

    @Volatile
    private var stopped = false

    private val selector = Selector.open()
    private val workers = ImmutableLists.ofArray(Array(WORKER_COUNT) { NetworkWorker(server, this) })
    private var index = 0

    private var serverSocket: ServerSocketChannel? = null
    private var socketAddress: SocketAddress? = null
    private var address: String? = null
    private var port = 0

    fun initialize(address: SocketAddress) {
        val family = when (address) {
            is InetSocketAddress -> {
                this.address = address.hostString
                this.port = address.port
                if (address.address.address.size == 4) StandardProtocolFamily.INET else StandardProtocolFamily.INET6
            }
            is UnixDomainSocketAddress -> {
                this.address = "unix://${address.path}"
                this.port = 0
                StandardProtocolFamily.UNIX
            }
            else -> error("Address must be an inet or unix domain socket address!")
        }

        val server = ServerSocketChannel.open(family)
        server.bind(address)
        server.configureBlocking(false)
        server.register(selector, SelectionKey.OP_ACCEPT)
        this.serverSocket = server
        this.socketAddress = address

        if (address is InetSocketAddress && port == 0) port = server.socket().localPort
    }

    fun start() {
        workers.forEach { it.start() }
        Thread({
            while (!stopped) {
                try {
                    selector.select { key ->
                        if (!key.isAcceptable) return@select
                        try {
                            val worker = findWorker()
                            val client = serverSocket!!.accept()
                            worker.handleConnection(client)
                        } catch (exception: IOException) {
                            LOGGER.error("Failed to accept connection!", exception)
                        }
                    }
                } catch (exception: IOException) {
                    LOGGER.error("Error while selecting!", exception)
                }
            }
        }, "Krypton Network Boss").start()
    }

    fun isOpen(): Boolean = !stopped

    fun stop() {
        stopped = true
        try {
            serverSocket?.close()
            val socketAddress = socketAddress
            if (socketAddress is UnixDomainSocketAddress) Files.deleteIfExists(socketAddress.path)
        } catch (exception: IOException) {
            LOGGER.error("Failed to close server socket!", exception)
        }
        selector.wakeup()
        workers.forEach { it.selector.wakeup() }
    }

    private fun findWorker(): NetworkWorker {
        index = ++index % WORKER_COUNT
        return workers.get(index)
    }

    companion object {

        private val LOGGER = LogManager.getLogger()
        private val WORKER_COUNT = Integer.getInteger("krypton.network.workers", Runtime.getRuntime().availableProcessors())
        @JvmField
        val MAX_PACKET_SIZE: Int = Integer.getInteger("krypton.network.max-packet-size", 2_097_152) // Max that can be written in a 3-byte var int
        @JvmField
        val SOCKET_SEND_BUFFER_SIZE: Int = Integer.getInteger("krypton.network.send-buffer-size", 262_143)
        @JvmField
        val SOCKET_RECEIVE_BUFFER_SIZE: Int = Integer.getInteger("krypton.network.receive-buffer-size", 32_767)
    }
}
