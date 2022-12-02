/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.attribute

import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.Krypton
import org.kryptonmc.internal.annotations.ImmutableType
import org.kryptonmc.internal.annotations.TypeFactory
import java.util.UUID

/**
 * A modifier that can be applied to an [Attribute].
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
public interface AttributeModifier {

    /**
     * The unique ID of the modifier.
     */
    @get:JvmName("uuid")
    public val uuid: UUID

    /**
     * The name of the modifier.
     */
    @get:JvmName("name")
    public val name: String

    /**
     * The amount to modify attribute values by.
     */
    @get:JvmName("amount")
    public val amount: Double

    /**
     * The operation to perform on the modifier.
     */
    @get:JvmName("operation")
    public val operation: ModifierOperation

    @ApiStatus.Internal
    @TypeFactory
    public interface Factory {

        public fun of(uuid: UUID, name: String, amount: Double, operation: ModifierOperation): AttributeModifier
    }

    public companion object {

        /**
         * Creates a new attribute modifier with the given values.
         *
         * @param uuid the unique ID of the modifier
         * @param name the name of the modifier
         * @param amount the amount to modify attribute values by
         * @param operation the operation to perform on the modifier
         * @return a new attribute modifier
         */
        @JvmStatic
        @Contract("_, _, _, _ -> new", pure = true)
        public fun of(uuid: UUID, name: String, amount: Double, operation: ModifierOperation): AttributeModifier =
            Krypton.factory<Factory>().of(uuid, name, amount, operation)
    }
}
