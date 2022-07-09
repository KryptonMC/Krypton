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
    public val TABBY: CatVariant = register("tabby")
    @JvmField
    public val BLACK: CatVariant = register("black")
    @JvmField
    public val RED: CatVariant = register("red")
    @JvmField
    public val SIAMESE: CatVariant = register("siamese")
    @JvmField
    public val BRITISH_SHORTHAIR: CatVariant = register("british_shorthair")
    @JvmField
    public val CALICO: CatVariant = register("calico")
    @JvmField
    public val PERSIAN: CatVariant = register("persian")
    @JvmField
    public val RAGDOLL: CatVariant = register("ragdoll")
    @JvmField
    public val WHITE: CatVariant = register("white")
    @JvmField
    public val JELLIE: CatVariant = register("jellie")
    @JvmField
    public val ALL_BLACK: CatVariant = register("all_black")

    @JvmStatic
    private fun register(name: String): CatVariant {
        val key = Key.key(name)
        return Registries.CAT_VARIANT.register(key, CatVariant.of(key))
    }
}
