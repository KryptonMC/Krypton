/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton.util.converters

import ca.spottedleaf.dataconverter.types.MapType
import ca.spottedleaf.dataconverter.types.ObjectType
import it.unimi.dsi.fastutil.HashCommon
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import it.unimi.dsi.fastutil.ints.IntArrayList
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap
import org.kryptonmc.api.util.ceillog2
import org.kryptonmc.krypton.util.converter.MCVersions
import org.kryptonmc.krypton.util.converter.PackedBitStorage
import org.kryptonmc.krypton.util.converter.StringDataConverter
import org.kryptonmc.krypton.util.converter.types.nbt.NBTTypeUtil
import org.kryptonmc.krypton.util.converters.helpers.BlockFlatteningHelper
import org.kryptonmc.krypton.util.converters.helpers.ItemNameHelper
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.util.toBooleanArray
import java.util.BitSet
import kotlin.math.max
import kotlin.math.min

object ChunkFlatteningConverter : StringDataConverter(MCVersions.V17W47A, 1) {

    private val LOGGER = logger<ChunkFlatteningConverter>()
    private val VIRTUAL_SET = BitSet(256).apply {
        set(54)
        set(146)
        set(25)
        set(26)
        set(51)
        set(53)
        set(67)
        set(108)
        set(109)
        set(114)
        set(128)
        set(134)
        set(135)
        set(136)
        set(156)
        set(163)
        set(164)
        set(180)
        set(203)
        set(55)
        set(85)
        set(113)
        set(188)
        set(189)
        set(190)
        set(191)
        set(192)
        set(93)
        set(94)
        set(101)
        set(102)
        set(160)
        set(106)
        set(107)
        set(183)
        set(184)
        set(185)
        set(186)
        set(187)
        set(132)
        set(139)
        set(199)
    }
    private val IDS_NEEDING_FIX_SET = BitSet(256).apply {
        set(2)
        set(3)
        set(110)
        set(140)
        set(144)
        set(25)
        set(86)
        set(26)
        set(176)
        set(177)
        set(175)
        set(64)
        set(71)
        set(193)
        set(194)
        set(195)
        set(196)
        set(197)
    }
    private val VIRTUAL = VIRTUAL_SET.toBooleanArray()
    private val IDS_NEEDING_FIX = IDS_NEEDING_FIX_SET.toBooleanArray()

    val PUMPKIN = BlockFlatteningHelper.parseTag("{Name:'minecraft:pumpkin'}")
    val SNOWY_PODZOL = BlockFlatteningHelper.parseTag("{Name:'minecraft:podzol',Properties:{snowy:'true'}}")
    val SNOWY_GRASS = BlockFlatteningHelper.parseTag("{Name:'minecraft:grass_block',Properties:{snowy:'true'}}")
    val SNOWY_MYCELIUM = BlockFlatteningHelper.parseTag("{Name:'minecraft:mycelium',Properties:{snowy:'true'}}")
    val UPPER_SUNFLOWER = BlockFlatteningHelper.parseTag("{Name:'minecraft:sunflower',Properties:{half:'upper'}}")
    val UPPER_LILAC = BlockFlatteningHelper.parseTag("{Name:'minecraft:lilac',Properties:{half:'upper'}}")
    val UPPER_TALL_GRASS = BlockFlatteningHelper.parseTag("{Name:'minecraft:tall_grass',Properties:{half:'upper'}}")
    val UPPER_LARGE_FERN = BlockFlatteningHelper.parseTag("{Name:'minecraft:large_fern',Properties:{half:'upper'}}")
    val UPPER_ROSE_BUSH = BlockFlatteningHelper.parseTag("{Name:'minecraft:rose_bush',Properties:{half:'upper'}}")
    val UPPER_PEONY = BlockFlatteningHelper.parseTag("{Name:'minecraft:peony',Properties:{half:'upper'}}")
    val AIR = BlockFlatteningHelper.getNBTForId(0)

