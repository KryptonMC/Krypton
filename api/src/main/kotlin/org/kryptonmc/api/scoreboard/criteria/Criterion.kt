/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.scoreboard.criteria

import org.kryptonmc.api.scoreboard.ObjectiveRenderType

/**
 * A criterion for a scoreboard objective to be displayed.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface Criterion {

    /**
     * The name of this criterion.
     */
    @get:JvmName("name")
    public val name: String

    /**
     * If this criterion is mutable.
     */
    public val isMutable: Boolean

    /**
     * The render type of this criterion.
     */
    @get:JvmName("renderType")
    public val renderType: ObjectiveRenderType
}
