/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.item

import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.entity.EquipmentSlot
import org.kryptonmc.api.entity.attribute.AttributeModifier
import org.kryptonmc.api.entity.attribute.AttributeType
import org.kryptonmc.api.util.provide

/**
 * An attribute that can be applied to an item that modifies a specific
 * attribute on an entity that has the item this attribute is applied to
 * equipped.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface ItemAttribute {

    /**
     * The type of the attribute.
     */
    @get:JvmName("type")
    public val type: AttributeType

    /**
     * The slot that the item has to be in for the attribute to apply.
     */
    @get:JvmName("slot")
    public val slot: EquipmentSlot

    /**
     * The modifier to apply to the item.
     */
    @get:JvmName("modifier")
    public val modifier: AttributeModifier

    @ApiStatus.Internal
    public interface Factory {

        public fun of(type: AttributeType, slot: EquipmentSlot, modifier: AttributeModifier): ItemAttribute
    }

    public companion object {

        /**
         * Creates a new item attribute with the given [type], [slot], and
         * [modifier].
         *
         * @param type the type of the attribute
         * @param slot the slot
         * @param modifier the modifier
         */
        @JvmStatic
        @Contract("_, _, _ -> new", pure = true)
        public fun of(type: AttributeType, slot: EquipmentSlot, modifier: AttributeModifier): ItemAttribute =
            Krypton.factoryProvider.provide<Factory>().of(type, slot, modifier)
    }
}
