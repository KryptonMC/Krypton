/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.animal.type

/**
 * A gene that a panda may possess.
 *
 * Every gene has its own list of traits that pandas will express if they have
 * the gene and it is expressed in its phenotype.
 */
public enum class PandaGene(
    /**
     * The probability that a baby panda born from two other pandas may
     * randomly mutate this gene.
     */
    public val mutationProbability: Float,
    /**
     * If this gene is a recessive allele.
     */
    public val isRecessive: Boolean
) {

    /**
     * Normal pandas have no unique personality traits.
     *
     * The mutation chance for this gene is 5/16.
     */
    NORMAL(5F / 16F, false),

    /**
     * Lazy pandas have the following unique personality traits:
     * - They will lie on their backs
     * - They are slower than normal pandas
     * - They will not follow players holding bamboo when they are lying on
     *   their backs.
     *
     * The mutation chance for this gene is 1/16.
     */
    LAZY(1F / 16F, false),

    /**
     * Worried pandas have the following unique personality traits:
     * - They will avoid the player and most hostile mobs, except for slimes,
     *   magma cubes, ghasts, shulkers, phantoms, and the ender dragon.
     * - They shake and hide their faces during thunderstorms.
     * - They do not eat bamboo or cake items on their own.
     *
     * The mutation chance for this gene is 1/16.
     */
    WORRIED(1F / 16F, false),

    /**
     * Playful pandas will roll over and jump around, even as adults. This
     * rolling around may sometimes cause harm or get them killed as it can
     * accidentally roll off a cliff or another high altitude.
     *
     * The mutation chance for this gene is 1/16.
     */
    PLAYFUL(1F / 16F, false),

    /**
     * Brown pandas have no unique personality traits, but are brown and white,
     * instead of the usual black and white.
     *
     * The mutation chance for this gene is 2/16.
     */
    BROWN(2F / 16F, true),

    /**
     * Weak pandas tend to sneeze more often as babies than regular baby
     * pandas, and have half the health of other pandas.
     *
     * The mutation chance for this gene is 5/16.
     */
    WEAK(5F / 16F, true),

    /**
     * Aggressive pandas have the following unique personality traits:
     * - When hit, they attack the player and other mobs continuously until the
     *   target dies or goes beyond detection range, instead of only once. They
     *   also do not panic when harmed.
     * - When nearby pandas are attacked, unless killed in one hit, they become
     *   hostile towards the attacker.
     * - They are slow, but have reach rivaling that of the player.
     *
     * The mutation chance for this gene is 1/16.
     */
    AGGRESSIVE(1F / 16F, false)
}
