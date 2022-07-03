/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
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
package org.kryptonmc.krypton.map

import net.kyori.adventure.text.Component
import org.kryptonmc.api.entity.Entity
import org.kryptonmc.api.map.MapInfo
import org.kryptonmc.api.map.color.MapColor
import org.kryptonmc.api.map.color.MapColorBrightness
import org.kryptonmc.api.map.color.MapColorType
import org.kryptonmc.api.map.decoration.MapDecoration
import org.kryptonmc.api.map.decoration.MapDecorationOrientation
import org.kryptonmc.api.map.decoration.MapDecorationType
import org.kryptonmc.api.map.marker.BannerMarker
import org.kryptonmc.api.map.marker.ItemFrameMarker
import org.kryptonmc.api.map.marker.MapMarker
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.world.World
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.map.marker.KryptonBannerMarker
import org.kryptonmc.krypton.map.marker.KryptonItemFrameMarker
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.packet.out.play.PacketOutMapData
import org.kryptonmc.krypton.util.clamp
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.data.PersistentData
import org.kryptonmc.krypton.world.dimension.parseDimension
import org.kryptonmc.nbt.ByteTag
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.compound
import org.spongepowered.math.vector.Vector2i
import java.awt.Color
import java.awt.Image
import java.awt.image.BufferedImage
import java.awt.image.DataBufferInt
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class KryptonMapInfo(
    override val centerX: Int,
    override val centerZ: Int,
    override val scale: Int,
    override val dimension: ResourceKey<World>,
    override val isLocked: Boolean,
    override val isTrackingPosition: Boolean,
    override val isTrackingUnlimited: Boolean,
    private val colors: ByteArray
) : PersistentData(), MapInfo {

    private val decorationMap = HashMap<String, MapDecoration>()
    private val bannerMarkerMap = HashMap<String, BannerMarker>()
    private val frameMarkerMap = HashMap<String, ItemFrameMarker>()
    private val viewers = mutableListOf<Viewer>()
    private val viewersByPlayer = mutableMapOf<KryptonPlayer, Viewer>()
    private var trackedDecorationCount = 0

    override val decorations: Collection<MapDecoration>
        get() = decorationMap.values
    override val bannerMarkers: Collection<BannerMarker>
        get() = bannerMarkerMap.values
    override val itemFrameMarkers: Collection<ItemFrameMarker>
        get() = frameMarkerMap.values

    init {
        markDirty()
    }

    override fun getColor(x: Int, y: Int): MapColor = MapColors.fromEncoded(colors[x + y * WIDTH].toInt())

    override fun setColor(x: Int, y: Int, color: MapColor) {
        colors[x + y * WIDTH] = MapColors.encode(color)
    }

    override fun setColor(x: Int, y: Int, color: Color) {
        setColor(x, y, color.rgb)
    }

    override fun setColor(x: Int, y: Int, color: Int) {
        colors[x + y * WIDTH] = MapColors.encode(MapColors.fromRGB(color))
    }

    override fun setColor(x: Int, y: Int, type: MapColorType, brightness: MapColorBrightness) {
        colors[x + y * WIDTH] = MapColors.encode(MapColors.fromId(type.ordinal * 4 + brightness.ordinal))
    }

    override fun getBannerMarker(x: Int, y: Int, z: Int): BannerMarker? = bannerMarkerMap["banner-$x,$y,$z"]

    override fun addBannerMarker(x: Int, y: Int, z: Int, marker: BannerMarker) {
        bannerMarkerMap["banner-$x,$y,$z"] = marker
    }

    override fun removeBannerMarker(x: Int, y: Int, z: Int): BannerMarker? = bannerMarkerMap.remove("banner-$x,$y,$z")

    override fun getItemFrameMarker(entity: Entity): ItemFrameMarker? = frameMarkerMap["frame-${entity.id}"]

    override fun addItemFrameMarker(marker: ItemFrameMarker) {
        frameMarkerMap["frame-${marker.entity.id}"] = marker
    }

    override fun removeItemFrameMarker(marker: ItemFrameMarker) {
        frameMarkerMap.remove("frame-${marker.entity.id}")
    }

    override fun save(): CompoundTag = compound {
        string("dimension", dimension.location.asString())
        int("xCenter", centerX)
        int("zCenter", centerZ)
        byte("scale", scale.toByte())
        byteArray("colors", colors)
        boolean("trackingPosition", isTrackingPosition)
        boolean("unlimitedTracking", isTrackingUnlimited)
        boolean("locked", isLocked)
        list("banners") { bannerMarkers.forEach { add(it.save()) } }
        list("frames") { itemFrameMarkers.forEach { add(it.save()) } }
    }

    fun viewer(player: KryptonPlayer): Viewer {
        var viewer = viewersByPlayer[player]
        if (viewer == null) {
            viewer = Viewer(player)
            viewersByPlayer[player] = viewer
            viewers.add(viewer)
        }
        return viewer
    }

    fun updatePacket(id: Int, player: KryptonPlayer): Packet? = viewersByPlayer[player]?.createUpdatePacket(id)

    private fun addDecoration(type: MapDecorationType, world: KryptonWorld?, id: String, x: Double, z: Double, rotation: Double, name: Component?) {
        val actualScale = 1 shl scale // fast shortcut for powers of two, this means 2^scale
        val scaledX = (x - centerX).toFloat() / actualScale
        val scaledZ = (z - centerZ).toFloat() / actualScale
        var mapX = ((scaledX * 2F).toDouble() + 0.5).toInt()
        var mapZ = ((scaledZ * 2F).toDouble() + 0.5).toInt()
        var tempRotation = rotation
        var mapRotation: Int
        var tempType = type
        if (scaledX >= -63F && scaledZ >= -63F && scaledX <= 63F && scaledZ <= 63F) {
            tempRotation += if (tempRotation < 0.0) -8.0 else 8.0
            mapRotation = ((tempRotation * 16.0) / 360.0).toInt()
            if (dimension == World.NETHER && world != null) {
                val time = (world.data.dayTime / 10L).toInt()
                mapRotation = ((time * time * 34187121 + time * 121) shr 15) and 15
            }
        } else {
            if (tempType != MapDecorationType.PLAYER) {
                removeDecoration(id)
                return
            }
            tempType = if (abs(scaledX) < 320F && abs(scaledZ) < 320F) {
                MapDecorationType.PLAYER_OFF_MAP
            } else {
                if (!isTrackingUnlimited) {
                    removeDecoration(id)
                    return
                }
                MapDecorationType.PLAYER_OFF_LIMITS
            }
            mapRotation = 0
            if (scaledX <= -63F) mapX = -128
            if (scaledZ <= -63F) mapZ = -128
            if (scaledX >= 63F) mapX = 127
            if (scaledZ >= 63F) mapZ = 127
        }
        val decoration = KryptonMapDecoration(tempType, mapX, mapZ, DECORATION_ORIENTATIONS[mapRotation], name)
        val existing = decorationMap.put(id, decoration)
        if (decoration == existing) return
        if (existing != null && existing.type.shouldTrackCount()) trackedDecorationCount--
        if (type.shouldTrackCount()) trackedDecorationCount++
        scheduleDecorationsUpdate()
    }

    private fun removeDecoration(id: String) {
        val decoration = decorationMap[id]
        if (decoration != null && decoration.type.shouldTrackCount()) trackedDecorationCount--
        scheduleDecorationsUpdate()
    }

    private fun scheduleDecorationsUpdate() {
        markDirty()
        viewers.forEach { it.scheduleDecorationsUpdate() }
    }

    inner class Viewer(private val player: KryptonPlayer) {

        private var currentStartX = 0
        private var currentStartY = 0
        private var currentWidth = 127
        private var currentHeight = 127
        private var updateColors = true
        private var updateDecorations = true
        private var tick = 0

        fun createUpdatePacket(id: Int): PacketOutMapData? {
            if (!updateColors && !updateDecorations) return null
            return PacketOutMapData(id, scale, isLocked, decorations(), colorUpdate())
        }

        fun scheduleColorUpdate(x: Int, y: Int) {
            if (updateColors) {
                currentStartX = min(currentStartX, x)
                currentStartY = min(currentStartY, y)
                currentWidth = max(currentWidth, x)
                currentHeight = max(currentHeight, y)
                return
            }
            updateColors = true
            currentStartX = x
            currentStartY = y
            currentWidth = x
            currentHeight = y
        }

        fun scheduleDecorationsUpdate() {
            updateDecorations = true
        }

        private fun colorUpdate(): PacketOutMapData.MapColorUpdate? {
            if (!updateColors) return null
            updateColors = false
            val startX = currentStartX
            val startY = currentStartY
            val width = currentWidth
            val height = currentHeight
            val colors = ByteArray(width * height)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    colors[x + y * width] = this@KryptonMapInfo.colors[startX + x + (startY + y) * WIDTH]
                }
            }
            return PacketOutMapData.MapColorUpdate(startX, startY, width, height, colors)
        }

        private fun decorations(): Collection<MapDecoration>? {
            if (!updateDecorations || tick++ % 5 != 0) return null
            updateDecorations = false
            return decorations
        }
    }

    class Builder : MapInfo.Builder {

        private var centerX = 0
        private var centerZ = 0
        private var dimension = World.OVERWORLD
        private var scale = 0
        private var locked = false
        private var trackingPosition = false
        private var unlimitedTracking = false
        private val colors = ByteArray(WIDTH * HEIGHT)
        private val decorations = mutableMapOf<String, MapDecoration>()
        private val bannerMarkers = mutableMapOf<String, BannerMarker>()
        private val frameMarkers = mutableMapOf<String, ItemFrameMarker>()

        override fun centerX(x: Int): MapInfo.Builder = apply { centerX = x }

        override fun centerZ(z: Int): MapInfo.Builder = apply { centerZ = z }

        override fun center(position: Vector2i): MapInfo.Builder = apply {
            centerX(position.x())
            centerZ(position.y())
        }

        override fun dimension(dimension: ResourceKey<World>): MapInfo.Builder = apply { this.dimension = dimension }

        override fun scale(scale: Int): MapInfo.Builder = apply { this.scale = scale }

        override fun lock(): MapInfo.Builder = apply { locked = true }

        override fun trackPosition(): MapInfo.Builder = apply { trackingPosition = true }

        override fun unlimitedTracking(): MapInfo.Builder = apply { unlimitedTracking = true }

        override fun color(x: Int, y: Int, color: MapColor): MapInfo.Builder = apply { colors[x + y * WIDTH] = MapColors.encode(color) }

        override fun color(x: Int, y: Int, color: Color): MapInfo.Builder = color(x, y, color.rgb)

        override fun color(x: Int, y: Int, color: Int): MapInfo.Builder = apply {
            colors[x + y * WIDTH] = MapColors.encode(MapColors.fromRGB(color))
        }

        override fun color(x: Int, y: Int, type: MapColorType, brightness: MapColorBrightness): MapInfo.Builder = apply {
            colors[x + y * WIDTH] = MapColors.encode(MapColors.fromId(type.ordinal * 4 + brightness.ordinal))
        }

        override fun decorate(decoration: MapDecoration): MapInfo.Builder = apply {
            decorations["custom-${decoration.x},${decoration.y}"] = decoration
        }

        override fun mark(marker: MapMarker): MapInfo.Builder = apply {
            when (marker) {
                is BannerMarker -> bannerMarkers["banner-${marker.position.x()},${marker.position.y()},${marker.position.z()}"] = marker
                is ItemFrameMarker -> frameMarkers["frame-${marker.entity.id}"] = marker
            }
        }

        override fun fromImage(image: Image): MapInfo.Builder = apply {
            require(image.getWidth(null) == WIDTH && image.getHeight(null) == HEIGHT) { "Image size is invalid! Must be $WIDTH x $HEIGHT!" }
            val bufferedImage = createBufferedImage(image)
            val pixels = (bufferedImage.raster.dataBuffer as DataBufferInt).data
            for (i in colors.indices) {
                colors[i] = MapColors.encode(MapColors.fromRGB(pixels[i]))
            }
        }

        override fun build(): MapInfo {
            val info = KryptonMapInfo(centerX, centerZ, scale, dimension, locked, trackingPosition, unlimitedTracking, colors)
            info.decorationMap.putAll(decorations)
            info.bannerMarkerMap.putAll(bannerMarkers)
            info.frameMarkerMap.putAll(frameMarkers)
            return info
        }
    }

    object Factory : MapInfo.Factory {

        override fun builder(): MapInfo.Builder = Builder()
    }

    companion object {

        private const val WIDTH = 128
        private const val HEIGHT = 128
        private val DECORATION_ORIENTATIONS = MapDecorationOrientation.values()

        @JvmStatic
        fun from(world: KryptonWorld, data: CompoundTag): KryptonMapInfo {
            val dimension = requireNotNull(data["dimension"]?.parseDimension()) { "Invalid map dimension ${data["dimension"]}!" }
            val centerX = data.getInt("xCenter")
            val centerZ = data.getInt("zCenter")
            val scale = data.getByte("scale").toInt().clamp(0, 4)
            val trackingPosition = !data.contains("trackingPosition", ByteTag.ID) || data.getBoolean("trackingPosition")
            val unlimitedTracking = data.getBoolean("unlimitedTracking")
            val locked = data.getBoolean("locked")
            var colors = data.getByteArray("colors")
            if (colors.size != WIDTH * HEIGHT) colors = ByteArray(WIDTH * HEIGHT)
            val info = KryptonMapInfo(centerX, centerZ, scale, dimension, locked, trackingPosition, unlimitedTracking, colors)
            data.getList("banners", CompoundTag.ID).forEachCompound {
                val marker = KryptonBannerMarker.from(it)
                val x = marker.position.x().toDouble()
                val z = marker.position.z().toDouble()
                info.bannerMarkerMap[marker.createId()] = marker
                info.addDecoration(marker.decoration, null, marker.createId(), x, z, 180.0, marker.name)
            }
            data.getList("frames", CompoundTag.ID).forEachCompound {
                val marker = KryptonItemFrameMarker.from(world, it)
                val x = marker.position.x().toDouble()
                val z = marker.position.z().toDouble()
                info.frameMarkerMap[marker.createId()] = marker
                info.addDecoration(MapDecorationType.FRAME, null, "frame-${marker.entity.id}", x, z, marker.rotation.toDouble(), null)
            }
            return info
        }

        @JvmStatic
        private fun createBufferedImage(image: Image): BufferedImage {
            if (image is BufferedImage && image.type == BufferedImage.TYPE_INT_RGB) return image
            val bufferedImage = BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB)
            val graphics = bufferedImage.createGraphics()
            graphics.drawImage(image, 0, 0, null)
            graphics.dispose()
            return bufferedImage
        }
    }
}
