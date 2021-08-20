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

import org.kryptonmc.api.permission.PermissionProvider
import org.kryptonmc.api.permission.Subject

/**
 * Called when the given [subject]'s permissions are initially being set up.
 *
 * @param subject the subject
 * @param defaultProvider the default permission provider
 */
class SetupPermissionsEvent(
    val subject: Subject,
    val defaultProvider: PermissionProvider
) {

    /**
     * The permission provider to use for the subject.
     */
    var provider = defaultProvider

    /**
     * Creates a [org.kryptonmc.api.permission.PermissionFunction] for the given [subject] using the [provider]
     * from this event.
     *
     * @param subject the subject
     * @return a permission function for the given subject
     */
    fun createFunction(subject: Subject) = provider.createFunction(subject)

    override fun toString() = "SetupPermissionsEvent(subject=$subject, defaultProvider=$defaultProvider, provider=$provider)"
}
