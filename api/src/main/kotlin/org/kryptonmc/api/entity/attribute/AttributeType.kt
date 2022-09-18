/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.attribute

import net.kyori.adventure.key.Keyed
import org.kryptonmc.api.util.CataloguedBy
import org.kryptonmc.api.util.TranslationHolder

/**
 * The type of an attribute.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@CataloguedBy(AttributeTypes::class)
public interface AttributeType : TranslationHolder, Keyed {

    /**
     * The default base value for attributes of this type.
     *
     * This must be between the [minimum] and [maximum] base values.
     */
    @get:JvmName("defaultBase")
    public val defaultBase: Double

    /**
     * The minimum base value for attributes of this type.
     */
    @get:JvmName("minimum")
    public val minimum: Double

    /**
     * The maximum base value for attributes of this type.
     *
     * This must be greater than the [minimum] base value.
     */
    @get:JvmName("maximum")
    public val maximum: Double

    /**
     * If attributes of this type should be sent to clients.
     *
     * When set to false, attributes of this type will only be stored server
     * side, and will not be accessible by clients.
     */
    @get:JvmName("sendToClient")
    public val sendToClient: Boolean
}
