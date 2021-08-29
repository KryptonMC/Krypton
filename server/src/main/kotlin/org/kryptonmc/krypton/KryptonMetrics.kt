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

import org.bstats.charts.DrilldownPie
import org.bstats.charts.SimplePie
import org.bstats.charts.SingleLineChart
import org.kryptonmc.krypton.util.logger

object KryptonMetrics {

    private val LOGGER = logger<Metrics>()

    fun initialize(server: KryptonServer, enabled: Boolean) {
        val metrics = Metrics(LOGGER, 11197, enabled)

        metrics.addCustomChart(SingleLineChart("players", server.players::size))
        metrics.addCustomChart(SimplePie("online_mode") { if (server.config.server.onlineMode) "online" else "offline" })
        metrics.addCustomChart(SimplePie("krypton_version", KryptonPlatform::version))
        metrics.addCustomChart(SimplePie("minecraft_version", KryptonPlatform::minecraftVersion))

        metrics.addCustomChart(DrilldownPie("java_version") {
            val javaVersion = System.getProperty("java.version")
            val major = javaVersion.split("\\.")[0]
            val dot = javaVersion.lastIndexOf('.')

            val release = "Java " + if (major == "1") {
                javaVersion.substring(0, dot)
            } else {
                val versionRegex = "\\d+".toRegex()
                if (versionRegex matches major) versionRegex.find(major)!!.groups[0]!!.value else major
            }

            mapOf<String, Map<String, Int>>(release to mapOf(javaVersion to 1))
        })
    }
}
