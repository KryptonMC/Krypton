package org.kryptonmc.krypton.world.block.palette.resize

fun interface PaletteResizer<T> : (Int, T) -> Int