    private val FLOWER_POT_MAP = mapOf(
        "minecraft:air0" to BlockFlatteningHelper.parseTag("{Name:'minecraft:flower_pot'}"),
        "minecraft:red_flower0" to BlockFlatteningHelper.parseTag("{Name:'minecraft:potted_poppy'}"),
        "minecraft:red_flower1" to BlockFlatteningHelper.parseTag("{Name:'minecraft:potted_blue_orchid'}"),
        "minecraft:red_flower2" to BlockFlatteningHelper.parseTag("{Name:'minecraft:potted_allium'}"),
        "minecraft:red_flower3" to BlockFlatteningHelper.parseTag("{Name:'minecraft:potted_azure_bluet'}"),
        "minecraft:red_flower4" to BlockFlatteningHelper.parseTag("{Name:'minecraft:potted_red_tulip'}"),
        "minecraft:red_flower5" to BlockFlatteningHelper.parseTag("{Name:'minecraft:potted_orange_tulip'}"),
        "minecraft:red_flower6" to BlockFlatteningHelper.parseTag("{Name:'minecraft:potted_white_tulip'}"),
        "minecraft:red_flower7" to BlockFlatteningHelper.parseTag("{Name:'minecraft:potted_pink_tulip'}"),
        "minecraft:red_flower8" to BlockFlatteningHelper.parseTag("{Name:'minecraft:potted_oxeye_daisy'}"),
        "minecraft:yellow_flower0" to BlockFlatteningHelper.parseTag("{Name:'minecraft:potted_dandelion'}"),
        "minecraft:sapling0" to BlockFlatteningHelper.parseTag("{Name:'minecraft:potted_oak_sapling'}"),
        "minecraft:sapling1" to BlockFlatteningHelper.parseTag("{Name:'minecraft:potted_spruce_sapling'}"),
        "minecraft:sapling2" to BlockFlatteningHelper.parseTag("{Name:'minecraft:potted_birch_sapling'}"),
        "minecraft:sapling3" to BlockFlatteningHelper.parseTag("{Name:'minecraft:potted_jungle_sapling'}"),
        "minecraft:sapling4" to BlockFlatteningHelper.parseTag("{Name:'minecraft:potted_acacia_sapling'}"),
        "minecraft:sapling5" to BlockFlatteningHelper.parseTag("{Name:'minecraft:potted_dark_oak_sapling'}"),
        "minecraft:red_mushroom0" to BlockFlatteningHelper.parseTag("{Name:'minecraft:potted_red_mushroom'}"),
        "minecraft:brown_mushroom0" to BlockFlatteningHelper.parseTag("{Name:'minecraft:potted_brown_mushroom'}"),
        "minecraft:deadbush0" to BlockFlatteningHelper.parseTag("{Name:'minecraft:potted_dead_bush'}"),
        "minecraft:tallgrass2" to BlockFlatteningHelper.parseTag("{Name:'minecraft:potted_fern'}"),
        "minecraft:cactus0" to BlockFlatteningHelper.parseTag("{Name:'minecraft:potted_cactus'}"), // we change default to empty
    )
    private val SKULL_MAP = HashMap<String, MapType<String>>().apply {
        mapSkull(0, "skeleton", "skull")
        mapSkull(1, "wither_skeleton", "skull")
        mapSkull(2, "zombie", "head")
        mapSkull(3, "player", "head")
        mapSkull(4, "creeper", "head")
        mapSkull(5, "dragon", "head")
    }
    private val DOOR_MAP = HashMap<String, MapType<String>>().apply {
        mapDoor("oak_door", 1024)
        mapDoor("iron_door", 1136)
        mapDoor("spruce_door", 3088)
        mapDoor("birch_door", 3104)
        mapDoor("jungle_door", 3120)
        mapDoor("acacia_door", 3136)
        mapDoor("dark_oak_door", 3152)
    }
    private val NOTE_BLOCK_MAP = HashMap<String, MapType<String>>().apply {
        for (note in 0..25) {
            put("true$note", BlockFlatteningHelper.parseTag("{Name:'minecraft:note_block',Properties:{powered:'true',note:'$note'}}"))
            put("true$note", BlockFlatteningHelper.parseTag("{Name:'minecraft:note_block',Properties:{powered:'false',note:'$note'}}"))
        }
    }
    private val DYE_COLOR_MAP = Int2ObjectOpenHashMap<String>().apply {
        put(0, "white")
        put(1, "orange")
        put(2, "magenta")
        put(3, "light_blue")
        put(4, "yellow")
        put(5, "lime")
        put(6, "pink")
        put(7, "gray")
        put(8, "light_gray")
        put(9, "cyan")
        put(10, "purple")
        put(11, "blue")
        put(12, "brown")
        put(13, "green")
        put(14, "red")
        put(15, "black")
    }
    private val BED_BLOCK_MAP = HashMap<String, MapType<String>>().apply {
        DYE_COLOR_MAP.int2ObjectEntrySet().fastForEach { if (it.value != "red") addBeds(it.intKey, it.value) }
    }
    private val BANNER_BLOCK_MAP = HashMap<String, MapType<String>>().apply {
        DYE_COLOR_MAP.int2ObjectEntrySet().fastForEach { if (it.value != "white") addBanners(15 - it.intKey, it.value) }
    }

    fun getName(blockState: MapType<String>) = blockState.getString("Name")

    fun getProperty(blockState: MapType<String>, propertyName: String): String {
        val properties = blockState.getMap<String>("Properties") ?: return ""
        return properties.getString(propertyName, "")!!
    }

    fun getSideMask(noLeft: Boolean, noRight: Boolean, noBack: Boolean, noForward: Boolean): Int {
        return if (noBack) when {
            noRight -> 2
            noLeft -> 128
            else -> 1
        } else if (noForward) when {
            noLeft -> 32
            noRight -> 8
            else -> 16
        } else if (noRight) 4 else if (noLeft) 64 else 0
    }

    override fun convert(data: MapType<String>, sourceVersion: Long, toVersion: Long): MapType<String>? {
        val level = data.getMap<String>("Level") ?: return null
        if (!level.hasKey("Sections", ObjectType.LIST)) return null
        data.setMap("Level", UpgradeChunk(level).writeBackToLevel())
        return null
    }

    private fun MutableMap<String, MapType<String>>.mapSkull(oldId: Int, newId: String, skullType: String) {
        put("${oldId}north", BlockFlatteningHelper.parseTag("{Name:'minecraft:${newId}_wall_$skullType',Properties:{facing:'north'}}"))
        put("${oldId}east", BlockFlatteningHelper.parseTag("{Name:'minecraft:${newId}_wall_$skullType',Properties:{facing:'east'}}"))
        put("${oldId}south", BlockFlatteningHelper.parseTag("{Name:'minecraft:${newId}_wall_$skullType',Properties:{facing:'south'}}"))
        put("${oldId}west", BlockFlatteningHelper.parseTag("{Name:'minecraft:${newId}_wall_$skullType',Properties:{facing:'west'}}"))

        for (rotation in 0..15) {
            put("$oldId$rotation", BlockFlatteningHelper.parseTag("{Name:'minecraft:${newId}_$skullType',Properties:{rotation:'$rotation'}}"))
        }
    }

