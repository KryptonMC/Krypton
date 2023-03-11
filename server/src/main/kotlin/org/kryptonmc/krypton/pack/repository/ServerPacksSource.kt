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
package org.kryptonmc.krypton.pack.repository

import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.pack.BuiltInMetadata
import org.kryptonmc.krypton.pack.PackResources
import org.kryptonmc.krypton.pack.PackType
import org.kryptonmc.krypton.pack.VanillaPackResources
import org.kryptonmc.krypton.pack.VanillaPackResourcesBuilder
import org.kryptonmc.krypton.pack.metadata.PackMetadataSection
import java.nio.file.Path

class ServerPacksSource : BuiltInPackSource(PackType.SERVER_DATA, createVanillaPackSource(), PACKS_DIRECTORY) {

    override fun getPackTitle(pack: String): Component = Component.text(pack)

    override fun createVanillaPack(resources: PackResources): Pack? =
        Pack.readMetaAndCreate(VANILLA_ID, VANILLA_NAME, false, { resources }, PackType.SERVER_DATA, Pack.Position.BOTTOM, PackSource.BUILT_IN)

    override fun createBuiltinPack(id: String, resources: Pack.ResourcesSupplier, title: Component): Pack? =
        Pack.readMetaAndCreate(id, title, false, resources, PackType.SERVER_DATA, Pack.Position.TOP, PackSource.FEATURE)

    companion object {

        private val VERSION_METADATA_SECTION = PackMetadataSection(
            Component.translatable("dataPack.vanilla.description"),
            PackType.SERVER_DATA.version()
        )
        private val BUILT_IN_METADATA = BuiltInMetadata.of(PackMetadataSection.Serializer, VERSION_METADATA_SECTION)
        private val VANILLA_NAME = Component.translatable("dataPack.vanilla.name")
        private val PACKS_DIRECTORY = Key.key(Key.MINECRAFT_NAMESPACE, "datapacks")

        @JvmStatic
        private fun createVanillaPackSource(): VanillaPackResources {
            return VanillaPackResourcesBuilder()
                .metadata(BUILT_IN_METADATA)
                .exposeNamespaces(Key.MINECRAFT_NAMESPACE)
                .pushJarResources()
                .build()
        }

        @JvmStatic
        fun createPackRepository(path: Path): PackRepository {
            return PackRepository(ServerPacksSource(), FolderRepositorySource(path, PackType.SERVER_DATA, PackSource.WORLD))
        }
    }
}
