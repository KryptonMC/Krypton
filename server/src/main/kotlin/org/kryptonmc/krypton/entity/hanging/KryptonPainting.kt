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
package org.kryptonmc.krypton.entity.hanging

import net.kyori.adventure.key.Key
import org.kryptonmc.api.entity.EntityTypes
import org.kryptonmc.api.entity.hanging.Picture
import org.kryptonmc.api.entity.hanging.Painting
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.packet.out.play.PacketOutSpawnPainting
import org.kryptonmc.krypton.registry.InternalRegistries
import org.kryptonmc.krypton.util.Directions
import org.kryptonmc.krypton.util.data2D
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.nbt.CompoundTag

class KryptonPainting(world: KryptonWorld) : KryptonHangingEntity(world, EntityTypes.PAINTING), Painting {

    override var picture: Picture? = null
    override val width: Int
        get() = picture?.width ?: 1
    override val height: Int
        get() = picture?.height ?: 1

    override fun load(tag: CompoundTag) {
        picture = InternalRegistries.PICTURE[Key.key(tag.getString("Motive"))]
        direction = Directions.of2D(tag.getByte("Facing").toInt())
        super.load(tag)
    }

    override fun save(): CompoundTag.Builder = super.save().apply {
        picture?.let { string("Motive", it.key().asString()) }
        byte("Facing", direction.data2D().toByte())
    }

    override fun getSpawnPacket(): Packet = PacketOutSpawnPainting(this)
}
