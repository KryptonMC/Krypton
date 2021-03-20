package org.kryptonmc.krypton.api.event.events.play

import org.kryptonmc.krypton.api.command.Sender
import org.kryptonmc.krypton.api.event.Event

data class PermissionCheckEvent(
    val sender: Sender,
    val permission: String
) : Event {

    var result = when {
        sender.hasPermission(permission) -> PermissionCheckResult.TRUE
        permission in sender.permissions -> PermissionCheckResult.FALSE
        else -> PermissionCheckResult.UNSET
    }
}

/**
 * The result of a permission check
 *
 * @author Callum Seabrook
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