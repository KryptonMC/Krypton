package org.kryptonmc.api.util

import net.kyori.adventure.key.Key

fun minecraftKey(value: String) = Key.key(Key.MINECRAFT_NAMESPACE, value)

fun String.toKey() = Key.key(this)
