/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
@file:JvmSynthetic
package org.kryptonmc.api.event

/**
 * Requests that the given [handler] be registered with this manager to listen
 * for and handle events of type [E] at
 * [medium priority][ListenerPriority.MEDIUM] for the given [plugin].
 *
 * @param E the type of the event
 * @param plugin the plugin the handler handles events for
 * @param handler the handler to register
 */
@JvmSynthetic
public inline fun <reified E> EventManager.registerHandler(plugin: Any, handler: EventHandler<E>) {
    registerHandler(plugin, E::class.java, handler)
}

/**
 * Requests that the given [handler] be registered with this manager to listen
 * for and handle events of type [E] for the given [plugin].
 *
 * @param E the type of the event
 * @param plugin the plugin the handler handles events for
 * @param priority the priority the handler will be executed with
 * @param handler the handler to register
 */
@JvmSynthetic
public inline fun <reified E> EventManager.registerHandler(plugin: Any, priority: ListenerPriority, handler: EventHandler<E>) {
    registerHandler(plugin, E::class.java, priority, handler)
}
