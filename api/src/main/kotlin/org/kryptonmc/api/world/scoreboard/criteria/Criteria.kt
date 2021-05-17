/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.world.scoreboard.criteria

/**
 * Criteria for a scoreboard objective to be displayed. Currently unused.
 */
interface Criteria {

    /**
     * The name of this criteria
     */
    val name: String

    /**
     * If this criteria is mutable
     */
    val isMutable: Boolean
}
