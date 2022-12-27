/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton.pack.repository

import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.pack.BuiltInMetadata
import org.kryptonmc.krypton.pack.PackResources
import org.kryptonmc.krypton.pack.PackType
import org.kryptonmc.krypton.pack.VanillaPackResources
import org.kryptonmc.krypton.pack.VanillaPackResourcesBuilder
import org.kryptonmc.krypton.pack.metadata.FeatureFlagsMetadataSection
import org.kryptonmc.krypton.pack.metadata.PackMetadataSection
import org.kryptonmc.krypton.world.flag.FeatureFlags
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
        private val FEATURE_FLAGS_METADATA_SECTION = FeatureFlagsMetadataSection(FeatureFlags.DEFAULT_FLAGS)
        private val BUILT_IN_METADATA = BuiltInMetadata.of(
            PackMetadataSection.Serializer, VERSION_METADATA_SECTION,
            FeatureFlagsMetadataSection.SERIALIZER, FEATURE_FLAGS_METADATA_SECTION
        )
        private val VANILLA_NAME = Component.translatable("dataPack.vanilla.name")
        private val PACKS_DIRECTORY = Key.key(Key.MINECRAFT_NAMESPACE, "datapacks")

        @JvmStatic
        private fun createVanillaPackSource(): VanillaPackResources =
            VanillaPackResourcesBuilder().metadata(BUILT_IN_METADATA).exposeNamespaces(Key.MINECRAFT_NAMESPACE).pushJarResources().build()

        @JvmStatic
        fun createPackRepository(path: Path): PackRepository =
            PackRepository(ServerPacksSource(), FolderRepositorySource(path, PackType.SERVER_DATA, PackSource.WORLD))
    }
}
