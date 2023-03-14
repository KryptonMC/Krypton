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
package org.kryptonmc.krypton.registry

import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.krypton.registry.holder.Holder
import org.kryptonmc.krypton.registry.holder.HolderGetter

/**
 * A registry that may be written to. This subclass exists to limit the scope of registry writing, as registries must be downcasted
 * to this type in order to access the register methods in this class.
 */
interface WritableRegistry<T> : KryptonRegistry<T> {

    fun register(id: Int, key: ResourceKey<T>, value: T): Holder.Reference<T>

    fun register(key: ResourceKey<T>, value: T): Holder.Reference<T>

    fun isEmpty(): Boolean

    fun createRegistrationLookup(): HolderGetter<T>
}
