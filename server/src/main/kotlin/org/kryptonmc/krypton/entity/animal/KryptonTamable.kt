package org.kryptonmc.krypton.entity.animal

import org.kryptonmc.api.entity.EntityType
import org.kryptonmc.api.entity.LivingEntity
import org.kryptonmc.api.entity.animal.Tamable
import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.api.scoreboard.Team
import org.kryptonmc.krypton.entity.attribute.AttributeSupplier
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.nbt.CompoundTag
import java.util.Optional
import java.util.UUID

abstract class KryptonTamable(
    world: KryptonWorld,
    type: EntityType<out Tamable>,
    attributeSupplier: AttributeSupplier
) : KryptonAnimal(world, type, attributeSupplier), Tamable {

    override var isOrderedToSit = false
    override var isTame: Boolean
        get() = data[MetadataKeys.TAMABLE.FLAGS].toInt() and 4 != 0
        set(value) {
            val existing = data[MetadataKeys.TAMABLE.FLAGS].toInt()
            data[MetadataKeys.TAMABLE.FLAGS] = if (value) (existing or 4).toByte() else (existing and -5).toByte()
        }
    override var isSitting: Boolean
        get() = data[MetadataKeys.TAMABLE.FLAGS].toInt() and 1 != 0
        set(value) {
            val existing = data[MetadataKeys.TAMABLE.FLAGS].toInt()
            data[MetadataKeys.TAMABLE.FLAGS] = if (value) (existing or 1).toByte() else (existing and -2).toByte()
        }
    var ownerUUID: UUID?
        get() = data[MetadataKeys.TAMABLE.OWNER].orElse(null)
        set(value) = data.set(MetadataKeys.TAMABLE.OWNER, Optional.ofNullable(value))
    override val owner: KryptonPlayer?
        get() {
            val uuid = ownerUUID ?: return null
            return world.players.firstOrNull { it.uuid == uuid }
        }
    override val team: Team?
        get() {
            if (isTame) return owner?.team
            return super.team
        }

    init {
        data.add(MetadataKeys.TAMABLE.FLAGS)
        data.add(MetadataKeys.TAMABLE.OWNER)
    }

    override fun tame(tamer: Player) {
        isTame = true
        ownerUUID = tamer.uuid
        // TODO: Trigger tame animal advancement criteria
    }

    override fun load(tag: CompoundTag) {
        super.load(tag)
        if (tag.hasUUID("Owner")) {
            try {
                ownerUUID = tag.getUUID("Owner")
                isTame = true
            } catch (exception: Throwable) {
                isTame = false
            }
        }
        isOrderedToSit = tag.getBoolean("Sitting")
        isSitting = isOrderedToSit
    }

    override fun save(): CompoundTag.Builder = super.save().apply {
        if (ownerUUID != null) uuid("Owner", ownerUUID!!)
        boolean("Sitting", isOrderedToSit)
    }
}
