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
public fun interface Subject {

    /**
     * Gets the value for the given [permission].
     *
     * For some subtypes of this interface, this may always be a constant
     * value, generally indicating that the type does not possess permissions.
     *
     * @param permission the permission
     * @return the value for the given permission
     */
    public fun getPermissionValue(permission: String): TriState

    /**
     * Checks if this subject has the given [permission].
     *
     * A subject having a permission is defined as the subject both possessing
     * the permission, and it being set to [TriState.TRUE].
     *
     * @param permission the permission
     * @return true if this subject has the permission, false otherwise
     */
    public fun hasPermission(permission: String): Boolean = getPermissionValue(permission) == TriState.TRUE

    /**
     * Converts this subject to its equivalent Adventure permission checker.
     *
     * @return the permission checker
     */
    public fun asPermissionChecker(): PermissionChecker = PermissionChecker { getPermissionValue(it) }
}
