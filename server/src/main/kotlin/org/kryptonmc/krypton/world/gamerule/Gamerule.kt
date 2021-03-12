package org.kryptonmc.krypton.world.gamerule

// TODO: Actually do something with this
data class Gamerule<T>(
    val type: GameruleType,
    val value: T
)

enum class GameruleType(val rule: String)