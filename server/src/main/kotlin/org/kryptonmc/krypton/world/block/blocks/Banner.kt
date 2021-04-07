package org.kryptonmc.krypton.world.block.blocks

import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import org.kryptonmc.krypton.api.space.Vector
import org.kryptonmc.krypton.world.block.tile.BlockEntity

// TODO: Add this
@Suppress("unused")
class Banner

data class BannerEntity(
    override val position: Vector,
    override val keepPacked: Boolean,
    val customName: Component?,
    val patterns: List<BannerPattern>
) : BlockEntity("banner") {

    companion object {

        fun fromNBT(position: Vector, keepPacked: Boolean, nbt: CompoundBinaryTag): BannerEntity {
            val customName = nbt.getString("CustomName").takeIf { it.isNotEmpty() }
                ?.let { GsonComponentSerializer.gson().deserialize(it) }
            val patterns = nbt.getList("Patterns").map {
                val nbtPattern = it as CompoundBinaryTag
                BannerPattern(
                    BannerPatternType.fromCode(nbtPattern.getString("Pattern")),
                    BannerPatternColor.fromId(nbtPattern.getInt("Color"))
                )
            }
            return BannerEntity(position, keepPacked, customName, patterns)
        }
    }
}

class BannerPattern(
    val pattern: BannerPatternType,
    val color: BannerPatternColor
)

enum class BannerPatternType(val code: String) {

    BASE("b"),
    BOTTOM_STRIPE("bs"),
    TOP_STRIPE("ts"),
    LEFT_STRIPE("ls"),
    RIGHT_STRIPE("rs"),
    CENTER_STRIPE("cs"),
    MIDDLE_STRIPE("ms"),
    DOWN_RIGHT_STRIPE("drs"),
    DOWN_LEFT_STRIPE("dls"),
    SMALL_STRIPES("ss"),
    DIAGONAL_CROSS("cr"),
    SQUARE_CROSS("sc"),
    LEFT_OF_DIAGONAL("ld"),
    RIGHT_OF_UPSIDE_DOWN_DIAGONAL("rud"),
    LEFT_OF_UPSIDE_DOWN_DIAGONAL("lud"),
    RIGHT_OF_DIAGONAL("rd"),
    LEFT_VERTICAL_HALF("vh"),
    RIGHT_VERTICAL_HALF("vhr"),
    TOP_HORIZONTAL_HALF("hh"),
    BOTTOM_HORIZONTAL_HALF("hhb"),
    BOTTOM_LEFT_CORNER("bl"),
    BOTTOM_RIGHT_CORNER("br"),
    TOP_LEFT_CORNER("tl"),
    TOP_RIGHT_CORNER("tr"),
    BOTTOM_TRIANGLE("bt"),
    TOP_TRIANGLE("tt"),
    BOTTOM_TRIANGLE_SAWTOOTH("bts"),
    TOP_TRIANGLE_SAWTOOTH("tts"),
    MIDDLE_CIRCLE("mc"),
    MIDDLE_RHOMBUS("mr"),
    BORDER("bo"),
    CURLY_BORDER("cbo"),
    BRICK("bri"),
    GRADIENT("gra"),
    UPSIDE_DOWN_GRADIENT("gru"),
    CREEPER("cre"),
    SKULL("sku"),
    FLOWER("flo"),
    MOJANG("moj"),
    GLOBE("glb"),
    PIGLIN("pig");

    companion object {

        private val VALUES = values().associateBy { it.code }

        fun fromCode(code: String) = VALUES.getValue(code)
    }
}

enum class BannerPatternColor {

    WHITE,
    ORANGE,
    MAGENTA,
    LIGHT_BLUE,
    YELLOW,
    LIME,
    PINK,
    GRAY,
    LIGHT_GRAY,
    CYAN,
    PURPLE,
    BLUE,
    BROWN,
    GREEN,
    RED,
    BLACK;

    companion object {

        private val VALUES = values().associateBy { it.ordinal }

        fun fromId(id: Int) = VALUES.getValue(id)
    }
}