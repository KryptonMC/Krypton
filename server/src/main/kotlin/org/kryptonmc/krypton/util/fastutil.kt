package org.kryptonmc.krypton.util

import it.unimi.dsi.fastutil.longs.Long2ObjectMap
import it.unimi.dsi.fastutil.longs.Long2ObjectMaps

fun <V> Long2ObjectMap<V>.synchronize(): Long2ObjectMap<V> = Long2ObjectMaps.synchronize(this)
