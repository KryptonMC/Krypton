/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.event

/**
 * Used to indicate that the target function is a listener for an event.
 *
 * @property priority the priority of the event (defaults to medium).
 * @property mustBeAsync whether the event handler this annotation is on must
 * be called asynchronously.
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
public annotation class Listener(public val priority: ListenerPriority = ListenerPriority.MEDIUM, public val mustBeAsync: Boolean = false)
