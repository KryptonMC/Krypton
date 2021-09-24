/*
 * This file is part of the Krypton API, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and the contributors to the Krypton project.
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the api top-level directory.
 */
package org.kryptonmc.api.item.meta

import net.kyori.adventure.pointer.Pointer
import org.kryptonmc.api.util.CataloguedBy

/**
 * A key used for retrieving item metadata.
 */
@CataloguedBy(MetaKeys::class)
public interface MetaKey<V : Any> : Pointer<V>
