/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.monster

import org.kryptonmc.api.entity.Mob

/**
 * An entity that will attack the player under certain conditions.
 *
 * Generally, most implementations of this will attack the player regardless of
 * the conditions, for example, a [Zombie]. However, other types may not attack
 * the player if certain conditions are met.
 *
 * For more information on each type's conditions, see the respective type.
 */
public interface Monster : Mob
