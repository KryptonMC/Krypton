/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.scoreboard

import net.kyori.adventure.key.Keyed
import org.kryptonmc.api.util.CataloguedBy
import org.kryptonmc.api.util.TranslationHolder

/**
 * A rule for collision between members of a [Team].
 */
@CataloguedBy(CollisionRules::class)
public interface CollisionRule : Keyed, TranslationHolder
