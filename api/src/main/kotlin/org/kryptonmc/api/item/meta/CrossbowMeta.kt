package org.kryptonmc.api.item.meta

import net.kyori.adventure.text.Component
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.item.ItemStack

/**
 * Item metadata for a crossbow.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface CrossbowMeta : ScopedItemMeta<CrossbowMeta> {

    /**
     * If the crossbow is charged.
     */
    public val isCharged: Boolean

    /**
     * The projectiles that the crossbow has charged.
     */
    @get:JvmName("projectiles")
    public val projectiles: List<ItemStack>

    /**
     * Creates new item metadata with the given [charged] setting.
     *
     * @param charged the new charged setting
     * @return new item metadata
     */
    public fun withCharged(charged: Boolean): CrossbowMeta

    /**
     * Creates new item metadata with the given [projectiles].
     *
     * @param projectiles the new projectiles
     * @return new item metadata
     */
    public fun withProjectiles(projectiles: List<ItemStack>): CrossbowMeta

    /**
     * Creates new item metadata with the given [projectile] added to the end
     * of the projectiles list.
     *
     * @param projectile the projectile to add
     * @return new item metadata
     */
    public fun addProjectile(projectile: Component): CrossbowMeta

    /**
     * Creates new item metadata with the projectile at the given [index]
     * removed from the projectiles list.
     *
     * @param index the index of the projectile to remove
     * @return new item metadata
     * @throws IllegalArgumentException if the index would result in an out of
     * bounds exception, i.e. when it is too small or too big
     */
    public fun removeProjectile(index: Int): CrossbowMeta

    /**
     * Creates new item metadata with the given [projectile] removed from the
     * projectiles list.
     *
     * @param projectile the projectile to remove
     * @return new item metadata
     */
    public fun removeProjectile(projectile: Component): CrossbowMeta

    /**
     * A builder for building crossbow metadata.
     */
    public interface Builder : ItemMetaBuilder<Builder, CrossbowMeta> {

        /**
         * Sets whether the crossbow is charged to the given [value].
         *
         * @param value whether the crossbow is charged
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun charged(value: Boolean): Builder

        /**
         * Sets the list of charged projectiles for the crossbow to the given
         * [projectiles].
         *
         * @param projectiles the projectiles
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun projectiles(projectiles: Iterable<ItemStack>): Builder

        /**
         * Sets the list of charged projectiles for the crossbow to the given
         * [projectiles].
         *
         * @param projectiles the projectiles
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun projectiles(vararg projectiles: ItemStack): Builder

        /**
         * Sets the list of charged projectiles for the crossbow to the given
         * [projectiles].
         *
         * @param projectiles the projectiles
         * @return this builder
         */
        @JvmSynthetic
        @JvmName("projectilesArray")
        @Contract("_ -> this", mutates = "this")
        public fun projectiles(projectiles: Array<ItemStack>): Builder

        /**
         * Adds the given [projectile] to the list of charged projectiles for
         * the crossbow.
         *
         * @param projectile the projectile to add
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun addProjectile(projectile: ItemStack): Builder
    }
}
