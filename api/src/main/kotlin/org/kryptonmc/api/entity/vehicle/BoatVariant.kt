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

import org.kryptonmc.api.block.Block
import org.kryptonmc.api.block.Blocks
import org.kryptonmc.api.registry.RegistryReference

/**
 * A variant of boat.
 *
 * @property planks The type of planks the boat is made out of.
 */
public enum class BoatVariant(public val planks: RegistryReference<Block>) {

    OAK(Blocks.OAK_PLANKS),
    SPRUCE(Blocks.SPRUCE_PLANKS),
    BIRCH(Blocks.BIRCH_PLANKS),
    JUNGLE(Blocks.JUNGLE_PLANKS),
    ACACIA(Blocks.ACACIA_PLANKS),
    DARK_OAK(Blocks.DARK_OAK_PLANKS);
}
