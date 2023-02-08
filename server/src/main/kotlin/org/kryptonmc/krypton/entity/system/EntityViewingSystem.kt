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
package org.kryptonmc.krypton.entity.system

import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.entity.KryptonExperienceOrb
import org.kryptonmc.krypton.entity.KryptonLivingEntity
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.packet.out.play.PacketOutPlayerInfoUpdate
import org.kryptonmc.krypton.packet.out.play.PacketOutPlayerInfoRemove
import org.kryptonmc.krypton.packet.out.play.PacketOutRemoveEntities
import org.kryptonmc.krypton.packet.out.play.PacketOutSetEntityMetadata
import org.kryptonmc.krypton.packet.out.play.PacketOutSetHeadRotation
import org.kryptonmc.krypton.packet.out.play.PacketOutSpawnEntity
import org.kryptonmc.krypton.packet.out.play.PacketOutSpawnExperienceOrb
import org.kryptonmc.krypton.packet.out.play.PacketOutSpawnPlayer
import org.kryptonmc.krypton.packet.out.play.PacketOutUpdateAttributes
import org.kryptonmc.krypton.util.ImmutableLists
import java.util.concurrent.ConcurrentHashMap

open class EntityViewingSystem<out T : KryptonEntity> private constructor(protected val entity: T) {

    private val viewers = ConcurrentHashMap.newKeySet<KryptonPlayer>()

    open fun addViewer(viewer: KryptonPlayer): Boolean {
        if (!viewers.add(viewer)) return false
        viewer.connection.send(getSpawnPacket())
        viewer.connection.send(PacketOutSetEntityMetadata(entity.id, entity.data.collectAll()))
        viewer.connection.send(PacketOutSetHeadRotation(entity.id, entity.position.yaw))
        if (entity is KryptonLivingEntity) viewer.connection.send(PacketOutUpdateAttributes.create(entity.id, entity.attributes.syncable()))
        return true
    }

    open fun removeViewer(viewer: KryptonPlayer): Boolean {
        if (!viewers.remove(viewer)) return false
        viewer.connection.send(PacketOutRemoveEntities.removeSingle(entity))
        return true
    }

    fun sendToViewers(packet: Packet) {
        entity.server.connectionManager.sendGroupedPacket(viewers, packet)
    }

    protected open fun getSpawnPacket(): Packet {
        if (entity is KryptonExperienceOrb) return PacketOutSpawnExperienceOrb.create(entity)
        return PacketOutSpawnEntity.fromEntity(entity)
    }

    private class Player(entity: KryptonPlayer) : EntityViewingSystem<KryptonPlayer>(entity) {

        override fun addViewer(viewer: KryptonPlayer): Boolean {
            if (viewer === entity) return false
            viewer.connection.send(PacketOutPlayerInfoUpdate.createPlayerInitializing(ImmutableLists.of(entity)))
            return super.addViewer(viewer)
        }

        override fun removeViewer(viewer: KryptonPlayer): Boolean {
            if (viewer === entity || !super.removeViewer(viewer)) return false
            viewer.connection.send(PacketOutPlayerInfoRemove(entity))
            return true
        }

        override fun getSpawnPacket(): Packet = PacketOutSpawnPlayer.create(entity)
    }

    companion object {

        @JvmStatic
        fun create(entity: KryptonEntity): EntityViewingSystem<KryptonEntity> {
            if (entity is KryptonPlayer) return Player(entity)
            return EntityViewingSystem(entity)
        }
    }
}
