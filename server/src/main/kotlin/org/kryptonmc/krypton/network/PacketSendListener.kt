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

import org.kryptonmc.krypton.packet.Packet
import java.util.function.Supplier

/**
 * A listener for when a packet is sent to a player.
 */
interface PacketSendListener {

    /**
     * Called when the packet has been successfully sent to the client without error.
     *
     * By default, this does nothing.
     */
    fun onSuccess() {
        // Nothing by default
    }

    /**
     * Called when the packet was not sent to the client due to an error.
     *
     * This method optionally returns a packet that will be sent to the client in case of an error.
     *
     * By default, this does nothing.
     */
    fun onFailure(): Packet? = null

    companion object {

        /**
         * The returned listener runs the given task on both success and failure, whether the packet was sent or not.
         */
        @JvmStatic
        fun thenRun(task: Runnable): PacketSendListener = object : PacketSendListener {
            override fun onSuccess() {
                task.run()
            }

            override fun onFailure(): Packet? {
                task.run()
                return null
            }
        }

        /**
         * The returned listener returns the result of the given supplier on failure and, as a result, sends that packet.
         */
        @JvmStatic
        fun sendOnFailure(failurePacket: Supplier<Packet?>): PacketSendListener = object : PacketSendListener {
            override fun onFailure(): Packet? = failurePacket.get()
        }
    }
}
