/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.map.marker

import org.jetbrains.annotations.ApiStatus
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.entity.Entity
import org.kryptonmc.api.util.provide
import org.spongepowered.math.vector.Vector3i

/**
 * A marker that marks the position of an item frame that contains the map this
 * marker is displayed on in it with a green arrow.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface ItemFrameMarker : MapMarker {

    /**
     * The item frame entity that this marker is marking the location of.
     */
    // TODO: Change the return type to ItemFrame when item frames are implemented
    @get:JvmName("entity")
    public val entity: Entity

    /**
     * The rotation of this marker on the map.
     */
    @get:JvmName("rotation")
    public val rotation: Int

    @ApiStatus.Internal
    public interface Factory {

        public fun of(position: Vector3i, entity: Entity, rotation: Int): ItemFrameMarker
    }

    public companion object {

        private val FACTORY = Krypton.factoryProvider.provide<Factory>()

        /**
         * Creates a new item frame marker at the given [position] on the map
         * marking the given [entity]
         */
        @JvmStatic
        public fun of(position: Vector3i, entity: Entity, rotation: Int): ItemFrameMarker = FACTORY.of(position, entity, rotation)
    }
}