    private fun MutableMap<String, MapType<String>>.mapDoor(type: String, oldId: Int) {
        put("minecraft:${type}eastlowerleftfalsefalse", BlockFlatteningHelper.parseTag("{Name:'minecraft:$type',Properties:{facing:'east',half:'lower',hinge:'left',open:'false',powered:'false'}}"))
        put("minecraft:${type}eastlowerleftfalsetrue", BlockFlatteningHelper.parseTag("{Name:'minecraft:$type',Properties:{facing:'east',half:'lower',hinge:'left',open:'false',powered:'true'}}"))
        put("minecraft:${type}eastlowerlefttruefalse", BlockFlatteningHelper.parseTag("{Name:'minecraft:$type',Properties:{facing:'east',half:'lower',hinge:'left',open:'true',powered:'false'}}"))
        put("minecraft:${type}eastlowerlefttruetrue", BlockFlatteningHelper.parseTag("{Name:'minecraft:$type',Properties:{facing:'east',half:'lower',hinge:'left',open:'true',powered:'true'}}"))
        put("minecraft:${type}eastlowerrightfalsefalse", BlockFlatteningHelper.getNBTForId(oldId))
        put("minecraft:${type}eastlowerrightfalsetrue", BlockFlatteningHelper.parseTag("{Name:'minecraft:$type',Properties:{facing:'east',half:'lower',hinge:'right',open:'false',powered:'true'}}"))
        put("minecraft:${type}eastlowerrighttruefalse", BlockFlatteningHelper.getNBTForId(oldId + 4))
        put("minecraft:${type}eastlowerrighttruetrue", BlockFlatteningHelper.parseTag("{Name:'minecraft:$type',Properties:{facing:'east',half:'lower',hinge:'right',open:'true',powered:'true'}}"))
        put("minecraft:${type}eastupperleftfalsefalse", BlockFlatteningHelper.getNBTForId(oldId + 8))
        put("minecraft:${type}eastupperleftfalsetrue", BlockFlatteningHelper.getNBTForId(oldId + 10))
        put("minecraft:${type}eastupperlefttruefalse", BlockFlatteningHelper.parseTag("{Name:'minecraft:$type',Properties:{facing:'east',half:'upper',hinge:'left',open:'true',powered:'false'}}"))
        put("minecraft:${type}eastupperlefttruetrue", BlockFlatteningHelper.parseTag("{Name:'minecraft:$type',Properties:{facing:'east',half:'upper',hinge:'left',open:'true',powered:'true'}}"))
        put("minecraft:${type}eastupperrightfalsefalse", BlockFlatteningHelper.getNBTForId(oldId + 9))
        put("minecraft:${type}eastupperrightfalsetrue", BlockFlatteningHelper.getNBTForId(oldId + 11))
        put("minecraft:${type}eastupperrighttruefalse", BlockFlatteningHelper.parseTag("{Name:'minecraft:$type',Properties:{facing:'east',half:'upper',hinge:'right',open:'true',powered:'false'}}"))
        put("minecraft:${type}eastupperrighttruetrue", BlockFlatteningHelper.parseTag("{Name:'minecraft:$type',Properties:{facing:'east',half:'upper',hinge:'right',open:'true',powered:'true'}}"))
        put("minecraft:${type}northlowerleftfalsefalse", BlockFlatteningHelper.parseTag("{Name:'minecraft:$type',Properties:{facing:'north',half:'lower',hinge:'left',open:'false',powered:'false'}}"))
        put("minecraft:${type}northlowerleftfalsetrue", BlockFlatteningHelper.parseTag("{Name:'minecraft:$type',Properties:{facing:'north',half:'lower',hinge:'left',open:'false',powered:'true'}}"))
        put("minecraft:${type}northlowerlefttruefalse", BlockFlatteningHelper.parseTag("{Name:'minecraft:$type',Properties:{facing:'north',half:'lower',hinge:'left',open:'true',powered:'false'}}"))
        put("minecraft:${type}northlowerlefttruetrue", BlockFlatteningHelper.parseTag("{Name:'minecraft:$type',Properties:{facing:'north',half:'lower',hinge:'left',open:'true',powered:'true'}}"))
        put("minecraft:${type}northlowerrightfalsefalse", BlockFlatteningHelper.getNBTForId(oldId + 3))
        put("minecraft:${type}northlowerrightfalsetrue", BlockFlatteningHelper.parseTag("{Name:'minecraft:$type',Properties:{facing:'north',half:'lower',hinge:'right',open:'false',powered:'true'}}"))
        put("minecraft:${type}northlowerrighttruefalse", BlockFlatteningHelper.getNBTForId(oldId + 7))
        put("minecraft:${type}northlowerrighttruetrue", BlockFlatteningHelper.parseTag("{Name:'minecraft:$type',Properties:{facing:'north',half:'lower',hinge:'right',open:'true',powered:'true'}}"))
        put("minecraft:${type}northupperleftfalsefalse", BlockFlatteningHelper.parseTag("{Name:'minecraft:$type',Properties:{facing:'north',half:'upper',hinge:'left',open:'false',powered:'false'}}"))
        put("minecraft:${type}northupperleftfalsetrue", BlockFlatteningHelper.parseTag("{Name:'minecraft:$type',Properties:{facing:'north',half:'upper',hinge:'left',open:'false',powered:'true'}}"))
        put("minecraft:${type}northupperlefttruefalse", BlockFlatteningHelper.parseTag("{Name:'minecraft:$type',Properties:{facing:'north',half:'upper',hinge:'left',open:'true',powered:'false'}}"))
        put("minecraft:${type}northupperlefttruetrue", BlockFlatteningHelper.parseTag("{Name:'minecraft:$type',Properties:{facing:'north',half:'upper',hinge:'left',open:'true',powered:'true'}}"))
        put("minecraft:${type}northupperrightfalsefalse", BlockFlatteningHelper.parseTag("{Name:'minecraft:$type',Properties:{facing:'north',half:'upper',hinge:'right',open:'false',powered:'false'}}"))
        put("minecraft:${type}northupperrightfalsetrue", BlockFlatteningHelper.parseTag("{Name:'minecraft:$type',Properties:{facing:'north',half:'upper',hinge:'right',open:'false',powered:'true'}}"))
        put("minecraft:${type}northupperrighttruefalse", BlockFlatteningHelper.parseTag("{Name:'minecraft:$type',Properties:{facing:'north',half:'upper',hinge:'right',open:'true',powered:'false'}}"))
        put("minecraft:${type}northupperrighttruetrue", BlockFlatteningHelper.parseTag("{Name:'minecraft:$type',Properties:{facing:'north',half:'upper',hinge:'right',open:'true',powered:'true'}}"))
        put("minecraft:${type}southlowerleftfalsefalse", BlockFlatteningHelper.parseTag("{Name:'minecraft:$type',Properties:{facing:'south',half:'lower',hinge:'left',open:'false',powered:'false'}}"))
        put("minecraft:${type}southlowerleftfalsetrue", BlockFlatteningHelper.parseTag("{Name:'minecraft:$type',Properties:{facing:'south',half:'lower',hinge:'left',open:'false',powered:'true'}}"))
        put("minecraft:${type}southlowerlefttruefalse", BlockFlatteningHelper.parseTag("{Name:'minecraft:$type',Properties:{facing:'south',half:'lower',hinge:'left',open:'true',powered:'false'}}"))
        put("minecraft:${type}southlowerlefttruetrue", BlockFlatteningHelper.parseTag("{Name:'minecraft:$type',Properties:{facing:'south',half:'lower',hinge:'left',open:'true',powered:'true'}}"))
        put("minecraft:${type}southlowerrightfalsefalse", BlockFlatteningHelper.getNBTForId(oldId + 1))
        put("minecraft:${type}southlowerrightfalsetrue", BlockFlatteningHelper.parseTag("{Name:'minecraft:$type',Properties:{facing:'south',half:'lower',hinge:'right',open:'false',powered:'true'}}"))
        put("minecraft:${type}southlowerrighttruefalse", BlockFlatteningHelper.getNBTForId(oldId + 5))
        put("minecraft:${type}southlowerrighttruetrue", BlockFlatteningHelper.parseTag("{Name:'minecraft:$type',Properties:{facing:'south',half:'lower',hinge:'right',open:'true',powered:'true'}}"))
        put("minecraft:${type}southupperleftfalsefalse", BlockFlatteningHelper.parseTag("{Name:'minecraft:$type',Properties:{facing:'south',half:'upper',hinge:'left',open:'false',powered:'false'}}"))
        put("minecraft:${type}southupperleftfalsetrue", BlockFlatteningHelper.parseTag("{Name:'minecraft:$type',Properties:{facing:'south',half:'upper',hinge:'left',open:'false',powered:'true'}}"))
        put("minecraft:${type}southupperlefttruefalse", BlockFlatteningHelper.parseTag("{Name:'minecraft:$type',Properties:{facing:'south',half:'upper',hinge:'left',open:'true',powered:'false'}}"))
        put("minecraft:${type}southupperlefttruetrue", BlockFlatteningHelper.parseTag("{Name:'minecraft:$type',Properties:{facing:'south',half:'upper',hinge:'left',open:'true',powered:'true'}}"))
        put("minecraft:${type}southupperrightfalsefalse", BlockFlatteningHelper.parseTag("{Name:'minecraft:$type',Properties:{facing:'south',half:'upper',hinge:'right',open:'false',powered:'false'}}"))
        put("minecraft:${type}southupperrightfalsetrue", BlockFlatteningHelper.parseTag("{Name:'minecraft:$type',Properties:{facing:'south',half:'upper',hinge:'right',open:'false',powered:'true'}}"))
        put("minecraft:${type}southupperrighttruefalse", BlockFlatteningHelper.parseTag("{Name:'minecraft:$type',Properties:{facing:'south',half:'upper',hinge:'right',open:'true',powered:'false'}}"))
        put("minecraft:${type}southupperrighttruetrue", BlockFlatteningHelper.parseTag("{Name:'minecraft:$type',Properties:{facing:'south',half:'upper',hinge:'right',open:'true',powered:'true'}}"))
        put("minecraft:${type}westlowerleftfalsefalse", BlockFlatteningHelper.parseTag("{Name:'minecraft:$type',Properties:{facing:'west',half:'lower',hinge:'left',open:'false',powered:'false'}}"))
        put("minecraft:${type}westlowerleftfalsetrue", BlockFlatteningHelper.parseTag("{Name:'minecraft:$type',Properties:{facing:'west',half:'lower',hinge:'left',open:'false',powered:'true'}}"))
        put("minecraft:${type}westlowerlefttruefalse", BlockFlatteningHelper.parseTag("{Name:'minecraft:$type',Properties:{facing:'west',half:'lower',hinge:'left',open:'true',powered:'false'}}"))
        put("minecraft:${type}westlowerlefttruetrue", BlockFlatteningHelper.parseTag("{Name:'minecraft:$type',Properties:{facing:'west',half:'lower',hinge:'left',open:'true',powered:'true'}}"))
        put("minecraft:${type}westlowerrightfalsefalse", BlockFlatteningHelper.getNBTForId(oldId + 2))
        put("minecraft:${type}westlowerrightfalsetrue", BlockFlatteningHelper.parseTag("{Name:'minecraft:$type',Properties:{facing:'west',half:'lower',hinge:'right',open:'false',powered:'true'}}"))
        put("minecraft:${type}westlowerrighttruefalse", BlockFlatteningHelper.getNBTForId(oldId + 6))
        put("minecraft:${type}westlowerrighttruetrue", BlockFlatteningHelper.parseTag("{Name:'minecraft:$type',Properties:{facing:'west',half:'lower',hinge:'right',open:'true',powered:'true'}}"))
        put("minecraft:${type}westupperleftfalsefalse", BlockFlatteningHelper.parseTag("{Name:'minecraft:$type',Properties:{facing:'west',half:'upper',hinge:'left',open:'false',powered:'false'}}"))
        put("minecraft:${type}westupperleftfalsetrue", BlockFlatteningHelper.parseTag("{Name:'minecraft:$type',Properties:{facing:'west',half:'upper',hinge:'left',open:'false',powered:'true'}}"))
        put("minecraft:${type}westupperlefttruefalse", BlockFlatteningHelper.parseTag("{Name:'minecraft:$type',Properties:{facing:'west',half:'upper',hinge:'left',open:'true',powered:'false'}}"))
        put("minecraft:${type}westupperlefttruetrue", BlockFlatteningHelper.parseTag("{Name:'minecraft:$type',Properties:{facing:'west',half:'upper',hinge:'left',open:'true',powered:'true'}}"))
        put("minecraft:${type}westupperrightfalsefalse", BlockFlatteningHelper.parseTag("{Name:'minecraft:$type',Properties:{facing:'west',half:'upper',hinge:'right',open:'false',powered:'false'}}"))
        put("minecraft:${type}westupperrightfalsetrue", BlockFlatteningHelper.parseTag("{Name:'minecraft:$type',Properties:{facing:'west',half:'upper',hinge:'right',open:'false',powered:'true'}}"))
        put("minecraft:${type}westupperrighttruefalse", BlockFlatteningHelper.parseTag("{Name:'minecraft:$type',Properties:{facing:'west',half:'upper',hinge:'right',open:'true',powered:'false'}}"))
        put("minecraft:${type}westupperrighttruetrue", BlockFlatteningHelper.parseTag("{Name:'minecraft:$type',Properties:{facing:'west',half:'upper',hinge:'right',open:'true',powered:'true'}}"))
    }

