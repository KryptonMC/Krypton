package org.kryptonmc.api.block.entity

import org.kryptonmc.api.block.entity.banner.BannerPattern
import org.kryptonmc.api.item.ItemStackLike
import org.kryptonmc.api.item.data.DyeColor

/**
 * A banner.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface Banner : NameableBlockEntity, ItemStackLike {

    /**
     * The base colour of this banner.
     */
    @get:JvmName("baseColor")
    public var baseColor: DyeColor

    /**
     * The patterns that this banner is decorated with.
     */
    @get:JvmName("patterns")
    public val patterns: List<BannerPattern>

    /**
     * Gets the pattern at the given [index].
     *
     * @param index the index
     * @return the pattern at the index
     * @throws IndexOutOfBoundsException if the given index is out of bounds
     * for the pattern list
     */
    public fun pattern(index: Int): BannerPattern

    /**
     * Sets the pattern at the given [index] to the given [pattern].
     *
     * @param index the index
     * @param pattern the pattern
     * @throws IndexOutOfBoundsException if the given index is out of bounds
     * for the pattern list
     */
    public fun setPattern(index: Int, pattern: BannerPattern)

    /**
     * Sets the patterns this banner has to the given [patterns].
     *
     * @param patterns the patterns
     */
    public fun patterns(patterns: Iterable<BannerPattern>)

    /**
     * Adds the given [pattern] to the list of applied patterns for this
     * banner.
     *
     * @param pattern the pattern to add
     */
    public fun addPattern(pattern: BannerPattern)

    /**
     * Removes the pattern at the given [index] from the patterns list and
     * returns it.
     *
     * @param index the index to remove
     * @return the removed pattern
     * @throws IndexOutOfBoundsException if the given index is out of bounds
     * for the pattern list
     */
    public fun removePattern(index: Int): BannerPattern
}
