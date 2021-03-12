package org.kryptonmc.krypton

import java.util.*
import java.util.concurrent.atomic.AtomicInteger

object ServerStorage {

    val PLAYER_COUNT = AtomicInteger(0)
    val NEXT_ENTITY_ID = AtomicInteger(0)
}

object ServerInfo {

    const val VERSION = "1.16.5"
    const val PROTOCOL = 754

    // always empty in the modern protocol
    const val SERVER_ID = ""
}

/**
 * A UUID of 00000000-0000-0000-0000-000000000000 is nil, and a player will never have this UUID,
 * so we can safely use it as the UUID to represent the server/console.
 */
val SERVER_UUID: UUID = UUID.fromString("00000000-0000-0000-0000-000000000000")