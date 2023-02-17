/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api

import net.kyori.adventure.audience.ForwardingAudience
import org.kryptonmc.api.command.CommandManager
import org.kryptonmc.api.command.ConsoleSender
import org.kryptonmc.api.command.Sender
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.api.event.GlobalEventNode
import org.kryptonmc.api.plugin.PluginManager
import org.kryptonmc.api.registry.RegistryHolder
import org.kryptonmc.api.scheduling.Scheduler
import org.kryptonmc.api.service.ServicesManager
import org.kryptonmc.api.util.FactoryProvider
import org.kryptonmc.api.world.WorldManager
import org.kryptonmc.api.scoreboard.Scoreboard
import org.kryptonmc.api.user.UserManager
import java.util.UUID

/**
 * The server is the centre of the API. It provides access to everything, and
 * is the central manager for most of the server.
 *
 * You can use the server to retrieve information, managers for various aspects
 * of the API, configuration options, status, and players.
 */
public interface Server : ForwardingAudience {

    /**
     * Information about this server implementation.
     */
    public val platform: Platform

    /**
     * The world manager for this server.
     *
     * The world manager is used to load, save, create and update
     * worlds.
     */
    public val worldManager: WorldManager

    /**
     * The command manager for this server.
     *
     * This is used to register and unregister commands.
     */
    public val commandManager: CommandManager

    /**
     * The plugin manager for this server.
     *
     * This is used to retrieve plugins and check whether they have
     * been initialised or not.
     */
    public val pluginManager: PluginManager

    /**
     * The services manager for this server.
     */
    public val servicesManager: ServicesManager

    /**
     * The global event node for the server.
     *
     * This is the root of the event tree. All other event nodes should be
     * registered with this node, or they will not receive any events from
     * the server.
     *
     * You should not register any event listeners to this node. It is
     * designed for calling events and registering child nodes.
     *
     * If you wish to register listeners, use the event node that is provided
     * with your plugin, which can be injected in to your plugin's main class.
     */
    public val eventNode: GlobalEventNode

    /**
     * The global registry holder for the server.
     */
    public val registryHolder: RegistryHolder

    /**
     * The factory provider for this server.
     *
     * This provides factories from the implementation, usually used to
     * construct certain things, like an object, or a builder.
     */
    public val factoryProvider: FactoryProvider

    /**
     * The user manager for this server.
     */
    public val userManager: UserManager

    /**
     * The scheduler for this server.
     *
     * This can be used to run and schedule asynchronous tasks.
     */
    public val scheduler: Scheduler

    /**
     * The configuration for the server.
     */
    public val config: ServerConfig

    /**
     * The list of online players.
     */
    public val players: Collection<Player>

    /**
     * The console's [Sender] object.
     */
    public val console: ConsoleSender

    /**
     * The server's [Scoreboard], or null if there isn't one.
     */
    public val scoreboard: Scoreboard

    /**
     * Gets the online player with the given [uuid], or returns null if there
     * is no player online with the given [uuid].
     *
     * @param uuid the UUID
     * @return the player, or null if not present
     */
    public fun getPlayer(uuid: UUID): Player?

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
    public fun getPlayer(name: String): Player?
}
