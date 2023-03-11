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
package org.kryptonmc.krypton.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.List;

/*
 * A wrapper around the immutable lists from Java 9.
 *
 * Kotlin doesn't like us using these directly, since it thinks `listOf` is a better option, which it isn't, as `listOf` returns a mutable
 * ArrayList, and we want a fully immutable list, not just a read-only list.
 */
public final class ImmutableLists {

    public static <E> @NotNull @Unmodifiable List<E> copyOf(Collection<E> collection) {
        return List.copyOf(collection);
    }

    public static <E> @NotNull @Unmodifiable List<E> of() {
        return List.of();
    }

    public static <E> @NotNull @Unmodifiable List<E> of(E e1) {
        return List.of(e1);
    }

    public static <E> @NotNull @Unmodifiable List<E> of(E e1, E e2) {
        return List.of(e1, e2);
    }

    public static <E> @NotNull @Unmodifiable List<E> of(E e1, E e2, E e3) {
        return List.of(e1, e2, e3);
    }

    @SafeVarargs
    public static <E> @NotNull @Unmodifiable List<E> of(E... elements) {
        return List.of(elements);
    }

    public static <E> @NotNull @Unmodifiable List<E> ofArray(E[] elements) {
        return List.of(elements);
    }

    private ImmutableLists() {
        throw new AssertionError("This class cannot be instantiated!");
    }
}
