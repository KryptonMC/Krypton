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
package org.kryptonmc.api.entity.vehicle

import org.kryptonmc.api.block.BlockState

/**
 * Something that shares some (or all) functionality with that of a [Minecart].
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface MinecartLike : DamageableVehicle {

    /**
     * The variant of this minecart like object.
     */
    public val variant: MinecartVariant

    /**
     * If the custom block is shown on the minecart.
     */
    @get:JvmName("showCustomBlock")
    public var showCustomBlock: Boolean

    /**
     * The custom block state shown within the minecart.
     */
    public var customBlock: BlockState

    /**
     * The offset of the custom block.
     */
    public var customBlockOffset: Int
}
