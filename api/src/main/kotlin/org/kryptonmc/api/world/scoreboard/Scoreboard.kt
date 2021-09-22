/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.world.scoreboard

import org.kryptonmc.api.entity.player.Player

/**
 * A [Scoreboard] is a method of keeping track of scores.
 * These are primarily for use in minigames.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface Scoreboard {

    /**
     * The position of this scoreboard.
     */
    @get:JvmName("position")
    public val position: ScoreboardPosition

    /**
     * The name of this [Scoreboard].
     */
    @get:JvmName("name")
    public val name: String

    /**
     * The list of [Objective]s registered on this [Scoreboard].
     */
    @get:JvmName("objectives")
    public val objectives: List<Objective>

    /**
     * The list of [Team]s tracked by this [Scoreboard].
     */
    @get:JvmName("teams")
    public val teams: List<Team>

    /**
     * All the [Score]s for every player who has a score on
     * this scoreboard.
     */
    @get:JvmName("scores")
    public val scores: Map<Player, Score>

    /**
     * The set of players tracked by this [Scoreboard].
     */
    @get:JvmName("players")
    public val players: Set<Player>

    /**
     * Registers a new [Objective] for this [Scoreboard].
     *
     * @param objective the [Objective] to register
     * @return the registered objective
     */
    public fun registerObjective(objective: Objective): Objective

    /**
     * Retrieves a [Team] by its name, or null if there is no team with the
     * specified [name].
     *
     * @param name the name of the team
     * @return the team with the given [name], or null if there isn't a team
     * with the [name]
     */
    public fun team(name: String): Team?

    /**
     * Retrieves the given [player]'s [Team] in this [Scoreboard].
     *
     * @param player the player to find the team for
     * @return the team of the given [player], or null, if the specified player
     * is not in a [Team] in this [Scoreboard]
     */
    public fun playerTeam(player: Player): Team?

    /**
     * Removes all [Score]s for the given [player] on this [Scoreboard].
     *
     * @param player the [Player] who's [Score]s should be removed
     */
    public fun resetScores(player: Player)
}
