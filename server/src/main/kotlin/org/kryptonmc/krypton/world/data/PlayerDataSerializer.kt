package org.kryptonmc.krypton.world.data

import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.nbt.CompoundTag
import java.util.UUID

interface PlayerDataSerializer {

    fun loadById(uuid: UUID): CompoundTag?

    fun load(player: KryptonPlayer): CompoundTag?

    fun save(player: KryptonPlayer): CompoundTag
}
