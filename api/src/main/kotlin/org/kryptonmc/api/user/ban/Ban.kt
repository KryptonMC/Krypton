/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.user.ban

import net.kyori.adventure.text.Component
import net.kyori.adventure.util.Buildable
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.auth.GameProfile
import org.kryptonmc.api.util.provide
import java.net.InetAddress
import java.time.OffsetDateTime

/**
 * A ban made on a target.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
public interface Ban : Buildable<Ban, Ban.Builder> {

    /**
     * The type of this ban.
     */
    @get:JvmName("type")
    public val type: BanType

    /**
     * The source that this ban originated from, or null if this ban does not
     * have an originating source.
     */
    @get:JvmName("source")
    public val source: Component

    /**
     * The reason for issuing the ban, or null if no reason was given.
     */
    @get:JvmName("reason")
    public val reason: Component

    /**
     * The date that this ban was initially created.
     */
    @get:JvmName("creationDate")
    public val creationDate: OffsetDateTime

    /**
     * The date that this ban will expire, or null if this ban will never
     * expire.
     */
    @get:JvmName("expirationDate")
    public val expirationDate: OffsetDateTime?

    /**
     * A ban targeting a profile.
     */
    public interface Profile : Ban {

        /**
         * The target profile.
         */
        @get:JvmName("profile")
        public val profile: GameProfile

        public companion object {

            /**
             * Creates a new builder for building a profile ban.
             *
             * @return a new builder
             */
            @JvmStatic
            @Contract("_ -> new", pure = true)
            public fun builder(): Builder = builder(BanTypes.PROFILE)
        }
    }

    /**
     * A ban targeting an IP address.
     */
    public interface IP : Ban {

        /**
         * The target address.
         */
        @get:JvmName("address")
        public val address: InetAddress

        public companion object {

            /**
             * Creates a new builder for building an IP ban.
             *
             * @return a new builder
             */
            @JvmStatic
            @Contract("_ -> new", pure = true)
            public fun builder(): Builder = builder(BanTypes.IP)
        }
    }

    /**
     * A builder for building bans.
     */
    public interface Builder : Buildable.Builder<Ban> {

        /**
         * Sets the target profile for the ban to the given [profile].
         *
         * This will only work if the ban type has been set to
         * [profile][BanTypes.PROFILE].
         *
         * @param profile the profile
         * @return this builder
         * @throws IllegalArgumentException if the ban type is not set to
         * [BanTypes.PROFILE]
         */
        @Contract("_ -> this", mutates = "this")
        public fun profile(profile: GameProfile): Builder

        /**
         * Sets the target address for the ban to the given [address].
         *
         * This will only work if the ban type has been set to
         * [IP][BanTypes.IP].
         *
         * @param address the address
         * @return this builder
         * @throws IllegalArgumentException if the ban type is not set to
         * [BanTypes.IP]
         */
        @Contract("_ -> this", mutates = "this")
        public fun address(address: InetAddress): Builder

        /**
         * Sets the type of the ban to the given [type].
         *
         * @param type the type
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun type(type: BanType): Builder

        /**
         * Sets the source of the ban to the given [source].
         *
         * @param source the source
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun source(source: Component): Builder

        /**
         * Sets the reason for the ban to the given [reason].
         *
         * @param reason the reason
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun reason(reason: Component?): Builder

        /**
         * Sets the date that the ban was created to the given [date].
         *
         * @param date the date
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun creationDate(date: OffsetDateTime): Builder

        /**
         * Sets the date that the ban will expire to the given [date].
         *
         * @param date the date
         * @return this builder
         */
        @Contract("_ -> this", mutates = "this")
        public fun expirationDate(date: OffsetDateTime): Builder
    }

    @ApiStatus.Internal
    public interface Factory {

        public fun builder(type: BanType): Builder
    }

    public companion object {

        private val FACTORY = Krypton.factoryProvider.provide<Factory>()

        /**
         * Creates a new builder for building a ban.
         *
         * @param type the type
         * @return a new builder
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun builder(type: BanType): Builder = FACTORY.builder(type)

        /**
         * Creates a new profile ban for the given [profile].
         *
         * If the given [expirationDate] is null, this ban will be permanent.
         *
         * @param profile the profile
         * @param expirationDate the date when the ban will expire on
         * @return a new profile ban
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun profile(
            profile: GameProfile,
            expirationDate: OffsetDateTime? = null
        ): Ban = Profile.builder().profile(profile).apply { if (expirationDate != null) expirationDate(expirationDate) }.build()

        /**
         * Creates a new IP ban for the given [address].
         *
         * If the given [expirationDate] is null, this ban will be permanent.
         *
         * @param address the address
         * @return a new IP ban
         */
        @JvmStatic
        @Contract("_ -> new", pure = true)
        public fun ip(
            address: InetAddress,
            expirationDate: OffsetDateTime? = null
        ): Ban = IP.builder().address(address).apply { if (expirationDate != null) expirationDate(expirationDate) }.build()
    }
}
