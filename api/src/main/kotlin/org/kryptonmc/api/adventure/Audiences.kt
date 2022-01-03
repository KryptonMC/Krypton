/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.adventure

import net.kyori.adventure.audience.Audience
import org.jetbrains.annotations.ApiStatus
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.api.util.provide
import java.util.function.Predicate

/**
 * A utility for retrieving common audiences.
 */
public object Audiences {

    private val FACTORY = Krypton.factoryProvider.provide<Factory>()

    /**
     * Gets the audience that contains all of the players and the console.
     *
     * @return the server audience
     */
    @JvmStatic
    public fun server(): Audience = FACTORY.server()

    /**
     * Gets the audience that contains all of the currently online players.
     *
     * @return the players audience
     */
    @JvmStatic
    public fun players(): Audience = FACTORY.players()

    /**
     * Gets the audience that contains all of the currently online players
     * that match the given [predicate].
     *
     * @param predicate the filter to filter online players
     * @return the players audience
     */
    @JvmStatic
    public fun players(predicate: Predicate<Player>): Audience = FACTORY.players(predicate)

    /**
     * Gets the audience for the server console.
     *
     * @return the console audience
     */
    @JvmStatic
    public fun console(): Audience = FACTORY.console()

    @ApiStatus.Internal
    public interface Factory {

        public fun server(): Audience

        public fun players(): Audience

        public fun players(predicate: Predicate<Player>): Audience

        public fun console(): Audience
    }
}