    private fun MutableMap<String, MapType<String>>.addBeds(colourId: Int, colourName: String) {
        put("southfalsefoot$colourId", BlockFlatteningHelper.parseTag("{Name:'minecraft:${colourName}_bed',Properties:{facing:'south',occupied:'false',part:'foot'}}"))
        put("westfalsefoot$colourId", BlockFlatteningHelper.parseTag("{Name:'minecraft:${colourName}_bed',Properties:{facing:'west',occupied:'false',part:'foot'}}"))
        put("northfalsefoot$colourId", BlockFlatteningHelper.parseTag("{Name:'minecraft:${colourName}_bed',Properties:{facing:'north',occupied:'false',part:'foot'}}"))
        put("eastfalsefoot$colourId", BlockFlatteningHelper.parseTag("{Name:'minecraft:${colourName}_bed',Properties:{facing:'east',occupied:'false',part:'foot'}}"))
        put("southfalsehead$colourId", BlockFlatteningHelper.parseTag("{Name:'minecraft:${colourName}_bed',Properties:{facing:'south',occupied:'false',part:'head'}}"))
        put("westfalsehead$colourId", BlockFlatteningHelper.parseTag("{Name:'minecraft:${colourName}_bed',Properties:{facing:'west',occupied:'false',part:'head'}}"))
        put("northfalsehead$colourId", BlockFlatteningHelper.parseTag("{Name:'minecraft:${colourName}_bed',Properties:{facing:'north',occupied:'false',part:'head'}}"))
        put("eastfalsehead$colourId", BlockFlatteningHelper.parseTag("{Name:'minecraft:${colourName}_bed',Properties:{facing:'east',occupied:'false',part:'head'}}"))
        put("southtruehead$colourId", BlockFlatteningHelper.parseTag("{Name:'minecraft:${colourName}_bed',Properties:{facing:'south',occupied:'true',part:'head'}}"))
        put("westtruehead$colourId", BlockFlatteningHelper.parseTag("{Name:'minecraft:${colourName}_bed',Properties:{facing:'west',occupied:'true',part:'head'}}"))
        put("northtruehead$colourId", BlockFlatteningHelper.parseTag("{Name:'minecraft:${colourName}_bed',Properties:{facing:'north',occupied:'true',part:'head'}}"))
        put("easttruehead$colourId", BlockFlatteningHelper.parseTag("{Name:'minecraft:${colourName}_bed',Properties:{facing:'east',occupied:'true',part:'head'}}"))
    }

