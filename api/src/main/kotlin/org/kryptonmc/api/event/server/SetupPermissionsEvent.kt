/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
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
