package org.kryptonmc.api.world.generation.context

import io.leangen.geantyref.TypeToken
import org.jetbrains.annotations.ApiStatus
import org.kryptonmc.api.Krypton
import org.kryptonmc.internal.annotations.TypeFactory

/**
 * A key in the generation context.
 *
 * This is a separate object as opposed to, for example, a name, which was the first idea,
 * to facilitate type safety. There is also no need for a name, as the keys should have identity
 * equality, so the key itself is the identifier.
 */
public interface GenerationContextKey<T> {

    public val type: TypeToken<T>

    @TypeFactory
    @ApiStatus.Internal
    public interface Factory {

        public fun <T> of(type: TypeToken<T>): GenerationContextKey<T>
    }

    public companion object {

        @JvmStatic
        public fun <T> of(type: TypeToken<T>): GenerationContextKey<T> = Krypton.factory<Factory>().of(type)
    }
}