    private fun MutableMap<String, MapType<String>>.addBanners(colourId: Int, colourName: String) {
        for (rotation in 0..15) {
            put("${rotation}_$colourId", BlockFlatteningHelper.parseTag("{Name:'minecraft:${colourName}_banner',Properties:{rotation:'$rotation'}}"))
        }
        put("north_$colourId", BlockFlatteningHelper.parseTag("{Name:'minecraft:${colourName}_wall_banner',Properties:{facing:'north'}}"))
        put("south_$colourId", BlockFlatteningHelper.parseTag("{Name:'minecraft:${colourName}_wall_banner',Properties:{facing:'south'}}"))
        put("west_$colourId", BlockFlatteningHelper.parseTag("{Name:'minecraft:${colourName}_wall_banner',Properties:{facing:'west'}}"))
        put("east_$colourId", BlockFlatteningHelper.parseTag("{Name:'minecraft:${colourName}_wall_banner',Properties:{facing:'east'}}"))
    }

    enum class Direction(val axis: Axis, val axisDirection: AxisDirection) {

        DOWN(Axis.Y, AxisDirection.NEGATIVE),
        UP(Axis.Y, AxisDirection.POSITIVE),
        NORTH(Axis.Z, AxisDirection.NEGATIVE),
        SOUTH(Axis.Z, AxisDirection.POSITIVE),
        WEST(Axis.X, AxisDirection.NEGATIVE),
        EAST(Axis.X, AxisDirection.POSITIVE);

