/*
 * This file is part of the Krypton project, and originates from the Velocity project,
 * licensed under the Apache License v2.0
 *
 * Copyright (C) 2018 Velocity Contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
