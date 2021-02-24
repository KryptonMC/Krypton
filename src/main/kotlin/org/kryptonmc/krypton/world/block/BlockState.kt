package org.kryptonmc.krypton.world.block

interface BlockState<T> {

    val value: T
}

enum class Facing(override val value: String) : BlockState<String> {

    NORTH("north"),
    SOUTH("south"),
    EAST("east"),
    WEST("west")
}

inline class GrowthAge(override val value: Byte) : BlockState<Byte>

enum class BambooLeafSize(override val value: String) : BlockState<String> {

    NONE("none"),
    SMALL("small"),
    LARGE("large")
}

enum class GrowthState(override val value: Int) : BlockState<Int> {

    NOT_GROWING(0),
    GROWING(1)
}

enum class BannerRotation(override val value: Int) : BlockState<Int> {

    SOUTH(0),
    SOUTH_SOUTHWEST(1),
    SOUTHWEST(2),
    WEST_SOUTHWEST(3),
    WEST(4),
    WEST_NORTHWEST(5),
    NORTHWEST(6),
    NORTH_NORTHWEST(7),
    NORTH(8),
    NORTH_NORTHEAST(9),
    NORTHEAST(10),
    EAST_NORTHEAST(11),
    EAST(12),
    EAST_SOUTHEAST(13),
    SOUTHEAST(14),
    SOUTH_SOUTHEAST(15)
}

enum class BedHalf(override val value: String) : BlockState<String> {

    HEAD("head"),
    FOOT("foot")
}

inline class HoneyLevel(override val value: Byte) : BlockState<Byte>

enum class Attachment(override val value: String) : BlockState<String> {

    FLOOR("floor"),
    CEILING("ceiling"),
    SINGLE_WALL("single_wall"),
    DOUBLE_WALL("double_wall")
}