/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.item.meta

import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import org.kryptonmc.api.block.Block
import java.awt.Color

/**
 * An object containing all the currently supported built-in item metadata
 * keys.
 */
@Suppress("UndocumentedPublicProperty")
public object MetaKeys {

    // @formatter:off
    @JvmField public val DAMAGE: MetaKey<Int> = of("damage")
    @JvmField public val UNBREAKABLE: MetaKey<Boolean> = of("unbreakable")
    @JvmField public val CAN_DESTROY: MetaKey<List<Block>> = of("can_destroy")
    @JvmField public val CUSTOM_MODEL_DATA: MetaKey<Int> = of("custom_model_data")
    @JvmField public val CAN_PLACE_ON: MetaKey<List<Block>> = of("block/can_place_on")
    @JvmField public val NAME: MetaKey<Component> = of("display/name")
    @JvmField public val LORE: MetaKey<List<Component>> = of("display/lore")
    @JvmField public val COLOR: MetaKey<Color> = of("display/color")

    @JvmField public val HIDE_ATTRIBUTES: MetaKey<Boolean> = of("hide_attributes")
    @JvmField public val HIDE_CAN_DESTROY: MetaKey<Boolean> = of("hide_can_destroy")
    @JvmField public val HIDE_CAN_PLACE_ON: MetaKey<Boolean> = of("hide_can_place_on")
    @JvmField public val HIDE_DYE: MetaKey<Boolean> = of("hide_dye")
    @JvmField public val HIDE_ENCHANTMENTS: MetaKey<Boolean> = of("hide_enchantments")
    @JvmField public val HIDE_MISCELLANEOUS: MetaKey<Boolean> = of("hide_miscellaneous")
    @JvmField public val HIDE_UNBREAKABLE: MetaKey<Boolean> = of("hide_unbreakable")

    // @formatter:on
    private inline fun <reified V : Any> of(key: String) = MetaKey(
        Key.key("krypton", "item/meta/$key"),
        V::class.java
    )
}
