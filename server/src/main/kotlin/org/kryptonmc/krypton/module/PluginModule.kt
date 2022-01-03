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
package org.kryptonmc.krypton.module

import com.google.inject.Scopes
import dev.misfitlabs.kotlinguice4.KotlinModule
import org.apache.logging.log4j.Logger
import org.kryptonmc.api.plugin.PluginContainer
import org.kryptonmc.api.plugin.PluginDescription
import org.kryptonmc.api.plugin.annotation.DataFolder
import org.kryptonmc.krypton.plugin.loader.LoadedPluginDescription
import org.kryptonmc.krypton.util.logger
import java.nio.file.Path

class PluginModule(
    private val description: LoadedPluginDescription,
    private val container: PluginContainer,
    private val basePluginPath: Path
) : KotlinModule() {

    override fun configure() {
        bind(description.mainClass).`in`(Scopes.SINGLETON)

        bind<Logger>().toInstance(logger(description.id))
        bind<Path>().annotatedWith<DataFolder>().toInstance(basePluginPath.resolve(description.id))
        bind<PluginDescription>().toInstance(description)
        bind<PluginContainer>().toInstance(container)
    }
}
