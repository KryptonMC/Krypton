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
import java.util.Optional

/**
 * Gets a service provider's implementation from a [service loader][java.util.ServiceLoader]
 */
inline fun <reified T> service(): Optional<T> = Services.service(T::class.java)
