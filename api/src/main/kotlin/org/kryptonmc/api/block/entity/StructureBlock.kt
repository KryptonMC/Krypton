/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.block.entity

import org.spongepowered.math.vector.Vector3i

/**
 * A structure block.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface StructureBlock : BlockEntity {

    /**
     * The position of the structure.
     */
    public var structurePosition: Vector3i

    /**
     * The size of the structure.
     */
    public var size: Vector3i

    /**
     * The author of the structure.
     */
    public var author: String

    /**
     * Whether the structure block is powered.
     */
    public var isPowered: Boolean

    /**
     * Whether the bounding box of the structure block is visible.
     */
    @get:JvmName("showBoundingBox")
    public var showBoundingBox: Boolean

    /**
     * Whether the air within the structure should be visible.
     */
    @get:JvmName("showAir")
    public var showAir: Boolean

    /**
     * Whether this structure block should ignore entities.
     */
    @get:JvmName("ignoreEntities")
    public var ignoreEntities: Boolean

    /**
     * The seed of the structure to be generated.
     */
    public var seed: Long

    /**
     * The integrity of the structure.
     *
     * This determines how complete the structure that gets placed will be.
     */
    public var integrity: Double
}
