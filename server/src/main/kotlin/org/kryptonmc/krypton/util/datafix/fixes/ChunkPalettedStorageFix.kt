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
package org.kryptonmc.krypton.util.datafix.fixes

import com.mojang.datafixers.DataFix
import com.mojang.datafixers.DataFixUtils
import com.mojang.datafixers.TypeRewriteRule
import com.mojang.datafixers.schemas.Schema
import com.mojang.serialization.Dynamic
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import it.unimi.dsi.fastutil.ints.IntArrayList
import it.unimi.dsi.fastutil.ints.IntList
import org.kryptonmc.krypton.util.IntIdentityHashBiMap
import org.kryptonmc.krypton.util.ceillog2
import org.kryptonmc.krypton.util.clamp
import org.kryptonmc.krypton.util.datafix.PackedBitStorage
import org.kryptonmc.krypton.util.datafix.References
import org.kryptonmc.krypton.util.datafix.fixes.BlockStateData.parse
import org.kryptonmc.krypton.util.datafix.fixes.BlockStateData.tag
import org.kryptonmc.krypton.util.logger
import java.util.Arrays
import java.util.BitSet
import java.util.Collections
import java.util.IdentityHashMap
import kotlin.math.max
import kotlin.streams.asStream

class ChunkPalettedStorageFix(outputSchema: Schema, changesType: Boolean) : DataFix(outputSchema, changesType) {

    override fun makeRule(): TypeRewriteRule {
        val inputType = inputSchema.getType(References.CHUNK)
        val outputType = outputSchema.getType(References.CHUNK)
        return writeFixAndRead("ChunkPalettedStorageFix", inputType, outputType) { it.fix() }
    }

    private fun Dynamic<*>.fix(): Dynamic<*> {
        val level = get("Level").result()
        return if (level.isPresent && level.get()["Sections"].asStreamOpt().result().isPresent) set("Level", UpgradeChunk(level.get()).write()) else this
    }

    class DataLayer(private val data: ByteArray = ByteArray(SIZE)) {

        init {
            require(data.size == SIZE) { "Chunk nibble arrays must be $SIZE bytes, not ${data.size}!" }
        }

        operator fun get(x: Int, y: Int, z: Int): Int {
            val packed = (y shl 8) or (x shl 4) or z
            val position = packed shl 1
            return if (packed and 1 == 0) data[position].toInt() and 15 else data[position].toInt() shr NIBBLE_SIZE and 15
        }

        companion object {

            private const val SIZE = 2048
            private const val NIBBLE_SIZE = 4
        }
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

    class Section(private val section: Dynamic<*>) {

        private val palette = IntIdentityHashBiMap(Dynamic::class.java, 32)
        private val listTag = mutableListOf<Dynamic<*>>()
        private val hasData = section["Blocks"].result().isPresent
        val y = section["Y"].asInt(0)
        val toFix = Int2ObjectLinkedOpenHashMap<IntList>()
        val update = IntArrayList()
        private val seen: MutableSet<Dynamic<*>> = Collections.newSetFromMap(IdentityHashMap())
        private val buffer = IntArray(SIZE)

        fun getBlock(index: Int) = if (index in 0..SIZE) palette[buffer[index]] ?: AIR else AIR

        fun setBlock(index: Int, block: Dynamic<*>) {
            if (seen.add(block)) listTag.add(if (block.name == "%%FILTER_ME%%") AIR else block)
            buffer[index] = palette.idFor(block)
        }

        fun upgrade(value: Int): Int {
            if (!hasData) return value
            var temp = value
            val blocks = section["Blocks"].asByteBufferOpt().result().get()
            val dataLayer = section["Data"].asByteBufferOpt().map { DataLayer(DataFixUtils.toArray(it)) }.result().orElseGet(ChunkPalettedStorageFix::DataLayer)
            val addLayer = section["Add"].asByteBufferOpt().map { DataLayer(DataFixUtils.toArray(it)) }.result().orElseGet(ChunkPalettedStorageFix::DataLayer)
            seen += AIR
            palette.idFor(AIR)
            listTag += AIR

            for (i in 0 until SIZE) {
                val x = i and 15
                val y = i shr 8 and 15
                val z = i shr 4 and 15
                val total = (addLayer[x, y, z] shl 12) or ((blocks[i].toInt() and 255) shl 4) or dataLayer[x, y, z]
                if (FIX[total shr 4]) toFix.getOrPut(total shr 4) { IntArrayList() } += i
                if (VIRTUAL[total shr 4]) {
                    val sideMask = sideMask(x == 0, x == 15, z == 0, z == 15)
                    if (sideMask == 0) update += i else temp = temp or sideMask
                }
                total.tag()?.let { setBlock(i, it) }
            }
            return temp
        }

