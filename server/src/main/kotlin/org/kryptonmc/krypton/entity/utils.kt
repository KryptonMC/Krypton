package org.kryptonmc.krypton.entity

import org.kryptonmc.api.entity.EntityType
import org.kryptonmc.api.entity.EntityTypes

fun EntityType<*>.trackDeltas() = this !== EntityTypes.PLAYER && this !== EntityTypes.LLAMA_SPIT && this !== EntityTypes.WITHER &&
        this !== EntityTypes.BAT && this !== EntityTypes.ITEM_FRAME && this !== EntityTypes.GLOW_ITEM_FRAME && this !== EntityTypes.LEASH_KNOT &&
        this !== EntityTypes.PAINTING && this !== EntityTypes.END_CRYSTAL && this !== EntityTypes.EVOKER_FANGS
