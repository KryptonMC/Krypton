/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.entity.animal.type

import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.util.Catalogue

/**
 * All of the built-in vanilla cat types.
 */
@Catalogue(CatVariant::class)
public object CatVariants {

    @JvmField
    public val TABBY: CatVariant = get("tabby")
    @JvmField
    public val BLACK: CatVariant = get("black")
    @JvmField
    public val RED: CatVariant = get("red")
    @JvmField
    public val SIAMESE: CatVariant = get("siamese")
    @JvmField
    public val BRITISH_SHORTHAIR: CatVariant = get("british_shorthair")
    @JvmField
    public val CALICO: CatVariant = get("calico")
    @JvmField
    public val PERSIAN: CatVariant = get("persian")
    @JvmField
    public val RAGDOLL: CatVariant = get("ragdoll")
    @JvmField
    public val WHITE: CatVariant = get("white")
    @JvmField
    public val JELLIE: CatVariant = get("jellie")
    @JvmField
    public val ALL_BLACK: CatVariant = get("all_black")

    @JvmStatic
    private fun get(name: String): CatVariant = Registries.CAT_VARIANT.get(Key.key(name))!!
}