        fun write(): Dynamic<*> {
            var temp = section
            if (!hasData) return temp
            temp = temp.set("Palette", temp.createList(listTag.stream()))
            val bits = max(4, seen.size.ceillog2())
            val storage = PackedBitStorage(bits, 4096)
            for (i in buffer.indices) storage[i] = buffer[i]
            temp = temp.set("BlockStates", temp.createLongList(Arrays.stream(storage.data)))
            temp = temp.remove("Blocks")
            temp = temp.remove("Data")
            return temp.remove("Add")
        }
    }

    class UpgradeChunk(private val level: Dynamic<*>) {

        private val x = level["xPos"].asInt(0) shl 4
        private val z = level["zPos"].asInt(0) shl 4
        private val sections = arrayOfNulls<Section>(16)
        private val blockEntities = Int2ObjectLinkedOpenHashMap<Dynamic<*>>(16)

        private var sides = 0

        init {
            level["TileEntities"].asStreamOpt().result().ifPresent { stream ->
                stream.forEach {
                    val x = it["x"].asInt(0) - x and 15
                    val y = it["y"].asInt(0)
                    val z = it["z"].asInt(0) - z and 15
                    val packed = y shl 8 or (z shl 4) or x
                    if (blockEntities.put(packed, it) != null) LOGGER.error("Duplicate block entity found in chunk (${this.x}, ${this.z}) at position ($x, $y, $z)")
                }
            }
            val isConvertedFromAlpha = level["convertedFromAlphaFormat"].asBoolean(false)
            level["Sections"].asStreamOpt().result().ifPresent { stream ->
                stream.forEach {
                    val section = Section(it)
                    sides = section.upgrade(sides)
                    sections[section.y] = section
                }
            }
            sections.asSequence().filterNotNull().forEach { section ->
                val var3 = section.y shl 12
                section.toFix.int2ObjectEntrySet().fastForEach { entry ->
                    when (entry.intKey) {
                        2 -> entry.value.forEach entries@{
                            val temp = it or var3
                            val block = getBlock(temp)
                            if (block.name != "minecraft:grass_block") return@entries
                            val name = getBlock(temp.relativeTo(Direction.UP)).name
                            if (name == "minecraft:snow" || name == "minecraft:snow_layer") setBlock(temp, SNOWY_GRASS)
                        }
                        3 -> entry.value.forEach entries@{
                            val temp = it or var3
                            val block = getBlock(temp)
                            if (block.name != "minecraft:podzol") return@entries
                            val name = getBlock(temp.relativeTo(Direction.UP)).name
                            if (name == "minecraft:snow" || name == "minecraft:snow_layer") setBlock(temp, SNOWY_GRASS)
                        }
                        25 -> entry.value.forEach entries@{
                            val temp = it or var3
                            val blockEntity = blockEntities.remove(temp) ?: return@entries
                            val data = blockEntity["powered"].asBoolean(false).toString() + blockEntity["note"].asInt(0).clamp(0, 24)
                            setBlock(temp, NOTE_BLOCK_REMAP.getOrDefault(data, NOTE_BLOCK_REMAP.getValue("false0")))
                        }
                        26 -> entry.value.forEach entries@ {
                            val temp = it or var3
                            val blockEntity = blockEntities[temp] ?: return@entries
                            val block = getBlock(temp)
                            val color = blockEntity["color"].asInt(0)
                            if (color == 14 || color !in 0..15) return@entries
                            val data = block.property("facing") + block.property("occupied") + block.property("part") + color
                            if (data in BED_BLOCK_REMAP) setBlock(temp, BED_BLOCK_REMAP.getValue(data))
                        }
                        64, 71, 193, 194, 195, 196, 197 -> entry.value.forEach entries@{ int ->
                            val temp = int or var3
                            val block = getBlock(temp)
                            if (!block.name.endsWith("_door")) return@entries
                            if (block.property("half") != "lower") return@entries
                            val relative = temp.relativeTo(Direction.UP)
                            val topHalf = getBlock(relative)
                            val name = block.name
                            if (name != topHalf.name) return@entries
                            val facing = block.property("facing")
                            val open = block.property("open")
                            val hinge = if (isConvertedFromAlpha) "left" else topHalf.property("hinge")
                            val powered = if (isConvertedFromAlpha) "false" else topHalf.property("powered")
                            DOOR_REMAP[name + facing + "lower" + hinge + open + powered]?.let { setBlock(temp, it) }
                            DOOR_REMAP[name + facing + "upper" + hinge + open + powered]?.let { setBlock(relative, it) }
                        }
                        86 -> entry.value.forEach entries@{
                            val temp = it or var3
                            val block = getBlock(temp)
                            if (block.name != "minecraft:carved_pumpkin") return@entries
                            val name = getBlock(temp.relativeTo(Direction.DOWN)).name
                            if (name == "minecraft:grass_block" || name == "minecraft:dirt") setBlock(temp, PUMPKIN)
                        }
                        110 -> entry.value.forEach entries@{
                            val temp = it or var3
                            val block = getBlock(temp)
                            if (block.name != "minecraft:mycelium") return@entries
                            val name = getBlock(temp.relativeTo(Direction.UP)).name
                            if (name == "minecraft:snow" || name == "minecraft:snow_layer") setBlock(temp, SNOWY_MYCELIUM)
                        }
                        140 -> entry.value.forEach entries@{
                            val temp = it or var3
                            val blockEntity = blockEntities.remove(temp) ?: return@entries
                            val data = blockEntity["Item"].asString("") + blockEntity["Data"].asInt(0)
                            FLOWER_POT_REMAP.getOrDefault(data, FLOWER_POT_REMAP.getValue("minecraft:air0"))?.let { setBlock(temp, it) }
                        }
                        144 -> entry.value.forEach entries@{
                            val temp = it or var3
                            val blockEntity = blockEntities[temp] ?: return@entries
                            val skullType = blockEntity["SkullType"].asInt(0).toString()
                            val facing = getBlock(temp).property("facing")
                            val data = if (facing != "up" && facing != "down") skullType + facing else skullType + blockEntity["Rot"].asInt(0)
                            blockEntity.remove("SkullType")
                            blockEntity.remove("facing")
                            blockEntity.remove("Rot")
                            setBlock(temp, SKULL_REMAP.getOrDefault(data, SKULL_REMAP.getValue("0north")))
                        }
                        175 -> entry.value.forEach entries@{
                            val temp = it or var3
                            val block = getBlock(temp)
                            if (block.property("half") != "upper") return@entries
                            val relative = getBlock(temp.relativeTo(Direction.DOWN))
                            when (relative.name) {
                                "minecraft:sunflower" -> setBlock(temp, UPPER_SUNFLOWER)
                                "minecraft:lilac" -> setBlock(temp, UPPER_LILAC)
                                "minecraft:tall_grass" -> setBlock(temp, UPPER_TALL_GRASS)
                                "minecraft:large_fern" -> setBlock(temp, UPPER_LARGE_FERN)
                                "minecraft:rose_bush" -> setBlock(temp, UPPER_ROSE_BUSH)
                                "minecraft:peony" -> setBlock(temp, UPPER_PEONY)
                            }
                        }
                        176, 177 -> entry.value.forEach entries@{
                            val temp = it or var3
                            val blockEntity = blockEntities[temp] ?: return@entries
                            val block = getBlock(temp)
                            val base = blockEntity["Base"].asInt(0)
                            if (base == 15 || base !in 0..15) return@entries
                            val data = block.property(if (entry.intKey == 176) "rotation" else "facing") + "_" + base
                            if (data in BANNER_BLOCK_REMAP) setBlock(temp, BANNER_BLOCK_REMAP.getValue(data))
                        }
                    }
                }
            }
        }



