/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api

import net.kyori.adventure.audience.ForwardingAudience
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import org.kryptonmc.api.auth.ProfileCache
import org.kryptonmc.api.block.BlockManager
import org.kryptonmc.api.command.CommandManager
import org.kryptonmc.api.command.ConsoleSender
import org.kryptonmc.api.command.Sender
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.api.event.EventManager
import org.kryptonmc.api.item.ItemManager
import org.kryptonmc.api.plugin.PluginManager
import org.kryptonmc.api.registry.RegistryManager
import org.kryptonmc.api.scheduling.Scheduler
import org.kryptonmc.api.service.ServicesManager
import org.kryptonmc.api.world.Difficulty
import org.kryptonmc.api.world.Gamemode
import org.kryptonmc.api.world.WorldManager
import org.kryptonmc.api.world.scoreboard.Scoreboard
import java.net.InetSocketAddress
import java.util.UUID

/**
 * The server.
 */
interface Server : ForwardingAudience {

    /**
     * Information about this server implementation.
     */
    val platform: Platform

    /**
     * The world manager for this server.
     *
     * The world manager is used to load, save, create and update
     * worlds.
     */
    val worldManager: WorldManager

    /**
     * The command manager for this server.
     *
     * This is used to register and unregister commands.
     */
    val commandManager: CommandManager

    /**
     * The plugin manager for this server.
     *
     * This is used to retrieve plugins and check whether they have
     * been initialised or not.
     */
    val pluginManager: PluginManager

    /**
     * The services manager for this server.
     */
    val servicesManager: ServicesManager

    /**
     * The event manager for this server.
     *
     * This is used to register/unregister listeners/handlers for specific
     * events that may be fired, and also to fire said events.
     */
    val eventManager: EventManager

    /**
     * The registry manager for this server.
     *
     * This is used to create new registries and register values to them.
     */
    val registryManager: RegistryManager

    /**
     * The block manager for this server.
     *
     * This is used to retrieve and register block handlers for blocks.
     */
    val blockManager: BlockManager

    /**
     * The item manager for this server.
     *
     * This is used to retrieve and register item handlers for item types.
     */
    val itemManager: ItemManager

    /**
     * The cache of [org.kryptonmc.api.auth.GameProfile]s.
     */
    val profileCache: ProfileCache

    /**
     * The scheduler for this server.
     *
     * This can be used to run and schedule asynchronous tasks.
     */
    val scheduler: Scheduler

    /**
     * The maximum amount of players that can be online at once.
     */
    val maxPlayers: Int

    /**
     * The message of the day for the server.
     */
    val motd: Component

    /**
     * If this server is in online mode, meaning it authenticates
     * players through Mojang.
     */
    val isOnline: Boolean

    /**
     * If this server has hardcore mode enabled.
     */
    val isHardcore: Boolean

    /**
     * The default difficulty of this server.
     */
    val difficulty: Difficulty

    /**
     * The default gamemode of this server.
     */
    val gamemode: Gamemode

    /**
     * The address that this server is currently bound to.
     */
    val address: InetSocketAddress

    /**
     * The list of online players.
     */
    val players: List<Player>

    /**
     * Gets the online player with the given [uuid], or returns null if there is no
     * player online with the given [uuid].
     *
     * @param uuid the UUID
     * @return the player, or null if not present
     */
    fun player(uuid: UUID): Player?

    /**
     * Gets the online player with the given [name], or returns null if there is no
     * player online with the given [name].
     *
     * Warning: Usernames are not unique past a single session. Do **not** use
     * usernames to uniquely identify users!
     *
     * @param name the player's name
     * @return the player, or null if not present
     */
    fun player(name: String): Player?

    /**
     * The console's [Sender] object.
     */
    val console: ConsoleSender

    /**
     * The server's [Scoreboard], or null if there isn't one.
     */
    val scoreboard: Scoreboard?

    /**
     * Sends a message to every player on the server with the specified [permission].
     *
     * @param message the message to send
     * @param permission the permission that players require to receive the message
     */
    fun sendMessage(message: Component, permission: String)

    /**
     * The list of registered plugin messaging channels.
     */
    val channels: Set<Key>

    /**
     * Registers a plugin messaging channel.
     *
     * @param channel the channel to register
     */
    fun registerChannel(channel: Key)

    /**
     * Unregisters a plugin messaging channel.
     *
     * @param channel the channel to register
     */
    fun unregisterChannel(channel: Key)
}
