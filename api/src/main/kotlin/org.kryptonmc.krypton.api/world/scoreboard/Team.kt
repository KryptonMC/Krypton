package org.kryptonmc.krypton.api.world.scoreboard

import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.api.entity.entities.Player

/**
 * Represents a [Scoreboard] team.
 *
 * Teams are groups of entities that have a name, prefix,
 * suffix, colour, and a specific set of rules they follow.
 *
 * @author Callum Seabrook
 */
data class Team(
    val name: String,
    val displayName: Component,
    val allowFriendlyFire: Boolean,
    val areInvisibleMembersVisible: Boolean,
    val options: Map<Option, OptionApplication>,
    val color: TeamColor,
    val prefix: Component,
    val suffix: Component,
    val members: List<Player>
)

/**
 * Represents options for teams.
 *
 * @author Callum Seabrook
 */
enum class Option {

    /**
     * Whether or not nametags are visible
     */
    NAMETAG_VISIBILITY,

    /**
     * Whether or not death messages are visible
     */
    DEATH_MESSAGE_VISIBILITY,

    /**
     * Whether or not players will collide with each other
     */
    COLLISION_RULE
}

/**
 * Represents how an [Option] is applied.
 *
 * @author Callum Seabrook
 */
enum class OptionApplication {

    /**
     * Always apply this option.
     */
    ALWAYS,

    /**
     * Never apply this option.
     */
    NEVER,

    /**
     * Only apply this option to other teams.
     */
    OTHER_TEAMS,

    /**
     * Only apply this option to our own team.
     */
    OWN_TEAM
}

/**
 * Represents the colour of a team. This is used because team colours are
 * legacy colours, and because they have special IDs.
 *
 * [id] is the protocol ID of this team colour. This should only need to
 * be used internally.
 *
 * @author Callum Seabrook
 */
enum class TeamColor(val id: Int) {

    /**
     * Colors
     */
    BLACK(0),
    DARK_BLUE(1),
    DARK_GREEN(2),
    DARK_CYAN(3),
    DARK_RED(4),
    PURPLE(5),
    GOLD(6),
    GRAY(7),
    DARK_GRAY(8),
    BLUE(9),
    BRIGHT_GREEN(10),
    CYAN(11),
    RED(12),
    PINK(13),
    YELLOW(14),
    WHITE(15),

    /**
     * Decoration
     */
    OBFUSCATED(16),
    BOLD(17),
    STRIKETHROUGH(18),
    UNDERLINED(19),
    ITALIC(20),

    /**
     * Other
     */
    RESET(21)
}