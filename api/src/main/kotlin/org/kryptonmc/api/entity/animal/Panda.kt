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
     * The main gene that this panda has.
     *
     * Panda genetics are very similar to real-world biology, except that, in
     * Minecraft, things are a lot more simple, as the pandas only have two
     * genes.
     *
     * There are dominant and recessive alleles, and dominant alleles will
     * always be expressed over recessive ones. If the panda has two recessive
     * alleles, the recessive trait will be expressed. The one special rule is
     * that if both genes are dominant, the main gene will always be expressed
     * over the hidden gene.
     */
    @get:JvmName("knownGene")
    public var knownGene: PandaGene

    /**
     * The hidden gene that this panda has.
     *
     * See [knownGene] for an explanation on panda genetics.
     */
    @get:JvmName("hiddenGene")
    public var hiddenGene: PandaGene

    /**
     * If this panda is currently unhappy.
     */
    public val isUnhappy: Boolean

    /**
     * If this panda is currently sneezing.
     *
     * Baby pandas have a 1/6000, or 0.000166666%, chance to sneeze on any
     * tick. This is higher for weak baby pandas, at 1/500, or 0.002%.
     * When a baby panda sneezes, all adult pandas within a 10 block radius
     * will jump, and the baby panda has a 1/700 chance to drop a slimeball.
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