        enum class Axis {

            X,
            Y,
            Z
        }

        enum class AxisDirection(val step: Int) {

            POSITIVE(1),
            NEGATIVE(-1)
        }
    }

    class DataLayer(val data: ByteArray = ByteArray(2048)) {

        init {
            require(data.size == 2048) { "ChunkNibbleArrays should be 2048 bytes, not ${data.size}!" }
        }

        operator fun get(index: Int): Int {
            val value = data[index ushr 1].toInt()

            // if we are an even index, we want lower 4 bits
            // if we are an odd index, we want upper 4 bits
            return value ushr (index and 1 shl 2) and 0xF
        }

        operator fun get(x: Int, y: Int, z: Int): Int {
            val index = y shl 8 or (z shl 4) or x
            val value = data[index ushr 1].toInt()

            // if we are an even index, we want lower 4 bits
            // if we are an odd index, we want upper 4 bits
            return value ushr (index and 1 shl 2) and 0xF
        }

        companion object {

            fun getOrCreate(data: ByteArray?) = if (data == null) DataLayer() else DataLayer(data)

            fun getOrNull(data: ByteArray?) = data?.let { DataLayer(data) }
        }
    }

    class UpgradeChunk(private val level: MapType<String>) {

        private val sections = arrayOfNulls<Section>(16)
        private val blockX = level.getInt("xPos") shl 4
        private val blockZ = level.getInt("zPos") shl 4
        private val tileEntities = Int2ObjectLinkedOpenHashMap<MapType<String>>(16)
        private var sides = 0

