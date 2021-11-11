package org.kryptonmc.api.entity.animal.cat

import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.util.Catalogue

/**
 * All of the built-in vanilla cat types.
 */
@Catalogue(CatType::class)
public object CatTypes {

    // @formatter:off
    @JvmField public val TABBY: CatType = register("tabby")
    @JvmField public val BLACK: CatType = register("black")
    @JvmField public val RED: CatType = register("red")
    @JvmField public val SIAMESE: CatType = register("siamese")
    @JvmField public val BRITISH_SHORTHAIR: CatType = register("british_shorthair")
    @JvmField public val CALICO: CatType = register("calico")
    @JvmField public val PERSIAN: CatType = register("persian")
    @JvmField public val RAGDOLL: CatType = register("ragdoll")
    @JvmField public val WHITE: CatType = register("white")
    @JvmField public val JELLIE: CatType = register("jellie")
    @JvmField public val ALL_BLACK: CatType = register("all_black")

    // @formatter:on
    @JvmStatic
    private fun register(name: String): CatType {
        val key = Key.key("krypton", name)
        return Registries.CAT_TYPES.register(key, CatType.of(key))
    }
}
