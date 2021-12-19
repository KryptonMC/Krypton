package org.kryptonmc.api.item.meta

import org.jetbrains.annotations.Contract
import org.kryptonmc.api.auth.GameProfile

/**
 * Item metadata for a player head.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface PlayerHeadMeta : ScopedItemMeta<PlayerHeadMeta> {

    /**
     * The profile of the player that the head belongs to.
     */
    @get:JvmName("owner")
    public val owner: GameProfile

    /**
     * Creates new item metadata with the given [owner].
     *
     * @param owner the new owner
     * @return new item metadata
     */
    public fun withOwner(owner: GameProfile): PlayerHeadMeta

    /**
     * A builder for building player head metadata.
     */
    public interface Builder : ItemMetaBuilder<Builder, PlayerHeadMeta> {

        /**
         * Sets the owner of the player head to the given [owner].
         *
         * @param owner the owner
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun owner(owner: GameProfile): Builder
    }
}
