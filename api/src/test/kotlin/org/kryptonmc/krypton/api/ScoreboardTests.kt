package org.kryptonmc.krypton.api

import net.kyori.adventure.text.Component.text
import org.kryptonmc.krypton.api.world.scoreboard.Objective
import org.kryptonmc.krypton.api.world.scoreboard.RenderType
import org.kryptonmc.krypton.api.world.scoreboard.Score
import org.kryptonmc.krypton.api.world.scoreboard.ScoreboardPosition
import org.kryptonmc.krypton.api.world.scoreboard.Team
import org.kryptonmc.krypton.api.world.scoreboard.TeamColor
import org.kryptonmc.krypton.api.world.scoreboard.criteria.Criterion
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

    @Test
    fun `test team data retention`() {
        val team = Team(
            "test",
            text("test"),
            allowFriendlyFire = true,
            areInvisibleMembersVisible = true,
            options = emptyMap(),
            color = TeamColor.BLACK,
            prefix = text("hello"),
            suffix = text("world"),
            members = listOf(player)
        )
        assertEquals("test", team.name)
        assertEquals(text("test"), team.displayName)
        assertTrue(team.allowFriendlyFire)
        assertTrue(team.areInvisibleMembersVisible)
        assertEquals(emptyMap(), team.options)
        assertEquals(TeamColor.BLACK, team.color)
        assertEquals(text("hello"), team.prefix)
        assertEquals(text("world"), team.suffix)
        assertEquals(listOf(player), team.members)

        val otherTeam = team.copy(allowFriendlyFire = false, areInvisibleMembersVisible = false)
        assertFalse(otherTeam.allowFriendlyFire)
        assertFalse(otherTeam.areInvisibleMembersVisible)
    }

    @Test
    fun `test ids`() {
        assertEquals(2, ScoreboardPosition.BELOW_NAME.id)
        assertFalse(Criterion.HEALTH.isMutable)
        assertTrue(Criterion.DUMMY.isMutable)
    }
}
