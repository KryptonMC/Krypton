/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api

import net.kyori.adventure.text.Component.text
import org.kryptonmc.api.world.scoreboard.Objective
import org.kryptonmc.api.world.scoreboard.RenderType
import org.kryptonmc.api.world.scoreboard.Score
import org.kryptonmc.api.world.scoreboard.ScoreboardPosition
import org.kryptonmc.api.world.scoreboard.Team
import org.kryptonmc.api.world.scoreboard.TeamColor
import org.kryptonmc.api.world.scoreboard.criteria.Criterion
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ScoreboardTests {

    private val objective = Objective("test", text("test"), Criterion.AIR, RenderType.INTEGER)

    @Test
    fun `test objective data retention`() {
        assertEquals("test", objective.name)
        assertEquals(text("test"), objective.displayName)
        assertEquals(Criterion.AIR, objective.criteria)
        assertEquals(RenderType.INTEGER, objective.renderType)
    }

    @Test
    fun `test score data retention`() {
        val score = Score(player, objective, 10)
        assertEquals(player, score.player)
        assertEquals(objective, score.objective)
        assertEquals(10, score.score)
    }

    @Suppress("BooleanLiteralArgument")
    @Test
    fun `test team data retention`() {
        val team = Team(
            "test",
            text("test"),
            allowFriendlyFire = true,
            canSeeInvisibleMembers = true,
            options = emptyMap(),
            color = TeamColor.BLACK,
            prefix = text("hello"),
            suffix = text("world"),
            members = listOf(player)
        )
        assertEquals("test", team.name)
        assertEquals(text("test"), team.displayName)
        assertTrue(team.allowFriendlyFire)
        assertTrue(team.canSeeInvisibleMembers)
        assertEquals(emptyMap(), team.options)
        assertEquals(TeamColor.BLACK, team.color)
        assertEquals(text("hello"), team.prefix)
        assertEquals(text("world"), team.suffix)
        assertEquals(listOf(player), team.members)

        val otherTeam = Team(
            team.name,
            team.displayName,
            false,
            false,
            team.options,
            team.color,
            team.prefix,
            team.suffix,
            team.members
        )
        assertFalse(otherTeam.allowFriendlyFire)
        assertFalse(otherTeam.canSeeInvisibleMembers)
    }

    @Test
    fun `test ids`() {
        assertEquals(2, ScoreboardPosition.BELOW_NAME.id)
        assertFalse(Criterion.HEALTH.isMutable)
        assertTrue(Criterion.DUMMY.isMutable)
    }
}
