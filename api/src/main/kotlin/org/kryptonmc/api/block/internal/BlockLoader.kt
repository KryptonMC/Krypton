package org.kryptonmc.api.block.internal

import net.kyori.adventure.key.Key
import net.kyori.adventure.util.Services
import org.jetbrains.annotations.ApiStatus
import org.kryptonmc.api.block.Block

@ApiStatus.Internal
interface BlockLoader {

    fun fromKey(key: Key): Block?

    fun fromKey(key: String): Block?
}

internal val BLOCK_LOADER = Services.service(BlockLoader::class.java).orElseThrow {
    IllegalStateException("No candidate for the block loader was found! If you are a server owner, contact the creator of your server software.")
}!!
