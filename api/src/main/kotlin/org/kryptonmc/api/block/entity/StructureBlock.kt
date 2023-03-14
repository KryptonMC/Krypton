/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.api.block.entity

import org.kryptonmc.api.util.Vec3i

/**
 * A structure block.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface StructureBlock : BlockEntity {

    /**
     * The position of the structure.
     */
    public var structurePosition: Vec3i

    /**
     * The size of the structure.
     */
    public var size: Vec3i

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
