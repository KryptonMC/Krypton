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
 * A function that calculates the permission settings for a given [Subject].
 */
fun interface PermissionFunction : (String) -> TriState {

    /**
     * Gets the value for the given [permission].
     *
     * @param permission the permission
     * @return the value for the given permission
     */
    operator fun get(permission: String): TriState

    override fun invoke(p1: String) = get(p1)

    companion object {

        /**
         * A permission function that always returns [TriState.TRUE].
         */
        @JvmField
        val ALWAYS_TRUE = PermissionFunction { TriState.TRUE }

        /**
         * A permission function that always returns [TriState.FALSE].
         */
        @JvmField
        val ALWAYS_FALSE = PermissionFunction { TriState.FALSE }

        /**
         * A permission function that always returns [TriState.NOT_SET].
         */
        @JvmField
        val ALWAYS_NOT_SET = PermissionFunction { TriState.NOT_SET }
    }
}
