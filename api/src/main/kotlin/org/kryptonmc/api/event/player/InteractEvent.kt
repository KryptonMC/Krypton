/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.event.player

import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.api.event.GenericResult
import org.kryptonmc.api.event.ResultedEvent

/**
 * The superclass for all events involving a player's interaction with
 * something in a world.
 *
 * @param player the player that performed the interaction
 * @param type the type of interaction performed
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public sealed class InteractEvent(
    @get:JvmName("player") public val player: Player,
    @get:JvmName("type") public val type: Type,
) : ResultedEvent<GenericResult> {

    @get:JvmName("result")
    override var result: GenericResult = GenericResult.allowed()

    /**
     * The type of interaction made by the player.
     */
    public enum class Type {

        /**
         * An interaction on an entity. Interactions of this type target the
         * entity as a whole.
         */
        INTERACT_ON_ENTITY,

        /**
         * An attack against an entity. Attacks are usually interactions that
         * do damage to entities.
         */
        ATTACK_ENTITY,

        /**
         * An interaction at an entity. Interactions of this type target a
         * specific part of the entity.
         */
        INTERACT_AT_ENTITY,

        /**
         * An item use. This is used when a player interacts with air while
         * holding an item in their hand.
         */
        USE_ITEM,

        /**
         * A block placement. THis is used when a player places a block in the
         * world.
         */
        PLACE_BLOCK
    }
}
