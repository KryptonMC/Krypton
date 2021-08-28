/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.util

import net.kyori.adventure.util.Services
import org.apache.logging.log4j.LogManager
import org.kryptonmc.api.Server
import java.util.ServiceLoader

/**
 * Gets a service provider's implementation from a [service loader][java.util.ServiceLoader]
 */
@JvmSynthetic
public inline fun <reified T> service(): T? = Services.service(T::class.java).getIfPresent()

private val LOGGER = LogManager.getLogger(Server::class.java)

// Internal function to print helpful log messages for when there is either more
// than one or no services available for the type T.
@JvmSynthetic
internal inline fun <reified T> serviceOrError(name: String): T = try {
    ServiceLoader.load(T::class.java, T::class.java.classLoader).single()
} catch (exception: NoSuchElementException) {
    LOGGER.error("No candidate found for $name! If you are the server owner, contact the creator of your server software.", exception)
    throw exception
} catch (exception: IllegalArgumentException) {
    LOGGER.error("Too many candidates found for $name! Perhaps a plugin is attempting to register their own provider?")
    throw exception
}
