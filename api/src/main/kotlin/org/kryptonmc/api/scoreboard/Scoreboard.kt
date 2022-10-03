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
import org.kryptonmc.api.util.Buildable
import java.util.function.Consumer

/**
 * A [Scoreboard] is a method of keeping track of scores.
 * These are primarily for use in minigames.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface Scoreboard : Buildable<Scoreboard.Builder, Scoreboard> {

    /**
     * All objectives registered on this scoreboard.
     */
    @get:JvmName("objectives")
    public val objectives: Collection<Objective>

    /**
     * All teams tracked by this scoreboard.
     */
    @get:JvmName("teams")
    public val teams: Collection<Team>

    /**
     * All scores tracked by this scoreboard.
     */
    @get:JvmName("scores")
    public val scores: Collection<Score>

    /**
     * Gets the objective with the given [name], or returns null if there are
     * no objectives registered with this scoreboard with the given [name].
     *
     * @param name the name of the registered objective
     * @return the registered objective, or null if not present
     */
    public fun objective(name: String): Objective?

    /**
     * Gets the objective in the given [slot], or returns null if there are no
     * objectives registered with this scoreboard in the given [slot].
     *
     * @param slot the slot the objective may be in
     * @return the registered objective, or null if not present
     */
    public fun objective(slot: DisplaySlot): Objective?

    /**
     * Gets all objectives with the given [criterion] that are registered with
     * this scoreboard.
     *
     * @param criterion the criterion
     * @return all objectives with the criterion
     */
    public fun objectives(criterion: Criterion): Set<Objective>

    /**
     * Adds the given [objective] to this scoreboard's list of registered
     * objectives.
     *
     * @param objective the objective
     * @throws IllegalArgumentException if an objective with the name of the
     * given objective is already registered with this scoreboard
     */
    public fun addObjective(objective: Objective)

    /**
     * Removes the given [objective] from this scoreboard's list of registered
     * objectives.
     *
     * @param objective the objective
     */
    public fun removeObjective(objective: Objective)

    /**
     * Updates the objective in the given [slot] to be the given [objective],
     * clearing the objective at the given [slot] if the given [objective] is
     * null.
     *
     * @param objective the objective, null to clear
     * @param slot the slot
     */
    public fun updateSlot(objective: Objective?, slot: DisplaySlot)

    /**
     * Clears any objective in the given [slot].
     *
     * @param slot the slot to clear
     */
    public fun clearSlot(slot: DisplaySlot)

    /**
     * Gets all scores with the given [name] that are tracked by this
     * scoreboard, across all objectives.
     *
     * If the same [Score] has been added to multiple objectives, it will only
     * appear once in the set. Duplicates will not be found in the set.
     *
     * @param name the name
     * @return all scores with the name
     */
    public fun scores(name: Component): Set<Score>

    /**
     * Removes all scores with the given [name] from the set of all scores
     * tracked by this scoreboard.
     *
     * @param name the name
     */
    public fun removeScores(name: Component)

    /**
     * Gets the team with the given [name] if there is one registered with this
     * scoreboard, or returns null if there is not.
     *
     * @param name the name of the team
     * @return the team with the name, or null if not present
     */
    public fun team(name: String): Team?

    /**
     * Gets the team that the given [member] is in on this scoreboard, or
     * returns null if there is no team with the given [member] in it.
     *
     * @param member the member
     * @return the team of the member, or null if not present
     */
    public fun memberTeam(member: Component): Team?

    /**
     * Adds the given [team] to this scoreboard's list of registered teams.
     *
     * @param team the team
     * @throws IllegalArgumentException if there is a registered team with the
     * same name as the given team, or if the given team is already registered
     * to a scoreboard
     */
    public fun addTeam(team: Team)

    /**
     * Removes the given [team] from this scoreboard's list of registered
     * teams.
     *
     * @param team the team
     */
    public fun removeTeam(team: Team)

    /**
     * A builder for scoreboards.
     */
    @ScoreboardDsl
    public interface Builder : AbstractBuilder<Scoreboard> {

        /**
         * Adds the given [objective] to the list of registered objectives for
         * the scoreboard.
         *
         * @param objective the objective
         * @return this builder
         */
        @ScoreboardDsl
        @Contract("_ -> this", mutates = "this")
        public fun objective(objective: Objective): Builder

        /**
         * Adds the given [objectives] to the list of objectives for the
         * scoreboard.
         *
         * @param objectives the objectives
         * @return this builder
         */
        @ScoreboardDsl
        @Contract("_ -> this", mutates = "this")
        public fun objectives(vararg objectives: Objective): Builder

        /**
         * Adds the given [objectives] to the list of objectives for the
         * scoreboard.
         *
         * @param objectives the objectives
         * @return this builder
         */
        @ScoreboardDsl
        @Contract("_ -> this", mutates = "this")
        public fun objectives(objectives: Iterable<Objective>): Builder

        /**
         * Adds the given [team] to the list of registered teams for the
         * scoreboard.
         *
         * @param team the team
         * @return this builder
         */
        @ScoreboardDsl
        @Contract("_ -> this", mutates = "this")
        public fun team(team: Team): Builder

        /**
         * Creates a new team builder with the given values, applies the given
         * [builder] function to the new builder, and then adds the built
         * objective to the list of registered objectives.
         *
         * @param builder the builder
         * @return this builder
         */
        @ScoreboardDsl
        @JvmSynthetic
        @Contract("_, _ -> this", mutates = "this")
        public fun team(name: String, builder: Team.Builder.() -> Unit): Builder = team(Team.builder(name).apply(builder).build())

        /**
         * Creates a new team builder with the given values, applies the given
         * [builder] function to the new builder, and then adds the built
         * objective to the list of registered objectives.
         *
         * @param builder the builder
         * @return this builder
         */
        @ScoreboardDsl
        @Contract("_, _ -> this", mutates = "this")
        public fun team(name: String, builder: Consumer<Team.Builder>): Builder = team(name) { builder.accept(this) }

        /**
         * Adds the given [teams] to the list of teams for the scoreboard.
         *
         * @param teams the teams
         * @return this builder
         */
        @ScoreboardDsl
        @Contract("_ -> this", mutates = "this")
        public fun teams(vararg teams: Team): Builder

        /**
         * Adds the given [teams] to the list of teams for the scoreboard.
         *
         * @param teams the teams
         * @return this builder
         */
        @ScoreboardDsl
        @Contract("_ -> this", mutates = "this")
        public fun teams(teams: Iterable<Team>): Builder
    }

    @ApiStatus.Internal
    public interface Factory {

        public fun builder(): Builder
    }

    public companion object {

        /**
         * Creates a new builder for building a scoreboard.
         *
         * @return a new builder
         */
        @JvmStatic
        @Contract("-> new", pure = true)
        public fun builder(): Builder = Krypton.factory<Factory>().builder()
    }
}
