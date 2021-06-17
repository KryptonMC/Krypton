/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.effect.sound

import net.kyori.adventure.key.Key
import net.kyori.adventure.key.Keyed
import net.kyori.adventure.sound.Sound

/**
 * A type of sound.
 *
 * @param key the key for this sound type
 */
class SoundType(val key: Key) : Sound.Type, Keyed {

    override fun key() = key
}
