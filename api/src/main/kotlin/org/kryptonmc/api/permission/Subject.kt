/*
 * This file is part of the Krypton API, and originates from the Velocity API,
 * licensed under the MIT license.
 *
 * Copyright (C) 2018 Velocity Contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 *
 * For the original file that this file is derived from, see here:
 * https://github.com/VelocityPowered/Velocity/blob/65db0fad6a221205ec001f1f68a032215da402d6/api/src/main/java/com/velocitypowered/api/permission/PermissionSubject.java
 */
package org.kryptonmc.api.permission

import net.kyori.adventure.permission.PermissionChecker
import net.kyori.adventure.util.TriState

/**
 * An object that has a queryable set of permissions.
 */
fun interface Subject : PermissionChecker {

    /**
     * Gets the value for the given [permission].
     *
     * @param permission the permission
     * @return the value for the given permission
     */
    fun getPermissionValue(permission: String): TriState

    /**
     * Returns true if this subject has the given [permission], false otherwise.
     *
     * @param permission the permission
     * @return true if this subject has the permission, false otherwise
     */
    fun hasPermission(permission: String) = getPermissionValue(permission) == TriState.TRUE

    override fun value(permission: String) = getPermissionValue(permission)
}
