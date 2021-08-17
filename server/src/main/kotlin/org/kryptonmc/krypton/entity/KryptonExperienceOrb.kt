package org.kryptonmc.krypton.entity

import org.kryptonmc.api.entity.EntityTypes
import org.kryptonmc.api.entity.ExperienceOrb
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.packet.out.play.PacketOutSpawnExperienceOrb
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.nbt.CompoundTag

class KryptonExperienceOrb(world: KryptonWorld) : KryptonEntity(world, EntityTypes.EXPERIENCE_ORB), ExperienceOrb {

    override var age = 0
    override var count = 1
    override var health = 5
    override var value = 0
    override var following: KryptonPlayer? = null

    override fun load(tag: CompoundTag) {
        super.load(tag)
        age = tag.getShort("Age").toInt()
        count = tag.getInt("Count")
        health = tag.getShort("Health").toInt()
        value = tag.getShort("Value").toInt()
    }

    override fun save(): CompoundTag.Builder = super.save().apply {
        short("Age", age.toShort())
        int("Count", count)
        short("Health", health.toShort())
        short("Value", value.toShort())
    }

    override fun getSpawnPacket() = PacketOutSpawnExperienceOrb(this)
}
