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
