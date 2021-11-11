package org.kryptonmc.krypton.entity.animal.cat

import net.kyori.adventure.sound.Sound
import org.kryptonmc.api.effect.sound.SoundEvents
import org.kryptonmc.api.entity.EntityTypes
import org.kryptonmc.api.entity.animal.Animal
import org.kryptonmc.api.entity.animal.cat.Cat
import org.kryptonmc.api.entity.animal.cat.CatType
import org.kryptonmc.api.entity.attribute.AttributeTypes
import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.api.item.meta.DyeColor
import org.kryptonmc.api.item.meta.DyeColors
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.krypton.entity.animal.KryptonTamable
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.world.KryptonWorld

class KryptonCat(world: KryptonWorld) : KryptonTamable(world, EntityTypes.CAT, ATTRIBUTES), Cat {

    override var catType: CatType
        get() = Registries.CAT_TYPES[data[MetadataKeys.CAT.TYPE]]!!
        set(value) = data.set(MetadataKeys.CAT.TYPE, Registries.CAT_TYPES.idOf(value))
    override var isLying: Boolean
        get() = data[MetadataKeys.CAT.LYING]
        set(value) = data.set(MetadataKeys.CAT.LYING, value)
    override var isRelaxed: Boolean
        get() = data[MetadataKeys.CAT.RELAXED]
        set(value) = data.set(MetadataKeys.CAT.RELAXED, value)
    override var collarColor: DyeColor
        get() = Registries.DYE_COLORS[data[MetadataKeys.CAT.COLLAR_COLOR]] ?: DyeColors.WHITE
        set(value) = data.set(MetadataKeys.CAT.COLLAR_COLOR, Registries.DYE_COLORS.idOf(value))

    init {
        data.add(MetadataKeys.CAT.TYPE)
        data.add(MetadataKeys.CAT.LYING)
        data.add(MetadataKeys.CAT.RELAXED)
        data.add(MetadataKeys.CAT.COLLAR_COLOR)
    }

    override fun hiss() {
        playSound(Sound.sound(SoundEvents.CAT_HISS, soundSource, soundVolume, voicePitch), location.x(), location.y(), location.z())
    }

    override fun canMate(target: Animal): Boolean {
        if (!isTame) return false
        if (target !is Cat) return false
        return target.isTame && super.canMate(target)
    }

    override fun isFood(item: ItemStack): Boolean = item.type === ItemTypes.COD || item.type === ItemTypes.SALMON

    companion object {

        private val ATTRIBUTES = attributes()
            .add(AttributeTypes.MAX_HEALTH, 10.0)
            .add(AttributeTypes.MOVEMENT_SPEED, 0.3)
            .add(AttributeTypes.ATTACK_DAMAGE, 3.0)
            .build()
    }
}
