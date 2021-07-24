/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.world.biome

import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
@Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
interface BiomeFactory {

    operator fun get(name: String): Biome
}
