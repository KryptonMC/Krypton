/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.animal

import org.kryptonmc.api.entity.animal.type.PandaGene

/**
 * A panda.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface Panda : Animal {

    /**
     * The main, known gene for this panda.
     */
    @get:JvmName("knownGene")
    public var knownGene: PandaGene

    /**
     * The hidden gene for this panda.
     */
    @get:JvmName("hiddenGene")
    public var hiddenGene: PandaGene

    /**
     * If this panda is currently unhappy
     */
    public val isUnhappy: Boolean

    /**
     * If this panda is currently sneezing.
     */
    public var isSneezing: Boolean

    /**
     * If this panda is currently eating.
     */
    public var isEating: Boolean

    /**
     * If this panda is currently rolling.
     */
    public var isRolling: Boolean

    /**
     * If this panda is currently sitting.
     */
    public var isSitting: Boolean

    /**
     * If this panda is currently lying on its back.
     */
    public var isLyingOnBack: Boolean

    /**
     * If this panda is scared.
     */
    public val isScared: Boolean

    /**
     * The remaining time, in ticks, this panda will be unhappy for.
     */
    @get:JvmName("unhappyTime")
    public var unhappyTime: Int

    /**
     * The remaining time, in ticks, this panda will be unhappy for.
     */
    @get:JvmName("sneezeTime")
    public var sneezingTime: Int

    /**
     * The remaining time, in ticks, this panda will be unhappy for.
     */
    @get:JvmName("eatTime")
    public var eatingTime: Int
}
