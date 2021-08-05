/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.util

import net.kyori.adventure.key.Key.key

/**
 * Convert this string to a [net.kyori.adventure.key.Key]. Must be in the format namespace:key,
 * or if the namespace is absent, it will default to "minecraft".
 */
@JvmSynthetic
fun String.toKey() = key(this)
