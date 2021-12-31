/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.scoreboard.criteria

/**
 * A criterion that is the parent of other sub-criterion.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface CompoundCriterion : Criterion {

    /**
     * The children of this compound criterion.
     */
    @get:JvmName("children")
    public val children: Set<Criterion>
}
