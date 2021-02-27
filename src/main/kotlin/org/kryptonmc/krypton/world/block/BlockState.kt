package org.kryptonmc.krypton.world.block

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*

@Serializable(with = BlockState.Companion::class)
interface BlockState {

    companion object : JsonContentPolymorphicSerializer<BlockState>(BlockState::class) {

        override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out BlockState> = when {
            element.jsonPrimitive.intOrNull != null -> IntegerState.serializer()
            element.jsonPrimitive.booleanOrNull != null -> BooleanState.serializer()
            "attachment" in element.jsonObject -> AttachmentState.serializer()
            "axis" in element.jsonObject -> AxisState.serializer()
            "face" in element.jsonObject -> FaceState.serializer()
            "facing" in element.jsonObject -> FacingState.serializer()
            "half" in element.jsonObject -> HalfState.serializer()
            "hinge" in element.jsonObject -> HingeState.serializer()
            "instrument" in element.jsonObject -> InstrumentState.serializer()
            "leaves" in element.jsonObject -> LeafSizeState.serializer()
            "mode" in element.jsonObject -> when (element.jsonPrimitive.content) {
                in ComparatorModeState.values().map { it.name.toLowerCase() } -> ComparatorModeState.serializer()
                in StructureModeState.values().map { it.name.toLowerCase() } -> StructureModeState.serializer()
                else -> throw IllegalArgumentException("Unknown type $element")
            }
            "part" in element.jsonObject -> PartState.serializer()
            "shape" in element.jsonObject -> when (element.jsonPrimitive.content) {
                in RailShapeState.values().map { it.name.toLowerCase() } -> RailShapeState.serializer()
                in StairShapeState.values().map { it.name.toLowerCase() } -> StairShapeState.serializer()
                else -> throw IllegalArgumentException("Unknown type $element")
            }
            "type" in element.jsonObject -> when (element.jsonPrimitive.content) {
                in PistonTypeState.values().map { it.name.toLowerCase() } -> PistonTypeState.serializer()
                in ChestTypeState.values().map { it.name.toLowerCase() } -> ChestTypeState.serializer()
                in SlabTypeState.values().map { it.name.toLowerCase() } -> SlabTypeState.serializer()
                else -> throw IllegalArgumentException("Unknown type $element")
            }
            listOf("north", "south", "east", "west").any { it in element.jsonObject } -> RedstoneDustOnState.serializer()
            else -> throw IllegalArgumentException("Unknown type $element")
        }
    }
}

@Serializable
data class IntegerState(val value: Int) : BlockState

@Serializable
data class BooleanState(val value: Boolean) : BlockState

@Serializable
enum class AttachmentState : BlockState {

    @SerialName("ceiling") CEILING,
    @SerialName("floor") FLOOR,
    @SerialName("single_wall") SINGLE_WALL,
    @SerialName("double_wall") DOUBLE_WALL
}

@Serializable
enum class AxisState : BlockState {

    @SerialName("x") X,
    @SerialName("y") Y,
    @SerialName("z") Z
}

@Serializable
enum class FaceState : BlockState {

    @SerialName("ceiling") CEILING,
    @SerialName("floor") FLOOR,
    @SerialName("wall") WALL
}

@Serializable
enum class FacingState : BlockState {

    @SerialName("up") UP,
    @SerialName("down") DOWN,
    @SerialName("north") NORTH,
    @SerialName("south") SOUTH,
    @SerialName("east") EAST,
    @SerialName("west") WEST
}

@Serializable
enum class HalfState : BlockState {

    // for doors and tall plants, the half that occupies the block space
    @SerialName("upper") UPPER,
    @SerialName("lower") LOWER,

    // for trapdoors and stairs, what part of the block space they are in
    @SerialName("top") TOP,
    @SerialName("bottom") BOTTOM
}

@Serializable
enum class HingeState : BlockState {

    @SerialName("left") LEFT,
    @SerialName("right") RIGHT
}

@Serializable
enum class InstrumentState : BlockState {

    @SerialName("banjo") BANJO,
    @SerialName("basedrum") BASEDRUM,
    @SerialName("bass") BASS,
    @SerialName("bell") BELL,
    @SerialName("bit") BIT,
    @SerialName("chime") CHIME,
    @SerialName("cow_bell") COW_BELL,
    @SerialName("digeridoo") DIGERIDOO,
    @SerialName("flute") FLUTE,
    @SerialName("guitar") GUITAR,
    @SerialName("harp") HARP,
    @SerialName("hat") HAT,
    @SerialName("iron_xylophone") IRON_XYLOPHONE,
    @SerialName("snare") SNARE,
    @SerialName("xylophone") XYLOPHONE
}

