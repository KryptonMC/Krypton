/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Map;

public final class ImmutableMaps {

    public static <K, V> @NotNull @Unmodifiable Map<K, V> copyOf(Map<? extends K, ? extends V> map) {
        return Map.copyOf(map);
    }

    public static <K, V> @NotNull @Unmodifiable Map<K, V> of() {
        return Map.of();
    }

    public static <K, V> @NotNull @Unmodifiable Map<K, V> of(K k1, V v1) {
        return Map.of(k1, v1);
    }

    public static <K, V> @NotNull @Unmodifiable Map<K, V> of(K k1, V v1, K k2, V v2) {
        return Map.of(k1, v1, k2, v2);
    }

    public static <K, V> @NotNull @Unmodifiable Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        return Map.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5);
    }

    private ImmutableMaps() {
        throw new AssertionError("This class cannot be instantiated!");
    }
}
