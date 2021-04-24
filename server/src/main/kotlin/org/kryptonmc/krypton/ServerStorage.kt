package org.kryptonmc.krypton

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
