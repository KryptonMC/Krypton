package org.kryptonmc.api.entity.animal.cat

import org.kryptonmc.api.effect.sound.SoundEvents
import org.kryptonmc.api.entity.animal.Tamable
import org.kryptonmc.api.item.meta.DyeColor

/**
 * A cat.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface Cat : Tamable {

    /**
     * The type of cat this cat is.
     */
    @get:JvmName("catType")
    public var catType: CatType

    /**
     * If this cat is currently lying down.
     */
    public var isLying: Boolean

    /**
     * If this cat is currently relaxed.
     */
    public var isRelaxed: Boolean

    /**
     * The colour of this cat's collar.
     */
    @get:JvmName("collarColor")
    public var collarColor: DyeColor

    /**
     * Orders the cat to hiss, playing the sound [SoundEvents.CAT_HISS] to
     * surrounding entities.
     */
    public fun hiss()
}
