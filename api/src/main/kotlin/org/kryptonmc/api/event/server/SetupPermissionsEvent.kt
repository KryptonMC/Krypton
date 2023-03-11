/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
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
