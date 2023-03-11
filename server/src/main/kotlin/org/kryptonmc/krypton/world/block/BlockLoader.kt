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
package org.kryptonmc.krypton.world.block

import com.google.gson.JsonObject
import net.kyori.adventure.key.Key
import org.kryptonmc.api.block.BlockSoundGroup
import org.kryptonmc.krypton.registry.KryptonRegistry
import org.kryptonmc.krypton.state.property.KryptonPropertyFactory
import org.kryptonmc.krypton.util.KryptonDataLoader
import org.kryptonmc.krypton.world.block.data.BlockSoundGroups
import org.kryptonmc.krypton.world.block.handlers.DefaultBlockHandler
import org.kryptonmc.krypton.world.material.Material
import org.kryptonmc.krypton.world.material.Materials
import java.lang.reflect.Modifier

class BlockLoader(registry: KryptonRegistry<KryptonBlock>) : KryptonDataLoader<KryptonBlock>("blocks", registry) {

    private val materialsByName: Map<String, Material> = Materials::class.java.declaredFields.asSequence()
        .filter { Modifier.isPublic(it.modifiers) && Modifier.isStatic(it.modifiers) }
        .filter { Material::class.java.isAssignableFrom(it.type) }
        .associate { it.name to it.get(null) as Material }

    private val soundGroupsByName: Map<String, BlockSoundGroup> = BlockSoundGroups::class.java.declaredFields.asSequence()
        .filter { Modifier.isPublic(it.modifiers) && Modifier.isStatic(it.modifiers) }
        .filter { BlockSoundGroup::class.java.isAssignableFrom(it.type) }
        .associate { it.name to it.get(null) as BlockSoundGroup }

    override fun create(key: Key, value: JsonObject): KryptonBlock {
        val materialName = value.get("material").asString
        val soundGroupName = value.get("soundType").asString

        val properties = BlockProperties(
            requireNotNull(materialsByName.get(materialName)) { "Could not find material for name $materialName!" },
            value.get("collision").asBoolean,
            requireNotNull(soundGroupsByName.get(soundGroupName)) { "Could not find sound group for name $soundGroupName!" },
            value.get("explosionResistance").asFloat,
            value.get("hardness").asFloat,
            value.get("requiresCorrectTool").asBoolean,
            value.get("friction").asFloat,
            value.get("speedFactor").asFloat,
            value.get("jumpFactor").asFloat,
            if (value.has("drops")) Key.key(value.get("drops").asString) else null,
            value.get("canOcclude").asBoolean,
            value.get("air").asBoolean,
            value.get("dynamicShape").asBoolean
        )

        val stateProperties = value.get("properties").asJsonArray.map { KryptonPropertyFactory.findByName(it.asString) }

        // TODO: Update this to get the handlers from somewhere
        return KryptonBlock(properties, DefaultBlockHandler, DefaultBlockHandler, DefaultBlockHandler, DefaultBlockHandler, stateProperties)
    }
}
