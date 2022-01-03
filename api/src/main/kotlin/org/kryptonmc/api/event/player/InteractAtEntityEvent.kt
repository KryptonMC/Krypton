/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.event.player

import org.kryptonmc.api.entity.Entity
import org.kryptonmc.api.entity.Hand
import org.kryptonmc.api.entity.player.Player

/**
 * Called when a player interacts at an entity.
 *
 * @param hand the hand the player used to interact with the target
 * @param target the entity that was interacted at
 * @param clickedX the X coordinate of the clicked position on the entity
 * @param clickedY the Y coordinate of the clicked position on the entity
 * @param clickedZ the Z coordinate of the clicked position on the entity
 */
public class InteractAtEntityEvent(
    player: Player,
    @get:JvmName("target") public val target: Entity,
    @get:JvmName("hand") public val hand: Hand,
    @get:JvmName("clickedX") public val clickedX: Double,
    @get:JvmName("clickedY") public val clickedY: Double,
    @get:JvmName("clickedZ") public val clickedZ: Double
) : InteractEvent(player, Type.INTERACT_AT_ENTITY)
