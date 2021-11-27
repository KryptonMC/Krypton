/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
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
package org.kryptonmc.krypton

import org.kryptonmc.api.Platform
import java.util.Properties

// TODO: Check on update
object KryptonPlatform : Platform {

    private val versions = Properties().apply { load(ClassLoader.getSystemResourceAsStream("META-INF/versions.properties")) }

    override val name = "Krypton"
    override val version: String = versions.getProperty("krypton")
    override val isStable = false
    override val minecraftVersion: String = versions.getProperty("minecraft")
    const val isStableMinecraft = false
    override val worldVersion = 2857
    override val protocolVersion = 1073741883
    override val dataPackVersion = 8

    val sparkVersion: String = versions.getProperty("spark")
    val dataVersionPrefix: String = versions.getProperty("data")
}
