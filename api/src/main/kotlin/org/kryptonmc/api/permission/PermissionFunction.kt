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
 * https://github.com/VelocityPowered/Velocity/blob/65db0fad6a221205ec001f1f68a032215da402d6/api/src/main/java/com/velocitypowered/api/permission/PermissionFunction.java
 */
package org.kryptonmc.api.permission

import net.kyori.adventure.util.TriState

/**
 * A function that provides permission settings.
 */
public fun interface PermissionFunction {

    /**
     * Gets the value for the given [permission].
     *
     * @param permission the permission
     * @return the value
     */
    public fun getPermissionValue(permission: String): TriState

    public companion object {

        /**
         * A permission function that always returns [TriState.TRUE].
         */
        @JvmField
        public val ALWAYS_TRUE: PermissionFunction = PermissionFunction { TriState.TRUE }

        /**
         * A permission function that always returns [TriState.FALSE].
         */
        @JvmField
        public val ALWAYS_FALSE: PermissionFunction = PermissionFunction { TriState.FALSE }

        /**
         * A permission function that always returns [TriState.NOT_SET].
         */
        @JvmField
        public val ALWAYS_NOT_SET: PermissionFunction = PermissionFunction { TriState.NOT_SET }
    }
}
