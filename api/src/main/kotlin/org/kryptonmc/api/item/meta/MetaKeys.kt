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
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.util.Catalogue
import java.awt.Color

/**
 * An object containing all the currently supported built-in item metadata
 * keys.
 */
@Suppress("UndocumentedPublicProperty")
@Catalogue(MetaKey::class)
public object MetaKeys {

    // @formatter:off
    @JvmField public val DAMAGE: MetaKey<Int> = get("damage")
    @JvmField public val UNBREAKABLE: MetaKey<Boolean> = get("unbreakable")
    @JvmField public val CAN_DESTROY: MetaKey<List<Block>> = get("can_destroy")
    @JvmField public val CUSTOM_MODEL_DATA: MetaKey<Int> = get("custom_model_data")
    @JvmField public val CAN_PLACE_ON: MetaKey<List<Block>> = get("can_place_on")
    @JvmField public val NAME: MetaKey<Component> = get("name")
    @JvmField public val LORE: MetaKey<List<Component>> = get("lore")
    @JvmField public val COLOR: MetaKey<Color> = get("color")

    @JvmField public val HIDE_ATTRIBUTES: MetaKey<Boolean> = get("hide_attributes")
    @JvmField public val HIDE_CAN_DESTROY: MetaKey<Boolean> = get("hide_can_destroy")
    @JvmField public val HIDE_CAN_PLACE_ON: MetaKey<Boolean> = get("hide_can_place_on")
    @JvmField public val HIDE_DYE: MetaKey<Boolean> = get("hide_dye")
    @JvmField public val HIDE_ENCHANTMENTS: MetaKey<Boolean> = get("hide_enchantments")
    @JvmField public val HIDE_MISCELLANEOUS: MetaKey<Boolean> = get("hide_miscellaneous")
    @JvmField public val HIDE_UNBREAKABLE: MetaKey<Boolean> = get("hide_unbreakable")

    // @formatter:on
    @Suppress("UNCHECKED_CAST")
    @JvmStatic
    private fun <V : Any> get(name: String) = Registries.META_KEYS[Key.key("krypton", name)]!! as MetaKey<V>
}
