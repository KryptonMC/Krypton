/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