        init {
            level.getList("TileEntities", ObjectType.MAP)?.let {
                for (i in 0 until it.size()) {
                    val tileEntity = it.getMap<String>(i)
                    val x = (tileEntity.getInt("x") - blockX) and 15
                    val y = tileEntity.getInt("y")
                    val z = (tileEntity.getInt("z") - blockZ) and 15
                    val index = y shl 8 or (z shl 4) or x
                    if (tileEntities.put(index, tileEntity) != null) {
                        LOGGER.warn("In chunk: ${blockX}x$blockZ found a duplicate block entity at position (ConverterFlattenChunk): [$x, $y, $z]")
                    }
                }
            }

            val convertedFromAlphaFormat = level.getBoolean("convertedFromAlphaFormat")
            level.getList("Sections", ObjectType.MAP)?.let {
                for (i in 0 until it.size()) {
                    val sectionData = it.getMap<String>(i)
                    val section = Section(sectionData)

                    if (section.y !in 0..15) {
                        LOGGER.warn("In chunk: ${blockX}x$blockZ found an invalid chunk section y: ${section.y} (ChunkFlatteningConverter)")
                        continue
                    }
                    if (sections[section.y] != null) {
                        LOGGER.warn("In chunk: ${blockX}x$blockZ found a duplicate chunk section: ${section.y} (ChunkFlatteningConverter)")
                    }

                    sides = section.upgrade(sides)
                    sections[section.y] = section
                }
            }

            sections.forEach { section ->
                if (section == null) return@forEach
                val yIndex = section.y shl 12

                section.toFix.int2ObjectEntrySet().fastForEach { entry ->
                    val positionIterator = entry.value.intIterator()
                    when (entry.intKey) {
                        2 -> while (positionIterator.hasNext()) { // grass block
                            val position = positionIterator.nextInt() or yIndex
                            val blockState = getBlock(position)
                            if (getName(blockState) != "minecraft:grass_block") continue
                            val blockAbove = getName(getBlock(relative(position, Direction.UP)))
                            if (blockAbove == "minecraft:snow" || blockAbove == "minecraft:snow_layer") {
                                setBlock(position, SNOWY_GRASS)
                            }
                        }
                        3 -> while (positionIterator.hasNext()) { // dirt
                            val position = positionIterator.nextInt() or yIndex
                            val blockState = getBlock(position)
                            if (getName(blockState) != "minecraft:podzol") continue
                            val blockAbove = getName(getBlock(relative(position, Direction.UP)))
                            if (blockAbove == "minecraft:snow" || blockAbove == "minecraft:snow_layer") {
                                setBlock(position, SNOWY_PODZOL)
                            }
                        }
                        25 -> while (positionIterator.hasNext()) { // note block
                            val position = positionIterator.nextInt() or yIndex
                            val tile = removeBlockEntity(position)
                            if (tile != null) {
                                val state = "${tile.getBoolean("powered")}${min(max(tile.getInt("note"), 0), 24).toByte()}"
                                setBlock(position, NOTE_BLOCK_MAP.getOrDefault(state, NOTE_BLOCK_MAP["false0"]!!))
                            }
                        }
                        26 -> while (positionIterator.hasNext()) { // bed
                            val position = positionIterator.nextInt() or yIndex
                            val tile = getBlockEntity(position) ?: continue
                            val blockState = getBlock(position)
                            val colour = tile.getInt("color")
                            if (colour != 14 && colour in 0..15) {
                                val state = getProperty(blockState, "facing") + getProperty(blockState, "occupied") + getProperty(blockState, "part") + colour
                                BED_BLOCK_MAP[state]?.let { setBlock(position, it) }
                            }
                        }
                        // oak, iron, spruce, birch, jungle, acacia, dark oak door (in that order)
                        64, 71, 193, 194, 195, 196, 197 -> while (positionIterator.hasNext()) { // a.k.a the door updater
                            val position = positionIterator.nextInt() or yIndex
                            val blockState = getBlock(position)
                            if (!getName(blockState)!!.endsWith("_door")) continue
                            if (getProperty(blockState, "half") != "lower") continue

                            val positionAbove = relative(position, Direction.UP)
                            val blockStateAbove = getBlock(positionAbove)

                            val name = getName(blockState)
                            if (name == getName(blockStateAbove)) {
                                val facingBelow = getProperty(blockState, "facing")
                                val openBelow = getProperty(blockState, "open")
                                val hingeAbove = if (convertedFromAlphaFormat) "left" else getProperty(blockStateAbove, "hinge")
                                val poweredAbove = if (convertedFromAlphaFormat) "false" else getProperty(blockStateAbove, "powered")

                                setBlock(position, DOOR_MAP["$name${facingBelow}lower$hingeAbove$openBelow$poweredAbove"]!!)
                                setBlock(position, DOOR_MAP["$name${facingBelow}upper$hingeAbove$openBelow$poweredAbove"]!!)
                            }
                        }
                        86 -> while (positionIterator.hasNext()) { // pumpkin
                            val position = positionIterator.nextInt() or yIndex
                            val blockState = getBlock(position)

                            // I guess this is some terrible hack to convert carved pumpkins from world gen into
                            // regular pumpkins?

                            if (getName(blockState) == "minecraft:carved_pumpkin") {
                                val downName = getName(getBlock(relative(position, Direction.DOWN)))
                                if (downName == "minecraft:grass_block" || downName == "minecraft:dirt") setBlock(position, PUMPKIN)
                            }
                        }
                        110 -> while (positionIterator.hasNext()) { // mycelium
                            val position = positionIterator.nextInt() or yIndex
                            val blockState = getBlock(position)
                            if (getName(blockState) == "minecraft:mycelium") {
                                val nameAbove = getName(getBlock(relative(position, Direction.UP)))
                                if (nameAbove == "minecraft:snow" || nameAbove == "minecraft:snow_layer") setBlock(position, SNOWY_MYCELIUM)
                            }
                        }
                        140 -> while (positionIterator.hasNext()) { // flower pot
                            val position = positionIterator.nextInt() or yIndex
                            val tile = removeBlockEntity(position) ?: continue

                            val item = if (tile.hasKey("Item", ObjectType.NUMBER)) {
                                // the item name converter should have migrated to number, however no legacy converter
                                // ever did this. so we can get data with versions above v102 (old worlds, converted prior to DFU)
                                // that didn't convert. so just do it here.
                                ItemNameHelper.getNameFromId(tile.getInt("Item"))
                            } else {
                                tile.getString("Item", "")!!
                            }

                            val state = item + tile.getInt("Data")
                            setBlock(position, FLOWER_POT_MAP.getOrDefault(state, FLOWER_POT_MAP["minecraft:air0"]!!))
                        }
                        144 -> while (positionIterator.hasNext()) { // mob head
                            val position = positionIterator.nextInt() or yIndex
                            val tile = getBlockEntity(position) ?: continue
                            val typeString = tile.getInt("SkullType").toString()
                            val facing = getProperty(getBlock(position), "facing")
                            val state = typeString + if (facing != "up" && facing != "down") facing else tile.getInt("Rot")

                            tile.remove("SkullType")
                            tile.remove("facing")
                            tile.remove("Rot")
                            setBlock(position, SKULL_MAP.getOrDefault(state, SKULL_MAP["0north"]!!))
                        }
                        175 -> while (positionIterator.hasNext()) { // sunflower
                            val position = positionIterator.nextInt() or yIndex
                            val blockState = getBlock(position)
                            if (getProperty(blockState, "half") != "upper") continue

                            val blockStateBelow = getBlock(relative(position, Direction.DOWN))
                            when (getName(blockStateBelow)) {
                                "minecraft:sunflower" -> setBlock(position, UPPER_SUNFLOWER)
                                "minecraft:lilac" -> setBlock(position, UPPER_LILAC)
                                "minecraft:tall_grass" -> setBlock(position, UPPER_TALL_GRASS)
                                "minecraft:large_fern" -> setBlock(position, UPPER_LARGE_FERN)
                                "minecraft:rose_bush" -> setBlock(position, UPPER_ROSE_BUSH)
                                "minecraft:peony" -> setBlock(position, UPPER_PEONY)
                            }
                        }
                        // free standing banner, wall mounted banner (in that order)
                        176, 177 -> while (positionIterator.hasNext()) {
                            val position = positionIterator.nextInt() or yIndex
                            val tile = getBlockEntity(position) ?: continue

                            val blockState = getBlock(position)
                            val base = tile.getInt("Base")
                            if (base != 15 && base in 0..15) {
                                val state = "${getProperty(blockState, if (entry.intKey == 176) "rotation" else "facing")}_$base"
                                BANNER_BLOCK_MAP[state]?.let { setBlock(position, it) }
                            }
                        }
                    }
                }
            }
        }

        fun writeBackToLevel(): MapType<String> {
            if (tileEntities.isEmpty()) {
                level.remove("TileEntities")
            } else {
                val tileEntities = NBTTypeUtil.createEmptyList()
                this.tileEntities.values.forEach(tileEntities::addMap)
                level.setList("TileEntities", tileEntities)
            }

            val indices = NBTTypeUtil.createEmptyMap<String>()
            val sections = NBTTypeUtil.createEmptyList()
            this.sections.forEach {
                if (it == null) return@forEach
                sections.addMap(it.writeBackToSection())
                indices.setInts(it.y.toString(), it.update.elements().copyOf())
            }
            level.setList("Sections", sections)

            val upgradeData = NBTTypeUtil.createEmptyMap<String>().apply {
                setByte("Sides", sides.toByte())
                setMap("Indices", indices)
            }
            level.setMap("UpgradeData", upgradeData)
            return level
        }

