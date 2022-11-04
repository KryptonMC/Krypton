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
 * https://github.com/VelocityPowered/Velocity/blob/65db0fad6a221205ec001f1f68a032215da402d6/api/src/main/java/com/velocitypowered/api/event/permission/PermissionsSetupEvent.java
 */
package org.kryptonmc.api.event.server

import org.kryptonmc.api.permission.PermissionFunction
import org.kryptonmc.api.permission.PermissionProvider
import org.kryptonmc.api.permission.Subject

/**
 * Called when the given [subject]'s permissions are initially being set up.
 */
public interface SetupPermissionsEvent {

    /**
     * The subject that is having their permissions set up.
     */
    public val subject: Subject

    /**
     * The provider that should be used to provide permissions for the subject.
     */
    public var provider: PermissionProvider

    /**
     * Resets the provider back to the default provider that the server would
     * use if this event did not modify the provider.
     */
    public fun resetProvider()

    /**
     * Gets a permission function for the given [subject] using the provider
     * from this event.
     *
     * @param subject the subject to get a function for
     * @return the permission function
     */
    public fun createFunction(subject: Subject): PermissionFunction
}