        private fun getBlock(index: Int) = if (index in 0..65535) sections.getOrNull(index shr 12)?.getBlock(index and 4095) ?: AIR else AIR

        private fun setBlock(index: Int, block: Dynamic<*>) {
            if (index !in 0..65535) return
            sections.getOrNull(index shr 12)?.setBlock(index and 4095, block)
        }

        fun write(): Dynamic<*> {
            var temp = level
            temp = if (blockEntities.isEmpty()) temp.remove("TileEntities") else temp.set("TileEntities", temp.createList(blockEntities.values.stream()))

            var sectionMap = temp.emptyMap()
            val sectionStream = sections.asSequence().filterNotNull().map {
                sectionMap = sectionMap.set(it.y.toString(), sectionMap.createIntList(Arrays.stream(it.update.toIntArray())))
                it.write()
            }.asStream()

            var upgradeData = temp.emptyMap()
            upgradeData = upgradeData.set("Sides", upgradeData.createByte(sides.toByte()))
            upgradeData = upgradeData.set("Indices", sectionMap)
            return temp.set("UpgradeData", upgradeData).set("Sections", upgradeData.createList(sectionStream))
        }
    }

    companion object {

        private const val NORTH_WEST_MASK = 128
        private const val WEST_MASK = 64
        private const val SOUTH_WEST_MASK = 32
        private const val SOUTH_MASK = 16
        private const val SOUTH_EAST_MASK = 8
        private const val EAST_MASK = 4
        private const val NORTH_EAST_MASK = 2
        private const val NORTH_MASK = 1
        private const val SIZE = 4096
        private val LOGGER = logger<ChunkPalettedStorageFix>()
        private val VIRTUAL = BitSet(256).apply {
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
        private val FIX = BitSet(256).apply {
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
        private val PUMPKIN = "{Name:'minecraft:pumpkin'}".parse()
        private val SNOWY_PODZOL: Dynamic<*> = "{Name:'minecraft:podzol',Properties:{snowy:'true'}}".parse()
        private val SNOWY_GRASS: Dynamic<*> = "{Name:'minecraft:grass_block',Properties:{snowy:'true'}}".parse()
        private val SNOWY_MYCELIUM: Dynamic<*> = "{Name:'minecraft:mycelium',Properties:{snowy:'true'}}".parse()
        private val UPPER_SUNFLOWER: Dynamic<*> = "{Name:'minecraft:sunflower',Properties:{half:'upper'}}".parse()
        private val UPPER_LILAC: Dynamic<*> = "{Name:'minecraft:lilac',Properties:{half:'upper'}}".parse()
        private val UPPER_TALL_GRASS: Dynamic<*> = "{Name:'minecraft:tall_grass',Properties:{half:'upper'}}".parse()
        private val UPPER_LARGE_FERN: Dynamic<*> = "{Name:'minecraft:large_fern',Properties:{half:'upper'}}".parse()
        private val UPPER_ROSE_BUSH: Dynamic<*> = "{Name:'minecraft:rose_bush',Properties:{half:'upper'}}".parse()
        private val UPPER_PEONY: Dynamic<*> = "{Name:'minecraft:peony',Properties:{half:'upper'}}".parse()

        private val FLOWER_POT_REMAP = mapOf(
            "minecraft:air0" to "{Name:'minecraft:flower_pot'}".parse(),
            "minecraft:red_flower0" to "{Name:'minecraft:potted_poppy'}".parse(),
            "minecraft:red_flower1" to "{Name:'minecraft:potted_blue_orchid'}".parse(),
            "minecraft:red_flower2" to "{Name:'minecraft:potted_allium'}".parse(),
            "minecraft:red_flower3" to "{Name:'minecraft:potted_azure_bluet'}".parse(),
            "minecraft:red_flower4" to "{Name:'minecraft:potted_red_tulip'}".parse(),
            "minecraft:red_flower5" to "{Name:'minecraft:potted_orange_tulip'}".parse(),
            "minecraft:red_flower6" to "{Name:'minecraft:potted_white_tulip'}".parse(),
            "minecraft:red_flower7" to "{Name:'minecraft:potted_pink_tulip'}".parse(),
            "minecraft:red_flower8" to "{Name:'minecraft:potted_oxeye_daisy'}".parse(),
            "minecraft:yellow_flower0" to "{Name:'minecraft:potted_dandelion'}".parse(),
            "minecraft:sapling0" to "{Name:'minecraft:potted_oak_sapling'}".parse(),
            "minecraft:sapling1" to "{Name:'minecraft:potted_spruce_sapling'}".parse(),
            "minecraft:sapling2" to "{Name:'minecraft:potted_birch_sapling'}".parse(),
            "minecraft:sapling3" to "{Name:'minecraft:potted_jungle_sapling'}".parse(),
            "minecraft:sapling4" to "{Name:'minecraft:potted_acacia_sapling'}".parse(),
            "minecraft:sapling5" to "{Name:'minecraft:potted_dark_oak_sapling'}".parse(),
            "minecraft:red_mushroom0" to "{Name:'minecraft:potted_red_mushroom'}".parse(),
            "minecraft:brown_mushroom0" to "{Name:'minecraft:potted_brown_mushroom'}".parse(),
            "minecraft:deadbush0" to "{Name:'minecraft:potted_dead_bush'}".parse(),
            "minecraft:tallgrass2" to "{Name:'minecraft:potted_fern'}".parse(),
            "minecraft:cactus0" to 2240.tag()
        )
        private val SKULL_REMAP = mutableMapOf<String, Dynamic<*>>().apply {
            mapSkull(0, "skeleton", "skull")
            mapSkull(1, "wither_skeleton", "skull")
            mapSkull(2, "zombie", "head")
            mapSkull(3, "player", "head")
            mapSkull(4, "creeper", "head")
            mapSkull(5, "dragon", "head")
        } as Map<String, Dynamic<*>>
        private val DOOR_REMAP = mutableMapOf<String, Dynamic<*>?>().apply {
            mapDoor("oak_door", 1024)
            mapDoor("iron_door", 1136)
            mapDoor("spruce_door", 3088)
            mapDoor("birch_door", 3104)
            mapDoor("jungle_door", 3120)
            mapDoor("acacia_door", 3136)
            mapDoor("dark_oak_door", 3152)
        } as Map<String, Dynamic<*>?>
        private val NOTE_BLOCK_REMAP = mutableMapOf<String, Dynamic<*>>().apply {
            for (i in 0 until 26) {
                put("true$i", "{Name:'minecraft:note_block',Properties:{powered:'true',note:'$i'}}".parse())
                put("false$i", "{Name:'minecraft:note_block',Properties:{powered:'false',note:'$i'}}".parse())
            }
        }
        private val DYE_COLOR_REMAP = Int2ObjectOpenHashMap<String>().apply {
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
        private val BED_BLOCK_REMAP = mutableMapOf<String, Dynamic<*>>().apply {
            DYE_COLOR_REMAP.int2ObjectEntrySet().fastForEach { if (it.value != "red") addBeds(it.intKey, it.value) }
        }
        private val BANNER_BLOCK_REMAP = mutableMapOf<String, Dynamic<*>>().apply {
            DYE_COLOR_REMAP.int2ObjectEntrySet().fastForEach { if (it.value != "white") addBanners(15 - it.intKey, it.value) }
        }
        private val AIR = 0.tag()!!

        fun sideMask(west: Boolean, east: Boolean, north: Boolean, south: Boolean): Int {
            var i = 0
            when {
                north -> i = if (east) i or NORTH_EAST_MASK else if (west) i or NORTH_WEST_MASK else i or NORTH_MASK
                south -> i = if (west) i or SOUTH_WEST_MASK else if (east) i or SOUTH_EAST_MASK else i or SOUTH_MASK
                east -> i = i or EAST_MASK
                west -> i = i or WEST_MASK
            }
            return i
        }

        private fun Int.relativeTo(direction: Direction) = when (direction.axis) {
            Direction.Axis.X -> {
                val amount = (this and 15) + direction.axisDirection.step
                if (amount in 0..15) this and -16 or amount else -1
            }
            Direction.Axis.Y -> {
                val amount = (this shr 8) + direction.axisDirection.step
                if (amount in 0..255) this and 255 or (amount shl 8) else -1
            }
            Direction.Axis.Z -> {
                val amount = (this shr 4 and 15) + direction.axisDirection.step
                if (amount in 0..15) this and -241 or (amount shl 4) else -1
            }
            else -> -1
        }
    }
}

private fun MutableMap<String, Dynamic<*>>.mapSkull(index: Int, entityName: String, skullName: String) {
    put("${index}north", "{Name:'minecraft:${entityName}_wall_${skullName}',Properties:{facing:'north'}}".parse())
    put("${index}east", "{Name:'minecraft:${entityName}_wall_${skullName}',Properties:{facing:'east'}}".parse())
    put("${index}south", "{Name:'minecraft:${entityName}_wall_${skullName}',Properties:{facing:'south'}}".parse())
    put("${index}west", "{Name:'minecraft:${entityName}_wall_${skullName}',Properties:{facing:'west'}}".parse())
    for (i in 0 until 16) {
        put("$index$i", "{Name:'minecraft:${entityName}_${skullName}',Properties:{rotation:'$i'}}".parse())
    }
}

private fun MutableMap<String, Dynamic<*>?>.mapDoor(doorName: String, index: Int) {
    put("minecraft:${doorName}eastlowerleftfalsefalse", "{Name:'minecraft:${doorName}',Properties:{facing:'east',half:'lower',hinge:'left',open:'false',powered:'false'}}".parse())
    put("minecraft:${doorName}eastlowerleftfalsetrue", "{Name:'minecraft:${doorName}',Properties:{facing:'east',half:'lower',hinge:'left',open:'false',powered:'true'}}".parse())
    put("minecraft:${doorName}eastlowerlefttruefalse", "{Name:'minecraft:${doorName}',Properties:{facing:'east',half:'lower',hinge:'left',open:'true',powered:'false'}}".parse())
    put("minecraft:${doorName}eastlowerlefttruetrue", "{Name:'minecraft:${doorName}',Properties:{facing:'east',half:'lower',hinge:'left',open:'true',powered:'true'}}".parse())
    put("minecraft:${doorName}eastlowerrightfalsefalse", index.tag())
    put("minecraft:${doorName}eastlowerrightfalsetrue", "{Name:'minecraft:${doorName}',Properties:{facing:'east',half:'lower',hinge:'right',open:'false',powered:'true'}}".parse())
    put("minecraft:${doorName}eastlowerrighttruefalse", 4.tag())
    put("minecraft:${doorName}eastlowerrighttruetrue", "{Name:'minecraft:${doorName}',Properties:{facing:'east',half:'lower',hinge:'right',open:'true',powered:'true'}}".parse())
    put("minecraft:${doorName}eastupperleftfalsefalse", 8.tag())
    put("minecraft:${doorName}eastupperleftfalsetrue", 10.tag())
    put("minecraft:${doorName}eastupperlefttruefalse", "{Name:'minecraft:${doorName}',Properties:{facing:'east',half:'upper',hinge:'left',open:'true',powered:'false'}}".parse())
    put("minecraft:${doorName}eastupperlefttruetrue", "{Name:'minecraft:${doorName}',Properties:{facing:'east',half:'upper',hinge:'left',open:'true',powered:'true'}}".parse())
    put("minecraft:${doorName}eastupperrightfalsefalse", 9.tag())
    put("minecraft:${doorName}eastupperrightfalsetrue", 11.tag())
    put("minecraft:${doorName}eastupperrighttruefalse", "{Name:'minecraft:${doorName}',Properties:{facing:'east',half:'upper',hinge:'right',open:'true',powered:'false'}}".parse())
    put("minecraft:${doorName}eastupperrighttruetrue", "{Name:'minecraft:${doorName}',Properties:{facing:'east',half:'upper',hinge:'right',open:'true',powered:'true'}}".parse())
    put("minecraft:${doorName}northlowerleftfalsefalse", "{Name:'minecraft:${doorName}',Properties:{facing:'north',half:'lower',hinge:'left',open:'false',powered:'false'}}".parse())
    put("minecraft:${doorName}northlowerleftfalsetrue", "{Name:'minecraft:${doorName}',Properties:{facing:'north',half:'lower',hinge:'left',open:'false',powered:'true'}}".parse())
    put("minecraft:${doorName}northlowerlefttruefalse", "{Name:'minecraft:${doorName}',Properties:{facing:'north',half:'lower',hinge:'left',open:'true',powered:'false'}}".parse())
    put("minecraft:${doorName}northlowerlefttruetrue", "{Name:'minecraft:${doorName}',Properties:{facing:'north',half:'lower',hinge:'left',open:'true',powered:'true'}}".parse())
    put("minecraft:${doorName}northlowerrightfalsefalse", 3.tag())
    put("minecraft:${doorName}northlowerrightfalsetrue", "{Name:'minecraft:${doorName}',Properties:{facing:'north',half:'lower',hinge:'right',open:'false',powered:'true'}}".parse())
    put("minecraft:${doorName}northlowerrighttruefalse", 7.tag())
    put("minecraft:${doorName}northlowerrighttruetrue", "{Name:'minecraft:${doorName}',Properties:{facing:'north',half:'lower',hinge:'right',open:'true',powered:'true'}}".parse())
    put("minecraft:${doorName}northupperleftfalsefalse", "{Name:'minecraft:${doorName}',Properties:{facing:'north',half:'upper',hinge:'left',open:'false',powered:'false'}}".parse())
    put("minecraft:${doorName}northupperleftfalsetrue", "{Name:'minecraft:${doorName}',Properties:{facing:'north',half:'upper',hinge:'left',open:'false',powered:'true'}}".parse())
    put("minecraft:${doorName}northupperlefttruefalse", "{Name:'minecraft:${doorName}',Properties:{facing:'north',half:'upper',hinge:'left',open:'true',powered:'false'}}".parse())
    put("minecraft:${doorName}northupperlefttruetrue", "{Name:'minecraft:${doorName}',Properties:{facing:'north',half:'upper',hinge:'left',open:'true',powered:'true'}}".parse())
    put("minecraft:${doorName}northupperrightfalsefalse", "{Name:'minecraft:${doorName}',Properties:{facing:'north',half:'upper',hinge:'right',open:'false',powered:'false'}}".parse())
    put("minecraft:${doorName}northupperrightfalsetrue", "{Name:'minecraft:${doorName}',Properties:{facing:'north',half:'upper',hinge:'right',open:'false',powered:'true'}}".parse())
    put("minecraft:${doorName}northupperrighttruefalse", "{Name:'minecraft:${doorName}',Properties:{facing:'north',half:'upper',hinge:'right',open:'true',powered:'false'}}".parse())
    put("minecraft:${doorName}northupperrighttruetrue", "{Name:'minecraft:${doorName}',Properties:{facing:'north',half:'upper',hinge:'right',open:'true',powered:'true'}}".parse())
    put("minecraft:${doorName}southlowerleftfalsefalse", "{Name:'minecraft:${doorName}',Properties:{facing:'south',half:'lower',hinge:'left',open:'false',powered:'false'}}".parse())
    put("minecraft:${doorName}southlowerleftfalsetrue", "{Name:'minecraft:${doorName}',Properties:{facing:'south',half:'lower',hinge:'left',open:'false',powered:'true'}}".parse())
    put("minecraft:${doorName}southlowerlefttruefalse", "{Name:'minecraft:${doorName}',Properties:{facing:'south',half:'lower',hinge:'left',open:'true',powered:'false'}}".parse())
    put("minecraft:${doorName}southlowerlefttruetrue", "{Name:'minecraft:${doorName}',Properties:{facing:'south',half:'lower',hinge:'left',open:'true',powered:'true'}}".parse())
    put("minecraft:${doorName}southlowerrightfalsefalse", 1.tag())
    put("minecraft:${doorName}southlowerrightfalsetrue", "{Name:'minecraft:${doorName}',Properties:{facing:'south',half:'lower',hinge:'right',open:'false',powered:'true'}}".parse())
    put("minecraft:${doorName}southlowerrighttruefalse", 5.tag())
    put("minecraft:${doorName}southlowerrighttruetrue", "{Name:'minecraft:${doorName}',Properties:{facing:'south',half:'lower',hinge:'right',open:'true',powered:'true'}}".parse())
    put("minecraft:${doorName}southupperleftfalsefalse", "{Name:'minecraft:${doorName}',Properties:{facing:'south',half:'upper',hinge:'left',open:'false',powered:'false'}}".parse())
    put("minecraft:${doorName}southupperleftfalsetrue", "{Name:'minecraft:${doorName}',Properties:{facing:'south',half:'upper',hinge:'left',open:'false',powered:'true'}}".parse())
    put("minecraft:${doorName}southupperlefttruefalse", "{Name:'minecraft:${doorName}',Properties:{facing:'south',half:'upper',hinge:'left',open:'true',powered:'false'}}".parse())
    put("minecraft:${doorName}southupperlefttruetrue", "{Name:'minecraft:${doorName}',Properties:{facing:'south',half:'upper',hinge:'left',open:'true',powered:'true'}}".parse())
    put("minecraft:${doorName}southupperrightfalsefalse", "{Name:'minecraft:${doorName}',Properties:{facing:'south',half:'upper',hinge:'right',open:'false',powered:'false'}}".parse())
    put("minecraft:${doorName}southupperrightfalsetrue", "{Name:'minecraft:${doorName}',Properties:{facing:'south',half:'upper',hinge:'right',open:'false',powered:'true'}}".parse())
    put("minecraft:${doorName}southupperrighttruefalse", "{Name:'minecraft:${doorName}',Properties:{facing:'south',half:'upper',hinge:'right',open:'true',powered:'false'}}".parse())
    put("minecraft:${doorName}southupperrighttruetrue", "{Name:'minecraft:${doorName}',Properties:{facing:'south',half:'upper',hinge:'right',open:'true',powered:'true'}}".parse())
    put("minecraft:${doorName}westlowerleftfalsefalse", "{Name:'minecraft:${doorName}',Properties:{facing:'west',half:'lower',hinge:'left',open:'false',powered:'false'}}".parse())
    put("minecraft:${doorName}westlowerleftfalsetrue", "{Name:'minecraft:${doorName}',Properties:{facing:'west',half:'lower',hinge:'left',open:'false',powered:'true'}}".parse())
    put("minecraft:${doorName}westlowerlefttruefalse", "{Name:'minecraft:${doorName}',Properties:{facing:'west',half:'lower',hinge:'left',open:'true',powered:'false'}}".parse())
    put("minecraft:${doorName}westlowerlefttruetrue", "{Name:'minecraft:${doorName}',Properties:{facing:'west',half:'lower',hinge:'left',open:'true',powered:'true'}}".parse())
    put("minecraft:${doorName}westlowerrightfalsefalse", 2.tag())
    put("minecraft:${doorName}westlowerrightfalsetrue", "{Name:'minecraft:${doorName}',Properties:{facing:'west',half:'lower',hinge:'right',open:'false',powered:'true'}}".parse())
    put("minecraft:${doorName}westlowerrighttruefalse", 6.tag())
    put("minecraft:${doorName}westlowerrighttruetrue", "{Name:'minecraft:${doorName}',Properties:{facing:'west',half:'lower',hinge:'right',open:'true',powered:'true'}}".parse())
    put("minecraft:${doorName}westupperleftfalsefalse", "{Name:'minecraft:${doorName}',Properties:{facing:'west',half:'upper',hinge:'left',open:'false',powered:'false'}}".parse())
    put("minecraft:${doorName}westupperleftfalsetrue", "{Name:'minecraft:${doorName}',Properties:{facing:'west',half:'upper',hinge:'left',open:'false',powered:'true'}}".parse())
    put("minecraft:${doorName}westupperlefttruefalse", "{Name:'minecraft:${doorName}',Properties:{facing:'west',half:'upper',hinge:'left',open:'true',powered:'false'}}".parse())
    put("minecraft:${doorName}westupperlefttruetrue", "{Name:'minecraft:${doorName}',Properties:{facing:'west',half:'upper',hinge:'left',open:'true',powered:'true'}}".parse())
    put("minecraft:${doorName}westupperrightfalsefalse", "{Name:'minecraft:${doorName}',Properties:{facing:'west',half:'upper',hinge:'right',open:'false',powered:'false'}}".parse())
    put("minecraft:${doorName}westupperrightfalsetrue", "{Name:'minecraft:${doorName}',Properties:{facing:'west',half:'upper',hinge:'right',open:'false',powered:'true'}}".parse())
    put("minecraft:${doorName}westupperrighttruefalse", "{Name:'minecraft:${doorName}',Properties:{facing:'west',half:'upper',hinge:'right',open:'true',powered:'false'}}".parse())
    put("minecraft:${doorName}westupperrighttruetrue", "{Name:'minecraft:${doorName}',Properties:{facing:'west',half:'upper',hinge:'right',open:'true',powered:'true'}}".parse())
}

private fun MutableMap<String, Dynamic<*>>.addBeds(id: Int, colorName: String) {
    put("southfalsefoot$id", "{Name:'minecraft:${colorName}_bed',Properties:{facing:'south',occupied:'false',part:'foot'}}".parse())
    put("westfalsefoot$id", "{Name:'minecraft:${colorName}_bed',Properties:{facing:'west',occupied:'false',part:'foot'}}".parse())
    put("northfalsefoot$id", "{Name:'minecraft:${colorName}_bed',Properties:{facing:'north',occupied:'false',part:'foot'}}".parse())
    put("eastfalsefoot$id", "{Name:'minecraft:${colorName}_bed',Properties:{facing:'east',occupied:'false',part:'foot'}}".parse())
    put("southfalsehead$id", "{Name:'minecraft:${colorName}_bed',Properties:{facing:'south',occupied:'false',part:'head'}}".parse())
    put("westfalsehead$id", "{Name:'minecraft:${colorName}_bed',Properties:{facing:'west',occupied:'false',part:'head'}}".parse())
    put("northfalsehead$id", "{Name:'minecraft:${colorName}_bed',Properties:{facing:'north',occupied:'false',part:'head'}}".parse())
    put("eastfalsehead$id", "{Name:'minecraft:${colorName}_bed',Properties:{facing:'east',occupied:'false',part:'head'}}".parse())
    put("southtruehead$id", "{Name:'minecraft:${colorName}_bed',Properties:{facing:'south',occupied:'true',part:'head'}}".parse())
    put("westtruehead$id", "{Name:'minecraft:${colorName}_bed',Properties:{facing:'west',occupied:'true',part:'head'}}".parse())
    put("northtruehead$id", "{Name:'minecraft:${colorName}_bed',Properties:{facing:'north',occupied:'true',part:'head'}}".parse())
    put("easttruehead$id", "{Name:'minecraft:${colorName}_bed',Properties:{facing:'east',occupied:'true',part:'head'}}".parse())
}

private fun MutableMap<String, Dynamic<*>>.addBanners(id: Int, patternName: String) {
    for (i in 0 until 16) {
        put("${i}_$id", "{Name:'minecraft:${patternName}_banner',Properties:{rotation:'$i'}}".parse())
    }
    put("north_$id", "{Name:'minecraft:${patternName}_wall_banner',Properties:{facing:'north'}}".parse())
    put("south_$id", "{Name:'minecraft:${patternName}_wall_banner',Properties:{facing:'south'}}".parse())
    put("west_$id", "{Name:'minecraft:${patternName}_wall_banner',Properties:{facing:'west'}}".parse())
    put("east_$id", "{Name:'minecraft:${patternName}_wall_banner',Properties:{facing:'east'}}".parse())
}

private val Dynamic<*>.name: String
    get() = get("Name").asString("")

fun Dynamic<*>.property(name: String): String = get("Properties")[name].asString("")

fun IntIdentityHashBiMap<Dynamic<*>>.idFor(data: Dynamic<*>): Int {
    var id = idOf(data)
    if (id == -1) id = add(data)
    return id
}
