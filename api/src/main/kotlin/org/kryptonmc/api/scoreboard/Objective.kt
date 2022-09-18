/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.scoreboard

import net.kyori.adventure.builder.AbstractBuilder
import net.kyori.adventure.text.Component
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.scoreboard.criteria.Criterion

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
    @ScoreboardDsl
    public interface Builder {

        /**
         * Sets the name of the objective to the given [name].
         *
         * @param name the name
         * @return this builder
         */
        @ScoreboardDsl
        @Contract("_ -> this", mutates = "this")
        public fun name(name: String): NamedStep

        /**
         * An intermediary step that ensures the name of the objective is set.
         */
        public interface NamedStep {

            /**
             * Sets the criterion for the objective to the given [criterion].
             *
             * @param criterion the criterion
             * @return this builder
             */
            @ScoreboardDsl
            @Contract("_ -> this", mutates = "this")
            public fun criterion(criterion: Criterion): EndStep
        }

        /**
         * The end step that ensures the name and the criterion of the
         * objective are set.
         */
        public interface EndStep : AbstractBuilder<Objective> {

            /**
             * Sets the display name of the objective to the given [name].
             *
             * @param name the display name
             * @return this builder
             */
            @ScoreboardDsl
            @Contract("_ -> this", mutates = "this")
            public fun displayName(name: Component): Builder

            /**
             * Sets the render type for the objective to the given [type].
             *
             * @param type the render type
             * @return this builder
             */
            @ScoreboardDsl
            @Contract("_ -> this", mutates = "this")
            public fun renderType(type: ObjectiveRenderType): Builder
        }
    }

    @ApiStatus.Internal
    public interface Factory {

        public fun builder(): Builder
    }

    public companion object {

        /**
         * Creates a new builder for building an objective.
         *
         * @return a new builder
         */
        @JvmStatic
        @Contract("_, _ -> new", pure = true)
        public fun builder(): Builder = Krypton.factory<Factory>().builder()
    }
}