@Serializable
enum class LeafSizeState : BlockState {

    @SerialName("none") NONE,
    @SerialName("small") SMALL,
    @SerialName("large") LARGE
}

@Serializable
enum class ComparatorModeState : BlockState {

    @SerialName("compare") COMPARE,
    @SerialName("subtract") SUBTRACT
}

@Serializable
enum class StructureModeState : BlockState {

    @SerialName("corner") CORNER,
    @SerialName("data") DATA,
    @SerialName("load") LOAD,
    @SerialName("save") SAVE
}

@Serializable
enum class RedstoneDustOnState : BlockState {

    @SerialName("up") UP,
    @SerialName("side") SIDE,
    @SerialName("none") NONE
}

@Serializable
enum class PartState : BlockState {

    @SerialName("head") HEAD,
    @SerialName("foot") FOOT
}

@Serializable
enum class RailShapeState : BlockState {

    @SerialName("ascending_north") ASCENDING_NORTH,
    @SerialName("ascending_south") ASCENDING_SOUTH,
    @SerialName("ascending_east") ASCENDING_EAST,
    @SerialName("ascending_west") ASCENDING_WEST,
    @SerialName("north_south") NORTH_SOUTH,
    @SerialName("east_west") EAST_WEST,

    // these only apply to normal rails
    @SerialName("north_east") NORTH_EAST,
    @SerialName("north_west") NORTH_WEST,
    @SerialName("south_east") SOUTH_EAST,
    @SerialName("south_west") SOUTH_WEST
}

@Serializable
enum class StairShapeState : BlockState {

    @SerialName("straight") STRAIGHT,
    @SerialName("inner_left") INNER_LEFT,
    @SerialName("inner_right") INNER_RIGHT,
    @SerialName("outer_left") OUTER_LEFT,
    @SerialName("outer_right") OUTER_RIGHT
}

@Serializable
enum class PistonTypeState : BlockState {

    @SerialName("normal") NORMAL,
    @SerialName("sticky") STICKY
}

@Serializable
enum class ChestTypeState : BlockState {

    @SerialName("left") LEFT,
    @SerialName("right") RIGHT,
    @SerialName("single") SINGLE
}

@Serializable
enum class SlabTypeState : BlockState {

    @SerialName("top") TOP,
    @SerialName("bottom") BOTTOM,
    @SerialName("double") DOUBLE
}

//enum class Facing(override val value: String) : BlockState<String> {
//
//    NORTH("north"),
//    SOUTH("south"),
//    EAST("east"),
//    WEST("west")
//}
//
//inline class GrowthAge(override val value: Byte) : BlockState<Byte>
//
//enum class BambooLeafSize(override val value: String) : BlockState<String> {
//
//    NONE("none"),
//    SMALL("small"),
//    LARGE("large")
//}
//
//enum class GrowthState(override val value: Int) : BlockState<Int> {
//
//    NOT_GROWING(0),
//    GROWING(1)
//}
//
//enum class BannerRotation(override val value: Int) : BlockState<Int> {
//
//    SOUTH(0),
//    SOUTH_SOUTHWEST(1),
//    SOUTHWEST(2),
//    WEST_SOUTHWEST(3),
//    WEST(4),
//    WEST_NORTHWEST(5),
//    NORTHWEST(6),
//    NORTH_NORTHWEST(7),
//    NORTH(8),
//    NORTH_NORTHEAST(9),
//    NORTHEAST(10),
//    EAST_NORTHEAST(11),
//    EAST(12),
//    EAST_SOUTHEAST(13),
//    SOUTHEAST(14),
//    SOUTH_SOUTHEAST(15)
//}
//
//enum class BedHalf(override val value: String) : BlockState<String> {
//
//    HEAD("head"),
//    FOOT("foot")
//}
//
//inline class HoneyLevel(override val value: Byte) : BlockState<Byte>
//
//enum class Attachment(override val value: String) : BlockState<String> {
//
//    FLOOR("floor"),
//    CEILING("ceiling"),
//    SINGLE_WALL("single_wall"),
//    DOUBLE_WALL("double_wall")
//}