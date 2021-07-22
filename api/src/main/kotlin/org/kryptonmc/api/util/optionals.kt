/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.util

import java.util.Optional

/**
 * Gets the value of this [Optional] if present, else returns null.
 *
 * Useful for converting [Optional] values from other Java-based libraries
 * in to nullable types.
 */
fun <T> Optional<T>.getIfPresent(): T? = if (isPresent) get() else null

/**
 * Gets the value of this [Optional] if present, else calls [error] with
 * the given [message] (throwing an [IllegalStateException]).
 *
 * @param message the message for the exception if thrown
 */
fun <T> Optional<T>.orThrow(message: String): T = if (isPresent) get() else error(message)