        fun getBlock(index: Int) = sections.getOrNull(index)?.getBlock(index and 4095) ?: AIR

        private fun setBlock(index: Int, blockState: MapType<String>) {
            if (index !in 0..65535) return
            getSection(index)?.setBlock(index and 4095, blockState)
        }

        private fun getSection(index: Int): Section? {
            val y = index shr 12
            return if (y < sections.size) sections[y] else null
        }

        private fun getBlockEntity(index: Int) = tileEntities[index]

        private fun removeBlockEntity(index: Int) = tileEntities.remove(index)

        companion object {

            fun relative(index: Int, direction: Direction): Int = when (direction.axis) {
                Direction.Axis.X -> {
                    val j = (index and 15) + direction.axisDirection.step
                    if (j in 0..15) index and -16 or j else -1
                }
                Direction.Axis.Y -> {
                    val k = (index shr 8) + direction.axisDirection.step
                    if (k in 0..255) index and 255 or (k shl 8) else -1
                }
                Direction.Axis.Z -> {
                    val l = (index shr 4 and 15) + direction.axisDirection.step
                    if (l in 0..15) index and -241 or (l shl 4) else -1
                }
            }
        }
    }

    class Section(private val section: MapType<String>) {

        private val palette = Palette()
        val y = section.getInt("Y")
        private val hasData = section.hasKey("Blocks", ObjectType.BYTE_ARRAY)
        val toFix = Int2ObjectLinkedOpenHashMap<IntArrayList>()
        val update = IntArrayList()
        private val buffer = IntArray(4096)

        fun getBlock(index: Int): MapType<String> = palette.byId.getOrNull(buffer[index]) ?: AIR

        fun setBlock(index: Int, blockState: MapType<String>) {
            buffer[index] = palette.getOrCreateId(blockState)
        }

        fun upgrade(sides: Int): Int {
            if (!hasData) return sides
            var temp = sides

            val blocks = section.getBytes("Blocks")
            val data = DataLayer.getOrNull(section.getBytes("Data"))
            val add = DataLayer.getOrNull(section.getBytes("Add"))
            palette.getOrCreateId(AIR)

            for (i in 0..4095) {
                val x = i and 15
                val z = i shr 4 and 15

                var blockStateId = (blocks[i].toInt() and 255) shl 4
                if (data != null) blockStateId = blockStateId or data[i]
                if (add != null) blockStateId = blockStateId or (add[i] shl 12)
                if (IDS_NEEDING_FIX[blockStateId ushr 4]) addFix(blockStateId ushr 4, i)

                if (VIRTUAL[blockStateId ushr 4]) {
                    val additionalSides = getSideMask(x == 0, x == 15, z == 0, z == 15)
                    if (additionalSides == 0) update.add(i) else temp = temp or i
                }
                setBlock(i, BlockFlatteningHelper.getNBTForId(blockStateId))
            }
            return temp
        }

        // Note: modifies the current section and returns it.
        fun writeBackToSection(): MapType<String> {
            if (!hasData) return section
            section.setList("Palette", palette.states.copy())

            val bitSize = max(4, palette.size.ceillog2())
            val packedIds = PackedBitStorage(bitSize, 4096)
            for (i in buffer.indices) packedIds[i] = buffer[i]

            section.setLongs("BlockStates", packedIds.data)
            section.remove("Blocks")
            section.remove("Data")
            section.remove("Add")
            return section
        }

        private fun addFix(block: Int, index: Int) = toFix.getOrPut(block) { IntArrayList() }.add(index)

        class Palette : Reference2IntOpenHashMap<MapType<String>>() {

            val states = NBTTypeUtil.createEmptyList()
            var byId = arrayOfNulls<MapType<String>>(4)
            private var last: MapType<String>? = null

            fun getOrCreateId(k: MapType<String>): Int {
                if (k === last) return size - 1
                val pos = find(k)
                if (pos >= 0) return value[pos]

                val insert = size
                var inPalette = k
                if (getName(k) == "%%FILTER_ME%%") inPalette = AIR

                if (insert >= byId.size) {
                    byId = byId.copyOf(byId.size * 2)
                    byId[insert] = k
                } else {
                    byId[insert] = k
                }
                states.addMap(inPalette)
                last = k
                insert(-pos - 1, k, insert)
                return insert
            }

            override fun getOrDefault(key: MapType<String>?, defaultValue: Int?): Int = super.getOrDefault(key, defaultValue)

            @Suppress("UNCHECKED_CAST")
            private fun find(k: MapType<String>?): Int {
                if (k == null) return if (containsNullKey) n else -(n + 1)
                val key = key as Array<Any>
                var pos = HashCommon.mix(System.identityHashCode(k)) and mask
                var curr: MapType<String>? = key[pos] as? MapType<String> ?: return -(pos + 1)
                if (k === curr) return pos
                while (true) {
                    pos = (pos + 1) and mask
                    curr = key[pos] as? MapType<String>
                    if (curr == null) return -(pos + 1)
                    if (k === curr) return pos
                }
            }

            @Suppress("UNCHECKED_CAST")
            private fun insert(pos: Int, k: MapType<String>, v: Int) {
                if (pos == n) containsNullKey = true
                (key as Array<Any>)[pos] = k
                value[pos] = v
                if (size++ >= maxFill) rehash(HashCommon.arraySize(size + 1, f))
            }
        }
    }
}
