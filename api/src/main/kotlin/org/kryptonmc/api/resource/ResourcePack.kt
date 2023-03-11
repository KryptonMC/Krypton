/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.api.resource

import net.kyori.adventure.text.Component
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.Krypton
import org.kryptonmc.internal.annotations.ImmutableType
import org.kryptonmc.internal.annotations.TypeFactory
import java.net.URI

/**
 * A resource pack that may be sent to clients.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
public interface ResourcePack {

    /**
     * The URI pointing to the location where this resource pack is from.
     */
    @get:JvmName("uri")
    public val uri: URI

    /**
     * The hash of the resource pack. This should be generated from hashing the
     * resource that the [uri] points to.
     */
    @get:JvmName("hash")
    public val hash: String

    /**
     * If clients must always display this resource pack.
     */
    public val isForced: Boolean

    /**
     * The message that will be shown to the client within the prompt used to
     * confirm the resource pack.
     */
    @get:JvmName("promptMessage")
    public val promptMessage: Component?

    /**
     * The status of a resource pack.
     */
    public enum class Status {

        /**
         * The client has successfully downloaded and applied the resource
         * pack.
         */
        SUCCESSFULLY_LOADED,

        /**
         * The client refused to accept the resource pack.
         */
        DECLINED,

        /**
         * The client accepted the resource pack, but it failed to download it.
         */
        FAILED_DOWNLOAD,

        /**
         * The client accepted the resource pack and is attempting to download
         * it.
         */
        ACCEPTED
    }

    @ApiStatus.Internal
    @TypeFactory
    public interface Factory {

        public fun of(uri: URI, hash: String, isForced: Boolean, promptMessage: Component?): ResourcePack
    }

    public companion object {

        /**
         * Creates a new resource pack with the given values.
         *
         * @param uri the URI
         * @param hash the hash of the resource at the URI
         * @param isForced if the resource pack must be used by all clients
         * @param promptMessage the message sent with the prompt to accept the
         * pack
         */
        @JvmStatic
        @Contract("_, _, _, _ -> new", pure = true)
        public fun of(uri: URI, hash: String, isForced: Boolean, promptMessage: Component?): ResourcePack =
            Krypton.factory<Factory>().of(uri, hash, isForced, promptMessage)
    }
}
