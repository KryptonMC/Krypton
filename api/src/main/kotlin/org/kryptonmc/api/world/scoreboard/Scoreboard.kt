/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.world.scoreboard

import org.kryptonmc.api.entity.entities.Player

/**
 * A [Scoreboard] is a method of keeping track of scores.
 * These are primarily for use in minigames.
 */
interface Scoreboard {

    /**
     * The position of this scoreboard.
     */
    val position: ScoreboardPosition

    /**
     * The name of this [Scoreboard].
     */
    val name: String

    /**
     * The list of [Objective]s registered on this [Scoreboard].
     */
    val objectives: List<Objective>

    /**
     * The list of [Team]s tracked by this [Scoreboard].
     */
    val teams: List<Team>

    /**
     * All the [Score]s for every player who has a score on
     * this scoreboard.
     */
    val scores: Map<Player, Score>

    /**
     * The set of players tracked by this [Scoreboard].
     */
    val players: Set<Player>

    /**
     * Registers a new [Objective] for this [Scoreboard].
     *
     * @param objective the [Objective] to register
     * @return the registered objective
     */
    fun registerObjective(objective: Objective): Objective

    /**
     * Retrieves a [Team] by its name, or null if there is no team with the specified
     * [name].
     *
     * @param name the name of the team
     * @return the team with the given [name], or null if there isn't a team with the [name]
     */
    fun team(name: String): Team?

    /**
     * Retrieves the given [player]'s [Team] in this [Scoreboard].
     *
     * @param player the player to find the team for
     * @return the team of the given [player], or null, if the specified player is not in a
     * [Team] in this [Scoreboard]
     */
    fun playerTeam(player: Player): Team?

    /**
     * Removes all [Score]s for the given [player] on this [Scoreboard].
     *
     * @param player the [Player] who's [Score]s should be removed
     */
    fun resetScores(player: Player)
}

/**
 * Represents positions that a [Scoreboard] may appear in.
 *
 * @param id the ID of this position, should only need to be used internally
 */
enum class ScoreboardPosition(val id: Int) {

    LIST(0),
    SIDEBAR(1),
    BELOW_NAME(2),
    TEAM_SPECIFIC(-1)
}
