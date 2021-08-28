/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.event

/**
 * This is used to indicate that the target function is a listener
 * for an event.
 *
 * @param priority the priority of the event (defaults to [ListenerPriority.MEDIUM])
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
public annotation class Listener(public val priority: ListenerPriority = ListenerPriority.MEDIUM)
