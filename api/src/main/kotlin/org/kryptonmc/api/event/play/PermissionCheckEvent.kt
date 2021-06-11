/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.event.play

import org.kryptonmc.api.command.Sender

/**
 * Called when a permission check is made (when [Sender.hasPermission] is called, or
 * when a command checks its permission)
 *
 * @param sender the sender who's permission is being checked
 * @param permission the permission being checked, or null if there isn't a permission
 * @param has if the player has the permission
 */
// TODO: Make this a ResultedEvent (or remove it) when the new permissions system is implemented
class PermissionCheckEvent(
    val sender: Sender,
    val permission: String?,
    private val has: Boolean
) {

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
 *
 * @param value the value as a boolean
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
