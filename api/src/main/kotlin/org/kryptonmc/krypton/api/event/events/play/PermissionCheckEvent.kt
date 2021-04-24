package org.kryptonmc.krypton.api.event.events.play

import org.kryptonmc.krypton.api.command.Sender
import org.kryptonmc.krypton.api.event.Event

/**
 * Called when a permission check is made (when [Sender.hasPermission] is called, or
 * when a command checks its permission)
 *
 * @param sender the sender who's permission is being checked
 * @param permission the permission being checked, or null if there isn't a permission
 * @param has if the player has the permission
 */
data class PermissionCheckEvent(
    val sender: Sender,
    val permission: String?,
    private val has: Boolean
) : Event {

    /**
     * The result of the permission check
     */
    var result = when {
        permission == null -> PermissionCheckResult.TRUE
        has -> PermissionCheckResult.TRUE
        permission in sender.permissions -> PermissionCheckResult.FALSE
        else -> PermissionCheckResult.UNSET
    }
}

/**
 * The result of a permission check
 */
enum class PermissionCheckResult(val value: Boolean) {

    /**
     * Indicates this permission is set and true
     */
    TRUE(true),

    /**
     * Indicates this permission is set and false
     */
    FALSE(false),

    /**
     * Indicates this permission is unset
     */
    UNSET(false)
}
