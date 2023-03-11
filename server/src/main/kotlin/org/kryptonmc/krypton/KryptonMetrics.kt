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
package org.kryptonmc.krypton

import org.apache.logging.log4j.LogManager
import org.bstats.MetricsBase
import org.bstats.charts.DrilldownPie
import org.bstats.charts.SimplePie
import org.bstats.charts.SingleLineChart
import org.bstats.config.MetricsConfig
import org.bstats.json.JsonObjectBuilder
import java.io.IOException
import java.nio.file.Path

/**
 * Responsible for setting up the metrics tracker for the server on bStats.
 *
 * Find the bStats page for this
 * [here](https://bstats.org/plugin/server-implementation/Krypton/11197).
 */
object KryptonMetrics {

    private val LOGGER = LogManager.getLogger()
    private const val SERVICE_ID = 11197
    private val VERSION_REGEX = "\\d+".toRegex()

    @JvmStatic
    fun initialize(server: KryptonServer, enabled: Boolean) {
        val config = try {
            MetricsConfig(Path.of("plugins/bStats/config.txt").toFile(), enabled)
        } catch (exception: IOException) {
            LOGGER.error("Failed to create bStats config!", exception)
            return
        }
        if (!config.didExistBefore()) {
            LOGGER.info("Krypton and some of its plugins collect metrics and send them to bStats (https://bstats.org).")
            LOGGER.info("bStats collects some basic information for plugin authors, like how many people use")
            LOGGER.info("their plugin and their total player count. It's recommended to keep bStats enabled, but")
            LOGGER.info("if you're not comfortable with this, you can opt-out by editing the config.txt file in")
            LOGGER.info("the '/plugins/bStats/' folder and setting enabled to false.")
        }

        val metrics = MetricsBase("server-implementation", config.serverUUID, SERVICE_ID, config.isEnabled, ::appendPlatformData, {}, null, { true },
            LOGGER::warn, LOGGER::info, config.isLogErrorsEnabled, config.isLogSentDataEnabled, config.isLogResponseStatusTextEnabled)

        metrics.addCustomChart(SingleLineChart("players") { server.players.size })
        metrics.addCustomChart(SimplePie("online_mode") { if (server.config.server.onlineMode) "online" else "offline" })
        metrics.addCustomChart(SimplePie("krypton_version") { KryptonPlatform.version })
        metrics.addCustomChart(SimplePie("minecraft_version") { KryptonPlatform.minecraftVersion })

        metrics.addCustomChart(DrilldownPie("java_version") {
            val javaVersion = System.getProperty("java.version")
            val major = javaVersion.split("\\.").get(0)
            val dot = javaVersion.lastIndexOf('.')
            val version = when {
                major == "1" -> javaVersion.substring(0, dot)
                VERSION_REGEX.matches(major) -> VERSION_REGEX.find(major)!!.groups.get(0)!!.value
                else -> major
            }
            mapOf<String, Map<String, Int>>("Java $version" to mapOf(javaVersion to 1))
        })
    }

    @JvmStatic
    private fun appendPlatformData(builder: JsonObjectBuilder) {
        builder.appendField("osName", System.getProperty("os.name"))
        builder.appendField("osArch", System.getProperty("os.arch"))
        builder.appendField("osVersion", System.getProperty("os.version"))
        builder.appendField("coreCount", Runtime.getRuntime().availableProcessors())
    }
}
