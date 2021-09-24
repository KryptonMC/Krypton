/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api

import org.kryptonmc.api.auth.ProfileCache
import org.kryptonmc.api.block.BlockManager
import org.kryptonmc.api.command.CommandManager
import org.kryptonmc.api.event.EventManager
import org.kryptonmc.api.fluid.FluidManager
import org.kryptonmc.api.item.ItemManager
import org.kryptonmc.api.plugin.PluginManager
import org.kryptonmc.api.registry.RegistryManager
import org.kryptonmc.api.scheduling.Scheduler
import org.kryptonmc.api.service.ServicesManager
import org.kryptonmc.api.util.FactoryProvider
import org.kryptonmc.api.world.WorldManager

/**
 * The static singleton accessor for Krypton. Provides static access to the
 * managers and platform, to make them easier to access.
 */
public object Krypton {

    /**
     * The singleton server instance.
     */
    @JvmStatic
    public val server: Server
        get() = internalServer!!

    /**
     * The platform information for the implementation.
     */
    @JvmStatic
    public val platform: Platform
        get() = server.platform

    /**
     * The world manager for the server.
     */
    @JvmStatic
    public val worldManager: WorldManager
        get() = server.worldManager

    /**
     * The command manager for the server.
     */
    @JvmStatic
    public val commandManager: CommandManager
        get() = server.commandManager

    /**
     * The plugin manager for the server.
     */
    @JvmStatic
    public val pluginManager: PluginManager
        get() = server.pluginManager

    /**
     * The services manager for the server.
     */
    @JvmStatic
    public val servicesManager: ServicesManager
        get() = server.servicesManager

    /**
     * The event manager for the server.
     */
    @JvmStatic
    public val eventManager: EventManager
        get() = server.eventManager

    /**
     * The registry manager for the server.
     */
    @JvmStatic
    public val registryManager: RegistryManager
        get() = internalRegistryManager!!

    /**
     * The block manager for the server.
     */
    @JvmStatic
    public val blockManager: BlockManager
        get() = server.blockManager

    /**
     * The item manager for the server.
     */
    @JvmStatic
    public val itemManager: ItemManager
        get() = server.itemManager

    /**
     * The fluid manager for the server.
     */
    @JvmStatic
    public val fluidManager: FluidManager
        get() = server.fluidManager

    /**
     * The profile cache for the server.
     */
    @JvmStatic
    public val profileCache: ProfileCache
        get() = server.profileCache

    /**
     * The scheduler for the server.
     */
    @JvmStatic
    public val scheduler: Scheduler
        get() = server.scheduler

    /**
     * The factory provider for the server.
     */
    @JvmStatic
    public val factoryProvider: FactoryProvider
        get() = internalFactoryProvider!!

    @JvmStatic
    private var internalServer: Server? = null
    @JvmStatic
    private var internalFactoryProvider: FactoryProvider? = null
    @JvmStatic
    private var internalRegistryManager: RegistryManager? = null
}
