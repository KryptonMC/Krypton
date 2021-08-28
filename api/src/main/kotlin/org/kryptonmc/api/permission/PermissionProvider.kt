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
 * https://github.com/VelocityPowered/Velocity/blob/65db0fad6a221205ec001f1f68a032215da402d6/api/src/main/java/com/velocitypowered/api/permission/PermissionProvider.java
 */
package org.kryptonmc.api.permission

/**
 * Provides [PermissionFunction]s for [Subject]s.
 */
fun interface PermissionProvider : (Subject) -> PermissionFunction {

    /**
     * Creates a [PermissionFunction] for the given [subject].
     *
     * @param subject the subject
     * @return the permission function for the given subject
     */
    fun createFunction(subject: Subject): PermissionFunction

    override fun invoke(p1: Subject) = createFunction(p1)
}
