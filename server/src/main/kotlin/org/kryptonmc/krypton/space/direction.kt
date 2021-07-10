package org.kryptonmc.krypton.space

import org.kryptonmc.api.space.Direction

fun String.toDirection(): Direction = Direction.valueOf(uppercase())
