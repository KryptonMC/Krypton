/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.world.scoreboard

import net.kyori.adventure.text.Component
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.util.provide
import org.kryptonmc.api.world.scoreboard.criteria.Criterion

/**
 * An objective for a scoreboard, that has optional criteria that must be met,
 * and information about what it's called and how it should be rendered.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface Objective {

    /**
     * The name of this objective.
     */
    @get:JvmName("name")
    public val name: String

    /**
     * The name that is displayed on the scoreboard to clients.
     */
    @get:JvmName("displayName")
    public val displayName: Component

    /**
     * The criterion that must be met for this objective. May be null.
     */
    @get:JvmName("criterion")
    public val criterion: Criterion?

    /**
     * The setting for how this objective should be displayed on the
     * scoreboard.
     */
    @get:JvmName("renderType")
    public val renderType: RenderType

    @Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
    @ApiStatus.Internal
    public interface Factory {

        public fun of(name: String, displayName: Component, criterion: Criterion?, renderType: RenderType): Objective
    }

    public companion object {

        private val FACTORY = Krypton.factoryProvider.provide<Factory>()

        /**
         * Creates a new objective with the given values.
         *
         * @param name the internal name
         * @param displayName the name displayed on the scoreboard to clients
         * @param criterion the criterion that must be met, may be null
         * @param renderType the setting for how it should be displayed on
         * the scoreboard
         * @return a new objective
         */
        @JvmStatic
        @JvmOverloads
        @Contract("_ -> new", pure = true)
        public fun of(
            name: String,
            displayName: Component,
            criterion: Criterion? = null,
            renderType: RenderType = RenderType.INTEGER
        ): Objective = FACTORY.of(name, displayName, criterion, renderType)
    }
}
