package org.kryptonmc.krypton.api.world.scoreboard.criteria

/**
 * Represents a single criteria, also known as a [Criterion]
 *
 * See [here](https://minecraft.gamepedia.com/Scoreboard#Single_criteria) for details
 *
 * @author Callum Seabrook
 */
enum class Criterion(override val isMutable: Boolean) : Criteria {

    DUMMY(true),
    TRIGGER(true),
    DEATH_COUNT(true),
    PLAYER_KILL_COUNT(true),
    TOTAL_KILL_COUNT(true),
    HEALTH(false),
    XP(false),
    LEVEL(false),
    FOOD(false),
    AIR(false),
    ARMOR(false)
}