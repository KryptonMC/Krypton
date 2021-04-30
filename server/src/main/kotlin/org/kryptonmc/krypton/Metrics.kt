package org.kryptonmc.krypton

import org.apache.logging.log4j.Logger
import org.bstats.MetricsBase
import org.bstats.charts.CustomChart
import org.bstats.charts.DrilldownPie
import org.bstats.charts.SimplePie
import org.bstats.charts.SingleLineChart
import org.bstats.config.MetricsConfig
import org.bstats.json.JsonObjectBuilder
import org.kryptonmc.krypton.server.OtherConfig
import org.kryptonmc.krypton.util.logger
import java.io.IOException
import java.nio.file.Path

class Metrics(logger: Logger, serviceId: Int, enabled: Boolean) {

    private val base: MetricsBase

    init {
        val config = try {
            MetricsConfig(Path.of("plugins").resolve("bStats").resolve("config.txt").toFile(), enabled)
        } catch (exception: IOException) {
            logger.error("Failed to create bStats config!", exception)
            throw exception // we throw here to allow us to not instantiate this object if this throws
        }

        base = MetricsBase(
            "server-implementation",
            config.serverUUID,
            serviceId,
            config.isEnabled,
            ::appendPlatformData,
            {},
            null,
            { true },
            logger::warn,
            logger::info,
            config.isLogErrorsEnabled,
            config.isLogSentDataEnabled,
            config.isLogResponseStatusTextEnabled
        )

        if (!config.didExistBefore()) {
            logger.info("Krypton and some of its plugins collect metrics and send them to bStats (https://bstats.org).")
            logger.info("bStats collects some basic information for plugin authors, like how many people use")
            logger.info("their plugin and their total player count. It's recommended to keep bStats enabled, but")
            logger.info("if you're not comfortable with this, you can opt-out by editing the config.txt file in")
            logger.info("the '/plugins/bStats/' folder and setting enabled to false.")
        }
    }

    operator fun plusAssign(chart: CustomChart) {
        base.addCustomChart(chart)
    }

    private fun appendPlatformData(builder: JsonObjectBuilder) {
        builder.appendField("osName", System.getProperty("os.name"))
        builder.appendField("osArch", System.getProperty("os.arch"))
        builder.appendField("osVersion", System.getProperty("os.version"))
        builder.appendField("coreCount", Runtime.getRuntime().availableProcessors())
    }
}

internal object KryptonMetrics {

    private val LOGGER = logger<Metrics>()

    fun initialize(server: KryptonServer, enabled: Boolean) {
        val metrics = Metrics(LOGGER, 11197, enabled)

        metrics += SingleLineChart("players", server.players::size)
        metrics += SimplePie("online_mode") { if (server.config.server.onlineMode) "online" else "offline" }
        metrics += SimplePie("krypton_version", KryptonServer.KryptonServerInfo::version)
        metrics += SimplePie("minecraft_version", KryptonServer.KryptonServerInfo::minecraftVersion)

        metrics += DrilldownPie("java_version") {
            val javaVersion = System.getProperty("java.version")
            val major = javaVersion.split("\\.")[0]
            val dot = javaVersion.indexOf('.')

            val release = "Java " + if (major == "1") {
                javaVersion.substring(0, dot)
            } else {
                val versionRegex = "\\d+".toRegex()
                if (versionRegex matches major) versionRegex.find(major)!!.groups[0]!!.value else major
            }

            mapOf<String, Map<String, Int>>(release to mapOf(javaVersion to 1))
        }
    }
}
