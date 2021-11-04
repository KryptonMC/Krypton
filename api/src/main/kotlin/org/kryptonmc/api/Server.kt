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
import net.kyori.adventure.text.Component
import org.kryptonmc.api.auth.ProfileCache
import org.kryptonmc.api.user.ban.BanManager
import org.kryptonmc.api.block.BlockManager
import org.kryptonmc.api.command.CommandManager
import org.kryptonmc.api.command.ConsoleSender
import org.kryptonmc.api.command.Sender
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.api.event.EventManager
import org.kryptonmc.api.fluid.FluidManager
import org.kryptonmc.api.item.ItemManager
import org.kryptonmc.api.plugin.PluginManager
import org.kryptonmc.api.registry.RegistryManager
import org.kryptonmc.api.scheduling.Scheduler
import org.kryptonmc.api.service.ServicesManager
import org.kryptonmc.api.util.FactoryProvider
import org.kryptonmc.api.world.WorldManager
import org.kryptonmc.api.scoreboard.Scoreboard
import org.kryptonmc.api.tags.TagManager
import org.kryptonmc.api.user.whitelist.WhitelistManager
import java.net.InetSocketAddress
import java.util.UUID

/**
 * The server is the centre of the API. It provides access to everything, and
 * is the central manager for most of the server.
 *
 * You can use the server to retrieve information, managers for various aspects
 * of the API, configuration options, status, and players.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface Server : ForwardingAudience {

    /**
     * Information about this server implementation.
     */
    @get:JvmName("platform")
    public val platform: Platform

    /**
     * The world manager for this server.
     *
     * The world manager is used to load, save, create and update
     * worlds.
     */
    @get:JvmName("worldManager")
    public val worldManager: WorldManager

    /**
     * The command manager for this server.
     *
     * This is used to register and unregister commands.
     */
    @get:JvmName("commandManager")
    public val commandManager: CommandManager

    /**
     * The plugin manager for this server.
     *
     * This is used to retrieve plugins and check whether they have
     * been initialised or not.
     */
    @get:JvmName("pluginManager")
    public val pluginManager: PluginManager

    /**
     * The services manager for this server.
     */
    @get:JvmName("servicesManager")
    public val servicesManager: ServicesManager

    /**
     * The event manager for this server.
     *
     * This is used to register/unregister listeners/handlers for specific
     * events that may be fired, and also to fire said events.
     */
    @get:JvmName("eventManager")
    public val eventManager: EventManager

    /**
     * The registry manager for this server.
     *
     * This is used to create new registries and register values to them.
     */
    @get:JvmName("registryManager")
    public val registryManager: RegistryManager

    /**
     * The tag manager for this server.
     */
    @get:JvmName("tagManager")
    public val tagManager: TagManager

    /**
     * The block manager for this server.
     *
     * This is used to retrieve and register block handlers for blocks.
     */
    @get:JvmName("blockManager")
    public val blockManager: BlockManager

    /**
     * The item manager for this server.
     *
     * This is used to retrieve and register item handlers for item types.
     */
    @get:JvmName("itemManager")
    public val itemManager: ItemManager

    /**
     * The fluid manager for this server.
     *
     * This is used to retrieve and register fluid handlers for fluids.
     */
    @get:JvmName("fluidManager")
    public val fluidManager: FluidManager

    /**
     * The factory provider for this server.
     *
     * This provides factories from the implementation, usually used to
     * construct certain things, like an object, or a builder.
     */
    @get:JvmName("factoryProvider")
    public val factoryProvider: FactoryProvider

    /**
     * The cache of [org.kryptonmc.api.auth.GameProfile]s.
     */
    @get:JvmName("profileCache")
    public val profileCache: ProfileCache

    /**
     * The manager of bans.
     */
    @get:JvmName("banManager")
    public val banManager: BanManager

    /**
     * The manager of the whitelist.
     */
    @get:JvmName("whitelistManager")
    public val whitelistManager: WhitelistManager

    /**
     * The scheduler for this server.
     *
     * This can be used to run and schedule asynchronous tasks.
     */
    @get:JvmName("scheduler")
    public val scheduler: Scheduler

    /**
     * The maximum amount of players that can be online at once.
     */
    @get:JvmName("maxPlayers")
    public val maxPlayers: Int

    /**
     * The message of the day for the server.
     */
    @get:JvmName("motd")
    public val motd: Component

    /**
     * If this server is in online mode, meaning it authenticates
     * players through Mojang.
     */
    public val isOnline: Boolean

    /**
     * The address that this server is currently bound to.
     */
    @get:JvmName("address")
    public val address: InetSocketAddress

    /**
     * The list of online players.
     */
    @get:JvmName("players")
    public val players: List<Player>

    /**
     * The console's [Sender] object.
     */
    @get:JvmName("console")
    public val console: ConsoleSender

    /**
     * The server's [Scoreboard], or null if there isn't one.
     */
    @get:JvmName("scoreboard")
    public val scoreboard: Scoreboard

    /**
     * Gets the online player with the given [uuid], or returns null if there
     * is no player online with the given [uuid].
     *
     * @param uuid the UUID
     * @return the player, or null if not present
     */
    public fun player(uuid: UUID): Player?

    /**
     * Gets the online player with the given [name], or returns null if there
     * is no player online with the given [name].
     *
     * Warning: Usernames are not unique past a single session. Do **not** use
     * usernames to uniquely identify users!
     *
     * @param name the player's name
     * @return the player, or null if not present
     */
    public fun player(name: String): Player?

    /**
     * Sends a message to every player on the server with the given
     * [permission].
     *
     * @param message the message to send
     * @param permission the permission that players require to receive the
     * message
     */
    public fun sendMessage(message: Component, permission: String)
}
