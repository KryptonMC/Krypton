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
package org.kryptonmc.krypton.server.query

import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.KryptonServer.KryptonServerInfo
import org.kryptonmc.krypton.util.NetworkDataOutputStream
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.util.thread.GenericThread
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.SocketAddress
import java.net.SocketTimeoutException
import kotlin.random.Random

/**
 * This query handler is responsible for handling UDP queries sent using the Game Stop 4 (GS4) protocol.
 */
// TODO: Look into Velocity's query handler and possibly convert this to a Netty handler
class GS4QueryHandler private constructor(
    private val server: KryptonServer,
    private val port: Int
) : GenericThread("Query Handler") {

    private val buffer = ByteArray(1460)

    private var lastChallengeCheck = 0L
    private var lastRulesResponse = 0L
    private lateinit var socket: DatagramSocket

    private val validChallenges = mutableMapOf<SocketAddress, RequestChallenge>()
    private val rulesResponse = NetworkDataOutputStream(1460)

    override fun start(): Boolean {
        if (isRunning) return true
        if (!createSocket()) return false
        return super.start()
    }

    override fun run() {
        LOGGER.info("Query running on ${server.config.server.ip}:$port")
        lastChallengeCheck = System.currentTimeMillis()
        val packet = DatagramPacket(buffer, buffer.size)
        try {
            while (isRunning) {
                try {
                    socket.receive(packet)
                    pruneChallenges()
                    packet.process()
                } catch (exception: SocketTimeoutException) {
                    pruneChallenges()
                } catch (exception: IOException) {
                    recoverSocketError(exception)
                }
            }
        } finally {
            LOGGER.debug("Closing socket running on ${server.config.server.ip}:$port")
            socket.close()
        }
    }

    private fun DatagramPacket.send(data: ByteArray) = socket.send(DatagramPacket(data, data.size, socketAddress))

    private fun DatagramPacket.process(): Boolean {
        LOGGER.debug("Packet length $length received from $socketAddress")
        if (length < 3 || data[0] != (-2).toByte() || data[1] != (-3).toByte()) {
            LOGGER.debug("Invalid packet received from $socketAddress")
            return false
        }

        LOGGER.debug("Packet \"${data[2].hex}\" received from $socketAddress")
        when (data[2].toInt()) {
            0 -> {
                if (!isValidChallenge) {
                    LOGGER.debug("$socketAddress failed the challenge (because they suck)")
                    return false
                }
                if (length == 15) {
                    send(buildRuleResponse(this))
                    LOGGER.debug("Sent rules to $socketAddress")
                    return true
                }
                val output = NetworkDataOutputStream(1460)
                output.write(0)
                output.write(validChallenges[socketAddress]!!.identBytes)
                output.write(PlainComponentSerializer.plain().serialize(server.status.motd))
                output.write("SMP")
                output.write(server.config.world.name)
                output.write("${server.players.size}")
                output.write("${server.status.maxPlayers}")
                output.write(server.config.server.port.toShort())
                output.write(server.config.server.ip)
                send(output.byteArray)
                LOGGER.debug("Sent status to $socketAddress")
            }
            9 -> {
                challenge()
                LOGGER.debug("Challenged $socketAddress")
                return true
            }
        }
        return true
    }

    private fun buildRuleResponse(packet: DatagramPacket): ByteArray {
        val timeNow = System.currentTimeMillis()
        if (timeNow < lastRulesResponse + 5000L) {
            val response = rulesResponse.byteArray
            val identBytes = validChallenges[packet.socketAddress]!!.identBytes
            response[1] = identBytes[0]
            response[2] = identBytes[1]
            response[3] = identBytes[2]
            response[4] = identBytes[3]
            return response
        }

        lastRulesResponse = timeNow
        rulesResponse.reset()
        rulesResponse.write(0)
        rulesResponse.write(validChallenges[packet.socketAddress]!!.identBytes)
        rulesResponse.write("splitnum")
        rulesResponse.write(128)
        rulesResponse.write(0)
        rulesResponse.write("hostname")
        rulesResponse.write(PlainComponentSerializer.plain().serialize(server.status.motd))
        rulesResponse.write("gametype")
        rulesResponse.write("SMP")
        rulesResponse.write("game_id")
        rulesResponse.write("KRYPTON")
        rulesResponse.write("version")
        rulesResponse.write(KryptonServerInfo.minecraftVersion)
        rulesResponse.write("plugins")
        rulesResponse.write(plugins)
        rulesResponse.write("map")
        rulesResponse.write(server.config.world.name)
        rulesResponse.write("numplayers")
        rulesResponse.write("${server.players.size}")
        rulesResponse.write("maxplayers")
        rulesResponse.write("${server.status.maxPlayers}")
        rulesResponse.write("hostport")
        rulesResponse.write("${server.config.server.port}")
        rulesResponse.write("hostip")
        rulesResponse.write(server.config.server.ip)
        rulesResponse.write(0)
        rulesResponse.write(1)
        rulesResponse.write("player_")
        rulesResponse.write(0)
        server.players.forEach { rulesResponse.write(it.name) }
        rulesResponse.write(0)
        return rulesResponse.byteArray
    }

    private fun DatagramPacket.challenge() {
        val challenge = RequestChallenge(this)
        validChallenges[socketAddress] = challenge
        send(challenge.challengeBytes)
    }

    private fun pruneChallenges() {
        if (!isRunning) return

        val timeNow = System.currentTimeMillis()
        if (timeNow < lastChallengeCheck + 30000L) return

        lastChallengeCheck = timeNow
        validChallenges.values.removeIf { it.isBefore(timeNow) }
    }

    private fun recoverSocketError(exception: Exception) {
        if (!isRunning) return

        LOGGER.warn("Unexpected exception", exception)
        if (!createSocket()) {
            LOGGER.error("Failed to recover from exception! Shutting down...")
            isRunning = false
        }
    }

    private fun createSocket() = try {
        socket = DatagramSocket(port, InetAddress.getByName(server.config.server.ip))
        socket.soTimeout = 500
        true
    } catch (exception: Exception) {
        LOGGER.warn("Unable to initialise query system on ${server.config.server.ip}:$port", exception)
        false
    }

    private val DatagramPacket.isValidChallenge: Boolean
        get() {
            val address = socketAddress
            if (address !in validChallenges) return false
            return validChallenges[address]?.challenge == data.networkToInt(7, length)
        }

    private val plugins by lazy {
        val result = StringBuilder("Krypton version ${KryptonServerInfo.version}")
        val plugins = server.pluginManager.plugins

        if (plugins.isNotEmpty()) {
            result.append(": ")

            plugins.forEachIndexed { index, plugin ->
                if (index > 0) result.append("; ")
                val context = server.pluginManager.contextOf(plugin)
                result.append(context.description.name).append(" ")
                result.append(context.description.version.replace(";", ","))
            }
        }

        result.toString()
    }

    companion object {

        private val LOGGER = logger<GS4QueryHandler>()

        fun create(server: KryptonServer): GS4QueryHandler? {
            val queryPort = server.config.query.port
            if (queryPort !in 0..65535) {
                LOGGER.warn("Invalid query port! Should be between 0 and 65535, was $queryPort. Querying disabled.")
                return null
            }

            val handler = GS4QueryHandler(server, queryPort)
            if (!handler.start()) return null
            return handler
        }
    }
}

private val HEX_CHARACTERS = "0123456789abcdef".toCharArray()

private val Byte.hex: String
    get() = "" + HEX_CHARACTERS[(this.toInt() and 0xF0) ushr 4] + HEX_CHARACTERS[this.toInt() and 0xF]

private fun ByteArray.networkToInt(index: Int, length: Int): Int {
    if (0 > length - index - 4) return 0
    return (this[index].toInt() shl 24) or ((this[index + 1].toInt() and 0xFF) shl 16) or ((this[index + 2].toInt() and 0xFF) shl 8) or (this[index + 3].toInt() and 0xFF)
}

private class RequestChallenge(packet: DatagramPacket) {

    private val time = System.currentTimeMillis()
    val challenge = Random.nextInt(0x1000000)
    val identBytes = ByteArray(4)
    private val ident: String
    val challengeBytes: ByteArray

    init {
        val data = packet.data
        identBytes[0] = data[3]
        identBytes[1] = data[4]
        identBytes[2] = data[5]
        identBytes[3] = data[6]
        ident = String(identBytes)
        challengeBytes = "\t$ident$challenge\u0000".encodeToByteArray()
    }

    fun isBefore(time: Long) = this.time < time
}
