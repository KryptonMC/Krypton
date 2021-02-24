package org.kryptonmc.krypton.world.gamerule

// TODO: Actually do something with this
data class Gamerule(
    val type: GameruleType,
    val value: Any = type.default
)

enum class GameruleType(val ruleName: String, val default: Any)