/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.api.scoreboard

import net.kyori.adventure.text.Component
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.scoreboard.criteria.Criterion

/**
 * A [Scoreboard] is a method of keeping track of scores.
 * These are primarily for use in minigames.
 */
public interface Scoreboard {

    /**
     * All objectives registered on this scoreboard.
     */
    public val objectives: Collection<Objective>

    /**
     * All teams tracked by this scoreboard.
     */
    public val teams: Collection<Team>

    /**
     * All scores tracked by this scoreboard.
     */
    public val scores: Collection<Score>

    /**
     * Gets the objective with the given [name], or returns null if there are
     * no objectives registered with this scoreboard with the given [name].
     *
     * @param name the name of the registered objective
     * @return the registered objective, or null if not present
     */
    public fun getObjective(name: String): Objective?

    /**
     * Gets the objective in the given [slot], or returns null if there are no
     * objectives registered with this scoreboard in the given [slot].
     *
     * @param slot the slot the objective may be in
     * @return the registered objective, or null if not present
     */
    public fun getObjective(slot: DisplaySlot): Objective?

    /**
     * Gets all objectives with the given [criterion] that are registered with
     * this scoreboard.
     *
     * @param criterion the criterion
     * @return all objectives with the criterion
     */
    public fun getObjectives(criterion: Criterion): Set<Objective>

    /**
     * Creates a new builder for building an objective that will be registered
     * to this scoreboard.
     *
     * @return a new objective builder
     */
    @Contract("_ -> new", pure = true)
    public fun createObjectiveBuilder(): Objective.Builder

    /**
     * Creates a new objective with the given [name], [criterion],
     * [displayName], and [renderType], and adds it to this scoreboard.
     *
     * @param name the name
     * @param criterion the criterion
     * @param displayName the display name
     * @param renderType the render type
     * @return the created objective
     * @throws IllegalArgumentException if an objective with the given name is
     * already registered with this scoreboard
     */
    public fun addObjective(name: String, criterion: Criterion, displayName: Component, renderType: ObjectiveRenderType): Objective

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
    public fun getScores(name: Component): Set<Score>

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
    public fun getTeam(name: String): Team?

    /**
     * Gets the team that the given [member] is in on this scoreboard, or
     * returns null if there is no team with the given [member] in it.
     *
     * @param member the member
     * @return the team of the member, or null if not present
     */
    public fun getMemberTeam(member: Component): Team?

    /**
     * Creates a new builder for building a team with the given [name] that
     * will be registered to this scoreboard.
     *
     * @param name the name of the team
     * @return a new team builder
     */
    public fun createTeamBuilder(name: String): Team.Builder

    /**
     * Creates a new team with the given [name] and adds it to the list of
     * registered teams for this scoreboard.
     *
     * @param name the name of the team
     * @throws IllegalArgumentException if there is a registered team with the
     * same name as the given name
     */
    public fun addTeam(name: String): Team

    /**
     * Gets the existing team with the given [name], or creates a new team with
     * the given [name] and adds it to the list of registered teams for this
     * scoreboard.
     *
     * @param name the name of the team
     * @return the existing team, or the new team if there was no existing one
     */
    public fun getOrAddTeam(name: String): Team

    /**
     * Removes the given [team] from this scoreboard's list of registered
     * teams.
     *
     * @param team the team
     */
    public fun removeTeam(team: Team)
}
