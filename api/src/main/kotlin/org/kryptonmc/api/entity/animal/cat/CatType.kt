package org.kryptonmc.api.entity.animal.cat

import net.kyori.adventure.key.Key
import net.kyori.adventure.key.Keyed
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.util.CataloguedBy
import org.kryptonmc.api.util.provide

/**
 * A type of cat.
 */
@CataloguedBy(CatTypes::class)
public interface CatType : Keyed {

    @ApiStatus.Internal
    public interface Factory {

        public fun of(key: Key): CatType
    }

    public companion object {

        private val FACTORY = Krypton.factoryProvider.provide<Factory>()

        /**
         * Creates a new cat type with the given [key].
         *
         * @param key the key
         * @return a new cat type
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun of(key: Key): CatType = FACTORY.of(key)
    }
}
