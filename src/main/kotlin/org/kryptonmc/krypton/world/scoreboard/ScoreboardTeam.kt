package org.kryptonmc.krypton.world.scoreboard

import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.entity.entities.Entity
import org.kryptonmc.krypton.entity.entities.Player

// TODO: Add support for entity members back once Player extends Entity
data class ScoreboardTeam(
    val name: String,
    val displayName: Component,
    val flags: TeamFlags,
    val nametagVisibility: NametagVisibility,
    val collisionRule: CollisionRule,
    val color: TeamColor,
    val prefix: Component,
    val suffix: Component,
    val members: MutableList<Player>
)

data class TeamFlags(
    val allowFriendlyFire: Boolean,
    val invisiblePlayersVisible: Boolean // if team members can see other members who are invisible
) {

    fun toProtocol(): Int {
        var byte = 0x0
        if (allowFriendlyFire) byte += 0x01
        if (invisiblePlayersVisible) byte += 0x02
        return byte
    }
}

enum class NametagVisibility(val id: String) {

    ALWAYS("always"),
    HIDE_FOR_OTHER_TEAMS("hideForOtherTeams"),
    HIDE_FOR_OWN_TEAM("hideForOwnTeam"),
    NEVER("never")
}

enum class CollisionRule(val id: String) {

    ALWAYS("always"),
    PUSH_OTHER_TEAMS("pushOtherTeams"),
    PUSH_OWN_TEAM("pushOwnTeam"),
    NEVER("never")
}

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