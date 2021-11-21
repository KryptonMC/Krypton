package org.kryptonmc.krypton.entity.item

import org.kryptonmc.api.entity.EntityTypes
import org.kryptonmc.api.util.BoundingBox
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.util.UUID

class KryptonItemEntity(world: KryptonWorld) : KryptonEntity(world, EntityTypes.ITEM) {

    init {
        data.add(MetadataKeys.ITEM.ITEM)
    }

    var age: Short = 0
    var health: Short = 5
    var pickupDelay: Short = 0
    var owner: UUID? = null
    var thrower: UUID? = null

    private val DELAY_NO_PICKUP: Short = 32767 // The delay value that prevents item pickup
    private val AGE_NO_DESPAWN: Short = -32768 // The age value that prevents age from incrementing
    private val AGE_DESPAWN: Short = 6000 // The age value that despawns the item entity

    final var item: KryptonItemStack
        get() = data[MetadataKeys.ITEM.ITEM]
        set(value) = data.set(MetadataKeys.ITEM.ITEM, value)
    override fun load(tag: CompoundTag) {
        super.load(tag)
        age = tag.getShort("Age")
        health = tag.getShort("Health")
        pickupDelay = tag.getShort("PickupDelay")
        if (tag.hasUUID("Owner")) owner = tag.getUUID("Owner")
        if (tag.hasUUID("Thrower")) thrower = tag.getUUID("Thrower")
        val itemNBT = tag.getCompound("Item")
        item = KryptonItemStack(itemNBT)
        if (item.isEmpty()) remove()
    }

    override fun tick() {
        if (this.item.isEmpty()) {
            remove()
            return
        }
        super.tick()
        if (pickupDelay > 0 && pickupDelay != DELAY_NO_PICKUP) pickupDelay--
        // TODO: Lots
        if (this.age != AGE_NO_DESPAWN) age++
        if (age >= AGE_DESPAWN) remove()
    }
}
