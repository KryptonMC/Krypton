package org.kryptonmc.krypton.entity.item

import org.kryptonmc.api.entity.EntityTypes
import org.kryptonmc.api.inventory.InventoryHolder
import org.kryptonmc.api.util.BoundingBox
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.entity.KryptonLivingEntity
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.util.Vectors
import org.kryptonmc.krypton.util.forEachEntityInBounds
import org.kryptonmc.krypton.util.forEachEntityInRange
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.util.UUID
import org.spongepowered.math.vector.Vector3d

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

    private fun entityPickupItem(entity: KryptonLivingEntity) {
        // TODO: Reduce stack to fit a full inventory and leave remainder
        if (entity is InventoryHolder) {
            entity.inventory.add(item)
        }
    }

    override fun tick() {
        if (item.isEmpty()) {
            remove()
            return
        }
        super.tick()
        if (pickupDelay > 0) {
            if (pickupDelay != DELAY_NO_PICKUP) pickupDelay--
            if (pickupDelay < 20) {
                // Item pickup zone
                val minimum = Vector3d.from(location.x() - 1, location.y() - 0.5, location.z() - 1.0)
                val maximum = Vector3d.from(location.x() + 1, location.y() + 0.5, location.z() + 1.0)
                world.entityManager.forEachEntityInBounds(BoundingBox.of(minimum, maximum)) {
                    if (it.isAlive && it is KryptonLivingEntity) {
                        // TODO: Test for mobs picking up items and handle item merge
                        entityPickupItem(it)
                    }
                }
            }
        }
        if (this.age != AGE_NO_DESPAWN) age++
        if (age >= AGE_DESPAWN) remove()
    }
}
