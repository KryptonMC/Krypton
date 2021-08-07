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
 * An object containing all the currently supported built-in item metadata keys.
 */
object MetaKeys {

    // @formatter:off
    @JvmField val DAMAGE = of<Int>("damage")
    @JvmField val UNBREAKABLE = of<Boolean>("unbreakable")
    @JvmField val CAN_DESTROY = of<List<Block>>("can_destroy")
    @JvmField val CUSTOM_MODEL_DATA = of<Int>("custom_model_data")
    @JvmField val CAN_PLACE_ON = of<List<Block>>("block/can_place_on")
    @JvmField val NAME = of<Component>("display/name")
    @JvmField val LORE = of<List<Component>>("display/lore")
    @JvmField val COLOR = of<Color>("display/color")

    @JvmField val HIDE_ATTRIBUTES = of<Boolean>("hide_attributes")
    @JvmField val HIDE_CAN_DESTROY = of<Boolean>("hide_can_destroy")
    @JvmField val HIDE_CAN_PLACE_ON = of<Boolean>("hide_can_place_on")
    @JvmField val HIDE_DYE = of<Boolean>("hide_dye")
    @JvmField val HIDE_ENCHANTMENTS = of<Boolean>("hide_enchantments")
    @JvmField val HIDE_MISCELLANEOUS = of<Boolean>("hide_miscellaneous")
    @JvmField val HIDE_UNBREAKABLE = of<Boolean>("hide_unbreakable")

    // @formatter:on
    private inline fun <reified V : Any> of(key: String) = MetaKey(Key.key("krypton", "item/meta/$key"), V::class.java)
}
