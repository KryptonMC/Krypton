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

import org.kryptonmc.api.event.type.EventWithResult
import org.kryptonmc.api.permission.PermissionFunction
import org.kryptonmc.api.permission.Subject
import org.kryptonmc.internal.annotations.ImmutableType

/**
 * Called when a subject's permissions are initially being set up.
 */
public interface SetupPermissionsEvent : EventWithResult<SetupPermissionsEvent.Result> {

    /**
     * The subject that is having their permissions set up.
     */
    public val subject: Subject

    /**
     * The default permission provider that will be used if no plugin
     * specifies one by setting the result of this event.
     */
    public val defaultFunction: PermissionFunction

    /**
     * The result of a setup permissions event.
     *
     * This is used to modify the function that is used to provide permissions
     * for the subject.
     *
     * @param function the permission function to use
     */
    @JvmRecord
    @ImmutableType
    public data class Result(public val function: PermissionFunction)
}
