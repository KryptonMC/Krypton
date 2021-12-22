package org.kryptonmc.krypton.world.block.entity.banner

import org.kryptonmc.api.block.entity.banner.BannerPattern
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.compound

fun BannerPattern.save(): CompoundTag = compound {
    int("Color", Registries.DYE_COLORS.idOf(color))
    string("Pattern", type.code)
}
