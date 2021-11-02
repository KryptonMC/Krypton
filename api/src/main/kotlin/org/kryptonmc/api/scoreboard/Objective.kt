/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.scoreboard

import net.kyori.adventure.text.Component
import net.kyori.adventure.util.Buildable
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.scoreboard.criteria.Criterion
import org.kryptonmc.api.util.provide

/**
 * An objective for a scoreboard, that has optional criteria that must be met,
 * and information about what it's called and how it should be rendered.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface Objective : Buildable<Objective, Objective.Builder> {

    /**
     * The name of this objective.
     */
    @get:JvmName("name")
    public val name: String

    /**
     * The name that is displayed on the scoreboard to clients.
     */
    @get:JvmName("displayName")
    public var displayName: Component

    /**
     * The criterion that must be met for this objective.
     */
    @get:JvmName("criterion")
    public val criterion: Criterion

    /**
     * The setting for how this objective should be displayed on the
     * scoreboard.
     */
    @get:JvmName("renderType")
    public val renderType: ObjectiveRenderType

    /**
     * A builder for objectives.
     */
    public interface Builder : Buildable.Builder<Objective> {

        /**
         * Sets the name of the objective to the given [name].
         *
         * @param name the name
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun name(name: String): Builder

        /**
         * Sets the display name of the objective to the given [name].
         *
         * @param name the display name
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun displayName(name: Component): Builder

        /**
         * Sets the criterion for the objective to the given [criterion].
         *
         * @param criterion the criterion
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun criterion(criterion: Criterion): Builder

        /**
         * Sets the render type for the objective to the given [type].
         *
         * @param type the render type
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun renderType(type: ObjectiveRenderType): Builder
    }

    @ApiStatus.Internal
    public interface Factory {

        public fun builder(name: String, criterion: Criterion): Builder
    }

    public companion object {

        private val FACTORY = Krypton.factoryProvider.provide<Factory>()

        /**
         * Creates a new builder for building an objective.
         *
         * @param name the name
         * @param criterion the criterion
         * @return a new builder
         */
        @JvmStatic
        @Contract("-> new", pure = true)
        public fun builder(name: String, criterion: Criterion): Builder = FACTORY.builder(name, criterion)
    }
}
