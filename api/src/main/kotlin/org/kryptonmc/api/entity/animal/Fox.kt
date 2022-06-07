/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.animal

import org.kryptonmc.api.entity.animal.type.FoxType
import java.util.UUID

/**
 * A fox.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface Fox : Animal {

    /**
     * The fox type of this fox.
     */
    @get:JvmName("foxType")
    public var foxType: FoxType

    /**
     * If this fox is currently sitting.
     *
     * Foxes sit down sometimes during the day if they cannot find a shaded
     * area to sleep. Foxes only sit for short periods of time, before hopping
     * back up. Foxes sit down even when they are attached to a lead or in a
     * boat. Foxes that have not been bred by the player always sit while in
     * the Nether, even while attached to a lead.
     */
    public var isSitting: Boolean

    /**
     * If this fox is currently crouching.
     */
    public var isCrouching: Boolean

    /**
     * If this fox is currently interested in something.
     */
    public var isInterested: Boolean

    /**
     * If this fox is currently pouncing at something.
     *
     * Foxes will naturally attack chickens, rabbits, cod, salmon, and
     * tropical fish. They will also attack baby turtles that are on land.
     * The red fox prefers chickens, rabbits, and baby turtles, and the arctic
     * fox prefers cod, salmon, and tropical fish.
     *
     * Foxes can either attack normally or pounce. When a fox pounces, it will
     * first prepare by tilting and lowering its head and body to become
     * shorter, then it will leap 2-5 blocks in the air. Foxes can jump over
     * fences and walls.
     */
    public var isPouncing: Boolean

    /**
     * If this fox is currently sleeping.
     *
     * During the day, if a thunderstorm is not occurring, foxes attempt to
     * find a space with a sky light level of 14 or less and sleep. Block
     * light has no effect on this. While it sleeps, a fox slowly moves its
     * head up and down. Foxes can also pick up items in their sleep.
     *
     * A fox will wake up if approached by a player or mob. Foxes do not flee
     * if the player approaches while sneaking until the player gets on an
     * adjacent block.
     *
     * If the sky light at the fox's position becomes 15 or above, either due
     * to a block above being destroyed or the fox being moved, the fox wakes
     * up. Foxes do not sleep if they are within 12 blocks of an armor stand.
     */
    public var isSleeping: Boolean

    /**
     * If this fox has faceplanted.
     *
     * This occurs when a fox pounces at something and lands head first in to
     * a layer of snow. They will become momentarily stuck, remaining face
     * down, and emitting particles while shaking. They will return to normal
     * shortly after.
     */
    @get:JvmName("hasFaceplanted")
    public var hasFaceplanted: Boolean

    /**
     * If this fox is currently defending a player it trusts.
     *
     * Foxes will naturally attack zombies, drowned, husks, zombified piglins,
     * skeletons, wither skeletons, strays, phantoms, silverfish, endermites,
     * spiders, cave spiders, vexes, vindicators, evokers, pillagers, ravagers,
     * blazes, endermen, goats, and pandas when these mobs attack the player.
     *
     * They will only defend the player if the player was attacked though, they
     * will not fight for the player if they attack, unlike tamed wolves.
     */
    public var isDefending: Boolean

    /**
     * The first player UUID that this fox trusts.
     */
    @get:JvmName("firstTrusted")
    public var firstTrusted: UUID?

    /**
     * The second player UUID that this fox trusts.
     */
    @get:JvmName("secondTrusted")
    public var secondTrusted: UUID?

    /**
     * Checks if this fox trusts the given [uuid].
     *
     * This will first check if the UUID is equal to the
     * [first trusted UUID][firstTrusted], and then if it is equal to the
     * [second trusted UUID][secondTrusted].
     *
     * @param uuid the UUID
     * @return true if the UUID is trusted by this fox, false otherwise
     */
    public fun trusts(uuid: UUID): Boolean
}
