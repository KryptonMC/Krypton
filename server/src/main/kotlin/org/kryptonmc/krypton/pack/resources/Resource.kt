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
package org.kryptonmc.krypton.pack.resources

import org.kryptonmc.krypton.pack.PackResources
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.util.function.Supplier

class Resource(
    private val source: PackResources,
    private val streamSupplier: Supplier<InputStream>,
    private val metadataSupplier: Supplier<ResourceMetadata>
) {

    private var cachedMetadata: ResourceMetadata? = null

    constructor(source: PackResources, streamSupplier: Supplier<InputStream>) : this(source, streamSupplier, ResourceMetadata.EMPTY_SUPPLIER) {
        cachedMetadata = ResourceMetadata.EMPTY
    }

    fun source(): PackResources = source

    fun sourcePackId(): String = source.packId()

    fun isBuiltin(): Boolean = source.isBuiltin()

    fun open(): InputStream = streamSupplier.get()

    fun openAsReader(): BufferedReader = BufferedReader(InputStreamReader(open(), Charsets.UTF_8))

    fun metadata(): ResourceMetadata {
        if (cachedMetadata == null) cachedMetadata = metadataSupplier.get()
        return cachedMetadata!!
    }
}
