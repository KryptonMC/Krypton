package org.kryptonmc.krypton.server

import org.kryptonmc.krypton.world.chunk.ChunkLoader
import org.kryptonmc.krypton.world.data.PlayerDataSerializer
import org.kryptonmc.krypton.world.data.WorldDataSerializer

/**
 * Classes required for the creation of the server.
 *
 * This exists to avoid having many constructor parameters in KryptonServer.
 */
@JvmRecord
data class InitContext(
    val statisticsSerializer: StatisticsSerializer,
    val worldDataSerializer: WorldDataSerializer,
    val playerDataSerializer: PlayerDataSerializer,
    val chunkLoader: ChunkLoader
)
